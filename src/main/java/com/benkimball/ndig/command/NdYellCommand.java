package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

import java.util.regex.Matcher;

@Immutable
public class NdYellCommand implements NdCommand {
    private final String text;

    public NdYellCommand(String text) {
        this.text = text;
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.tell("Yell is unimplemented.");
        return false;
    }
}
