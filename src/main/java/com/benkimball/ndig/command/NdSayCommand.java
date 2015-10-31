package com.benkimball.ndig.command;

import com.benkimball.ndig.NdPlayer;
import io.netty.channel.ChannelHandlerContext;

import java.util.regex.Matcher;

public class NdSayCommand implements NdCommand {
    private final String text;

    public NdSayCommand(String msg) {
        text = msg;
    }

    @Override
    public void invoke(Object gameContext, NdPlayer player) {
        ChannelHandlerContext ctx = (ChannelHandlerContext)gameContext;
        ctx.writeAndFlush(text);
    }
}
