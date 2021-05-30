package pl.marchel.remotecontrolserver.utils;

import org.springframework.stereotype.Component;
import pl.marchel.remotecontrolserver.model.Robot;

import java.util.HashMap;
import java.util.Map;

@Component
public class RobotRegistry {

    private final Map<Long, Robot> registry = new HashMap<>();

    public void register(Robot robot){
        registry.put(robot.getId(), robot);
    }

    public void remove(Long id){
        registry.remove(id);
    }

    public boolean connect(String userId, Long robotId){

        Robot robot = registry.get(robotId);
        if(robot != null) {
            synchronized (robot) {
                if (robot.getConnectedWith() == null) {
                    robot.setConnectedWith(userId);
                    return true;
                }
            }
        }
        return false;
    }

    public int status(Long robotId){
        Robot robot = registry.get(robotId);
        if(robot != null){
            if(robot.getConnectedWith() != null) return 2;
            return 1;
        }
        return 0;
    }

    public void disconnect(Long robotId){
        Robot robot = registry.get(robotId);
        robot.setConnectedWith(null);
    }
}
