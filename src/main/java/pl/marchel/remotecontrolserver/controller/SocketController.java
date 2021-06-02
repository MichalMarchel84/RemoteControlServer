package pl.marchel.remotecontrolserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.utils.RobotRegistry;
import pl.marchel.remotecontrolserver.utils.ClientRegistry;
import pl.marchel.remotecontrolserver.service.MessageService;
import pl.marchel.remotecontrolserver.utils.Utils;

import java.security.Principal;

@Slf4j
@Controller
public class SocketController {

    private final SimpMessagingTemplate template;
    private final RobotRegistry robotRegistry;
    private final ClientRegistry clientRegistry;
    private final MessageService messageService;

    public SocketController(SimpMessagingTemplate template, RobotRegistry registry, ClientRegistry clientRegistry, MessageService messageService) {
        this.template = template;
        this.robotRegistry = registry;
        this.clientRegistry = clientRegistry;
        this.messageService = messageService;
    }

    @SubscribeMapping("/public")
    public void subscribe(Message message, @Header("simpSessionId") String id) {
        template.convertAndSend("/channels/public", "", message.getHeaders());
    }

    @MessageMapping("/signalling")
    public void doSignalling(@Payload String payload,
                             @Header("simpSessionId") String sessionId,
                             @Header("caller") String caller){
        if(caller.equals("robot")){
            messageService.relay(payload, robotRegistry.getConnectedWith(sessionId));
        }else if(caller.equals("user")){
            messageService.relay(payload, clientRegistry.getConnectedWith(sessionId));
        }
    }

    @SubscribeMapping("/begin")
    public void begin(Message message,
                      @AuthenticationPrincipal Principal user,
                      @Header("simpSessionId") String clientSession,
                      @Header("robotId") String robotId) {

        template.convertAndSend("/channels/" + clientSession, "", message.getHeaders());
        Robot robot = robotRegistry.getRobotById(robotId);
        if(Utils.verifyAuthorized(user, robot)){
            synchronized (robot) {
                clientRegistry.connectWith(clientSession, robotId);
            }
            messageService.sendToClient(clientSession, "");
        }else {
            messageService.sendToClient(clientSession, "Robot not available");
        }
    }
}
