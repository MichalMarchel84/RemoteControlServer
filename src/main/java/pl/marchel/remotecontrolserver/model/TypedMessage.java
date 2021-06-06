package pl.marchel.remotecontrolserver.model;

import lombok.Data;

@Data
public class TypedMessage {

    private String type;
    private String data;

    public TypedMessage(String type, String data) {
        this.type = type;
        this.data = data;
    }
}
