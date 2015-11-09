package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

import java.util.regex.Matcher;

@Immutable
public class NdIgnoreCommand implements NdCommand {
    private final int line_number;

    public NdIgnoreCommand(Matcher matcher) {
        line_number = Integer.parseInt(matcher.group(1));
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        NdPlayer brat = game.roster.getPlayer(line_number);
        player.ignore(brat);
        player.tell(String.format("Ignoring (%d) %s", line_number, player.getName()));
        return false;
    }
}
