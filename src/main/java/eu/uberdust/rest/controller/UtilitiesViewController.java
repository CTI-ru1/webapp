package eu.uberdust.rest.controller;

import eu.uberdust.caching.EvictCache;
import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.annotation.WiseLog;
import eu.wisebed.wisedb.model.Statistics;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;
import java.util.List;

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
    @RequestMapping(value = "/apps", method = RequestMethod.GET)
    public ModelAndView getApps() {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // Prepare data to pass to jsp

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        return new ModelAndView("help/apps.html", refData);
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
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public ModelAndView statistics() {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());


        // Prepare data to pass to jsp
        Date now = new Date(System.currentTimeMillis());
        Date yesterday = new Date(System.currentTimeMillis() - 12 * 60 * 60 * 1000);


        final List<Statistics> statsNodes = statisticsManager.list("/testbed/node/", yesterday, now);
        final List<Statistics> statsNode = statisticsManager.list("/testbed/node/show/", yesterday, now);
        final List<Statistics> statsLink = statisticsManager.list("/testbed/link/show/", yesterday, now);
        final List<Statistics> statsLinks = statisticsManager.list("/testbed/link/", yesterday, now);
        final List<Statistics> statsHome = statisticsManager.list("/testbed/", yesterday, now);
        final List<Statistics> statsPing = statisticsManager.list("/ping/", yesterday, now);

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        StringBuilder readingsHome = new StringBuilder();
        for (int i = 0; i < statsHome.size(); i++) {
            if (i == 0) {
                readingsHome.append("[");
            } else {
                readingsHome.append(",[");
            }
            readingsHome.append(statsHome.get(i).getDate().getTime()).append(",").append(statsHome.get(i).getMillis()).append("]");
        }
        refData.put("readingsHome", readingsHome.toString());
        StringBuilder readingsPing = new StringBuilder();
        for (int i = 0; i < statsPing.size(); i++) {
            if (i == 0) {
                readingsPing.append("[");
            } else {
                readingsPing.append(",[");
            }
            readingsPing.append(statsPing.get(i).getDate().getTime()).append(",").append(statsPing.get(i).getMillis()).append("]");
        }
        refData.put("readingsPing", readingsPing.toString());
        return new ModelAndView("statistics/stats.html", refData);
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
    @WiseLog(logName = "/ping/")
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity<String> listTestbeds() throws IOException, NotImplementedException {
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long start = System.currentTimeMillis();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");

        return new ResponseEntity<String>("pong", responseHeaders, HttpStatus.OK);
    }

    @Loggable
    @RequestMapping(value = "/username", method = RequestMethod.GET)
    public ResponseEntity<String> showUsername() throws IOException, NotImplementedException {
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return rawResponse(current_user);
    }

    @PostConstruct
    public void initQuartz() {
        quartzJobScheduler.init();
    }
}
