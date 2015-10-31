package com.benkimball.ndig.command;

import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdWhoisCommand implements NdCommand {
    public NdWhoisCommand(Matcher matcher) {
    }

    @Override
    public void invoke(Object gameContext, NdPlayer player) {
        // player.tell game.getPlayerByLine(line).info
    }
}
