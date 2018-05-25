package com.benkimball.ndig.command;

import com.benkimball.ndig.NdDirection;
import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdRoom;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

@Immutable
public class NdDigCommand implements NdCommand {
    private final String direction;

    public NdDigCommand(String direction) {
        this.direction = NdDirection.normalize(direction);
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        NdRoom location = player.getLocation();
        NdRoom new_location = location.dig(direction);
        location.out(player);
        new_location.in(player);
        return false;
    }
}
