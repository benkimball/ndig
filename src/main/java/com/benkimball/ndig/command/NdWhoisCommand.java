package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdWhoisCommand implements NdCommand {
    public NdWhoisCommand(Matcher matcher) {
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.tell("Whois is unimplemented.");
        return false;
    }
}
