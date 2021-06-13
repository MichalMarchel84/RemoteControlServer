package pl.marchel.remotecontrolserver.service;

import org.springframework.stereotype.Service;
import pl.marchel.remotecontrolserver.model.Script;
import pl.marchel.remotecontrolserver.repository.ScriptRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ScriptServiceImpl implements ScriptService{

    private final ScriptRepository repository;

    public ScriptServiceImpl(ScriptRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Script> getUserScripts(String username) {
        return repository.findByOwnerUsername(username);
    }

    @Override
    public Optional<Script> getScript(Long id) {
        return repository.findById(id);
    }

    @Override
    public void save(Script script) {
        repository.save(script);
    }

    @Override
    public void delete(Script script) {
        repository.delete(script);
    }
}
