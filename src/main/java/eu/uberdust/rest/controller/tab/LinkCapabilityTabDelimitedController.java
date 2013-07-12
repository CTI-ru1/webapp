package eu.uberdust.rest.controller.tab;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.TextFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.exception.*;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.LinkController;
import eu.wisebed.wisedb.controller.LinkReadingController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.Link;
import eu.wisebed.wisedb.model.LinkReading;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Controller class that returns an HTML page containing a list of the readings for a node/capability.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/link/{sourceName}/{targetName}/capability/{capabilityName}/tabdelimited/limit/{limit}")
public final class LinkCapabilityTabDelimitedController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LinkCapabilityTabDelimitedController.class);

    /**
     * Node peristence manager.
     */
    private transient LinkController linkManager;

    /**
     * Capability persistence manager.
     */
    private transient CapabilityController capabilityManager;

    /**
     * NodeReading persistence manager.
     */
    private transient LinkReadingController linkReadingManager;

    /**
     * Testbed peristence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Sets node persistence manager.
     *
     * @param linkManager node persistence manager.
     */
    @Autowired
    public void setLinkManager(final LinkController linkManager) {
        this.linkManager = linkManager;
    }

    /**
     * Sets capability persistence manager.
     *
     * @param capabilityManager capability persistence manager.
     */
    @Autowired
    public void setCapabilityManager(final CapabilityController capabilityManager) {
        this.capabilityManager = capabilityManager;
    }

    /**
     * Sets NodeReading persistence manager.
     *
     * @param linkReadingManager NodeReading persistence manager.
     */
    @Autowired
    public void setLinkReadingManager(final LinkReadingController linkReadingManager) {
        this.linkReadingManager = linkReadingManager;
    }

    /**
     * Sets Testbed persistence manager.
     *
     * @param testbedManager Testbed persistence manager.
     */
    @Autowired
    public void setTestbedManager(final TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidNodeIdException
     *          invalid node id exception.
     * @throws eu.uberdust.rest.exception.InvalidCapabilityNameException
     *          invalid capability name exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *          invalid testbed id exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *          testbed not found exception.
     * @throws eu.uberdust.rest.exception.NodeNotFoundException
     *          node not found exception.
     * @throws eu.uberdust.rest.exception.CapabilityNotFoundException
     *          capability not found exception.
     * @throws eu.uberdust.rest.exception.InvalidLimitException
     *          invalid limit exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> showReadings(@PathVariable("testbedId") int testbedId, @PathVariable("sourceName") String sourceName, @PathVariable("targetName") String targetName, @PathVariable("capabilityName") String capabilityName, @PathVariable("limit") int limit)
            throws CapabilityNotFoundException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException, InvalidCapabilityNameException, InvalidNodeIdException, InvalidLimitException, NotImplementedException {

        final long start = System.currentTimeMillis();

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // retrieve link
        final Link link = linkManager.getByID(sourceName, targetName);
        if (link == null) {
            throw new NodeNotFoundException("Cannot find link [" + sourceName + "-" + targetName + "]");
        }

        // retrieve capability
        final Capability capability = capabilityManager.getByID(capabilityName);
        if (capability == null) {
            throw new CapabilityNotFoundException("Cannot find capability [" + capabilityName + "]");
        }

        // retrieve readings based on link/capability
        final List<LinkReading> linkReadings;

        linkReadings = linkReadingManager.list(link, capability, limit);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(TextFormatter.getInstance().formatLinkReadings(linkReadings), responseHeaders, HttpStatus.OK);

    }
}
