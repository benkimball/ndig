package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdRoom;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

@Immutable
public class NdDigCommand implements NdCommand {
    private final String direction;

    public NdDigCommand(String direction) {
        this.direction = direction;
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        NdRoom new_room = player.getLocation().dig(direction);
        if(new_room == null) {
            player.tell("You can't dig that way.");
        } else {
            new_room.in(player);
        }
        return false;
    }
}
