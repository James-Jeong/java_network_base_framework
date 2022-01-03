package network.socket;

import instance.BaseEnvironment;

public class Socket {

    private final BaseEnvironment baseEnvironment;
    private NetAddress netAddress = null;

    public Socket(BaseEnvironment baseEnvironment) {
        this.baseEnvironment = baseEnvironment;
    }

    public NetAddress getNetAddress() {
        return netAddress;
    }

    public void setNetAddress(NetAddress netAddress) {
        this.netAddress = netAddress;
    }

    public void reset() {
        if (netAddress == null) { return; }
        netAddress.clear();
    }

}
