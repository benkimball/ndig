package com.benkimball.ndig;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.util.concurrent.CopyOnWriteArraySet;

@ThreadSafe
public class NdNode {
    @GuardedBy("this") private final CopyOnWriteArraySet<NdPlayer> occupants =
            new CopyOnWriteArraySet<>();
    private final Node neonode;

    public NdNode(Node neonode) {
        this.neonode = neonode;
    }

    protected Node getNeonode() {
        return neonode;
    }

    public synchronized void in(NdPlayer player) {
        announceEntrance(player);
        occupants.add(player);
        player.setLocation(this);
        player.tell(getName());
        player.tell(getDescription());
    }

    public synchronized void out(NdPlayer player) {
        if(occupants.remove(player)) {
            player.setLocation(null);
            announceDeparture(player);
        }
    }

    public synchronized String getName() {
        String name;
        try(Transaction tx = neonode.getGraphDatabase().beginTx()) {
            name = (String)neonode.getProperty("name");
            tx.success();
        }
        return name;
    }

//    public synchronized NdNode setName(String new_name) {
//        try(Transaction tx = neonode.getGraphDatabase().beginTx()) {
//            neonode.setProperty("name", new_name);
//            tx.success();
//        }
//        return this;
//    }

    public synchronized String getDescription() {
        String description;
        try(Transaction tx = neonode.getGraphDatabase().beginTx()) {
            description = (String)neonode.getProperty("description");
            tx.success();
        }
        return description;
    }

//    public synchronized NdNode setDescription(String new_description) {
//        try(Transaction tx = neonode.getGraphDatabase().beginTx()) {
//            neonode.setProperty("description", new_description);
//            tx.success();
//        }
//        return this;
//    }

    public synchronized void tell(String message, NdPlayer from) {
        occupants.stream().forEach(p -> p.tell(message, from));
    }

    @Override
    public int hashCode() {
        return neonode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NdNode && neonode.equals(((NdNode)o).getNeonode());
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
