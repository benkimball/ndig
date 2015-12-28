package com.benkimball.ndig;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

enum EdgeType implements RelationshipType {
    EXIT
}

enum NodeType implements Label {
    ROOM
}

@ThreadSafe
public class NdRoom {

    private static final Log log = LogFactory.getLog("NdRoom");

    private static final String NO_NAME = "Void";
    private static final String NO_DESCRIPTION =
            "This room is without form, and void; and darkness is upon the face of the deep.";

    private static final GraphDatabaseService gdb;
    private static final NdRoom home;
    private static AtomicInteger last_id = new AtomicInteger(0);
    private static final Map<Long,NdRoom> active_rooms;

    static {
        gdb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("target/graph.db"));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() { gdb.shutdown(); }
        });
        log.info("Initialized graph database");
    }

    static {
        try(Transaction tx = gdb.beginTx();
            Result result = gdb.execute("MERGE (n:Room {id: 0})" +
                    " ON CREATE SET" +
                    " n.name='Home'" +
                    ", n.description='This comfortable room has lots of rugs and pillows.'" +
                    " RETURN n")) {
            home = new NdRoom(0L, (Node)result.columnAs("n").next());
            tx.success();
            log.info("Initialized default room");
        }

        active_rooms = new ConcurrentHashMap<>();
        active_rooms.put(0L, home); // home is always active
        log.info("Added default room to active rooms");
    }

    private final Node node;
    private final Set<NdPlayer> occupants;

    public NdRoom(Long id, Node node) {
        this.node = node;
        this.occupants = new CopyOnWriteArraySet<>();
        log.info(String.format("Instantiated new NdRoom from neo4j node with ID %d", id));
    }

    public NdRoom() {
        long id = (long)last_id.incrementAndGet();
        try (Transaction tx = gdb.beginTx()) {
            node = gdb.createNode(NodeType.ROOM);
            node.setProperty("id", id);
            node.setProperty("name", NO_NAME);
            node.setProperty("description", NO_DESCRIPTION);
            log.info(String.format("Created new neo4j room node with ID %d", id));
            tx.success();
        }
        occupants = new CopyOnWriteArraySet<>();
    }

    public static NdRoom getDefaultRoom() throws NdException {
        if(home == null) throw new NdException("This server has no rooms");
        return home;
    }

    public Node getNode() {
        return node;
    }

    public Long getId() {
        return (Long)getProperty("id", null);
    }

    public boolean isHome() {
        return(0L == getId());
    }

    public String getName() {
        return (String)getProperty("name", NO_NAME);
    }

    public NdResult setName(String new_name) throws NdException {
        if(isHome()) {
            log.debug("Denied attempt to set name of Home room");
            throw new NdException("The Home room may not be renamed");
        }
        if(new_name.equals("Home")) {
            log.debug("Denied attempt to set name of room to Home");
            throw new NdException("No other room may be named 'Home'");
        }
        setProperty("name", new_name);
        log.info(String.format("Changed name of room to %s", new_name));
        return new NdResult("Room name changed to %s", new_name);
    }

    public NdResult setDescription(String new_description) {
        setProperty("description", new_description);
        return new NdResult("Changed description of room to %s", new_description);
    }

    public NdRoom getDestination(String exitName) throws NdException {
        NdRoom destination = null;
        log.info(String.format("Searching for exit %s from %s", exitName, this));
        try (Transaction tx = gdb.beginTx()) {
            for (Relationship r : node.getRelationships(EdgeType.EXIT, Direction.OUTGOING)) {
                String direction = (String)r.getProperty("direction");
                Node destination_node = r.getEndNode();
                Long destination_id = (Long)r.getProperty("id");

                if(direction.equalsIgnoreCase(exitName)) {
                    log.info(String.format("Found matching exit leading to room %d", destination_id));
                    destination = active_rooms.get(destination_id);
                    if(destination == null) {
                        destination = new NdRoom(destination_id, destination_node);
                        active_rooms.putIfAbsent(destination_id, destination);
                    }
                    break;
                }
            }
            tx.success();
        }
        if(destination == null) {
            log.info("No matching exit found");
            throw new NdException("No such exit found");
        }
        return destination;
    }

    public Set<String> getExitNames() {
        Set<String> exitNames = new CopyOnWriteArraySet<>();
        try(Transaction tx = gdb.beginTx()) {
            for(Relationship r : node.getRelationships(EdgeType.EXIT, Direction.OUTGOING)) {
                String exit_name = (String)r.getProperty("direction", null);
                if(exit_name != null) exitNames.add(exit_name);
            }
            tx.success();
        }
        return exitNames;
    }

    public String getAppearance() {
        StringJoiner sj = new StringJoiner("\n", "\n", "");
        sj.add(getName());
        sj.add(getDescription());
        if(hasExits()) {
            StringJoiner ej = new StringJoiner(", ", "Visible exits: ", ".");
            getExitNames().forEach(ej::add);
            sj.add(ej.toString());
        }
        return sj.toString();
    }

    public void tell(String message, NdPlayer from) {
        occupants.stream().forEach(p -> p.tell(message, from));
    }

    public void in(NdPlayer player) {
        log.info(String.format("Adding %s to %s", player, this));
        announceEntrance(player);
        occupants.add(player);
        player.setLocation(this);
        player.tell(getAppearance());
    }

    public void out(NdPlayer player) {
        log.info(String.format("Removing %s from %s", player, this));
        if(occupants.remove(player)) {
            player.setLocation(null);
            announceDeparture(player);
        } else {
            log.info(String.format("Unable to remove %s from occupants list", player));
        }
        if(occupants.isEmpty() && !isHome()) {
            log.info(String.format("%s now empty of occupants, removing from active rooms", this));
            active_rooms.remove(getId());
        }
    }

    public NdRoom dig(String direction) {
        NdRoom new_room;
        if(!getExitNames().contains(direction)) {
            log.info(String.format("Digging %s from room %d", direction, getId()));
            new_room = new NdRoom();
            log.info(String.format("Adding tunnel in %s direction", direction));
            addExit(direction, new_room);
            String oppdir = NdDirection.reverse(direction);
            log.info(String.format("Adding reverse tunnel in %s direction", oppdir));
            new_room.addExit(NdDirection.reverse(direction), this);
            return new_room;
        }
        return null;
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NdRoom && node.equals(((NdRoom)o).getNode());
    }

    @Override
    public String toString() {
        return String.format("Room[%d:%s]", getId(), getName());
    }


    /********************** PRIVATE ************************/

    private Object getProperty(String name, Object not_found) {
        Object o;
        try(Transaction tx = gdb.beginTx()) {
            o = node.getProperty(name, not_found);
            tx.success();
        }
        return o;
    }

    private void setProperty(String name, Object value) {
        try(Transaction tx = gdb.beginTx()) {
            node.setProperty(name, value);
            tx.success();
        }
    }

    private String getDescription() {
        return (String)getProperty("description", NO_DESCRIPTION);
    }

    private void addExit(String direction, NdRoom destination) {
        try(Transaction tx = gdb.beginTx()) {
            Relationship r = node.createRelationshipTo(destination.getNode(), EdgeType.EXIT);
            r.setProperty("direction", direction);
            tx.success();
            log.info(String.format("Added exit pointing %s leading to room %d", direction, destination.getId()));
        }
    }

    private boolean hasExits() {
        boolean has_exits;
        try(Transaction tx = gdb.beginTx()) {
            has_exits = node.hasRelationship(EdgeType.EXIT, Direction.OUTGOING);
            tx.success();
        }
        return has_exits;
    }

    private void announceEntrance(NdPlayer player) {
        String message = String.format("(%d) %s has entered the room.", player.getLineNumber(), player.getName());
        occupants.stream().forEach(p -> p.tell(message));
    }

    private void announceDeparture(NdPlayer player) {
        String message = String.format("(%d) %s has left the room.", player.getLineNumber(), player.getName());
        occupants.stream().forEach(p -> p.tell(message));
    }
}
