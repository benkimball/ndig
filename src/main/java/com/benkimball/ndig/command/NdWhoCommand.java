package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import io.netty.channel.ChannelFuture;

import java.util.regex.Matcher;

public class NdWhoCommand implements NdCommand {
    public NdWhoCommand(Matcher matcher) {
    }

    @Override
    public ChannelFuture invoke(NdGame game, NdPlayer player) {
        return player.tell("Who is unimplemented.");
    }
}
