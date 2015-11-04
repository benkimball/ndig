package com.benkimball.ndig;

public class NdNode {
    public String name;
    public String description;
    public NdPlayerSet occupants;

    public NdNode(String name, String description) {
        this.name = name;
        this.description = description;
        occupants = new NdPlayerSet();
    }

    public void in(NdPlayer player) {
        announceEntrance(player);
        occupants.add(player);
        player.setLocation(this);
        player.tell(name);
        player.tell(description);
    }

    public void out(NdPlayer player) {
        if(occupants.remove(player)) {
            player.setLocation(null);
            announceDeparture(player);
        }
    }

    private void announceEntrance(NdPlayer player) {
        String message = String.format("(%d) %s has entered.", player.getLineNumber(), player.getName());
        occupants.stream().forEach(p -> p.tell(message));
    }

    private void announceDeparture(NdPlayer player) {
        String message = String.format("(%d) %s has left.", player.getLineNumber(), player.getName());
        occupants.stream().forEach(p -> p.tell(message));
    }
}
