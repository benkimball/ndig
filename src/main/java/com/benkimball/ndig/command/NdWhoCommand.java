package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

@Immutable
public class NdWhoCommand implements NdCommand {

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        String fmt = "%-5s %-20.20s %-8s %-30.30s %-8s";
        player.tell(String.format(fmt, "line", "name", "idle", "from", "online"));
        game.roster.eachPlayer(p -> player.tell(String.format(fmt, p.getLineNumber(), p.getName(), p.getIdleDuration(),
                p.getFrom(), p.getLoginDuration())));
        return false;
    }
}
