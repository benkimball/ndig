package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

@Immutable
public class NdNullCommand implements NdCommand {

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        return false;
    }
}
