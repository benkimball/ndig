package com.benkimball.ndig;

import com.benkimball.ndig.command.NdCommand;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NdCommandHandler extends SimpleChannelInboundHandler<NdCommand> {

    private static final Log log = LogFactory.getLog("NdCommandHandler");
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
        ctx.write("Connected to ndig\n");
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
