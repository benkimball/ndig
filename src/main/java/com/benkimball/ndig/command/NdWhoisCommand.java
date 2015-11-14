package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

import java.util.regex.Matcher;

@Immutable
public class NdWhoisCommand implements NdCommand {
    private final int line_number;

    public NdWhoisCommand(int line_number) {
        this.line_number = line_number;
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        NdPlayer subject = game.roster.getPlayer(line_number);
        player.tell("> " + subject.whois());
        return false;
    }
}
