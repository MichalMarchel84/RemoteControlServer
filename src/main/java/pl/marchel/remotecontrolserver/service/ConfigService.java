package pl.marchel.remotecontrolserver.service;

import pl.marchel.remotecontrolserver.model.Configuration;
import pl.marchel.remotecontrolserver.model.Robot;

import java.util.List;

public interface ConfigService {
    List<Configuration> updateConfig(List<Configuration> configs, Robot robot);
}
