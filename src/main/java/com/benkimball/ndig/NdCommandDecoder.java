package com.benkimball.ndig;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import com.benkimball.ndig.command.*;

import java.util.List;
import java.util.regex.*;

@Sharable
public class NdCommandDecoder extends MessageToMessageDecoder<String> {

    private static final Pattern SAY     = Pattern.compile("^['\"](.+)$");
    private static final Pattern EMOTE   = Pattern.compile("^:(.+)$");
    private static final Pattern QUIT    = Pattern.compile("^\\.q$");
    private static final Pattern HUSH    = Pattern.compile("^\\.h$");
    private static final Pattern IGNORE  = Pattern.compile("^\\.i ?(\\d+)$");
    private static final Pattern WHO     = Pattern.compile("^\\.w$");
    private static final Pattern WHOIS   = Pattern.compile("^\\.w ?(\\d+)$");
    private static final Pattern YELL    = Pattern.compile("^\\.y (.*)$");
    private static final Pattern NAME    = Pattern.compile("^\\.n (.*)$");
    private static final Pattern PM      = Pattern.compile("^\\.p ?(\\d+) (.*)$");
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

        m = NULL.matcher(msg);
        if(m.matches()) { return; }

        // if all else fails, it's unknown
        out.add(new NdUnknownCommand(msg));
    }
}
