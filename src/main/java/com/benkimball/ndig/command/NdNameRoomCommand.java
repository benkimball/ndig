package com.benkimball.ndig.command;

import com.benkimball.ndig.*;
import net.jcip.annotations.Immutable;

@Immutable
public class NdNameRoomCommand implements NdCommand {
    private final String name;

    public NdNameRoomCommand(String name) {
        this.name = name;
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        try {
            NdResult r = player.getLocation().setName(name);
            player.tell(r.getMessage());
        } catch (NdException e) {
            player.tell(e.getMessage());
        }
        return false;
    }
}
