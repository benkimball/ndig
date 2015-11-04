package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import io.netty.channel.ChannelFuture;

import java.util.regex.Matcher;

public class NdBroadcastCommand implements NdCommand {
    private final String text;

    public NdBroadcastCommand(Matcher matcher) {
        text = matcher.group(1);
    }

    @Override
    public ChannelFuture invoke(NdGame game, NdPlayer player) {
        game.allChannels.writeAndFlush("\n*** SERVER MESSAGE: " + text + " ***\n");
        return null;
    }
}
