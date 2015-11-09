package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

import java.util.regex.Matcher;

@Immutable
public class NdBroadcastCommand implements NdCommand {
    private final String text;

    public NdBroadcastCommand(Matcher matcher) {
        text = matcher.group(1);
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        game.broadcast("\n*** SERVER MESSAGE: " + text + " ***\n");
        return false;
    }
}
