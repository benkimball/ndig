package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdRoom;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

@Immutable
public class NdNameRoomCommand implements NdCommand {
    private final String name;

    public NdNameRoomCommand(String name) {
        this.name = name;
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        if(name.equals("Home")) {
            player.tell("Invalid name.");
        } else {
            player.getLocation().setName(name);
            player.tell("Room renamed.");
        }
        return false;
    }
}
