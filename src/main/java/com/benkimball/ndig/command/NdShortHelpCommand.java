package com.benkimball.ndig.command;

import com.benkimball.ndig.NdGame;
import com.benkimball.ndig.NdPlayer;
import net.jcip.annotations.Immutable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Immutable
public class NdShortHelpCommand implements NdCommand {

    public static StringBuffer helpText = new StringBuffer();

    static {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    NdHelpCommand.class.getResourceAsStream("/help-commands.txt")));
            String line;
            while((line = reader.readLine()) != null) {
                helpText.append(line);
                helpText.append("\n");
            }
        } catch(IOException ex) {
            throw new RuntimeException("Unexpected exception reading command help text", ex);
        }
    }

    @Override
    public boolean invoke(NdGame game, NdPlayer player) {
        player.tell(helpText.toString());
        return false;
    }
}
