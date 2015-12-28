package com.benkimball.ndig.command;

import com.benkimball.ndig.*;
import net.jcip.annotations.Immutable;

@Immutable
public class NdMoveCommand implements NdCommand {
    private final String direction;

    public NdMoveCommand(String direction) {
        this.direction = direction.toLowerCase();
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        NdRoom location = player.getLocation();
        String[] exits = location.getExitNames().
                stream().
                filter(exit -> exit.startsWith(direction)).
                toArray(String[]::new);
        if(exits.length == 0) {
            player.tell("Unknown direction");
        } else if(exits.length > 1) {
            player.tell(String.format("Multiple possible directions for %s", direction));
        } else {
            try {
                NdRoom new_location = location.getDestination(exits[0]);
                location.out(player);
                new_location.in(player);
            } catch (NdException e) {
                player.tell("Unknown direction");
            }
        }
        return false;
    }
}
