package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import io.netty.channel.ChannelFuture;

import java.util.regex.Matcher;

public class NdEmoteCommand implements NdCommand {
    private final String text;

    public NdEmoteCommand(Matcher matcher) {
        text = matcher.group(1);
    }

    @Override
    public ChannelFuture invoke(NdGame game, NdPlayer player) {
        return player.tell(player.getName() + " " + text);
    }
}
