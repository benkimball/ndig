package com.benkimball.ndig.command;

import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdHushCommand implements NdCommand {
    public NdHushCommand(Matcher matcher) {
    }

    @Override
    public void invoke(Object gameContext, NdPlayer player) {
        // player.setHush(true)
    }
}
