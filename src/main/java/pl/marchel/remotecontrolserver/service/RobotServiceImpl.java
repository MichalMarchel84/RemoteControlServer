package pl.marchel.remotecontrolserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.repository.RobotRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RobotServiceImpl implements RobotService{

    private final RobotRepository repository;
    private final UserService userService;

    public RobotServiceImpl(RobotRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public Optional<Robot> findById(String id) {
        try {
            return repository.findById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            log.error("On parsing robot id {}", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Robot> findByUser(String userName) {
        return repository.findByOwner(userService.findByUserName(userName));
    }
}
