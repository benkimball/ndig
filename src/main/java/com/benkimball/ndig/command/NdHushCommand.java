package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

@Immutable
public class NdHushCommand implements NdCommand {

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        if(player.toggleHushed()) {
            player.tell("Yells suppressed.");
        } else {
            player.tell("Yells no longer suppressed.");
        }
        return false;
    }
}
