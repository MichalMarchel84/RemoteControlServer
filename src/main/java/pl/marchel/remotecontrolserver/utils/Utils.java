package pl.marchel.remotecontrolserver.utils;

import pl.marchel.remotecontrolserver.model.Robot;

import java.security.Principal;

public class Utils {

    public static boolean verifyAuthorized(Principal user, Robot robot){
        if(robot == null) return false;
        else {
            if (!robot.getOwner().getUsername().equals("admin")) {
                if ((user == null) || (!user.getName().equals(robot.getOwner().getUsername()))) {
                    return false;
                }
            }
        }
        return true;
    }
}
