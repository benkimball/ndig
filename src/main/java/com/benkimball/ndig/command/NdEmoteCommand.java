package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdEmoteCommand implements NdCommand {
    private final String text;

    public NdEmoteCommand(Matcher matcher) {
        text = matcher.group(1);
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        String message = String.format("(%d) %s %s\n", player.getLineNumber(), player.getName(), text);
        game.broadcast(message);
        return false;
    }
}
