package pl.marchel.remotecontrolserver.service;

import org.springframework.stereotype.Service;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.repository.RobotRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RobotServiceImpl implements RobotService{

    private final RobotRepository repository;
    private final UserService userService;

    public RobotServiceImpl(RobotRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public Optional<Robot> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Robot> findByUser(String userName) {
        return repository.findByOwner(userService.findByUserName(userName));
    }
}
