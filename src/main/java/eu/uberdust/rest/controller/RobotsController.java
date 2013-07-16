package eu.uberdust.rest.controller;

import eu.uberdust.caching.Loggable;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller class that returns an HTML page containing a list of the readings for a node/capability.
 */
@Controller
@RequestMapping("/robots.txt")
public final class RobotsController {
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(RobotsController.class);

    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getHelp() {
        final String robotsTXT = "User-agent: *\n" +
                "Disallow: /rest/testbed/*";
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");

        return new ResponseEntity<String>(robotsTXT, responseHeaders, HttpStatus.OK);

    }
}
