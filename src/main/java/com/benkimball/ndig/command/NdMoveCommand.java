package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

@Immutable
public class NdMoveCommand implements NdCommand {
    private final String direction;

    public NdMoveCommand(String direction) {
        this.direction = direction.toLowerCase();
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.follow(direction);
        return false;
    }
}
