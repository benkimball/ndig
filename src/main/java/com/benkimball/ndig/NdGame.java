package com.benkimball.ndig;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetSocketAddress;

public class NdGame {
    private NdNumberPool lines;

    public NdGame(int max_connections) {
        lines = new NdNumberPool(max_connections);
    }

    public final ChannelGroup allChannels =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public NdPlayer createPlayer(ChannelHandlerContext ctx) {
        NdPlayer player = null;
        Number line_number = lines.acquire();
        if(line_number != null) {
            player = new NdPlayer(ctx, line_number);
            allChannels.add(ctx.channel());
        }
        return player;
    }

    public void handleLogout(NdPlayer player) {
        Number line_number = player.getLineNumber();
        lines.release(line_number);
        player = null; // TODO is this necessary?
    }
}
