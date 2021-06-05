package pl.marchel.remotecontrolserver.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.model.Script;
import pl.marchel.remotecontrolserver.model.User;
import pl.marchel.remotecontrolserver.service.RobotService;
import pl.marchel.remotecontrolserver.service.UserService;
import pl.marchel.remotecontrolserver.utils.RobotRegistry;
import pl.marchel.remotecontrolserver.utils.Utils;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class PageController {

    private final RobotService service;
    private final RobotRegistry registry;
    private final UserService users;

    public PageController(RobotService service, RobotRegistry registry, UserService users) {
        this.service = service;
        this.registry = registry;
        this.users = users;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Robot> robots = service.findByUser("admin");
        robots.forEach(robot -> robot.setConnectedWith(Integer.toString(registry.status(robot.getId().toString()))));
        model.addAttribute("robots", robots);
        return "page/home";
    }

    @GetMapping("/devices")
    public String devices(@AuthenticationPrincipal UserDetails details, Model model) {
        model.addAttribute("robots", service.findByUser(details.getUsername()));
        model.addAttribute("userName", details.getUsername());
        return "user/devices";
    }

    @GetMapping("/control")
    public String controlPublic(@AuthenticationPrincipal Principal user, @RequestParam String id, Model model) {
        Robot robot = registry.getRobotById(id);
        if (Utils.verifyAuthorized(user, robot)) {
            model.addAttribute("robotId", id);
            model.addAttribute("robotName", robot.getName());
            Script script = robot.getScript();
            if ((script == null) || (script.equals(""))) model.addAttribute("script", "src=resources/js/control.js");
            else model.addAttribute("script", script.getScript());
            return "control";
        }
        return "error/unavailable";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "page/register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid User user, BindingResult result, @RequestParam String repeat, Model model) {
        if(result.hasErrors()){
            return "page/register";
        }else if(users.findByUserName(user.getUsername()) != null) {
            model.addAttribute("nameError", "This name is not available");
            return "page/register";
        }else if(!user.getPassword().equals(repeat)){
            model.addAttribute("repeatError", "Passwords does not match");
            return "page/register";
        }
        users.saveUser(user);
        model.addAttribute("newAccount", "New account created");
        return "page/login";
    }

    @GetMapping("/login")
    public String login() {
        return "page/login";
    }
}
