package com.benkimball.ndig;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ThreadSafe
public class NdServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final Log log = LogFactory.getLog("NdServerInitializer");
    private static final StringDecoder DECODER = new StringDecoder();
    private static final NdCommandDecoder COMMAND_DECODER = new NdCommandDecoder();
    private static final StringEncoder ENCODER = new StringEncoder();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new NdCommandDecoder());
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(DECODER);
        pipeline.addLast(COMMAND_DECODER);
        pipeline.addLast(ENCODER);
        pipeline.addLast(new NdCommandHandler());
        log.info("Initialized channel");
    }
}
