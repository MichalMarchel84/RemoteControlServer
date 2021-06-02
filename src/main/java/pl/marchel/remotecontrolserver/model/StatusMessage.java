package pl.marchel.remotecontrolserver.model;

import lombok.Data;

@Data
public class StatusMessage {

    private final String type = "info";
    private String robotId;
    private int status;

    public StatusMessage(String robotId, int status) {
        this.robotId = robotId;
        this.status = status;
    }
}
