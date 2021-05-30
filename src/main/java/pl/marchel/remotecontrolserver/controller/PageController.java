package pl.marchel.remotecontrolserver.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.service.RobotService;
import pl.marchel.remotecontrolserver.utils.RobotRegistry;

import javax.servlet.http.HttpServletRequest;
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
        robots.forEach(robot -> robot.setConnectedWith(Integer.toString(registry.status(robot.getId()))));
        model.addAttribute("robots", robots);
        return "home";
    }

    @GetMapping("/devices")
    public String devices(@AuthenticationPrincipal UserDetails details){

        return "devices";
    }
}
