package network.netty.channel;

import instance.BaseEnvironment;
import io.netty.channel.Channel;

public class NettyChannel {

    ////////////////////////////////////////////////////////////
    // VARIABLES
    private final BaseEnvironment baseEnvironment;
    private final long sessionId;
    private final int threadCount;
    private final int sendBufSize;
    private final int recvBufSize;

    private String listenIp = null;
    private int listenPort = 0;
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public NettyChannel(BaseEnvironment baseEnvironment, long sessionId, int threadCount, int sendBufSize, int recvBufSize) {
        this.baseEnvironment = baseEnvironment;
        this.sessionId = sessionId;
        this.threadCount = threadCount;
        this.sendBufSize = sendBufSize;
        this.recvBufSize = recvBufSize;
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public void stop() {}

    public BaseEnvironment getBaseEnvironment() {
        return baseEnvironment;
    }

    public long getSessionId() {
        return sessionId;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public int getSendBufSize() {
        return sendBufSize;
    }

    public int getRecvBufSize() {
        return recvBufSize;
    }

    public String getListenIp() {
        return listenIp;
    }

    public void setListenIp(String listenIp) {
        this.listenIp = listenIp;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public Channel openListenChannel(String ip, int port) {
        return null;
    }

    public void closeListenChannel() {}

    public Channel openConnectChannel(String ip, int port) {
        return null;
    }

    public void closeConnectChannel() {}
    ////////////////////////////////////////////////////////////

}
