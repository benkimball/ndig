package com.benkimball.ndig;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ThreadSafe
public class NdServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final Log log = LogFactory.getLog("NdServerInitializer");
    private static final StringDecoder STRING_DECODER = new StringDecoder();
    private static final NdCommandDecoder COMMAND_DECODER = new NdCommandDecoder();
    private static final StringEncoder STRING_ENCODER = new StringEncoder();

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().
                addLast(new LineBasedFrameDecoder(8192)).
                addLast(STRING_DECODER).
                addLast(COMMAND_DECODER).
                addLast(STRING_ENCODER).
                addLast(new NdCommandHandler());
        log.info("Initialized channel");
    }
}
