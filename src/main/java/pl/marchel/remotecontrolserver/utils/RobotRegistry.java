package pl.marchel.remotecontrolserver.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.service.MessageService;

import java.util.HashMap;
import java.util.Map;

@Component
public class RobotRegistry {

    private final Map<String, Robot> registry = new HashMap<>();
    private final Map<String, String> association = new HashMap<>();
    private final MessageService messageService;
    private ClientRegistry clientRegistry;

    public RobotRegistry(MessageService messageService) {
        this.messageService = messageService;
    }

    @Lazy
    @Autowired
    public void setClientRegistry(ClientRegistry clientRegistry) {
        this.clientRegistry = clientRegistry;
    }

    public void register(String sessionId, Robot robot) {

        registry.put(sessionId, robot);
        association.put(robot.getId().toString(), sessionId);
    }

    public void remove(String sessionId) {
        Robot robot = registry.get(sessionId);
        if (robot != null) {
            synchronized (robot) {
                if (robot.getConnectedWith() != null) {
                    clientRegistry.dropConnection(robot.getConnectedWith());
                    messageService.sendToSession("message", robot.getConnectedWith(), "Robot disconnected - connection lost");
                }
                association.remove(robot);
                registry.remove(sessionId);
            }
        }
    }

    public void connect(String userSessionId, String robotId) {

        Robot robot = registry.get(association.get(robotId));
        if (robot != null) {
            synchronized (robot) {
                if (robot.getConnectedWith() == null) {
                    robot.setConnectedWith(userSessionId);
                }
            }
        }
    }

    public int status(String id) {
        Robot robot = registry.get(association.get(id));
        if (robot != null) {
            if (robot.getConnectedWith() != null) return 2;
            return 1;
        }
        return 0;
    }

    public void disconnect(String sessionId) {
        Robot robot = registry.get(sessionId);
        if(robot.getConnectedWith() != null) {
            clientRegistry.dropConnection(robot.getConnectedWith());
            if(robot.getOwner().getUsername().equals("admin")) messageService.sendPublic(robot.getId().toString(), 1);
            messageService.sendStatusToClient(robot.getOwner().getUsername(), robot.getId().toString(), 1);
        }
        robot.setConnectedWith(null);
    }

    public void dropConnection(String robotSession) {
        Robot robot = registry.get(robotSession);
        if((robot != null) && (robot.getConnectedWith() != null)) {
            if(robot.getOwner().getId() == 1) messageService.sendPublic(robot.getId().toString(), 1);
            messageService.sendStatusToClient(robot.getOwner().getUsername(), robot.getId().toString(), 1);
            robot.setConnectedWith(null);
        }
    }

    public String getSessionId(String robotId) {
        return association.get(robotId);
    }

    public Robot getRobotById(String robotId) {
        String sessionId = getSessionId(robotId);
        if (sessionId != null) return registry.get(sessionId);
        return null;
    }

    public Robot getRobotBySession(String robotSession){
        return registry.get(robotSession);
    }

    public String getConnectedWith(String robotSession) {
        return registry.get(robotSession).getConnectedWith();
    }
}
