package network.netty.channel.udp;

import instance.BaseEnvironment;
import instance.DebugLevel;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import network.netty.channel.NettyChannel;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NettyUdpChannel extends NettyChannel {

    private final NioEventLoopGroup nioEventLoopGroup;
    private final Bootstrap bootstrap;
    private Channel listenChannel = null;

    public NettyUdpChannel(BaseEnvironment baseEnvironment, long sessionId, int threadCount, int sendBufSize, int recvBufSize) {
        super(baseEnvironment, sessionId, threadCount, sendBufSize, recvBufSize);

        nioEventLoopGroup = new NioEventLoopGroup(threadCount);
        bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, false)
                .option(ChannelOption.SO_SNDBUF, sendBufSize)
                .option(ChannelOption.SO_RCVBUF, recvBufSize)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
    }

    @Override
    public void stop() {
        closeListenChannel();
        nioEventLoopGroup.shutdownGracefully();
    }

    @Override
    public Channel openListenChannel(String ip, int port) {
        if (listenChannel != null) {
            getBaseEnvironment().printMsg(DebugLevel.WARN, "Channel is already opened.");
            return null;
        }

        InetAddress address;
        ChannelFuture channelFuture;

        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            getBaseEnvironment().printMsg(DebugLevel.WARN, "UnknownHostException is occurred. (ip=%s) (%s)", ip, e.toString());
            return null;
        }

        try {
            channelFuture = bootstrap.bind(address, port).sync();
            this.listenChannel = channelFuture.channel();
            getBaseEnvironment().printMsg("Channel is opened. (ip=%s, port=%s)", address, port);

            return this.listenChannel;
        } catch (Exception e) {
            getBaseEnvironment().printMsg(DebugLevel.WARN, "Channel is interrupted. (address=%s:%s) (%s)", ip, port, e.toString());
            return null;
        }
    }

    @Override
    public void closeListenChannel() {
        if (listenChannel == null) { return; }

        listenChannel.close();
        listenChannel = null;
    }

    public void setChannelHandler(ChannelInitializer<NioDatagramChannel> channelHandler) {
        bootstrap.group(nioEventLoopGroup).channel(NioDatagramChannel.class).handler(channelHandler);
    }

}
