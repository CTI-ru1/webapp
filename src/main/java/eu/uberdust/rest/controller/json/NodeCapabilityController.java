package eu.uberdust.rest.controller.json;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.JsonFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.*;
import eu.wisebed.wisedb.controller.*;
import eu.wisebed.wisedb.model.*;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for returning a list of readings for a node/capability pair in JSON format.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/node/{nodeName}/capability/{capabilityName}")
public final class NodeCapabilityController extends UberdustSpringController{

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NodeCapabilityController.class);

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws InvalidTestbedIdException      invalid testbed id exception.
     * @throws TestbedNotFoundException       testbed not found exception.
     * @throws InvalidNodeIdException         invalid Node id exception.
     * @throws NodeNotFoundException          node not found exception.
     * @throws InvalidCapabilityNameException invalid capability name exception.
     * @throws CapabilityNotFoundException    capability not found exception.
     * @throws IOException                    IO exception.
     * @throws InvalidLimitException          invalid limit exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET, value = "/json/limit/{limit}")
    public ResponseEntity<String> showReadings(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, @PathVariable("capabilityName") String capabilityName, @PathVariable("limit") int limit)
            throws InvalidNodeIdException, InvalidCapabilityNameException, InvalidTestbedIdException,
            TestbedNotFoundException, NodeNotFoundException, CapabilityNotFoundException,
            IOException, InvalidLimitException, NotImplementedException {
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

        // retrieve capability
        final Capability capability = capabilityManager.getByID(capabilityName);
        if (capability == null) {
            throw new CapabilityNotFoundException("Cannot find capability [" + capabilityName + "]");
        }

        // retrieve readings based on node/capability
        List<NodeReading> nodeReadings = new ArrayList<NodeReading>();

        if (limit == 1) {
            final LastNodeReading lnr = lastNodeReadingManager.getByNodeCapability(node, capability);
            NodeReading nr = new NodeReading();
            nr.setCapability(lnr.getNodeCapability());
            nr.setReading(lnr.getReading());
            nr.setStringReading(lnr.getStringReading());
            nr.setTimestamp(lnr.getTimestamp());
            nodeReadings.add(nr);
        } else {
            nodeReadings = nodeReadingManager.listNodeReadings(node, capability, limit);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>((String) JsonFormatter.getInstance().formatNodeReadings(nodeReadings), responseHeaders, HttpStatus.OK);
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
    @RequestMapping(value = "/json/from/{from}/to/{to}/", method = RequestMethod.GET)
    public ResponseEntity<String> getReadingsByDate(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, @PathVariable("capabilityName") String capabilityName, @PathVariable("from") long from, @PathVariable("to") long to)
            throws CapabilityNotFoundException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException, InvalidCapabilityNameException, InvalidNodeIdException, InvalidLimitException, IOException {
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

        // retrieve capability
        final Capability capability = capabilityManager.getByID(capabilityName);
        if (capability == null) {
            throw new CapabilityNotFoundException("Cannot find capability [" + capabilityName + "]");
        }

        // retrieve readings based on node/capability
        final List<NodeReading> nodeReadings;

        // no limit is provided
        nodeReadings = nodeReadingManager.listNodeReadings(node, capability, from, to);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        try {
            return new ResponseEntity<String>((String) JsonFormatter.getInstance().formatNodeReadings(nodeReadings), responseHeaders, HttpStatus.OK);
        } catch (NotImplementedException e) {
            return new ResponseEntity<String>(e.getMessage(), responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
