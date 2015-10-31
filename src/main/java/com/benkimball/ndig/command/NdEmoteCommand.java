package com.benkimball.ndig.command;

import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdEmoteCommand implements NdCommand {
    private final String message;

    public NdEmoteCommand(Matcher matcher) {
        message = matcher.group(1);
    }

    @Override
    public void invoke(Object gameContext, NdPlayer player) {
        /* player.location.peeps.ignore(player).tell(message) */
    }
}
