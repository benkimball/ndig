package com.benkimball.ndig;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import com.benkimball.ndig.command.*;

import java.util.List;
import java.util.regex.*;

public class NdCommandDecoder extends MessageToMessageDecoder<String> {

    private final Pattern EMOTE   = Pattern.compile("^:(.*)");
    private final Pattern QUIT    = Pattern.compile("^\\.q$");
    private final Pattern HUSH    = Pattern.compile("^\\.h$");
    private final Pattern IGNORE  = Pattern.compile("^\\.i ?(\\d+)$");
    private final Pattern WHO     = Pattern.compile("^\\.w$");
    private final Pattern WHOIS   = Pattern.compile("^\\.w ?(\\d+)$");
    private final Pattern YELL    = Pattern.compile("^\\.y (.*)$");
    private final Pattern NAME    = Pattern.compile("^\\.n (.*)$");
    private final Pattern PM      = Pattern.compile("^\\.p ?(\\d+) (.*)$");
    private final Pattern HELP    = Pattern.compile("^\\.\\?$");

    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        Matcher m;

        m = EMOTE.matcher(msg);
        if(m.matches()) { out.add(new NdEmoteCommand(m)); return; }

        m = QUIT.matcher(msg);
        if(m.matches()) { out.add(new NdQuitCommand(m)); return; }

        m = HUSH.matcher(msg);
        if(m.matches()) { out.add(new NdHushCommand(m)); return; }

        m = IGNORE.matcher(msg);
        if(m.matches()) { out.add(new NdIgnoreCommand(m)); return; }

        m = WHO.matcher(msg);
        if(m.matches()) { out.add(new NdWhoCommand(m)); return; }

        m = WHOIS.matcher(msg);
        if(m.matches()) { out.add(new NdWhoisCommand(m)); return; }

        m = YELL.matcher(msg);
        if(m.matches()) { out.add(new NdYellCommand(m)); return; }

        m = NAME.matcher(msg);
        if(m.matches()) { out.add(new NdNameCommand(m)); return; }

        m = PM.matcher(msg);
        if(m.matches()) { out.add(new NdPrivateMessageCommand(m)); return; }

        m = HELP.matcher(msg);
        if(m.matches()) { out.add(new NdHelpCommand(m)); return; }

        out.add(new NdSayCommand(msg));
    }
}
