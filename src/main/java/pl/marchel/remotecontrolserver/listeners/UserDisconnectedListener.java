package pl.marchel.remotecontrolserver.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import pl.marchel.remotecontrolserver.utils.RobotRegistry;

@Component
public class UserDisconnectedListener implements ApplicationListener<SessionDisconnectEvent> {

    private final RobotRegistry registry;

    public UserDisconnectedListener(RobotRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        System.out.println("<<<<<<<<<<user>>>>>>>>>>>");
        System.out.println(sessionDisconnectEvent.getMessage());
    }
}