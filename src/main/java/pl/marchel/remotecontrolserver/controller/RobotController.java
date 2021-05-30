package pl.marchel.remotecontrolserver.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.marchel.remotecontrolserver.model.Robot;
import pl.marchel.remotecontrolserver.model.TypedMessage;
import pl.marchel.remotecontrolserver.service.RobotService;
import pl.marchel.remotecontrolserver.utils.RobotRegistry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/robot")
public class RobotController {

    private final RobotService service;
    private final RobotRegistry registry;
    private final SimpMessagingTemplate template;

    public RobotController(RobotService service, RobotRegistry registry, SimpMessagingTemplate template) {
        this.service = service;
        this.registry = registry;
        this.template = template;
    }

    @RequestMapping("/authenticate")
    @ResponseBody
    public String authenticate(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestHeader("robotId") String id,
                               @RequestHeader("robotPass") String pass){

        long parsedId;
        try {
            parsedId = Long.parseLong(id);
        }catch (NumberFormatException e){
            return "Id format incorrect";
        }
        var res = service.findById(parsedId);
        if(res.isPresent()){
            Robot robot = res.get();
            if(robot.getPassword().equals(pass)){
                Set<GrantedAuthority> authorities = new HashSet<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_ROBOT"));
                User user = new User(id, pass, authorities);
                Authentication auth = new UsernamePasswordAuthenticationToken(user, pass, authorities);
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(auth);
                HttpSession session = request.getSession();
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
                registry.register(robot);
                String payload = "{\"robotId\": " + robot.getId() + ", \"status\": 1}";
                TypedMessage message = new TypedMessage();
                message.setType("info");
                message.setData(payload);
                template.convertAndSend("/channels/public", message);
                response.addHeader("Access-Control-Allow-Origin", "chrome-extension://omcjoefhfhpmjkofljiolojnmofenkpe");
                response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");
                response.addHeader("Access-Control-Allow-Credentials", "true");
//                response.addHeader("Access-Control-Allow-Credentials", "true");
                return "OK";
            }else return "Wrong password";
        }else return "Wrong id";
    }
}
