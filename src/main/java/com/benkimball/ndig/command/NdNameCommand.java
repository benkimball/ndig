package com.benkimball.ndig.command;

import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdNameCommand implements NdCommand {
    public NdNameCommand(Matcher matcher) {
    }

    @Override
    public void invoke(Object gameContext, NdPlayer player) {
        // player.setName(msg)
    }
}
