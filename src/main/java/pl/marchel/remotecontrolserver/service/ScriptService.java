package pl.marchel.remotecontrolserver.service;

import pl.marchel.remotecontrolserver.model.Script;

import java.util.List;
import java.util.Optional;

public interface ScriptService {

    List<Script> getUserScripts(String username);
    Optional<Script> getScript(Long id);
    void save(Script script);
    void delete(Script script);
}
