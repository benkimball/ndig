package com.benkimball.ndig;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetSocketAddress;

public class NdGame {
    private NdNumberPool lines;
    private NdNode home;

    public NdGame(int max_connections) {
        lines = new NdNumberPool(max_connections);
        home = new NdNode("Home", "This comfortable room contains many rugs and pillows.");
    }

    private final ChannelGroup allChannels =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public NdPlayer createPlayer(ChannelHandlerContext ctx) {
        NdPlayer player = null;
        Number line_number = lines.acquire();
        if(line_number != null) {
            player = new NdPlayer(ctx, line_number);
            home.in(player);
            allChannels.add(ctx.channel());
        }
        return player;
    }

    public void handleLogout(NdPlayer player) {
        NdNode location = player.getLocation();
        if(location != null) {
            location.out(player);
        }
        Number line_number = player.getLineNumber();
        lines.release(line_number);
        player = null; // TODO is this necessary?
    }

    public void broadcast(String message) {
        allChannels.writeAndFlush(message);
    }
}
