package pl.marchel.remotecontrolserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.model.User;
import pl.marchel.remotecontrolserver.service.MessageService;
import pl.marchel.remotecontrolserver.service.RobotService;
import pl.marchel.remotecontrolserver.service.UserService;
import pl.marchel.remotecontrolserver.utils.RobotRegistry;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RobotService robotService;
    private final RobotRegistry robotRegistry;
    private final MessageService messageService;
    private final ObjectMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService, RobotService robotService, RobotRegistry robotRegistry, MessageService messageService, ObjectMapper mapper, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.robotService = robotService;
        this.robotRegistry = robotRegistry;
        this.messageService = messageService;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String devices(@AuthenticationPrincipal UserDetails user, Model model) {
        List<Robot> robots = robotService.findByUser(user.getUsername());
        robots.forEach(robot -> robot.setConnectedWith(Integer.toString(robotRegistry.status(robot.getId().toString()))));
        model.addAttribute("robots", robots);
        model.addAttribute("userName", user.getUsername());
        return "user/dashboard";
    }

    @GetMapping("/new")
    public String newDevice(@AuthenticationPrincipal UserDetails userDetails){
        User user = userService.findByUserName(userDetails.getUsername());
        Robot robot = new Robot();
        robot.setName("New device");
        robot.setOwner(user);
        robot.setPassword(passwordEncoder.encode(Long.toString(System.currentTimeMillis())));
        robotService.save(robot);
        return "redirect:/user/settings?robotId=" + robot.getId();
    }

    @GetMapping("/settings")
    public String robotSettings(@AuthenticationPrincipal UserDetails user, @RequestParam String robotId, Model model){
        model.addAttribute("userName", user.getUsername());
        Optional<Robot> ro = robotService.findById(robotId);
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
        Optional<Robot> ro = robotService.findById(robot.getId().toString());
        if(ro.isPresent()){
            Robot storedRobot = ro.get();
            storedRobot.setName(robot.getName());
            storedRobot.setConfigurations(robot.getConfigurations());
            robotService.save(storedRobot);
        }
        String robotSession = robotRegistry.getSessionId(robot.getId().toString());
        if(robotSession != null){
            try {
                messageService.sendToSession("config", robotSession, mapper.writeValueAsString(robot.getConfigurations()));
                robotRegistry.getRobotBySession(robotSession).setConfigurations(robot.getConfigurations());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/user";
    }

    @GetMapping("/delete")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserDetails user, @RequestParam String robotId){
        Optional<Robot> ro = robotService.findById(robotId);
        if(ro.isPresent()){
            Robot robot = ro.get();
            if(robot.getOwner().getUsername().equals(user.getUsername())) robotService.delete(robot);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
