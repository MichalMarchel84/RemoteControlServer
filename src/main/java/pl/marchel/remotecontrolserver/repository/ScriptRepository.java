package pl.marchel.remotecontrolserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.marchel.remotecontrolserver.model.Script;

import java.util.List;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Long> {

    List<Script> findByOwnerUsername(String username);
}
