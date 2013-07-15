package eu.uberdust.rest.controller.kml;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.exception.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller class that returns the positoin of a node in KML format.
 */
@Controller
public final class ShowNodeKmlController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ShowNodeKmlController.class);

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     */
    // TODO make this controller
    @RequestMapping("/testbed/{testbedId}/kml")
    public ModelAndView showTestbedKML(@PathVariable("testbedId") int testbedId) throws NotImplementedException {
        LOGGER.info("showTestbedKmlController(...)");
        throw new NotImplementedException();
    }

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     */
    // TODO make this controller
    @Loggable
    @RequestMapping("/testbed/{testbedId}/node/{nodeName}/kml")
    public ModelAndView showNodeKML(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName) throws NotImplementedException {
        LOGGER.info("showNodeKmlController(...)");
        throw new NotImplementedException();
    }
}
