package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import io.netty.channel.ChannelFuture;

public interface NdCommand {
    public ChannelFuture invoke(NdGame game, NdPlayer player);
}
