package pl.marchel.remotecontrolserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.service.MessageService;
import pl.marchel.remotecontrolserver.service.RobotService;
import pl.marchel.remotecontrolserver.utils.RobotRegistry;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final RobotService service;
    private final RobotRegistry robotRegistry;
    private final MessageService messageService;
    private final ObjectMapper mapper;

    public UserController(RobotService service, RobotRegistry robotRegistry, MessageService messageService, ObjectMapper mapper) {
        this.service = service;
        this.robotRegistry = robotRegistry;
        this.messageService = messageService;
        this.mapper = mapper;
    }

    @GetMapping
    public String devices(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("robots", service.findByUser(user.getUsername()));
        model.addAttribute("userName", user.getUsername());
        return "user/dashboard";
    }

    @GetMapping("/new")
    public String newDevice(){
        return "user/new";
    }

    @GetMapping("/settings")
    public String robotSettings(@AuthenticationPrincipal UserDetails user, @RequestParam String robotId, Model model){
        model.addAttribute("userName", user.getUsername());
        Optional<Robot> ro = service.findById(robotId);
        if(ro.isPresent()){
            Robot robot = ro.get();
            if(robot.getOwner().getUsername().equals(user.getUsername())){
                model.addAttribute("robot", robot);
            }
        }
        return "user/settings";
    }

    @PostMapping("/settings")
    public String saveSettings(Robot robot){
        Optional<Robot> ro = service.findById(robot.getId().toString());
        if(ro.isPresent()){
            Robot storedRobot = ro.get();
            storedRobot.setName(robot.getName());
            storedRobot.setConfigurations(robot.getConfigurations());
            service.save(storedRobot);
        }
        String robotSession = robotRegistry.getSessionId(robot.getId().toString());
        if(robotSession != null){
            try {
                messageService.sendToSession("config", robotSession, mapper.writeValueAsString(robot.getConfigurations()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/user";
    }

}
