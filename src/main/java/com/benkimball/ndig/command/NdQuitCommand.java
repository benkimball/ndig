package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import io.netty.channel.ChannelFuture;

import java.util.regex.Matcher;

public class NdQuitCommand implements NdCommand {
    public NdQuitCommand(Matcher matcher) {
    }

    @Override
    public ChannelFuture invoke(NdGame game, NdPlayer player) {
        player.setQuitting(true);
        return player.tell("Bye!");
    }
}
