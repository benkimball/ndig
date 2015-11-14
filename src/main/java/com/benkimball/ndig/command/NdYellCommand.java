package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

import java.util.regex.Matcher;

@Immutable
public class NdYellCommand implements NdCommand {
    private final String text;

    public NdYellCommand(String text) {
        this.text = text;
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        if(player.isHushed()) {
            player.tell("> You may not yell while hushed.");
        } else {
            String yell = String.format("(*%d, %s*) %s", player.getLineNumber(), player.getName(), text);
            game.roster.eachPlayer(p -> p.yell(yell, player));
        }
        return false;
    }
}
