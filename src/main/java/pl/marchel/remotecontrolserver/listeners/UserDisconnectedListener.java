package pl.marchel.remotecontrolserver.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.service.MessageService;
import pl.marchel.remotecontrolserver.utils.RobotRegistry;

@Component
public class UserDisconnectedListener implements ApplicationListener<SessionDisconnectEvent> {

    private final RobotRegistry robotRegistry;
    private final MessageService messageService;

    public UserDisconnectedListener(RobotRegistry robotRegistry, MessageService messageService) {
        this.robotRegistry = robotRegistry;
        this.messageService = messageService;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        Robot robot = robotRegistry.getRobotBySession(sessionDisconnectEvent.getSessionId());
        if(robot != null) {
            robotRegistry.remove(sessionDisconnectEvent.getSessionId());
            if(robot.getOwner().getId() == 1){
                messageService.sendPublic(robot.getId().toString(), 0);
            }
        }
    }
}