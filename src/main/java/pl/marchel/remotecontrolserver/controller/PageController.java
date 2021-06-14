package pl.marchel.remotecontrolserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.Optional;

@Controller
public class PageController {

    private final RobotService service;
    private final RobotRegistry registry;
    private final UserService users;
    private final ObjectMapper mapper;

    public PageController(RobotService service, RobotRegistry registry, UserService users, ObjectMapper mapper) {
        this.service = service;
        this.registry = registry;
        this.users = users;
        this.mapper = mapper;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Robot> robots = service.findByUser("admin");
        robots.forEach(robot -> robot.setConnectedWith(Integer.toString(registry.status(robot.getId().toString()))));
        model.addAttribute("robots", robots);
        return "page/home";
    }

    @GetMapping("/control")
    public String controlPublic(@AuthenticationPrincipal UserDetails user, @RequestParam String id, Model model) {
        Optional<Robot> ro = service.findById(id);
        if(ro.isPresent()) {
            Robot robot = ro.get();
            if (Utils.verifyAuthorized(user, robot)) {
                model.addAttribute("robotId", id);
                model.addAttribute("robotName", robot.getName());
                try {
                    model.addAttribute("configs", mapper.writeValueAsString(robot.getConfigurations()));
                } catch (JsonProcessingException e) {
                }
                Script script = robot.getScript();
                if ((script == null) || (script.getScript().trim().isEmpty()))
                    model.addAttribute("script", "src=resources/js/control.js");
                else model.addAttribute("scriptCont", script.getScript());
                return "control";
            }
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

    @GetMapping("/download")
    public String download() {return "page/download"; }

    @GetMapping("/robot-articles")
    public String showRobotArticle(@RequestParam String name){
        return "page/articles/" + name;
    }
}
