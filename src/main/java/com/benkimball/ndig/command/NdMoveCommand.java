package com.benkimball.ndig.command;

import com.benkimball.ndig.*;
import net.jcip.annotations.Immutable;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Immutable
public class NdMoveCommand implements NdCommand {
    private final String direction;

    public NdMoveCommand(String direction) {
        this.direction = NdDirection.normalize(direction);
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        NdRoom location = player.getLocation();
        try {
            String exit = location.getExitNames().
                    stream().
                    filter(e -> e.equals(direction))
                    .findFirst()
                    .get();
            NdRoom new_location = location.getDestination(exit);
            location.out(player);
            new_location.in(player);
        } catch (NoSuchElementException | NdException e) {
            player.tell("Unknown direction");
        }
        return false;
    }
}
