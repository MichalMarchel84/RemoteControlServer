package pl.marchel.remotecontrolserver.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.model.StatusMessage;
import pl.marchel.remotecontrolserver.model.TypedMessage;
import pl.marchel.remotecontrolserver.utils.RobotRegistry;

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
        TypedMessage publicMsg = new TypedMessage("start", "");
        publicMsg.setType("start");
        template.convertAndSend("/channels/" + robotSession, publicMsg);
    }

    public void sendToSession(String tag, String session, String msg){
        TypedMessage clientMsg = new TypedMessage(tag, msg);
        template.convertAndSend("/channels/" + session, clientMsg);
    }

    public void sendStatusToClient(String clientName, String robotId, int status){
        StatusMessage statusMsg = new StatusMessage(robotId, status);
        template.convertAndSend("/channels/" + clientName, statusMsg);
    }

    public void relay(String payload, String sessionId){
        template.convertAndSend("/channels/" + sessionId, payload);
    }
}
