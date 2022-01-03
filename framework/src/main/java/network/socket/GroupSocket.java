package network.socket;

import util.type.UINT32_T;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.HashMap;
import java.util.Map;

public class GroupSocket { // SEND-ONLY

    ////////////////////////////////////////////////////////////
    // VARIABLES
    private final GroupEndpointId incomingGroupEndpointId;
    private final Map<UINT32_T, DestinationRecord> destinationMap = new HashMap<>();
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public GroupSocket(NetAddress groupAddress) {
        this.incomingGroupEndpointId = new GroupEndpointId(groupAddress);

    }
    public GroupSocket(NetAddress groupAddress, NetAddress sourceFilterAddress) {
        this.incomingGroupEndpointId = new GroupEndpointId(groupAddress, sourceFilterAddress);
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public void addDestination(NetAddress netAddress, NetAddress sourceFilterAddress, UINT32_T sessionId) {
        if (netAddress == null || sessionId == null) { return; }
        if (netAddress.isIpv4() != incomingGroupEndpointId.isIpv4()) { return; }

        // Destination 추가 시, SessionId & GroupAddress 같으면 안된다. 둘 중 하나는 달라도 된다.
        int isSameDestination = 0;
        DestinationRecord destinationRecord = getDestination(sessionId);
        if (destinationRecord != null) {
            UINT32_T curSessionId = destinationRecord.getSessionId();
            if (curSessionId.equal(sessionId)) {
                isSameDestination += 1;
            }

            GroupEndpointId groupEndpointId = destinationRecord.getGroupEndpointId();
            if (groupEndpointId != null) {
                NetAddress curNetAddress = groupEndpointId.getGroupAddress();
                if (curNetAddress != null) {
                    if (netAddress.isIpv4()) {
                        Inet4Address curInet4Address = curNetAddress.getInet4Address();
                        Inet4Address inet4Address = netAddress.getInet4Address();
                        if (curInet4Address != null && inet4Address != null) {
                            if (curInet4Address.equals(inet4Address)) {
                                isSameDestination += 1;
                            }
                        }
                    } else {
                        Inet6Address curInet6Address = curNetAddress.getInet6Address();
                        Inet6Address inet6Address = netAddress.getInet6Address();
                        if (curInet6Address != null && inet6Address != null) {
                            if (curInet6Address.equals(inet6Address)) {
                                isSameDestination += 1;
                            }
                        }
                    }
                }
            }
        }
        if (isSameDestination == 2) { return; }

        destinationMap.put(
                sessionId,
                new DestinationRecord(
                        sessionId,
                        new GroupEndpointId(netAddress, sourceFilterAddress)
                )
        );
    }

    public void removeDestination(UINT32_T sessionId) {
        if (sessionId == null) { return; }

        destinationMap.remove(sessionId);
    }

    public void removeAllDestinations() {
        destinationMap.clear();
    }

    public DestinationRecord getDestination(UINT32_T sessionId) {
        if (sessionId == null) { return null; }

        return destinationMap.get(sessionId);
    }

    public GroupEndpointId getIncomingGroupEndpointId() {
        return incomingGroupEndpointId;
    }
    ////////////////////////////////////////////////////////////

}
