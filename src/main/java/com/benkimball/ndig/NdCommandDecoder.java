package com.benkimball.ndig;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import com.benkimball.ndig.command.*;
import net.jcip.annotations.Immutable;

import java.util.List;
import java.util.regex.*;

@Immutable
@Sharable
public class NdCommandDecoder extends MessageToMessageDecoder<String> {

    private static final Pattern SAY     = Pattern.compile("^['\"](.+)$");
    private static final Pattern EMOTE   = Pattern.compile("^:(.+)$");
    private static final Pattern QUIT    = Pattern.compile("^\\.q$");
    private static final Pattern HUSH    = Pattern.compile("^\\.h$");
    private static final Pattern GAG     = Pattern.compile("^\\.g ?(\\d+)$");
    private static final Pattern WHO     = Pattern.compile("^\\.w$");
    private static final Pattern WHOIS   = Pattern.compile("^\\.w ?(\\d+)$");
    private static final Pattern YELL    = Pattern.compile("^\\.y (.*)$");
    private static final Pattern NAME    = Pattern.compile("^\\.n (.*)$");
    private static final Pattern PAGE    = Pattern.compile("^\\.p ?(\\d+) (.*)$");
    private static final Pattern HELP    = Pattern.compile("^\\.\\?$");
    private static final Pattern NULL    = Pattern.compile("^[:'\"].*$");

    private static final Pattern BROADCAST = Pattern.compile("^@b(.+)$");

    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        Matcher m;

        m = BROADCAST.matcher(msg);
        if(m.matches()) { out.add(new NdBroadcastCommand(m)); return; }

        m = SAY.matcher(msg);
        if(m.matches()) { out.add(new NdSayCommand(m)); return; }

        m = EMOTE.matcher(msg);
        if(m.matches()) { out.add(new NdEmoteCommand(m)); return; }

        m = QUIT.matcher(msg);
        if(m.matches()) { out.add(new NdQuitCommand()); return; }

        m = HUSH.matcher(msg);
        if(m.matches()) { out.add(new NdHushCommand()); return; }

        m = GAG.matcher(msg);
        if(m.matches()) { out.add(new NdGagCommand(m)); return; }

        m = WHO.matcher(msg);
        if(m.matches()) { out.add(new NdWhoCommand(m)); return; }

        m = WHOIS.matcher(msg);
        if(m.matches()) { out.add(new NdWhoisCommand(m)); return; }

        m = YELL.matcher(msg);
        if(m.matches()) { out.add(new NdYellCommand(m)); return; }

        m = NAME.matcher(msg);
        if(m.matches()) { out.add(new NdNameCommand(m)); return; }

        m = PAGE.matcher(msg);
        if(m.matches()) { out.add(new NdPageCommand(m)); return; }

        m = HELP.matcher(msg);
        if(m.matches()) { out.add(new NdHelpCommand()); return; }

        m = NULL.matcher(msg);
        if(m.matches()) { return; }

        // if all else fails, it's unknown
        out.add(new NdUnknownCommand(msg));
    }
}
