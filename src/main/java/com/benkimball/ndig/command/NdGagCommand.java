package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

import java.util.regex.Matcher;

@Immutable
public class NdGagCommand implements NdCommand {
    private final int line_number;

    public NdGagCommand(int line_number) {
        this.line_number = line_number;
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        if(player.getLineNumber() == line_number) {
            player.tell("You cannot ignore yourself.");
        } else {
            NdPlayer brat = game.roster.getPlayer(line_number);
            boolean ignoring = player.toggleIgnore(brat);
            if(ignoring) {
                player.tell(String.format("Ignoring (%d) %s", line_number, brat.getName()));
            } else {
                player.tell(String.format("No longer ignoring (%d) %s", line_number, brat.getName()));
            }
        }
        return false;
    }
}
