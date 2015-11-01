package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import io.netty.channel.ChannelFuture;

public class NdUnknownCommand implements NdCommand {
    private final String text;

    public NdUnknownCommand(String msg) {
        this.text = msg;
    }

    @Override
    public ChannelFuture invoke(NdGame game, NdPlayer player) {
        return player.tell("Unknown command '"+text+"'");
    }
}
