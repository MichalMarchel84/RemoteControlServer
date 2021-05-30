package pl.marchel.remotecontrolserver.controller;

import org.springframework.stereotype.Controller;
import pl.marchel.remotecontrolserver.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


}