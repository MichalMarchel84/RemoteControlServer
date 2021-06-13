package pl.marchel.remotecontrolserver.utils;

import org.springframework.stereotype.Component;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.service.MessageService;

import java.util.HashMap;
import java.util.Map;

@Component
public class ClientRegistry {

    private final Map<String, String> connections = new HashMap<>();
    private final RobotRegistry robotRegistry;
    private final MessageService messageService;

    public ClientRegistry(RobotRegistry robotRegistry, MessageService messageService) {
        this.robotRegistry = robotRegistry;
        this.messageService = messageService;
    }

    public void connectWith(String clientSession, String robotId){
        String robotSession = robotRegistry.getSessionId(robotId);
        Robot robot = robotRegistry.getRobotBySession(robotSession);
        if(connections.containsKey(clientSession)){
            messageService.sendToSession("message", clientSession, "You have an open connection already, close it first");
        }else if((robot == null) || (robot.getConnectedWith() != null)){
            messageService.sendToSession("message", clientSession, "Robot not available");
        }else {
            connections.put(clientSession, robotSession);
            robotRegistry.connect(clientSession, robotId);
            messageService.sendStart(robotSession);
            if(robot.getOwner().getId() == 1) messageService.sendPublic(robotId, 2);
            messageService.sendStatusToClient(robot.getOwner().getUsername(), robotId, 2);
        }
    }

    public String getConnectedWith(String clientSession){
        return connections.get(clientSession);
    }

    public void dropConnection(String clientSession){
        connections.remove(clientSession);
    }

    public void disconnect(String clientSession){
        String robotSession = connections.get(clientSession);
        if(robotSession != null) {
            robotRegistry.dropConnection(robotSession);
        }
        connections.remove(clientSession);
    }
}
