package pl.marchel.remotecontrolserver.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.marchel.remotecontrolserver.service.RobotService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final RobotService service;

    public UserController(RobotService service) {
        this.service = service;
    }

    @GetMapping
    public String devices(@AuthenticationPrincipal UserDetails details, Model model) {
        model.addAttribute("robots", service.findByUser(details.getUsername()));
        model.addAttribute("userName", details.getUsername());
        return "user/dashboard";
    }

    @GetMapping("/new")
    public String newDevice(){
        return "user/new";
    }

    @GetMapping("/settings")
    public String userSettings(){
        return "user/settings";
    }
}
