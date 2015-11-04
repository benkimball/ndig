package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdSayCommand implements NdCommand {
    private final String text;

    public NdSayCommand(Matcher m) {
        text = m.group(1);
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.tell(text);
        return false;
    }
}
