package network.socket;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetSocketAddress;

public class NetAddress {

    ////////////////////////////////////////////////////////////
    // VARIABLES
    private Inet4Address inet4Address = null;
    private Inet6Address inet6Address = null;
    private boolean isIpv4;
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public NetAddress(String ip, int port, boolean isIpv4) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
        if (isIpv4) {
            inet4Address = (Inet4Address) inetSocketAddress.getAddress();
        } else {
            inet6Address = (Inet6Address) inetSocketAddress.getAddress();
        }
        this.isIpv4 = isIpv4;
    }

    public NetAddress(InetSocketAddress inetSocketAddress, boolean isIpv4) {
        if (isIpv4) {
            inet4Address = (Inet4Address) inetSocketAddress.getAddress();
        } else {
            inet6Address = (Inet6Address) inetSocketAddress.getAddress();
        }
        this.isIpv4 = isIpv4;
    }

    public NetAddress(Inet4Address inet4Address) {
        this.inet4Address = inet4Address;
        this.isIpv4 = true;
    }

    public NetAddress(Inet6Address inet6Address) {
        this.inet6Address = inet6Address;
        this.isIpv4 = false;
    }

    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public Inet4Address getInet4Address() {
        return inet4Address;
    }

    public void setInet4Address(Inet4Address inet4Address) {
        this.inet4Address = inet4Address;
    }

    public Inet6Address getInet6Address() {
        return inet6Address;
    }

    public void setInet6Address(Inet6Address inet6Address) {
        this.inet6Address = inet6Address;
    }

    public boolean isIpv4() {
        return isIpv4;
    }

    public void setIpv4(boolean ipv4) {
        isIpv4 = ipv4;
    }

    public void clear() {
        inet4Address = null;
        inet6Address = null;
    }

    /**
     * @fn public boolean addressIsNull()
     * @brief Network address null 여부 확인 함수
     * @return Network address 가 null 이면 true, 아니면 false 를 반환
     */
    public boolean addressIsNull() {
        return isIpv4 ? this.inet4Address == null : this.inet6Address == null;
    }

    public boolean equal(NetAddress right) {
        if (isIpv4) {
            Inet4Address rightAddress = right.getInet4Address();
            if (rightAddress != null) {
                return rightAddress.equals(this.inet4Address);
            } else {
                return false;
            }
        } else {
            Inet6Address rightAddress = right.getInet6Address();
            if (rightAddress != null) {
                return rightAddress.equals(this.inet6Address);
            } else {
                return false;
            }
        }
    }

    public boolean isMulticastAddress() {
        if (isIpv4) {
            if (this.inet4Address != null) {
                return inet4Address.isMulticastAddress();
            } else {
                return false;
            }
        } else {
            if (this.inet6Address != null) {
                return inet6Address.isMulticastAddress();
            } else {
                return false;
            }
        }
    }

    public String getAddressString() {
        if (isIpv4) {
            if (this.inet4Address != null) {
                return inet4Address.getHostAddress();
            } else {
                return "";
            }
        } else {
            if (this.inet6Address != null) {
                return inet6Address.getHostAddress();
            } else {
                return "";
            }
        }
    }
    ////////////////////////////////////////////////////////////

}
