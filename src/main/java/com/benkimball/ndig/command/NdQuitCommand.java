package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public class NdQuitCommand implements NdCommand {
    public NdQuitCommand(Matcher matcher) {
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.tell("Bye!");
        player.getLocation().out(player);
        return true;
    }
}
