package pl.marchel.remotecontrolserver.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.model.Script;
import pl.marchel.remotecontrolserver.service.RobotService;
import pl.marchel.remotecontrolserver.utils.RobotRegistry;
import pl.marchel.remotecontrolserver.utils.Utils;

import java.security.Principal;
import java.util.List;

@Controller
public class PageController {

    private final RobotService service;
    private final RobotRegistry registry;

    public PageController(RobotService service, RobotRegistry registry) {
        this.service = service;
        this.registry = registry;
    }

    @GetMapping("/home")
    public String home(Model model){
        List<Robot> robots = service.findByUser("admin");
        robots.forEach(robot -> robot.setConnectedWith(Integer.toString(registry.status(robot.getId().toString()))));
        model.addAttribute("robots", robots);
        return "home";
    }

    @GetMapping("/devices")
    public String devices(@AuthenticationPrincipal UserDetails details, Model model){
        model.addAttribute("robots", service.findByUser(details.getUsername()));
        model.addAttribute("userName", details.getUsername());
        return "devices";
    }

    @GetMapping("/control")
    public String controlPublic(@AuthenticationPrincipal Principal user, @RequestParam String id, Model model){
        Robot robot = registry.getRobotById(id);
        if(Utils.verifyAuthorized(user, robot)){
            model.addAttribute("robotId", id);
            model.addAttribute("robotName", robot.getName());
            Script script = robot.getScript();
            if((script == null) || (script.equals(""))) model.addAttribute("script", "src=resources/js/control.js");
            else model.addAttribute("script", script.getScript());
            return "control";
        }
        return "unavailable";
    }
}
