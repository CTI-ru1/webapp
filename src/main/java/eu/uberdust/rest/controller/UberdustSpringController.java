package eu.uberdust.rest.controller;

import org.springframework.security.core.userdetails.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amaxilatis
 * Date: 8/26/13
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class UberdustSpringController {
    protected Map<String, Object> refData;
    protected String current_user;


    public void initialize(Object user) {
        refData = new HashMap<String, Object>();
        String username;
        if (user instanceof User) {
            username = ((User) user).getUsername();
        } else {
            username = null;
        }
        current_user=username;
        refData.put("username", username);
    }
}
