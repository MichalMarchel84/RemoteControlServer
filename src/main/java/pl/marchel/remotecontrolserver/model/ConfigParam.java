package pl.marchel.remotecontrolserver.model;

import lombok.Data;

@Data
public class ConfigParam {

    private String name;
    private String value;

    public ConfigParam() {
    }

    public ConfigParam(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
