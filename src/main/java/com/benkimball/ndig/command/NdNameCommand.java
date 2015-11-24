package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

import java.util.regex.Matcher;

@Immutable
public class NdNameCommand implements NdCommand {
    private final String name;

    public NdNameCommand(String name) {
        this.name = name;
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        String old_name = player.getName();
        if(old_name.equals(name)) {
            player.tell("You are already known as " + name + ".");
        } else {
            if(game.roster.requestName(player, name)) {
                player.tell("Name changed.");
                String announcement = String.format("(%d, %s) is now known as %s.",
                        player.getLineNumber(), old_name, player.getName());
                player.getLocation().tell(announcement, player);
            } else {
                player.tell("That name is already in use.");
            }
        }
        return false;
    }
}
