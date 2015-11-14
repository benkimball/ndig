package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

import java.util.regex.Matcher;

@Immutable
public class NdPageCommand implements NdCommand {
    private final int line_number;
    private final String text;

    public NdPageCommand(int line_number, String text) {
        this.line_number = line_number;
        this.text = text;
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.tell("Private message is unimplemented.");
        return false;
    }
}
