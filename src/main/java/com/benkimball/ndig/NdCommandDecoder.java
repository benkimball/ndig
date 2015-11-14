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

    private static final Pattern SAY     = Pattern.compile("^['\"](?<text>.+)$");
    private static final Pattern EMOTE   = Pattern.compile("^:(?<text>.+)$");
    private static final Pattern QUIT    = Pattern.compile("^\\.q(u|ui|uit)?$");
    private static final Pattern HUSH    = Pattern.compile("^\\.h(u|us|ush)?$");
    private static final Pattern GAG     = Pattern.compile("^\\.g(a|ag)? ?(?<linenumber>\\d+)$");
    private static final Pattern WHO     = Pattern.compile("^\\.w(h|ho)?$");
    private static final Pattern WHOIS   = Pattern.compile("^\\.w(h|ho|hoi|hois)? ?(?<linenumber>\\d+)$");
    private static final Pattern YELL    = Pattern.compile("^\\.y(e|el|ell)? (?<text>.*)$");
    private static final Pattern NAME    = Pattern.compile("^\\.n(a|am|ame)? (?<text>.*)$");
    private static final Pattern PAGE    = Pattern.compile("^\\.p(a|ag|age)? ?(?<linenumber>\\d+) (?<text>.*)$");
    private static final Pattern HELP    = Pattern.compile("^\\.(\\?|he(l|lp)?)$");
    private static final Pattern NULL    = Pattern.compile("^[:'\"].*$");

    private static final Pattern BROADCAST = Pattern.compile("^@b(r|ro|roa|road|roadc|roadca|roadcas|roadcast)? (?<text>.+)$");

    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        Matcher m;

        m = BROADCAST.matcher(msg);
        if(m.matches()) { out.add(new NdBroadcastCommand(m.group("text"))); return; }

        m = SAY.matcher(msg);
        if(m.matches()) { out.add(new NdSayCommand(m.group("text"))); return; }

        m = EMOTE.matcher(msg);
        if(m.matches()) { out.add(new NdEmoteCommand(m.group("text"))); return; }

        m = QUIT.matcher(msg);
        if(m.matches()) { out.add(new NdQuitCommand()); return; }

        m = HUSH.matcher(msg);
        if(m.matches()) { out.add(new NdHushCommand()); return; }

        m = GAG.matcher(msg);
        if(m.matches()) { out.add(new NdGagCommand(Integer.parseInt(m.group("linenumber")))); return; }

        m = WHO.matcher(msg);
        if(m.matches()) { out.add(new NdWhoCommand()); return; }

        m = WHOIS.matcher(msg);
        if(m.matches()) { out.add(new NdWhoisCommand(Integer.parseInt(m.group("linenumber")))); return; }

        m = YELL.matcher(msg);
        if(m.matches()) { out.add(new NdYellCommand(m.group("text"))); return; }

        m = NAME.matcher(msg);
        if(m.matches()) { out.add(new NdNameCommand(m.group("text"))); return; }

        m = PAGE.matcher(msg);
        if(m.matches()) {
            out.add(new NdPageCommand(Integer.parseInt(m.group("linenumber")), m.group("text")));
            return;
        }

        m = HELP.matcher(msg);
        if(m.matches()) { out.add(new NdHelpCommand()); return; }

        m = NULL.matcher(msg);
        if(m.matches()) { return; }

        // if all else fails, it's unknown
        out.add(new NdUnknownCommand(msg));
    }
}
