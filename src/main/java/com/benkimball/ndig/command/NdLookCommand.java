package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

@Immutable
public class NdLookCommand implements NdCommand {

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.tell(player.getLocation().getAppearance());
        return false;
    }
}
