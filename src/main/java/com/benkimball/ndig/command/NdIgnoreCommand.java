package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdIgnoreCommand implements NdCommand {
    public NdIgnoreCommand(Matcher matcher) {
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.tell("Ignore is unimplemented.");
        return false;
    }
}
