package eu.uberdust.rest.controller.tab;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.TextFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.exception.*;
import eu.wisebed.wisedb.controller.*;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.LastNodeReading;
import eu.wisebed.wisedb.model.Node;
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

import java.io.IOException;

/**
 * Controller class for returning the latest reading for a node/capability.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/node/{nodeName}/capability/{capabilityName}/latestreading")
public final class NodeCapabilityLatestReadingController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NodeCapabilityLatestReadingController.class);

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Node persistence manager.
     */
    private transient NodeController nodeManager;

    /**
     * Capability persistence manager.
     */
    private transient CapabilityController capabilityManager;
    /**
     * LastNodeReading persistence manager.
     */
    private transient NodeCapabilityController nodeCapabilityManager;
    /**
     *
     */
    private LastNodeReadingController lastNodeReadingManager;

    /**
     * Sets testbed persistence manager.
     *
     * @param testbedManager testbed persistence manager.
     */
    @Autowired
    public void setTestbedManager(final TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }

    /**
     * Sets node persistence manager.
     *
     * @param nodeManager node persistence manager.
     */
    @Autowired
    public void setNodeManager(final NodeController nodeManager) {
        this.nodeManager = nodeManager;
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

    @Autowired
    public void setNodeCapabilityManager(final NodeCapabilityController nodeCapabilityManager) {
        this.nodeCapabilityManager = nodeCapabilityManager;
    }

    @Autowired
    public void setLastNodeReadingManager(LastNodeReadingController lastNodeReadingManager) {
        this.lastNodeReadingManager = lastNodeReadingManager;
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws InvalidNodeIdException         InvalidNodeIdException exception.
     * @throws InvalidCapabilityNameException InvalidNodeCapability exception.
     * @throws InvalidTestbedIdException      InvalidTestbedIdException exception.
     * @throws TestbedNotFoundException       TestbedNotFoundException exception.
     * @throws NodeNotFoundException          NodeNotFoundException exception.
     * @throws CapabilityNotFoundException    CapabilityNotFoundException exception.
     * @throws IOException                    IOException exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> showReadings(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, @PathVariable("capabilityName") String capabilityName)
            throws InvalidNodeIdException, InvalidCapabilityNameException, InvalidTestbedIdException, TestbedNotFoundException, NodeNotFoundException, CapabilityNotFoundException, IOException {

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // retrieve node
        final Node node = nodeManager.getByName(nodeName);
        if (node == null) {
            throw new NodeNotFoundException("Cannot find node [" + nodeName + "]");
        }

        // retrieve capability
        final Capability capability = capabilityManager.getByID(capabilityName);
        if (capability == null) {
            throw new CapabilityNotFoundException("Cannot find capability [" + capabilityName + "]");
        }
        // retrieve last node rading for this node/capability
        final LastNodeReading lnr = lastNodeReadingManager.getByNodeCapability(node, capability);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        try {
            return new ResponseEntity<String>(TextFormatter.getInstance().formatNodeReading(lnr), responseHeaders, HttpStatus.OK);
        } catch (NotImplementedException e) {
            return new ResponseEntity<String>(e.getMessage(), responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
