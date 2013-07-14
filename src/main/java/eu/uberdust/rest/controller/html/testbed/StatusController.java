package eu.uberdust.rest.controller.html.testbed;

import eu.uberdust.caching.Loggable;
import eu.uberdust.command.TestbedCommand;
import eu.uberdust.formatter.HtmlFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.*;
import eu.wisebed.wisedb.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Controller class that returns the status page for the nodes and links of a testbed.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/status")
public final class StatusController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(StatusController.class);

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Last node reading persistence manager.
     */
    private transient NodeCapabilityController nodeCapabilityManager;

    /**
     * Last link reading persistence manager.
     */
    private transient LinkCapabilityController linkCapabilityManager;

    /**
     * Sets testbed persistence manager.
     *
     * @param testbedManager testbed persistence manager.
     */
    @Autowired
    public void setTestbedManager(final TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }

    @Autowired
    public void setNodeCapabilityManager(final NodeCapabilityController nodeCapabilityManager) {
        this.nodeCapabilityManager = nodeCapabilityManager;
    }

    @Autowired
    public void setLinkCapabilityManager(final LinkCapabilityController linkCapabilityManager) {
        this.linkCapabilityManager = linkCapabilityManager;
    }

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws InvalidTestbedIdException a InvalidTestbedIDException exception.
     * @throws TestbedNotFoundException  a TestbedNotFoundException exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showTestbedStatus(@PathVariable("testbedId") int testbedId) throws TestbedNotFoundException {
        final long start = System.currentTimeMillis();

        long start1 = System.currentTimeMillis();


        LOGGER.info("--------- Get Testbed id: " + (System.currentTimeMillis() - start1));
        start1 = System.currentTimeMillis();

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }
        LOGGER.info("got testbed " + testbed);

        LOGGER.info("--------- Get Testbed: " + (System.currentTimeMillis() - start1));


        if (nodeCapabilityManager == null) {
            LOGGER.error("nodeCapabilityManager==null");
        }

        long before5Min = new Date().getTime() - 5 * 60 * 1000;
        start1 = System.currentTimeMillis();
        // get a list of node last readings from testbed
        final List<NodeCapability> nodeCapabilities = nodeCapabilityManager.list(testbed.getSetup());
        final Set<String> updated = new HashSet<String>();

        for (NodeCapability nodeCapability : nodeCapabilities) {
            if (nodeCapability.getLastNodeReading().getTimestamp().getTime() - before5Min > 0)
                updated.add(nodeCapability.getNode().getName());
        }
        LOGGER.info("--------- list nodeCapabilities: " + (System.currentTimeMillis() - start1));

        start1 = System.currentTimeMillis();

        LOGGER.info("--------- format last node readings: " + (System.currentTimeMillis() - start1));

        start1 = System.currentTimeMillis();
        // get a list of link statistics from testbed
        final List<LinkCapability> linkCapabilities = linkCapabilityManager.list(testbed.getSetup());
        LOGGER.info("--------- List link capabilities: " + (System.currentTimeMillis() - start1));


        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put("testbed", testbed);
        refData.put("lastNodeReadings", nodeCapabilities);
        refData.put("updated", updated);

        try {
            start1 = System.currentTimeMillis();
            refData.put("lastLinkReadings", HtmlFormatter.getInstance().formatLastLinkReadings(linkCapabilities));
            LOGGER.info("--------- format link Capabilites: " + (System.currentTimeMillis() - start1));
        } catch (NotImplementedException e) {
            LOGGER.error(e);
        }

        LOGGER.info("--------- Total time: " + (System.currentTimeMillis() - start));
        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        LOGGER.info("prepared map");

        return new ModelAndView("testbed/status.html", refData);

    }
}
