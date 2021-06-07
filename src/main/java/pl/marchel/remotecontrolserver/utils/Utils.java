package pl.marchel.remotecontrolserver.utils;

import org.springframework.security.core.userdetails.UserDetails;
import pl.marchel.remotecontrolserver.model.Robot;

public class Utils {

    public static boolean verifyAuthorized(UserDetails user, Robot robot){
        if(robot == null) return false;
        else {
            if (!robot.getOwner().getUsername().equals("admin")) {
                if ((user == null) || (!user.getUsername().equals(robot.getOwner().getUsername()))) {
                    return false;
                }
            }
        }
        return true;
    }
}
