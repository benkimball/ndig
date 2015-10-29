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
import java.util.ArrayList;

@Sharable
public class NdServerHandler extends SimpleChannelInboundHandler<String> {

    private NdPlayer player;

    private static final String CRLF = "\n";
    private static final String PROMPT = "> ";
    private static final String line(String s) {
        return(s + CRLF);
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
        player = new NdPlayer(addr);
        ctx.write("\033[2J");
        ctx.write(line("Welcome to ndig."));
        ctx.write(CRLF);
        ctx.write(line("Your name is " + player.getName()));
        ctx.write(line("Use `/name <newname>` to register."));
        ctx.write(CRLF);
        ctx.write(PROMPT);
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String in) throws Exception {
        String out;
        boolean close = false;

        if (in.isEmpty()) {
            out = line("Sorry, I didn't get that.");
        } else if ("exit".equalsIgnoreCase(in) || "quit".equalsIgnoreCase(in)) {
            out = "Bye!" + CRLF + CRLF;
            close = true;
        } else {
            out = line("I heard you say, '" + in + "'.");
        }
        ChannelFuture future = ctx.write(out + PROMPT);
        if(close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            ctx.writeAndFlush("\033[s\r\033[Kspam"+CRLF+PROMPT+"\033[u");
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
