package pl.marchel.remotecontrolserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.model.User;

import java.util.List;

@Repository
public interface RobotRepository extends JpaRepository<Robot, Long> {

    List<Robot> findByOwner(User user);
}
