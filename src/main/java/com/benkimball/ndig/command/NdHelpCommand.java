package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdHelpCommand implements NdCommand {
    public NdHelpCommand(Matcher matcher) {
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.tell("Help is unimplemented.");
        return false;
    }
}
