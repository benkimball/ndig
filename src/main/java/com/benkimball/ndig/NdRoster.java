package com.benkimball.ndig;

import io.netty.channel.ChannelHandlerContext;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Consumer;

@ThreadSafe
public class NdRoster {

    private static final Log log = LogFactory.getLog("NdRoster");

    private final Object lock = new Object();
    @GuardedBy("lock") private final ConcurrentSkipListSet<Integer> available_lines;
    @GuardedBy("lock") private final ConcurrentHashMap<Integer,NdPlayer> lines;

    public NdRoster(int size) {
        available_lines = new ConcurrentSkipListSet<>();
        for (int ix = 1; ix < size; ix++) available_lines.add(ix);
        lines = new ConcurrentHashMap<>(size);
        log.info(String.format("Created for %d lines", size));
    }

    public NdPlayer createPlayer(ChannelHandlerContext ctx) throws NdException {
        NdPlayer player;
        synchronized (lock) {
            Integer line_number = available_lines.pollFirst();
            if(line_number != null) {
                player = new NdPlayer(ctx, line_number);
                lines.put(line_number, player);
                log.info(String.format("Assigned new player to line number %d", line_number));
            } else {
                log.info("No available line number for new player");
                throw new NdException("Server is full");
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
                log.info(String.format("Released line number %d", line_number));
            } else {
                log.info(String.format("Unable to release line number %d", line_number));
            }
        }
    }

    public boolean requestName(NdPlayer player, String name) {
        synchronized (lock) {
            if(lines.values().stream().noneMatch(p -> p.getName().equals(name))) {
                log.info(String.format("Name %s is available", name));
                player.setName(name);
                return true;
            } else {
                log.info(String.format("Name %s is not available", name));
                return false;
            }
        }
    }

    public void eachPlayer(Consumer<NdPlayer> fn) {
        synchronized (lock) {
            lines.values().stream().forEach(fn);
        }
    }
}
