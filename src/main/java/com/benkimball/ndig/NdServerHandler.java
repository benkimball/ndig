package com.benkimball.ndig;

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

public class NdServerHandler extends SimpleChannelInboundHandler<String> {

    private NdPlayer player;

    private static final String CRLF = "\n";
    private static final String PROMPT = "> ";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
        player = new NdPlayer(addr);
        ctx.write("Connected to ndig" + CRLF);
        ctx.write("Your name is " + player.getName() + CRLF);
        ctx.write(PROMPT);
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String in) throws Exception {
        if(in.isEmpty()) { return; }
        if ("exit".equalsIgnoreCase(in) || "quit".equalsIgnoreCase(in)) {
            ctx.writeAndFlush("Bye!" + CRLF + CRLF).addListener(ChannelFutureListener.CLOSE);
        } else {
            ctx.writeAndFlush("I heard you say, '" + in + "'." + CRLF);
        }
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
