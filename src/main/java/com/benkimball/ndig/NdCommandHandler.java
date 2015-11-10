package com.benkimball.ndig;

import com.benkimball.ndig.command.NdCommand;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.Channel;
import java.util.ArrayList;

public class NdCommandHandler extends SimpleChannelInboundHandler<NdCommand> {

    private final NdGame game = NdServer.game;
    private NdPlayer player;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        player = game.createPlayer(ctx);
        if(player == null) {
            ctx.writeAndFlush("Sorry, server is full.\n\n").addListener(ChannelFutureListener.CLOSE);
        } else {
            ctx.write("Connected to ndig\n");
            ctx.write("Your name is " + player.getName() + "\n");
            ctx.write("You have line " + player.getLineNumber() + "\n\n");
            game.home.in(player);
            ctx.flush();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        game.handleLogout(player);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NdCommand in) throws Exception {
        boolean quitting = in.invoke(game, player);
        if(quitting) ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
