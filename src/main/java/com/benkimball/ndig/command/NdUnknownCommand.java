package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

@Immutable
public class NdUnknownCommand implements NdCommand {
    private final String text;

    public NdUnknownCommand(String msg) {
        this.text = msg;
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.tell("Unknown command '"+text+"'. To speak, preface your text with a single or double quote.");
        return false;
    }
}