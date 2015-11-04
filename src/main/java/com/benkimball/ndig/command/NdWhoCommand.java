package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdWhoCommand implements NdCommand {
    public NdWhoCommand(Matcher matcher) {
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.tell("Who is unimplemented.");
        return false;
    }
}
