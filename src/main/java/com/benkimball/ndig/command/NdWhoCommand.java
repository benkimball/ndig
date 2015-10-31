package com.benkimball.ndig.command;

import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdWhoCommand implements NdCommand {
    public NdWhoCommand(Matcher matcher) {
    }

    @Override
    public void invoke(Object gameContext, NdPlayer player) {
        // player.tell game.getAllPlayers.map(info)
    }
}
