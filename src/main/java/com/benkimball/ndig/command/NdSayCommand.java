package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

import java.util.regex.Matcher;

@Immutable
public class NdSayCommand implements NdCommand {
    private final String text;

    public NdSayCommand(String text) {
        this.text = text;
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        String message = String.format("(%d, %s) %s", player.getLineNumber(), player.getName(), text);
        player.getLocation().tell(message, player);
        return false;
    }
}
