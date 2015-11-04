package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;

public interface NdCommand {
    boolean invoke(NdGame game, NdPlayer player);
}
