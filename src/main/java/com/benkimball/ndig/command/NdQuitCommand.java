package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

@Immutable
public class NdQuitCommand implements NdCommand {

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.tell("Bye!");
        player.getLocation().out(player);
        return true;
    }
}
