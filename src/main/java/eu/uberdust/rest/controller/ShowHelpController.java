package eu.uberdust.rest.controller;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.HtmlFormatter;
import eu.uberdust.rest.exception.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller class that returns an HTML page containing a list of the readings for a node/capability.
 */
@Controller
@RequestMapping("/help")
public final class ShowHelpController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ShowHelpController.class);

    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handle()
            throws CapabilityNotFoundException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException, InvalidCapabilityNameException, InvalidNodeIdException, InvalidLimitException {


        final long start = System.currentTimeMillis();

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // check type of view requested
        return new ModelAndView("help.html", refData);
    }
}
