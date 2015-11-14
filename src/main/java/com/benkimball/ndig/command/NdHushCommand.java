package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

import java.util.regex.Matcher;

@Immutable
public class NdHushCommand implements NdCommand {

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.tell("Hush is unimplemented.");
        return false;
    }
}