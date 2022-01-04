package network.socket;

import instance.BaseEnvironment;
import io.netty.channel.ChannelInitializer;

import java.util.HashMap;

public class SocketManager {

    ////////////////////////////////////////////////////////////
    // VARIABLES
    private final BaseEnvironment baseEnvironment;
    private final NetInterface netInterface;
    private final HashMap<String, GroupSocket> groupSocketList = new HashMap<>();
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public SocketManager(BaseEnvironment baseEnvironment, boolean isStream, int threadCount, int sendBufSize, int recvBufSize) {
        this.baseEnvironment = baseEnvironment;
        this.netInterface = new NetInterface(baseEnvironment, isStream, threadCount, sendBufSize, recvBufSize);
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public boolean addSocket(NetAddress netAddress, ChannelInitializer<?> channelHandler) {
        GroupSocket groupSocket = new GroupSocket(baseEnvironment, netInterface, netAddress, channelHandler);
        return groupSocketList.putIfAbsent(netAddress.getAddressString(), groupSocket) == null;
    }

    public boolean removeSocket(NetAddress netAddress) {
        if (netAddress == null) { return false; }
        GroupSocket groupSocket = getSocket(netAddress);
        groupSocket.getListenSocket().stop();
        return groupSocketList.remove(netAddress.getAddressString()) != null;
    }

    public GroupSocket getSocket(NetAddress netAddress) {
        if (netAddress == null) { return null; }
        return groupSocketList.get(netAddress.getAddressString());
    }
    ////////////////////////////////////////////////////////////

}
