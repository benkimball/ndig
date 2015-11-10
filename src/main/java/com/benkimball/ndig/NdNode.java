package com.benkimball.ndig;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.CopyOnWriteArraySet;

@ThreadSafe
public class NdNode {
    @GuardedBy("this") private String name;
    @GuardedBy("this") private String description;
    @GuardedBy("this") private final CopyOnWriteArraySet<NdPlayer> occupants =
            new CopyOnWriteArraySet<NdPlayer>();

    public NdNode(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public synchronized void in(NdPlayer player) {
        announceEntrance(player);
        occupants.add(player);
        player.setLocation(this);
        player.tell(name);
        player.tell(description);
    }

    public synchronized void out(NdPlayer player) {
        if(occupants.remove(player)) {
            player.setLocation(null);
            announceDeparture(player);
        }
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void setName(String new_name) {
        name = new_name;
    }

    public synchronized String getDescription() {
        return description;
    }

    public synchronized void setDescription(String new_description) {
        description = new_description;
    }

    public synchronized void tell(String message, NdPlayer from) {
        occupants.stream().forEach(p -> p.tell(message, from));
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
