package com.benkimball.ndig.command;

import com.benkimball.ndig.NdPlayer;

import java.util.regex.Matcher;

public interface NdCommand {
    public void invoke(Object gameContext, NdPlayer player);
}
