package eu.uberdust.rest.controller.tab;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.TextFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.exception.*;
import eu.wisebed.wisedb.controller.NodeCapabilityController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.NodeCapability;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class that returns an HTML page containing a list of the readings for a node/capability.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/node/{nodeName}/capabilities")
public final class ListNodeCapabilitiesController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ListNodeCapabilitiesController.class);

    /**
     * Node peristence manager.
     */
    private transient NodeController nodeManager;

    /**
     * Testbed peristence manager.
     */
    private transient TestbedController testbedManager;

    private transient NodeCapabilityController nodeCapabilityManager;

    @Autowired
    public void setNodeCapabilityManager(final NodeCapabilityController nodeCapabilityManager) {
        this.nodeCapabilityManager = nodeCapabilityManager;
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
     * @throws InvalidNodeIdException         invalid node id exception.
     * @throws InvalidCapabilityNameException invalid capability name exception.
     * @throws InvalidTestbedIdException      invalid testbed id exception.
     * @throws TestbedNotFoundException       testbed not found exception.
     * @throws NodeNotFoundException          node not found exception.
     * @throws CapabilityNotFoundException    capability not found exception.
     * @throws InvalidLimitException          invalid limit exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> showReadings(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName) throws CapabilityNotFoundException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException, InvalidCapabilityNameException, InvalidNodeIdException, InvalidLimitException, NotImplementedException {

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
        final List<Capability> capabilities = new ArrayList<Capability>();
        for (final NodeCapability capability : nodeCapabilityManager.list(node)) {
            capabilities.add(capability.getCapability());
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");

        return new ResponseEntity<String>(TextFormatter.getInstance().formatCapabilities(testbed, capabilities), responseHeaders, HttpStatus.OK);
    }
}
