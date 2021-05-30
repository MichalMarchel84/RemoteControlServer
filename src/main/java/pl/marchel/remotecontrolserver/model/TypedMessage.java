package pl.marchel.remotecontrolserver.model;

import lombok.Data;

@Data
public class TypedMessage {

    private String type;
    private String data;
}
