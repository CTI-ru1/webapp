package eu.uberdust.rest.controller.georss;

import com.sun.syndication.io.FeedException;
import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.GeoRssFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.NodeNotFoundException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.NodeCapabilityController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.NodeCapability;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that returns the position of a node in GeoRSS format.
 */
@Controller
public final class GeoRssViewController extends UberdustSpringController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(GeoRssViewController.class);

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws IOException               an IOException exception.
     * @throws FeedException             a FeedException exception.
     * @throws NodeNotFoundException     NodeNotFoundException exception.
     * @throws TestbedNotFoundException  TestbedNotFoundException exception.
     * @throws InvalidTestbedIdException InvalidTestbedIdException exception.
     */
    @Loggable
    @RequestMapping(value = "/testbed/{testbedId}/node/{nodeName}/georss", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> showNodeGeorssFeed(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, HttpServletRequest request, HttpServletResponse response)
            throws IOException, FeedException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException, NotImplementedException {
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // look up node
        final Node node = nodeManager.getByName(nodeName);
        if (node == null) {
            // if no node is found throw exception
            throw new NodeNotFoundException("Cannot find testbed [" + nodeName + "].");
        }

        final String description = nodeManager.getDescription(node);
        final Position nodePos = nodeManager.getPosition(node);
        String output = "";

        output = GeoRssFormatter.getInstance().describeNode(node,
                request.getRequestURL().toString(),
                request.getRequestURI().toString(),
                description, nodePos);
        return xmlResponse(output);
    }

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws TestbedNotFoundException  a TestbedNotFoundException exception.
     * @throws InvalidTestbedIdException a InvalidTestbedIdException exception.
     * @throws IOException               a IOException exception.
     * @throws FeedException             a FeedException exception.
     */
    @SuppressWarnings("unchecked")
    @Loggable
    @RequestMapping(value = "/testbed/{testbedId}/georss", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> showTestbedGeorssFeed(@PathVariable("testbedId") int testbedId, HttpServletRequest request, HttpServletResponse response)
            throws TestbedNotFoundException, InvalidTestbedIdException, IOException, FeedException, NotImplementedException {
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Testbed testbed = testbedManager.getByID(testbedId);

        final List<Node> nodes = nodeManager.list(testbed.getSetup());
        final List<Node> nodesList = new ArrayList<Node>();
        final Map<Node, String> descriptionMap = new HashMap<Node, String>();
        final Map<Node, List<NodeCapability>> capabilityMap = new HashMap<Node, List<NodeCapability>>();
        final Map<Node, Position> originMap = new HashMap<Node, Position>();
        for (final Node node : nodes) {
            if (node.getName().contains("virtual")) {
                continue;
            }
            String desc = null;
            List<NodeCapability> caps;
            Position origins;
            try {
                desc = nodeManager.getDescription(node);
                caps = nodeCapabilityManager.list(node);
                origins = nodeManager.getPosition(node);
                if ((desc == null) || (caps == null) || (origins == null)) {
                    continue;
                }
            } catch (Exception e) {
                continue;
            }

            nodesList.add(node);
            descriptionMap.put(node, desc);
            capabilityMap.put(node, caps);
            originMap.put(node, origins);

        }

        String output = "";

        output = GeoRssFormatter.getInstance().describeTestbed(testbed,
                request.getRequestURL().toString(),
                request.getRequestURI(),
                nodesList, descriptionMap, capabilityMap, originMap);

        return xmlResponse(output);
    }
}
