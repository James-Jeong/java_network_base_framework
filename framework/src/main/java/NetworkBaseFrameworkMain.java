import instance.BaseEnvironment;
import instance.DebugLevel;
import service.ResourceManager;
import service.scheduler.schedule.ScheduleManager;

public class NetworkBaseFrameworkMain {

    public static void main(String[] args) {
        BaseEnvironment baseEnvironment = new BaseEnvironment(
                new ScheduleManager(),
                new ResourceManager(),
                DebugLevel.DEBUG
        );


    }

}
