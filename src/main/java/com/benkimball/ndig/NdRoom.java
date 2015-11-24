package com.benkimball.ndig;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.neo4j.graphdb.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

enum EdgeType implements RelationshipType {
    EXIT
}

@ThreadSafe
public class NdRoom {
    @GuardedBy("this") private final CopyOnWriteArraySet<NdPlayer> occupants =
            new CopyOnWriteArraySet<>();
    private final Node node;

    private static final String NO_NAME = "Void";
    private static final String NO_DESC = "This room is without form, and void; and darkness is upon the face of the deep.";

    public NdRoom(Node node) {
        this.node = node;
    }

    protected Node getNode() {
        return node;
    }

    public synchronized void in(NdPlayer player) {
        announceEntrance(player);
        occupants.add(player);
        player.setLocation(this);
        player.tell(getAppearance());
    }

    public synchronized void out(NdPlayer player) {
        if(occupants.remove(player)) {
            player.setLocation(null);
            announceDeparture(player);
        }
    }

    public synchronized String getName() {
        String name;
        try(Transaction tx = node.getGraphDatabase().beginTx()) {
            name = (String)node.getProperty("name", NO_NAME);
            tx.success();
        }
        return name;
    }

    public synchronized boolean setName(String new_name) {
        if(new_name.equals("Home")) return false;

        try(Transaction tx = node.getGraphDatabase().beginTx()) {
            node.setProperty("name", new_name);
            tx.success();
        }
        return true;
    }

    public synchronized String getDescription() {
        String description;
        try(Transaction tx = node.getGraphDatabase().beginTx()) {
            description = (String)node.getProperty("description", NO_DESC);
            tx.success();
        }
        return description;
    }

    public synchronized void setDescription(String new_description) {
        try(Transaction tx = node.getGraphDatabase().beginTx()) {
            node.setProperty("description", new_description);
            tx.success();
        }
    }

    public synchronized String getAppearance() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add(getName());
        sj.add(getDescription());
        if(hasExits()) sj.add(getExitsAsString());
        return sj.toString();
    }

    public synchronized NdRoom dig(String direction) {
        NdRoom new_room = null;
        Node new_node = null;
        try(Transaction tx = node.getGraphDatabase().beginTx()) {
            if(!getExits().contains(direction)) {
                new_node = node.getGraphDatabase().createNode(DynamicLabel.label("Room"));
                Relationship tunnel = node.createRelationshipTo(new_node, EdgeType.EXIT);
                tunnel.setProperty("name", direction);
            }
            tx.success();
        }
        if(new_node != null) new_room = new NdRoom(new_node);
        return new_room;
    }

    public synchronized boolean hasExits() {
        boolean has_exits;
        try(Transaction tx = node.getGraphDatabase().beginTx()) {
            has_exits = node.hasRelationship(EdgeType.EXIT, Direction.OUTGOING);
            tx.success();
        }
        return has_exits;
    }

    public synchronized List<String> getExits() {
        List<String> exits = Collections.synchronizedList(new ArrayList<>());
        try(Transaction tx = node.getGraphDatabase().beginTx()) {
            for(Relationship r : node.getRelationships(EdgeType.EXIT, Direction.OUTGOING)) {
                String exit_name = (String)r.getProperty("name", null);
                if(exit_name != null) exits.add(exit_name);
            }
            tx.success();
        }
        return exits;
    }

    public synchronized String getExitsAsString() {
        StringJoiner sj = new StringJoiner(", ", "Visible exits: ", ".");
        getExits().forEach(sj::add);
        return sj.toString();
    }

    public synchronized NdRoom getExit(String name) {
        NdRoom new_room = null;
        Node new_node = null;
        try(Transaction tx = node.getGraphDatabase().beginTx()) {
            for(Relationship r : node.getRelationships(EdgeType.EXIT, Direction.OUTGOING)) {
                String exit_name = (String)r.getProperty("name", null);
                if(exit_name != null && exit_name.equals(name)) {
                    new_node = r.getEndNode();
                    break;
                }
            }
            tx.success();
        }
        if(new_node != null) new_room = new NdRoom(new_node);
        return new_room;
    }

    public synchronized void tell(String message, NdPlayer from) {
        occupants.stream().forEach(p -> p.tell(message, from));
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
        return "Node[" + getName() + "]";
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
