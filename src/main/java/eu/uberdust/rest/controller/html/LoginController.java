package eu.uberdust.rest.controller.html;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    private transient eu.wisebed.wisedb.controller.UserController userManager;

    @Autowired
    public void setUserManager(final eu.wisebed.wisedb.controller.UserController userManager) {
        this.userManager = userManager;
    }


    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {

        String name = getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("username", name);

        model.addAttribute("message", "Spring Security login + database example");
        return "hello";

    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage(ModelMap model) {
        String name = getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("uuid", userManager.getFormInt());
        model.addAttribute("username", name);
        return "register";
    }

    @RequestMapping(value = "/register/{referer}", method = RequestMethod.GET)
    public String registerReferedPage(ModelMap model, @PathVariable("referer") String referer) {
        String name = getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("uuid", userManager.getFormInt());
        model.addAttribute("username", name);
        model.addAttribute("referer", referer);
        return "register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model) {
        String name = getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("username", name);
        return "login";

    }

    @RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
    public String loginerror(ModelMap model) {
        String name = getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("username", name);
        model.addAttribute("error", "true");
        return "login";

    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(ModelMap model) {
        String name = getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("username", name);

        return "login";

    }

    public String getUser(Object user) {

        String username;
        if (user instanceof User) {
            username = ((User) user).getUsername();
        } else {
            username = null;
        }
        return username;
    }
}