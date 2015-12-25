package com.benkimball.ndig;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ThreadSafe
public class NdGame {
    private static final Log log = LogFactory.getLog("NdGame");

    public final NdRoster roster; // is thread safe

    private final ChannelGroup allChannels =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public NdGame(int max_connections) {
        this.roster = new NdRoster(max_connections);
        log.info("Created for "+Integer.toString(max_connections)+" connections");
    }

    public NdPlayer handleLogin(ChannelHandlerContext ctx) {
        NdPlayer player = roster.createPlayer(ctx);
        if(player != null) {
            broadcast(String.format("New arrival on line %d.", player.getLineNumber()));
            allChannels.add(ctx.channel());
        }
        return player;
    }

    public void handleLogout(NdPlayer player) {
        String message = String.format("Line %d (%s) has departed.", player.getLineNumber(), player.getName());
        NdRoom room = player.getLocation();
        if(room != null) {
            room.out(player);
        }
        roster.removePlayer(player);
        broadcast(message);
        log.info(String.format("%s [%d] logged out", player.getName(), player.getLineNumber()));
    }

    public void broadcast(String message) {
        allChannels.writeAndFlush(message + "\n");
        log.info("BROADCAST: "+message);
    }
}
