package com.benkimball.ndig;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.jcip.annotations.NotThreadSafe;

import java.util.concurrent.ConcurrentHashMap;

@NotThreadSafe // or is it?
public class NdGame {
    public final NdRoster roster;
    private final NdNode home;

    private final ChannelGroup allChannels =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public NdGame(int max_connections) {
        roster = new NdRoster(max_connections);
        home = new NdNode("Home", "This comfortable room contains many rugs and pillows.");
    }

    public NdPlayer createPlayer(ChannelHandlerContext ctx) {
        NdPlayer player = roster.createPlayer(ctx);
        if(player != null) {
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
        roster.removePlayer(player);
    }

    public void broadcast(String message) {
        allChannels.writeAndFlush(message);
    }
}
