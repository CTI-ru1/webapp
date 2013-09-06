package eu.uberdust.rest.controller.tab;

import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.*;
import eu.wisebed.wisedb.controller.*;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.Position;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * Controller class that returns readings of a specific node in a tab delimited format.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/node/{nodeName}/position/")
public final class NodePositionTabDelimitedViewController extends UberdustSpringController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NodePositionTabDelimitedViewController.class);

    /**
     * Node persistence manager.
     */
    private transient NodeController nodeManager;

    /**
     * Capability persistence manager.
     */
    private transient CapabilityController capabilityManager;

    /**
     * NodeReading persistence manager.
     */
    private transient NodeReadingController nodeReadingManager;

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;
    /**
     *
     */
    private LastNodeReadingController lastNodeReadingManager;

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

    /**
     * Sets NodeReading persistence manager.
     *
     * @param nodeReadingManager NodeReading persistence manager.
     */
    @Autowired
    public void setNodeReadingManager(final NodeReadingController nodeReadingManager) {
        this.nodeReadingManager = nodeReadingManager;
    }

    /**
     * Sets testbed persistence manager.
     *
     * @param testbedManager testbed peristence manager.
     */
    @Autowired
    public void setTestbedManager(final TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }

    @Autowired
    public void setLastNodeReadingManager(LastNodeReadingController lastNodeReadingManager) {
        this.lastNodeReadingManager = lastNodeReadingManager;
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidNodeIdException
     *                             InvalidNodeIdException exception.
     * @throws eu.uberdust.rest.exception.InvalidCapabilityNameException
     *                             InvalidNodeCapability exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                             InvalidTestbedIdException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                             TestbedNotFoundException exception.
     * @throws eu.uberdust.rest.exception.NodeNotFoundException
     *                             NodeNotFoundException exception.
     * @throws eu.uberdust.rest.exception.CapabilityNotFoundException
     *                             CapabilityNotFoundException exception.
     * @throws java.io.IOException IOException exception.
     */
    @Loggable
    @RequestMapping(value = "/x", method = RequestMethod.GET)
    public ResponseEntity<String> getX(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName)
            throws InvalidNodeIdException, InvalidCapabilityNameException, InvalidTestbedIdException, TestbedNotFoundException, NodeNotFoundException, CapabilityNotFoundException, IOException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

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

        Position nodePosition = nodeManager.getAbsolutePosition(node);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(nodePosition.getX().toString(), responseHeaders, HttpStatus.OK);
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidNodeIdException
     *                             InvalidNodeIdException exception.
     * @throws eu.uberdust.rest.exception.InvalidCapabilityNameException
     *                             InvalidNodeCapability exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                             InvalidTestbedIdException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                             TestbedNotFoundException exception.
     * @throws eu.uberdust.rest.exception.NodeNotFoundException
     *                             NodeNotFoundException exception.
     * @throws eu.uberdust.rest.exception.CapabilityNotFoundException
     *                             CapabilityNotFoundException exception.
     * @throws java.io.IOException IOException exception.
     */
    @Loggable
    @RequestMapping(value = "/y", method = RequestMethod.GET)
    public ResponseEntity<String> getY(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName)
            throws InvalidNodeIdException, InvalidCapabilityNameException, InvalidTestbedIdException, TestbedNotFoundException, NodeNotFoundException, CapabilityNotFoundException, IOException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

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

        Position nodePosition = nodeManager.getAbsolutePosition(node);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(nodePosition.getY().toString(), responseHeaders, HttpStatus.OK);
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidNodeIdException
     *                             InvalidNodeIdException exception.
     * @throws eu.uberdust.rest.exception.InvalidCapabilityNameException
     *                             InvalidNodeCapability exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                             InvalidTestbedIdException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                             TestbedNotFoundException exception.
     * @throws eu.uberdust.rest.exception.NodeNotFoundException
     *                             NodeNotFoundException exception.
     * @throws eu.uberdust.rest.exception.CapabilityNotFoundException
     *                             CapabilityNotFoundException exception.
     * @throws java.io.IOException IOException exception.
     */
    @Loggable
    @RequestMapping(value = "/z", method = RequestMethod.GET)
    public ResponseEntity<String> getZ(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName)
            throws InvalidNodeIdException, InvalidCapabilityNameException, InvalidTestbedIdException, TestbedNotFoundException, NodeNotFoundException, CapabilityNotFoundException, IOException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

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

        Position nodePosition = nodeManager.getAbsolutePosition(node);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(nodePosition.getZ().toString(), responseHeaders, HttpStatus.OK);
    }
}
