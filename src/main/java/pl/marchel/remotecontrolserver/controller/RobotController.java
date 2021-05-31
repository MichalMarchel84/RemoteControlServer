package pl.marchel.remotecontrolserver.controller;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.model.TypedMessage;
import pl.marchel.remotecontrolserver.service.MessageService;
import pl.marchel.remotecontrolserver.service.RobotService;
import pl.marchel.remotecontrolserver.utils.ClientRegistry;
import pl.marchel.remotecontrolserver.utils.RobotRegistry;

@Controller
public class RobotController {

    private final RobotService service;
    private final RobotRegistry robotRegistry;
    private final SimpMessagingTemplate template;
    private final MessageService messageService;

    public RobotController(RobotService service, RobotRegistry registry, SimpMessagingTemplate template, MessageService messageService) {
        this.service = service;
        this.robotRegistry = registry;
        this.template = template;
        this.messageService = messageService;
    }

    @SubscribeMapping("/authenticate")
    public void authenticate(Message message,
                             @Header("simpSessionId") String sessionId,
                             @Header("robotId") String robotId,
                             @Header("robotPass") String pass) {


        var res = service.findById(robotId);
        if (res.isPresent()) {
            Robot robot = res.get();
            if (robot.getPassword().equals(pass)) {
                robotRegistry.register(sessionId, robot);
                if(robot.getOwner().getId() == 1)messageService.sendPublic(robot.getId().toString(), 1);
                template.convertAndSend("/channels/" + sessionId, "", message.getHeaders());
            }
        }
    }

    @MessageMapping("/reports")
    public void report(@Payload TypedMessage message, @Header("simpSessionId") String robotSession){
        String clientSession = robotRegistry.getConnectedWith(robotSession);
        if(clientSession != null) {
            switch (message.getType()) {
                case "connect":
                    messageService.sendToClient(clientSession, "Connected");
                    break;
                case "disconnect":
                    messageService.sendToClient(clientSession, "You are disconnected");
                    robotRegistry.disconnect(robotSession);
                    break;
                case "failed":
                    messageService.sendToClient(clientSession, "Failed to establish connection");
                    robotRegistry.disconnect(robotSession);
                    break;
                case "timeout":
                    messageService.sendToClient(clientSession, "Signalling timed out");
                    robotRegistry.disconnect(robotSession);
                    break;
            }
        }
    }
}
