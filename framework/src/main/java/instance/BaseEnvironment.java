package instance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.scheduler.schedule.ScheduleManager;
import service.ResourceManager;

public class BaseEnvironment {

    ////////////////////////////////////////////////////////////
    // VARIABLES
    private static final Logger logger = LoggerFactory.getLogger(BaseEnvironment.class);
    private ScheduleManager scheduleManager = null;
    private ResourceManager portResourceManager = null;
    private DebugLevel debugLevel = null;
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    public BaseEnvironment() {}

    public BaseEnvironment(ScheduleManager scheduleManager, ResourceManager portResourceManager, DebugLevel debugLevel) {
        this.scheduleManager = scheduleManager;
        this.portResourceManager = portResourceManager;
        this.debugLevel = debugLevel;
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS
    public void printMsg(String msg, Object... parameters) {
        String log = String.format(msg, parameters);
        if (this.debugLevel == DebugLevel.INFO) {
            logger.info("{}", log);
        } else if (this.debugLevel == DebugLevel.DEBUG) {
            logger.debug("{}", log);
        } else if (this.debugLevel == DebugLevel.WARN) {
            logger.warn("{}", log);
        } else if (this.debugLevel == DebugLevel.ERROR) {
            logger.error("{}", log);
        }
    }

    public void printMsg(DebugLevel debugLevel, String msg, Object... parameters) {
        String log = String.format(msg, parameters);
        if (debugLevel == DebugLevel.INFO) {
            logger.info("{}", log);
        } else if (debugLevel == DebugLevel.DEBUG) {
            logger.debug("{}", log);
        } else if (debugLevel == DebugLevel.WARN) {
            logger.warn("{}", log);
        } else if (debugLevel == DebugLevel.ERROR) {
            logger.error("{}", log);
        }
    }

    public DebugLevel getDebugLevel() {
        return debugLevel;
    }

    public void setDebugLevel(DebugLevel debugLevel) {
        this.debugLevel = debugLevel;
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS : ScheduleManager
    public ScheduleManager getScheduleManager() {
        return scheduleManager;
    }

    public void setScheduleManager(ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager;
    }
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // FUNCTIONS : ResourceManager
    public ResourceManager getPortResourceManager() {
        return portResourceManager;
    }

    public void setPortResourceManager(ResourceManager portResourceManager) {
        this.portResourceManager = portResourceManager;
    }
    ////////////////////////////////////////////////////////////

}
