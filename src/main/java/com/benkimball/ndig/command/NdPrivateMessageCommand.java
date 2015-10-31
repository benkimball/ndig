package com.benkimball.ndig.command;

import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdPrivateMessageCommand implements NdCommand {
    public NdPrivateMessageCommand(Matcher matcher) {
    }

    @Override
    public void invoke(Object gameContext, NdPlayer player) {
        // game.getPlayerByLine(line).ignore(player).tell(msg)
    }
}
