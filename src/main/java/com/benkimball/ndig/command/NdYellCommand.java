package com.benkimball.ndig.command;

import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdYellCommand implements NdCommand {
    public NdYellCommand(Matcher matcher) {
    }

    @Override
    public void invoke(Object gameContext, NdPlayer player) {
        //game.getAllPlayers.reject(hushed).ignore(player).each(tell msg)
    }
}
