package eu.uberdust.rest.controller.tab;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.TextFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller class that returns a list of testbed in Raw text format.
 */
@Controller
@RequestMapping("/testbed/raw")
public final class ListTestbedsTabDelimitedViewController extends UberdustSpringController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ListTestbedsTabDelimitedViewController.class);

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws IOException IO exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> listTestbeds() throws IOException, NotImplementedException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

// testbed list
        final List<Testbed> testbeds = testbedManager.list();
        final Map<String, Long> nodesCount = testbedManager.countNodes();
        final Map<String, Long> linksCount = testbedManager.countLinks();


        return rawResponse(TextFormatter.getInstance().formatTestbeds(testbeds, nodesCount, linksCount));

    }
}
