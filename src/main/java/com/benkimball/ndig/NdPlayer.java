package com.benkimball.ndig;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import net.jcip.annotations.GuardedBy;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NdPlayer {
    private final ChannelHandlerContext ctx;
    private final Integer line_number;
    @GuardedBy("this") private String name;
    @GuardedBy("this") private NdNode location;
    private final Set<NdPlayer> ignores = Collections.synchronizedSet(new HashSet<>());

    public NdPlayer(ChannelHandlerContext ctx, Integer line_number) {
        this.ctx = ctx;
        this.line_number = line_number;
        this.name = defaultName();
        this.location = null;
    }

    public synchronized String getName() {
        return name;
    }

//    public synchronized void setName(String new_name) {
//        name = new_name;
//    }

    public Integer getLineNumber() { return line_number; }

    public ChannelFuture tell(String text) {
        return ctx.writeAndFlush(text + "\n");
    }

    public ChannelFuture tell(String message, NdPlayer from) {
        if(!ignores.contains(from)) return tell(message);
        return null;
    }

    public synchronized void setLocation(NdNode location) {
        this.location = location;
    }

    public synchronized NdNode getLocation() {
        return location;
    }

    public boolean toggleIgnore(NdPlayer other) {
        synchronized(ignores) {
            if(ignores.contains(other)) {
                ignores.remove(other);
                return false;
            } else {
                ignores.add(other);
                return true;
            }
        }
    }

    private String defaultName() {
        InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
        return "guest" + address.getPort();
    }
}
