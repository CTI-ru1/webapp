package eu.uberdust.rest.controller.html.testbed;

import eu.uberdust.caching.Cachable;
import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.*;
import eu.wisebed.wisedb.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that returns the a web page for a testbed.
 */
@Controller
@RequestMapping("/testbed/{testbedId}")
public final class ShowController extends UberdustSpringController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ShowController.class);

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Capability persistence manager.
     */
    private transient CapabilityController capabilityManager;

    /**
     * Link persistence manager.
     */
    private transient LinkController linkManager;

    /**
     * Node persistence manager.
     */
    private transient NodeController nodeManager;


    /**
     * Node Capability persistence manager.
     */
    private transient NodeCapabilityController nodeCapabilityManager;


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
     * Sets capability persistence manager.
     *
     * @param capabilityManager capability persistence manager.
     */
    @Autowired
    public void setCapabilityManager(final CapabilityController capabilityManager) {
        this.capabilityManager = capabilityManager;
    }

    /**
     * Sets link persistence manager.
     *
     * @param linkManager link persistence manager.
     */
    @Autowired
    public void setLinkManager(final LinkController linkManager) {
        this.linkManager = linkManager;
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

    @Autowired
    public void setNodeCapabilityManager(final NodeCapabilityController nodeCapabilityManager) {
        this.nodeCapabilityManager = nodeCapabilityManager;
    }

    /**
     * Handle req and return the appropriate response.
     *
     * @throws TestbedNotFoundException  a TestbedNotFoundException exception.
     * @throws InvalidTestbedIdException a InvalidTestbedException exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showTestbed(@PathVariable("testbedId") int testbedId)
            throws TestbedNotFoundException, InvalidTestbedIdException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());


        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }


        // get testbed nodes
        final List<Node> allNodes = nodeManager.list(testbed.getSetup());
        final List<Node> nodes = new ArrayList<Node>();
        final List<Node> virtual = new ArrayList<Node>();
        for (Node node : allNodes) {
            if (node.getName().contains("virtual")) {
                virtual.add(node);
            } else {
                nodes.add(node);
            }
        }

        Map<String, Position> nodePositions = getNodePositions(testbed);
        Map<String, String> nodeTypes = getNodeTypes(testbed);

        // get testbed links
        final List<Link> links = linkManager.list(testbed.getSetup());
        // get testbed capabilities
        final List<Capability> capabilities = capabilityManager.list(testbed.getSetup());

        // Prepare data to pass to jsp


        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);
        refData.put("setup", testbed.getSetup());
        refData.put("testbed", testbed);
        refData.put("nodes", nodes);
        refData.put("links", links);
        refData.put("virtual", virtual);
        refData.put("capabilities", capabilities);
        refData.put("nodePositions", nodePositions);
        refData.put("nodeTypes", nodeTypes);
        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));

        return new ModelAndView("testbed/show.html", refData);
    }

    @Cachable
    private Map<String, Position> getNodePositions(Testbed testbed) {
        final List<Node> nodes = nodeManager.list(testbed.getSetup());
        Map<String, Position> nodePositions = new HashMap<String, Position>();
        for (Node node : nodes) {
            Position nodePosition = nodeManager.getAbsolutePosition(node);
            nodePositions.put(node.getName(), nodePosition);
        }
        return nodePositions;
    }

    @Cachable
    private Map<String, String> getNodeTypes(Testbed testbed) {
        final List<Node> nodes = nodeManager.list(testbed.getSetup());
        Map<String, String> nodeTypes = new HashMap<String, String>();
        for (Node node : nodes) {
            NodeCapability cap = nodeCapabilityManager.getByID(node, "nodeType");
            if (cap != null) {
                nodeTypes.put(node.getName(), cap.getLastNodeReading().getStringReading());
            } else {
                nodeTypes.put(node.getName(), "default");
            }

        }
        return nodeTypes;
    }
}
