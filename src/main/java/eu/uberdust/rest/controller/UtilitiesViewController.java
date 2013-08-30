package eu.uberdust.rest.controller;

import eu.uberdust.caching.EvictCache;
import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.util.QuartzJobScheduler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller class that returns an HTML page containing a list of the readings for a node/capability.
 */
@Controller
@RequestMapping()
public final class UtilitiesViewController extends UberdustSpringController {
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(UtilitiesViewController.class);

    private transient QuartzJobScheduler quartzJobScheduler;

    @Autowired
    public void setQuartzJobScheduler(QuartzJobScheduler quartzJobScheduler) {
        this.quartzJobScheduler = quartzJobScheduler;
    }

    @Loggable
    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public ModelAndView getHelp() {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // Prepare data to pass to jsp

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        return new ModelAndView("help/help.html", refData);
    }

    @Loggable
    @RequestMapping(value = "/help/websockets", method = RequestMethod.GET)
    public ModelAndView getHelpWebsockets() {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // Prepare data to pass to jsp

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        return new ModelAndView("help/websockets.html", refData);
    }


    @Loggable
    @RequestMapping(value = "/cleancache", method = RequestMethod.GET)
    @EvictCache(cacheName = "")
    public ModelAndView cleanCache() {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // Prepare data to pass to jsp

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        return new ModelAndView("help/help.html", refData);
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws java.io.IOException IO exception.
     */
    @Loggable
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity<String> listTestbeds() throws IOException, NotImplementedException {
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");

        quartzJobScheduler.init();

        return new ResponseEntity<String>("pong", responseHeaders, HttpStatus.OK);
    }
}
