package pl.marchel.remotecontrolserver.service;

import org.springframework.stereotype.Service;
import pl.marchel.remotecontrolserver.model.ConfigParam;
import pl.marchel.remotecontrolserver.model.Configuration;
import pl.marchel.remotecontrolserver.model.Robot;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfigServiceImpl implements ConfigService{

    private final RobotService robotService;

    public ConfigServiceImpl(RobotService robotService) {
        this.robotService = robotService;
    }

    @Override
    public List<Configuration> updateConfig(List<Configuration> configs, Robot robot) {
        if(robot.getConfigurations() != null) {
            var storedConfigs = robot.getConfigurations().stream()
                    .filter(config -> config.getName() != null)
                    .collect(Collectors.toMap(Configuration::getName,
                            config -> config.getParams().stream()
                                    .filter(param -> param.getName() != null)
                                    .collect(Collectors.toMap(ConfigParam::getName, ConfigParam::getValue))));

            configs.forEach(config -> {
                if ((config.getName() != null) && storedConfigs.containsKey(config.getName())) {
                    var storedParams = storedConfigs.get(config.getName());
                    config.getParams().forEach(param -> {
                        if ((param.getName() != null) && storedParams.containsKey(param.getName())) {
                            param.setValue(storedParams.get(param.getName()));
                        }
                    });
                }
            });
        }
        robot.setConfigurations(configs);
        robotService.save(robot);
        return configs;
    }
}
