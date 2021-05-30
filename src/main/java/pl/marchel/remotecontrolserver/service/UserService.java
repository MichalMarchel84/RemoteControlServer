package pl.marchel.remotecontrolserver.service;

import pl.marchel.remotecontrolserver.model.User;

public interface UserService {
    User findByUserName(String name);
    void saveUser(User user);
}