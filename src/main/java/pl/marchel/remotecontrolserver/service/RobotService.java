package pl.marchel.remotecontrolserver.service;

import pl.marchel.remotecontrolserver.model.Robot;

import java.util.List;
import java.util.Optional;

public interface RobotService {

    Optional<Robot> findById(Long robotId);
    List<Robot> findByUser(String userName);
}
