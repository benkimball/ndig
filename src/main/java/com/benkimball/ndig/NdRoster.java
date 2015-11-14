package com.benkimball.ndig;

import io.netty.channel.ChannelHandlerContext;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@ThreadSafe
public class NdRoster {
    private final Object lock = new Object();
    @GuardedBy("lock") private final ConcurrentSkipListSet<Integer> available_lines;
    @GuardedBy("lock") private final ConcurrentHashMap<Integer,NdPlayer> lines;

    public NdRoster(int size) {
        available_lines = new ConcurrentSkipListSet();
        for (int ix = 0; ix < size; ix++) available_lines.add(ix);
        lines = new ConcurrentHashMap<>(size);
    }

    public NdPlayer createPlayer(ChannelHandlerContext ctx) {
        NdPlayer player = null;
        synchronized (lock) {
            Integer line_number = available_lines.pollFirst();
            if(line_number != null) {
                player = new NdPlayer(ctx, line_number);
                lines.put(line_number, player);
            }
        }
        return player;
    }

    public NdPlayer getPlayer (Integer line_number) {
        synchronized (lock) {
            return lines.get(line_number);
        }
    }

    public void removePlayer(NdPlayer player) {
        Integer line_number = player.getLineNumber();
        synchronized (lock) {
            if (lines.remove(line_number, player)) {
                available_lines.add(line_number);
            }
        }
    }

    public boolean requestName(NdPlayer player, String name) {
        synchronized (lock) {
            if(lines.values().stream().noneMatch(p -> p.getName().equals(name))) {
                player.setName(name);
                return true;
            } else return false;
        }
    }
}
