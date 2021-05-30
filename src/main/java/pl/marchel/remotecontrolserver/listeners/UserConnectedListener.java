package pl.marchel.remotecontrolserver.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import pl.marchel.remotecontrolserver.utils.RobotRegistry;

@Component
public class UserConnectedListener implements ApplicationListener<SessionConnectedEvent> {

    private final RobotRegistry registry;

    public UserConnectedListener(RobotRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent) {
        System.out.println("<<<<<<<<<<Connect>>>>>>>>>>>");
        System.out.println(sessionConnectedEvent.getMessage());
    }
}