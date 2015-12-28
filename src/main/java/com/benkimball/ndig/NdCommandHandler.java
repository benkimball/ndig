package com.benkimball.ndig;

import com.benkimball.ndig.command.NdCommand;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NdCommandHandler extends SimpleChannelInboundHandler<NdCommand> {

    private static final Log log = LogFactory.getLog("NdCommandHandler");
    private static StringBuffer bannerText = new StringBuffer();
    static {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    NdCommandHandler.class.getResourceAsStream("/banner.txt")));
            String line;
            while((line = reader.readLine()) != null) {
                bannerText.append(line);
                bannerText.append("\n");
            }
        } catch(IOException ex) {
            throw new RuntimeException("Unexpected exception reading banner text", ex);
        }
    }
    private final NdGame game = NdServer.game;
    private NdPlayer player;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        NdRoom starting_location;
        try {
            player = game.handleLogin(ctx);
            starting_location = NdRoom.getDefaultRoom();
        } catch(NdException e) {
            ctx.writeAndFlush(e.getMessage()+"\n\n").addListener(ChannelFutureListener.CLOSE);
            return;
        }
        ctx.write(bannerText.toString());

        ctx.write("Your name is " + player.getName() + "\n");
        ctx.write("You have line " + player.getLineNumber() + "\n");
        ctx.write("Help available; enter '.help' or '.?'\n");
        starting_location.in(player);
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        game.handleLogout(player);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NdCommand in) {
        player.setLastSeen();
        log.debug(String.format("%s [%d]: %s", player.getName(), player.getLineNumber(), in.getClass().getSimpleName()));
        boolean quitting = in.invoke(game, player);
        if(quitting) ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn("exception caught", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
