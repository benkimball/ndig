package com.benkimball.ndig;

import net.jcip.annotations.ThreadSafe;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
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

    private static final String NO_NAME = "Void";
    private static final String NO_DESCRIPTION =
            "This room is without form, and void; and darkness is upon the face of the deep.";

    private static final GraphDatabaseService gdb;
    private static final NdRoom home;
    private static AtomicInteger last_id = new AtomicInteger(0);
    private static final Map<Integer,NdRoom> active_rooms;

    static {
        gdb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("target/graph.db"));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() { gdb.shutdown(); }
        });
    }

    static {
        try(Transaction tx = gdb.beginTx();
            Result result = gdb.execute("MERGE (n:Room {id: 0})" +
                    " ON CREATE SET" +
                    " n.name='Home'" +
                    ", n.description='This comfortable room has lots of rugs and pillows.'" +
                    " RETURN n")) {
            home = new NdRoom((Node)result.columnAs("n").next());
            tx.success();
        }

        active_rooms = new ConcurrentHashMap<>();
        active_rooms.put(0, home); // home is always active
    }

    private final Node node;
    private final Set<NdPlayer> occupants;

    public NdRoom(Node node) {
        this.node = node;
        this.occupants = new CopyOnWriteArraySet<>();
    }

    public NdRoom() {
        try (Transaction tx = gdb.beginTx()) {
            node = gdb.createNode(NodeType.ROOM);
            node.setProperty("id", last_id.incrementAndGet());
            node.setProperty("name", NO_NAME);
            node.setProperty("description", NO_DESCRIPTION);
            tx.success();
        }
        occupants = new CopyOnWriteArraySet<>();
    }

    public static NdRoom getDefaultRoom() {
        return home;
    }

    public Node getNode() {
        return node;
    }

    public Integer getId() {
        return (Integer)getProperty("id", null);
    }

    public String getName() {
        return (String)getProperty("name", NO_NAME);
    }

    public boolean setName(String new_name) {
        if(new_name.equals("Home")) return false;
        setProperty("name", new_name);
        return true;
    }

    public String getDescription() {
        return (String)getProperty("description", NO_DESCRIPTION);
    }

    public void setDescription(String new_description) {
        setProperty("description", new_description);
    }

    public void addExit(String direction, NdRoom destination) {
        try(Transaction tx = gdb.beginTx()) {
            Relationship r = node.createRelationshipTo(destination.getNode(), EdgeType.EXIT);
            r.setProperty("direction", direction);
            tx.success();
        }
    }

    public boolean hasExits() {
        boolean has_exits;
        try(Transaction tx = gdb.beginTx()) {
            has_exits = node.hasRelationship(EdgeType.EXIT, Direction.OUTGOING);
            tx.success();
        }
        return has_exits;
    }

    public NdRoom getDestination(String exitName) {
        NdRoom destination = null;
        Map<String,Object> params = new HashMap<>();
        params.put("d", exitName);
        try(Transaction tx = gdb.beginTx();
            Result result = gdb.execute("MATCH (:Room)-[:EXIT {direction: {d}}]->(n:Room) RETURN n;", params)) {
            if(result.hasNext()) {
                Node destination_node = (Node)result.columnAs("n");
                Integer destination_id = (Integer)node.getProperty("id");
                destination = active_rooms.putIfAbsent(destination_id, new NdRoom(destination_node));
            }
            tx.success();
        }
        return destination;
    }

    public List<String> getExitNames() {
        List<String> exitNames = new CopyOnWriteArrayList<>();
        try(Transaction tx = gdb.beginTx()) {
            for(Relationship r : node.getRelationships(EdgeType.EXIT, Direction.OUTGOING)) {
                String exit_name = (String)r.getProperty("direction", null);
                if(exit_name != null) exitNames.add(exit_name);
            }
            tx.success();
        }
        return exitNames;
    }

    public String getExitsAsString() {
        StringJoiner sj = new StringJoiner(", ", "Visible exits: ", ".");
        getExitNames().forEach(sj::add);
        return sj.toString();
    }

    public String getAppearance() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add(getName());
        sj.add(getDescription());
        if(hasExits()) sj.add(getExitsAsString());
        return sj.toString();
    }

    public void tell(String message, NdPlayer from) {
        occupants.stream().forEach(p -> p.tell(message, from));
    }

    public void in(NdPlayer player) {
        announceEntrance(player);
        occupants.add(player);
        player.setLocation(this);
        player.tell(getAppearance());
    }

    public void out(NdPlayer player) {
        if(occupants.remove(player)) {
            player.setLocation(null);
            announceDeparture(player);
        }
        if(occupants.isEmpty()) {
            active_rooms.remove(getId());
        }
    }

    public NdRoom dig(String direction) {
        NdRoom new_room;
        if(!getExitNames().contains(direction)) {
            new_room = new NdRoom();
            addExit(direction, new_room);
            String oppdir = NdDirection.reverse(direction);
            if(oppdir != null) {
                new_room.addExit(NdDirection.reverse(direction), this);
            }
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

    private void announceEntrance(NdPlayer player) {
        String message = String.format("(%d) %s has entered the room.", player.getLineNumber(), player.getName());
        occupants.stream().forEach(p -> p.tell(message));
    }

    private void announceDeparture(NdPlayer player) {
        String message = String.format("(%d) %s has left the room.", player.getLineNumber(), player.getName());
        occupants.stream().forEach(p -> p.tell(message));
    }
}
