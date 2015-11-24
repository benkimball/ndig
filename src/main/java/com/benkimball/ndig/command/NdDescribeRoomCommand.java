package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

@Immutable
public class NdDescribeRoomCommand implements NdCommand {
    private final String description;

    public NdDescribeRoomCommand(String description) {
        this.description = description;
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.getLocation().setDescription(description);
        return false;
    }
}
