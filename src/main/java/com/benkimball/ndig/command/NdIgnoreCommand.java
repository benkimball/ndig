package com.benkimball.ndig.command;

import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdIgnoreCommand implements NdCommand {
    public NdIgnoreCommand(Matcher matcher) {
    }

    @Override
    public void invoke(Object gameContext, NdPlayer player) {
        // player.ignores.add(game.getPlayerByLine(line))
    }
}
