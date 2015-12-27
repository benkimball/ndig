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

    // @commands
    private static final Pattern DIG       = Pattern.compile("^@d(i|ig)? (?<text>.+)$");
    private static final Pattern NAMEROOM  = Pattern.compile("^@n(a|am|ame)? (?<text>.+)$");
    private static final Pattern DESCROOM  = Pattern.compile("^@d(e|es|esc|escr|escri|escrib|escribe)? (?<text>.+)$");
    private static final Pattern BROADCAST = Pattern.compile("^@b(r|ro|roa|road|roadc|roadca|roadcas|roadcast)? (?<text>.+)$");

    // >commands
    private static final Pattern MOVE      = Pattern.compile("^> ?(?<text>.+)$");

    // :commands
    private static final Pattern EMOTE     = Pattern.compile("^:(?<text>.+)$");

    // .commands
    private static final Pattern QUIT      = Pattern.compile("^\\.q(u|ui|uit)?$");
    private static final Pattern HUSH      = Pattern.compile("^\\.h(u|us|ush)?$");
    private static final Pattern GAG       = Pattern.compile("^\\.g(a|ag)? ?(?<linenumber>\\d+)$");
    private static final Pattern LOOK      = Pattern.compile("^\\.l(o|oo|ook)?$");
    private static final Pattern WHO       = Pattern.compile("^\\.w(h|ho)?$");
    private static final Pattern WHOIS     = Pattern.compile("^\\.w(h|ho|hoi|hois)? ?(?<linenumber>\\d+)$");
    private static final Pattern YELL      = Pattern.compile("^\\.y(e|el|ell)? (?<text>.*)$");
    private static final Pattern NAME      = Pattern.compile("^\\.n(a|am|ame)? (?<text>.*)$");
    private static final Pattern PAGE      = Pattern.compile("^\\.p(a|ag|age)? ?(?<linenumber>\\d+) (?<text>.*)$");
    private static final Pattern HELP      = Pattern.compile("^\\.he(l|lp)?$");
    private static final Pattern SHORT     = Pattern.compile("^\\.\\?$");

    // not a command
    private static final Pattern NULL      = Pattern.compile("^[:\\.]?$");

    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        Matcher m;
        NdCommand decoded;

        if((m = DIG.matcher(msg)).matches()) decoded = new NdDigCommand(m.group("text"));
        else if((m = NAMEROOM.matcher(msg)).matches()) decoded = new NdNameRoomCommand(m.group("text"));
        else if((m = DESCROOM.matcher(msg)).matches()) decoded = new NdDescribeRoomCommand(m.group("text"));
        else if((m = BROADCAST.matcher(msg)).matches()) decoded = new NdBroadcastCommand(m.group("text"));
        else if((m = MOVE.matcher(msg)).matches()) decoded = new NdMoveCommand(m.group("text"));
        else if((m = EMOTE.matcher(msg)).matches()) decoded = new NdEmoteCommand(m.group("text"));
        else if((QUIT.matcher(msg)).matches()) decoded = new NdQuitCommand();
        else if((HUSH.matcher(msg)).matches()) decoded = new NdHushCommand();
        else if((m = GAG.matcher(msg)).matches()) decoded = new NdGagCommand(Integer.parseInt(m.group("linenumber")));
        else if((LOOK.matcher(msg)).matches()) decoded = new NdLookCommand();
        else if((WHO.matcher(msg)).matches()) decoded = new NdWhoCommand();
        else if((m = WHOIS.matcher(msg)).matches()) decoded = new NdWhoisCommand(Integer.parseInt(m.group("linenumber")));
        else if((m = YELL.matcher(msg)).matches()) decoded = new NdYellCommand(m.group("text"));
        else if((m = NAME.matcher(msg)).matches()) decoded = new NdNameCommand(m.group("text"));
        else if((m = PAGE.matcher(msg)).matches()) decoded = new NdPageCommand(Integer.parseInt(m.group("linenumber")), m.group("text"));
        else if((HELP.matcher(msg)).matches()) decoded = new NdHelpCommand();
        else if((SHORT.matcher(msg)).matches()) decoded = new NdShortHelpCommand();
        else if(NULL.matcher(msg).matches()) decoded = new NdNullCommand();
        else decoded = new NdSayCommand(msg);

        out.add(decoded);
    }
}
