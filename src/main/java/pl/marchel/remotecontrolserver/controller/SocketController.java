package pl.marchel.remotecontrolserver.controller;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    private final SimpMessagingTemplate template;

    public SocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @SubscribeMapping("/public")
    public void subscribe(Message message) {
        //TODO authorization
        template.convertAndSend("/channels/public", "", message.getHeaders());
    }

}
