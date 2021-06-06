package pl.marchel.remotecontrolserver.model;

import lombok.Data;

import java.util.List;

@Data
public class Configuration {

    private String name;
    private List<ConfigParam> params;

    public Configuration() {
    }

    public Configuration(String name, List<ConfigParam> params) {
        this.name = name;
        this.params = params;
    }
}
