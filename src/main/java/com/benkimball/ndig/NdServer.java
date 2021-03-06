package com.benkimball.ndig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NdServer {

    private static final Log log = LogFactory.getLog("NdServer");
    private static final int PORT = 9916;
    public static final NdGame game = new NdGame(60); // safely published by static initializer

    public static void main(String[] args) {
        log.info("Starting up");
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new NdServerInitializer());
            b.bind(PORT).sync().channel().closeFuture().sync();
        } catch (Exception e) {
            log.fatal("Caught exception", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log.info("Shut down gracefully");
        }
    }
}
