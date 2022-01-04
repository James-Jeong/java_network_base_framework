package network.netty;

import instance.BaseEnvironment;
import instance.DebugLevel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import network.netty.channel.NettyChannel;
import network.netty.channel.tcp.NettyTcpChannel;
import network.netty.channel.udp.NettyUdpChannel;
import network.socket.NetInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class NettyManager {

    ////////////////////////////////////////////////////////////
    // VARIABLES
    private final BaseEnvironment baseEnvironment;
    private final NetInterface netInterface;

    /* Key: Channel-ID, value: NettyChannel */
    private final HashMap<Long, NettyChannel> channelMap = new HashMap<>();
    /* Proxy Channel Map Lock */
    private final ReentrantLock channelMapLock = new ReentrantLock();
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public NettyManager(BaseEnvironment baseEnvironment, NetInterface netInterface) {
        this.baseEnvironment = baseEnvironment;
        this.netInterface = netInterface;
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public BaseEnvironment getBaseEnvironment() {
        return baseEnvironment;
    }

    public NetInterface getNetInterface() {
        return netInterface;
    }

    public boolean addChannel(long sessionId, String ip, ChannelInitializer<?> channelHandler) {
        int port = baseEnvironment.getPortResourceManager().takePort();
        if (port == -1) {
            baseEnvironment.printMsg(DebugLevel.WARN, "Fail to add the channel. Port is full. (key=%s)", sessionId);
            return false;
        }

        return addChannel(sessionId, ip, port, channelHandler);
    }

    public boolean addChannel(long sessionId, String ip, int port, ChannelInitializer<?> channelHandler) {
        try {
            channelMapLock.lock();

            if (channelMap.get(sessionId) != null) {
                baseEnvironment.printMsg(DebugLevel.WARN, "Fail to add the channel. Key is duplicated. (key=%s)", sessionId);
                return false;
            }

            NettyChannel nettyChannel;
            if (netInterface.isStream()) {
                nettyChannel = new NettyTcpChannel(
                        baseEnvironment,
                        sessionId,
                        netInterface.getThreadCount(),
                        netInterface.getSendBufSize(),
                        netInterface.getRecvBufSize(),
                        (ChannelInitializer<SocketChannel>) channelHandler
                );
                Channel channel = nettyChannel.openListenChannel(ip, port);
                if (channel == null) {
                    nettyChannel.closeListenChannel();
                    baseEnvironment.printMsg(DebugLevel.WARN, "Fail to add the channel. (key=%s)", sessionId);
                    return false;
                }
            } else {
                nettyChannel = new NettyUdpChannel(
                        baseEnvironment,
                        sessionId,
                        netInterface.getThreadCount(),
                        netInterface.getSendBufSize(),
                        netInterface.getRecvBufSize(),
                        (ChannelInitializer<NioDatagramChannel>) channelHandler
                );
                Channel channel = nettyChannel.openListenChannel(ip, port);
                if (channel == null) {
                    nettyChannel.closeListenChannel();
                    baseEnvironment.printMsg(DebugLevel.WARN, "Fail to add the channel. (key=%s)", sessionId);
                    return false;
                }
            }

            channelMap.putIfAbsent(sessionId, nettyChannel);
            baseEnvironment.printMsg("Success to add channel (key=%s).", sessionId);
        } catch (Exception e) {
            baseEnvironment.printMsg(DebugLevel.WARN, "Fail to add channel. (key=%s) (%s)", sessionId, e.toString());
        } finally {
            channelMapLock.unlock();
        }

        return true;
    }

    public void deleteChannel(long key) {
        try {
            channelMapLock.lock();

            if (!channelMap.isEmpty()) {
                NettyChannel nettyChannel = channelMap.get(key);
                if (nettyChannel == null) {
                    return;
                }

                int port = nettyChannel.getListenPort();
                baseEnvironment.getPortResourceManager().restorePort(port);

                nettyChannel.closeListenChannel();
                channelMap.remove(key);
                nettyChannel.stop();

                baseEnvironment.printMsg("Success to close the channel. (key=%s)", key);
            }
        } catch (Exception e) {
            baseEnvironment.printMsg(DebugLevel.WARN, "Fail to close the channel. (key=%s) (%s)", key, e.toString());
        } finally {
            channelMapLock.unlock();
        }
    }

    public void deleteAllChannels () {
        try {
            channelMapLock.lock();

            if (!channelMap.isEmpty()) {
                for (Map.Entry<Long, NettyChannel> entry : getCloneChannelMap().entrySet()) {
                    NettyChannel nettyChannel = entry.getValue();
                    if (nettyChannel == null) {
                        continue;
                    }

                    int port = nettyChannel.getListenPort();
                    nettyChannel.closeListenChannel();
                    baseEnvironment.getPortResourceManager().restorePort(port);
                    channelMap.remove(entry.getKey());
                    nettyChannel.stop();
                }

                baseEnvironment.printMsg("Success to close all channel(s).");
            }
        } catch (Exception e) {
            baseEnvironment.printMsg(DebugLevel.WARN, "Fail to close all channel(s). (%s)", e.toString());
        } finally {
            channelMapLock.unlock();
        }
    }

    public NettyChannel getChannel(long key) {
        return channelMap.get(key);
    }

    public Map<Long, NettyChannel> getCloneChannelMap() {
        HashMap<Long, NettyChannel> cloneMap;

        try {
            channelMapLock.lock();

            try {
                cloneMap = (HashMap<Long, NettyChannel>) channelMap.clone();
            } catch (Exception e) {
                baseEnvironment.printMsg(DebugLevel.WARN, "Fail to clone the channel map.");
                cloneMap = channelMap;
            }
        } catch (Exception e) {
            baseEnvironment.printMsg(DebugLevel.WARN, "NettyManager.getCloneChannelMap.Exception (%s)", e.toString());
            return null;
        } finally {
            channelMapLock.unlock();
        }

        return cloneMap;
    }

    public void printTrafficStats() {
        baseEnvironment.printMsg("Total packet: %s / Total bytes: %s", netInterface.getTotalNumPackets(), netInterface.getTotalNumBytes());
    }
    ////////////////////////////////////////////////////////////

}
