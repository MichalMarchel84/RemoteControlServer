package pl.marchel.remotecontrolserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import pl.marchel.remotecontrolserver.model.Configuration;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.model.TypedMessage;
import pl.marchel.remotecontrolserver.service.ConfigService;
import pl.marchel.remotecontrolserver.service.MessageService;
import pl.marchel.remotecontrolserver.service.RobotService;
import pl.marchel.remotecontrolserver.utils.RobotRegistry;

import java.util.List;

@Controller
public class RobotController {

    private final RobotService service;
    private final RobotRegistry robotRegistry;
    private final SimpMessagingTemplate template;
    private final MessageService messageService;
    private final ConfigService configService;
    private final ObjectMapper mapper;

    public RobotController(RobotService service, RobotRegistry registry, SimpMessagingTemplate template, MessageService messageService, ConfigService configService, ObjectMapper mapper) {
        this.service = service;
        this.robotRegistry = registry;
        this.template = template;
        this.messageService = messageService;
        this.configService = configService;
        this.mapper = mapper;
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
                messageService.sendStatusToClient(robot.getOwner().getUsername(), robot.getId().toString(), 1);
                template.convertAndSend("/channels/" + sessionId, "", message.getHeaders());
            }
        }
    }

    @MessageMapping("/config")
    public void config(@Payload List<Configuration> configurations, @Header("simpSessionId") String robotSession){
        Robot robot = robotRegistry.getRobotBySession(robotSession);
        if(robot != null) {
            var configUpdated = configService.updateConfig(configurations, robot);
            robot.setConfigurations(configUpdated);
            try {
                messageService.sendToSession("config", robotSession, mapper.writeValueAsString(configUpdated));
            } catch (JsonProcessingException e) {}
        }
    }

    @MessageMapping("/reports")
    public void report(@Payload TypedMessage message, @Header("simpSessionId") String robotSession){
        String clientSession = robotRegistry.getConnectedWith(robotSession);
        if(clientSession != null) {
            switch (message.getType()) {
                case "connected":
                    //handle event
                    break;
                case "disconnect":
                    robotRegistry.disconnect(robotSession);
                    break;
            }
            messageService.sendToSession("message", clientSession, message.getData());
        }
    }
}
