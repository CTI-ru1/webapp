package eu.uberdust.rest.controller.html;

import eu.uberdust.caching.Cachable;
import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.wisebed.wisedb.model.User;
import eu.wisebed.wisedb.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/user")
public class UserController extends UberdustSpringController {

    @RequestMapping(value = "/{username}/{email}/{password}/{uuid}/", method = RequestMethod.POST)
    public ResponseEntity<String> showReadings(@PathVariable("username") String username, @PathVariable("email") String email, @PathVariable("password") String password, @PathVariable("uuid") Integer uuid) {
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        User user = new User();
        user.setEmail(email);
        user.setEnabled(true);
        user.setPassword(password);
        user.setUsername(username);
        try {
            userManager.add(user, uuid);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
            return new ResponseEntity<String>("ok", responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
            return new ResponseEntity<String>("request denied", responseHeaders, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Handle req and return the appropriate response.
     *
     * @return http servlet response.
     */
    @Loggable
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ModelAndView getNode(@PathVariable("username") String username) {

        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        User user = userManager.getByUsername(username);
        List<UserRole> roles = userManager.listRoles(user);
        // else put thisNode instance in refData and return index view

        refData.put("user", user);
        refData.put("roles", roles);
        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        if (username.equals(current_user)) {
            return new ModelAndView("user/showme.html", refData);
        }
        return new ModelAndView("user/show.html", refData);
    }

    /**
     * Handle req and return the appropriate response.
     *
     * @return http servlet response.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    @Cachable
    public ModelAndView listUsers() {

        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (userRoleManager.isAdmin(userManager.getByUsername(current_user))) {

            List<User> users = userManager.list();
            Map<User, List<UserRole>> roles = new HashMap<User, List<UserRole>>();
            for (User user : users) {
                roles.put(user, userManager.listRoles(user));
            }
            // else put thisNode instance in refData and return index view

            refData.put("users", users);
            refData.put("roles", roles);
            refData.put("time", String.valueOf((System.currentTimeMillis() - start)));

            return new ModelAndView("user/list.html", refData);

        } else {
            return new ModelAndView("unauthorized.html", refData);
        }
    }
}