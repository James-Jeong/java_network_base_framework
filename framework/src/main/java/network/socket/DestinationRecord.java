package network.socket;

import util.type.UINT32_T;

public class DestinationRecord {

    ////////////////////////////////////////////////////////////
    // VARIABLES
    private final UINT32_T sessionId;
    private final GroupEndpointId groupEndpointId;
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public DestinationRecord(UINT32_T sessionId, GroupEndpointId groupEndpointId) {
        this.sessionId = sessionId;
        this.groupEndpointId = groupEndpointId;
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public UINT32_T getSessionId() {
        return sessionId;
    }

    public GroupEndpointId getGroupEndpointId() {
        return groupEndpointId;
    }
    ////////////////////////////////////////////////////////////

}
