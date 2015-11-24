package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdRoom;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

@Immutable
public class NdMoveCommand implements NdCommand {
    private final String direction;

    public NdMoveCommand(String direction) {
        this.direction = direction;
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        NdRoom current_room = player.getLocation();
        NdRoom new_room = current_room.getExit(direction);
        if(new_room == null) {
            player.tell("There is no exit called " + direction + ".");
        } else {
            current_room.out(player);
            new_room.in(player);
        }
        return false;
    }
}
