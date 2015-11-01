package com.benkimball.ndig;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

public class NdPlayer {
    private ChannelHandlerContext ctx;
    private String name;
    private Number line_number;
    private boolean quitting = false;

    public NdPlayer(ChannelHandlerContext ctx, Number line_number) {
        this.ctx = ctx;
        this.line_number = line_number;
        this.name = defaultName();
    }

    public String getName() {
        return name;
    }
    public Number getLineNumber() { return line_number; }
    public boolean isQuitting() { return quitting; }

    public ChannelFuture tell(String text) {
        return ctx.writeAndFlush(text + "\n");
    }

    public void setQuitting(boolean quitting) {
        this.quitting = quitting;
    }

    private String defaultName() {
        InetSocketAddress addr = (InetSocketAddress)ctx.channel().remoteAddress();
        return "guest" + addr.getPort();
    }
}
