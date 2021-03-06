package eu.uberdust.rest.controller.tab;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.TextFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.CapabilityController;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.*;

/**
 * Controller class that returns the status page for the nodes and links of a testbed.
 */
@Controller
@RequestMapping("/testbed/{testbedId}")
public final class TestbedTabDelimitedViewController extends UberdustSpringController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(TestbedTabDelimitedViewController.class);

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException a InvalidTestbedIDException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException  a TestbedNotFoundException exception.
     */
    @Loggable
    @RequestMapping(value = "/rooms", method = RequestMethod.GET)
    public ResponseEntity<String> getTestbedRooms(@PathVariable("testbedId") int testbedId)
            throws InvalidTestbedIdException, TestbedNotFoundException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        final Capability capability = capabilityManager.getByID("room");


        // get a list of node last readings from testbed
        final List<NodeCapability> nodeCapabilities = nodeCapabilityManager.list(testbed.getSetup(), capability);

        try {
            return rawResponse(TextFormatter.getInstance().formatUniqueLastNodeReadings(nodeCapabilities));
        } catch (NotImplementedException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
            return new ResponseEntity<String>(e.getMessage(), responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException a InvalidTestbedIDException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException  a TestbedNotFoundException exception.
     */
    @Loggable
    @RequestMapping(value = "/timezone", method = RequestMethod.GET)
    public ResponseEntity<String> getTestbedTimezone(@PathVariable("testbedId") int testbedId)
            throws InvalidTestbedIdException, TestbedNotFoundException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        return rawResponse(testbed.getTimeZone().getDisplayName(Locale.getDefault()));
    }

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException a InvalidTestbedIDException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException  a TestbedNotFoundException exception.
     */
    @Loggable
    @RequestMapping(value = "/timezone/offset", method = RequestMethod.GET)
    public ResponseEntity<String> getTestbedTimezoneOffset(@PathVariable("testbedId") int testbedId)
            throws InvalidTestbedIdException, TestbedNotFoundException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        int offset = testbed.getTimeZone().getRawOffset() + testbed.getTimeZone().getDSTSavings();

        return rawResponse("" + offset);
    }


    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException a InvalidTestbedIDException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException  a TestbedNotFoundException exception.
     */
    @Loggable
    @RequestMapping(value = "/status/raw", method = RequestMethod.GET)
    public ResponseEntity<String> getTestbedStatus(@PathVariable("testbedId") int testbedId)
            throws InvalidTestbedIdException, TestbedNotFoundException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());


        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }
        LOGGER.info("got testbed " + testbed);

        if (nodeCapabilityManager == null) {
            LOGGER.error("nodeCapabilityManager==null");
        }

        // get a list of node last readings from testbed
        final List<NodeCapability> nodeCapabilities = nodeCapabilityManager.list(testbed.getSetup());

        try {
            return rawResponse(TextFormatter.getInstance().formatLastNodeReadings(nodeCapabilities));
        } catch (NotImplementedException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
            return new ResponseEntity<String>(e.getMessage(), responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException a InvalidTestbedIDException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException  a TestbedNotFoundException exception.
     */
    @Loggable
    @RequestMapping(value = "/timeout", method = RequestMethod.GET)
    public ResponseEntity<String> getTestbedTimeouts(@PathVariable("testbedId") int testbedId)
            throws InvalidTestbedIdException, TestbedNotFoundException, IOException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        LOGGER.info("showTestbedAdminStatusController(...)");

        Testbed testbed = testbedManager.getByID(testbedId);
        List<Node> nodes = nodeManager.list(testbed.getSetup());

        int counter = 0;

        StringBuilder responseSB = new StringBuilder("Running nodes :\n");
        String type = "";
        String room = "";
        String status = "";
        for (Node node : nodes) {
            type = "";
            room = "";
            status = "";
            if (node.getName().contains("virtual")) continue;
            List<NodeCapability> ncaps = nodeCapabilityManager.list(node);
            long max = -1;
            for (NodeCapability ncap : ncaps) {
                if (ncap.getCapability().getName().contains("nodetype")) {
                    type = ncap.getLastNodeReading().getStringReading();
                }
                if (ncap.getCapability().getName().contains("status")) {
                    status = ncap.getLastNodeReading().getStringReading();
                }
                if (ncap.getCapability().getName().contains("room")) {
                    room = ncap.getLastNodeReading().getStringReading();
                }
                long timediff = System.currentTimeMillis() - ncap.getLastNodeReading().getTimestamp().getTime();
                if (max == -1) {
                    max = timediff;
                } else if (timediff < max) {
                    max = timediff;
                }
            }

            if (status.contains("offline")) continue;

            long secs = max / 1000;
            long min = secs / 60;
            long hours = min / 60;
            long days = hours / 24;
            String mess = new StringBuilder().append(node).append(",").append(type).append(" ").append(room).append(" @ ").toString();
            if (secs < 60) {
                responseSB.append(mess).append(min).append(" mins ago\n");
            } else if (min < 35) {
                responseSB.append(mess).append(min).append(" mins ago\n");
            } else if (min < 60) {
                responseSB.insert(0, new StringBuilder().append(mess).append(min).append(" mins ago\n").toString());
            } else if (hours < 24) {
                counter++;
                responseSB.insert(0, new StringBuilder().append(mess).append(hours).append(" hours ago\n").toString());
            } else {
                counter++;
                responseSB.insert(0, new StringBuilder().append(mess).append(days).append(" days ago\n").toString());
            }
        }
        responseSB.insert(0, "Late Nodes :\n");
        responseSB.insert(0, "Total Nodes: " + nodes.size() + "\n");

        return rawResponse(responseSB.toString());
    }

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException a InvalidTestbedIDException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException  a TestbedNotFoundException exception.
     */
    @Loggable
    @RequestMapping(value = "/adminstatus", method = RequestMethod.GET)
    public ResponseEntity<String> getTestbedAdminStatus(@PathVariable("testbedId") int testbedId)
            throws InvalidTestbedIdException, TestbedNotFoundException, IOException {

        LOGGER.info("showTestbedAdminStatusController(...)");

        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed                                              nodeCapabilityManager
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }
        LOGGER.info("got testbed " + testbed);

        // get a list of node last readings from testbed
        final List<NodeCapability> nodeCapabilities = nodeCapabilityManager.list(testbed.getSetup());

        // Prepare data to pass to jsp


        Map<Node, List<NodeCapability>> nodeCapabilityMap = new HashMap<Node, List<NodeCapability>>();

        for (final NodeCapability capability : nodeCapabilities) {
            if (nodeCapabilityMap.containsKey(capability.getNode())) {
                nodeCapabilityMap.get(capability.getNode()).add(capability);
            } else {
                nodeCapabilityMap.put(capability.getNode(), new ArrayList<NodeCapability>());
                nodeCapabilityMap.get(capability.getNode()).add(capability);
            }
        }
        final StringBuilder output = new StringBuilder();
        for (final Node node : nodeCapabilityMap.keySet()) {
            boolean outdated = true;

            for (NodeCapability nodeCapability : nodeCapabilityMap.get(node)) {
                outdated = outdated && isOutdated(nodeCapability);
            }

            if (outdated) {
                final NodeCapability roomCap = nodeCapabilityManager.getByID(node, "room");
                final NodeCapability nodeCap = nodeCapabilityManager.getByID(node, "nodetype");
                final String room = roomCap == null ? "null" : roomCap.getLastNodeReading().getStringReading();
                final String nodetype = nodeCap == null ? "null" : nodeCap.getLastNodeReading().getStringReading();

                if (!node.getName().contains("virtual")) {
                    output.append(node.getName()).append("\t").
                            append(room == null ? "null" : room).append("\t")
                            .append(nodetype == null ? "null" : nodetype).append("\n");
                }
            }
        }


        return rawResponse(output.toString());
    }

    private boolean isOutdated(NodeCapability capability) {
        if (capability.getLastNodeReading().getTimestamp() != null) {
            return (System.currentTimeMillis() - capability.getLastNodeReading().getTimestamp().getTime()) > 86400000;
        }
        return false;
    }
}
