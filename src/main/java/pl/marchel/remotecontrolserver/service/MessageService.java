package pl.marchel.remotecontrolserver.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import pl.marchel.remotecontrolserver.model.StatusMessage;
import pl.marchel.remotecontrolserver.model.TypedMessage;

@Component
public class MessageService {

    private final SimpMessagingTemplate template;

    public MessageService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendPublic(String robotId, int status){

        StatusMessage publicMsg = new StatusMessage(robotId, status);
        template.convertAndSend("/channels/public", publicMsg);
    }

    public void sendStart(String robotSession){
        TypedMessage publicMsg = new TypedMessage();
        publicMsg.setType("start");
        template.convertAndSend("/channels/" + robotSession, publicMsg);
    }

    public void sendToClient(String clientSession, String msg){
        TypedMessage clientMsg = new TypedMessage();
        clientMsg.setType("message");
        clientMsg.setData(msg);
        template.convertAndSend("/channels/" + clientSession, clientMsg);
    }

    public void relay(String payload, String sessionId){
        template.convertAndSend("/channels/" + sessionId, payload);
    }
}
