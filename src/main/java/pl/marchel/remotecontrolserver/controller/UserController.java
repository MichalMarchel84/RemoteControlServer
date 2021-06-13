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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.model.Script;
import pl.marchel.remotecontrolserver.model.User;
import pl.marchel.remotecontrolserver.service.MessageService;
import pl.marchel.remotecontrolserver.service.RobotService;
import pl.marchel.remotecontrolserver.service.ScriptService;
import pl.marchel.remotecontrolserver.service.UserService;
import pl.marchel.remotecontrolserver.utils.RobotRegistry;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final ScriptService scriptService;
    private final UserService userService;
    private final RobotService robotService;
    private final RobotRegistry robotRegistry;
    private final MessageService messageService;
    private final ObjectMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(ScriptService scriptService, UserService userService, RobotService robotService, RobotRegistry robotRegistry, MessageService messageService, ObjectMapper mapper, BCryptPasswordEncoder passwordEncoder) {
        this.scriptService = scriptService;
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

    @GetMapping("/scripts")
    public String showScripts(@AuthenticationPrincipal UserDetails user, Model model){
        List<Script> list = scriptService.getUserScripts(user.getUsername());
        model.addAttribute("scripts", list);
        model.addAttribute("userName", user.getUsername());
        return "user/scripts";
    }

    @PostMapping("/scripts")
    public String saveScripts(@AuthenticationPrincipal UserDetails user,
                              @RequestParam Long id,
                              @RequestParam String name,
                              @RequestParam String content){
        Script script = new Script();
        if(id > 0) script.setId(id);
        script.setName(name);
        script.setScript(content);
        script.setOwner(userService.findByUserName(user.getUsername()));
        scriptService.save(script);
        return "redirect:/user/scripts";
    }

    @GetMapping("/delete-script")
    public ResponseEntity<String> deleteScript(@AuthenticationPrincipal UserDetails user, @RequestParam Long scriptId){
        Optional<Script> sc = scriptService.getScript(scriptId);
        if(sc.isPresent()){
            Script script = sc.get();
            if(script.getOwner().getUsername().equals(user.getUsername())) scriptService.delete(script);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
