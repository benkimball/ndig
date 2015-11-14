package com.benkimball.ndig;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import net.jcip.annotations.GuardedBy;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NdPlayer {
    private final ChannelHandlerContext ctx;
    private final Integer line_number;
    private final Instant on_since;
    private Instant last_seen;
    @GuardedBy("this") private String name;
    @GuardedBy("this") private NdNode location;
    private final Set<NdPlayer> ignores = Collections.synchronizedSet(new HashSet<>());
    private final String from;
    private boolean hushed;

    public NdPlayer(ChannelHandlerContext ctx, Integer line_number) {
        this.ctx = ctx;
        InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
        this.from = address.getHostName();
        this.name = "guest" + address.getPort();
        this.line_number = line_number;
        this.location = null;
        this.on_since = Instant.now();
        this.last_seen = Instant.from(on_since);
        this.hushed = false;
    }

    public void seen() {
        last_seen = Instant.now();
    }

    public String whois() {
        return String.format("(%d) %s, idle %s, on for %s\n", getLineNumber(), getName(),
                getIdleDuration(), getLoginDuration());
    }

    public String getIdleDuration() {
        return NdDurationFormatter.format(Duration.between(last_seen, Instant.now()));
    }

    public String getLoginDuration() {
        return NdDurationFormatter.format(Duration.between(on_since, Instant.now()));
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void setName(String new_name) {
        name = new_name;
    }

    public String getFrom() {
        return from;
    }

    public Integer getLineNumber() { return line_number; }

    public ChannelFuture tell(String text) {
        return ctx.writeAndFlush(text + "\n");
    }

    public ChannelFuture tell(String message, NdPlayer from) {
        if(!ignores.contains(from)) return tell(message);
        return null;
    }

    public ChannelFuture yell(String message, NdPlayer from) {
        if(!hushed) return tell(message, from);
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

    public boolean toggleHushed() {
        hushed = !hushed;
        return hushed;
    }

    public boolean isHushed() {
        return hushed;
    }
}
