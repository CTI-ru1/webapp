package eu.uberdust.rest.controller.html.testbed;

import eu.uberdust.caching.Cachable;
import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.HtmlFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.annotation.WiseLog;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.model.*;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Controller class that returns a list of testbed in HTML format.
 */
@Controller
public final class HtmlTestbedController extends UberdustSpringController {


    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(HtmlTestbedController.class);

    @PostConstruct
    public void initfun() {
        LOGGER.info("running initfun---");
        for (Node n : nodeManager.list()) {
            nodeManager.getAbsolutePosition(n);
        }
        LOGGER.info("finished init---");
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     */
    @Loggable
    @WiseLog(logName = "/testbed/")
    @RequestMapping(value = {"/", "/testbed"}, method = RequestMethod.GET)
    public ModelAndView listTestbeds() {
        final long start = System.currentTimeMillis();
        try {
            initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        } catch (Exception e) {
            refData = new HashMap<String, Object>();
        }
        try {

            // testbed list
            final List<Testbed> testbeds = testbedManager.list();
            if (testbeds.size() == 0) {
                return new ModelAndView("testbed/add.html", refData);
            }
            LOGGER.info("Got Testbed " + String.valueOf((System.currentTimeMillis() - start)));
            Map<Integer, Origin> origins = new HashMap<Integer, Origin>();
//            List<Position> nodePositions = new ArrayList<Position>();
            for (Testbed testbed : testbeds) {
                origins.put(testbed.getId(), testbed.getSetup().getOrigin());
//                Map<String, Position> testbedNodePositions = getNodePositions(testbed);
//                nodePositions.addAll(testbedNodePositions.values());
            }
            LOGGER.info("Got Positions " + String.valueOf((System.currentTimeMillis() - start)));


            final Map<String, Long> nodesCount = testbedManager.countNodes();
            LOGGER.info("Got nodeCount " + String.valueOf((System.currentTimeMillis() - start)));
            final Map<String, Long> linksCount = testbedManager.countLinks();
            LOGGER.info("Got linkCount" + String.valueOf((System.currentTimeMillis() - start)));

            refData.put("testbeds", testbeds);
            refData.put("nodes", nodesCount);
            refData.put("links", linksCount);
            refData.put("origins", origins);
//            refData.put("nodePositions", nodePositions);

            refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
            return new ModelAndView("testbed/list.html", refData);
        } catch (Exception e) {
            LOGGER.error("e", e);
        }
        return null;
    }

    /**
     * Handle req and return the appropriate response.
     *
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *          a TestbedNotFoundException exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *          a InvalidTestbedException exception.
     */
    @Loggable
    @WiseLog(logName = "/testbed/id/")
    @RequestMapping(value = "/testbed/{testbedId}", method = RequestMethod.GET)
    public ModelAndView showTestbed(@PathVariable("testbedId") int testbedId)
            throws InvalidTestbedIdException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());


        refData.put("admin", userRoleManager.isAdmin(userManager.getByUsername(current_user)));

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            return new ModelAndView("testbed/404.html", refData);
        }

        LOGGER.info("Got Testbed" + String.valueOf((System.currentTimeMillis() - start)));

        // get testbed nodes
        final List<Node> allNodes = nodeManager.list(testbed.getSetup());
        LOGGER.info("Got Testbed" + String.valueOf((System.currentTimeMillis() - start)));
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

        final List<Link> links = linkManager.list(setupManager.getByID(testbed.getId()));
        // get testbed capabilities
        final List<Capability> capabilities = capabilityManager.list(setupManager.getByID(testbed.getId()));

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


    @RequestMapping(value = "/testbed/{testbedId}/name", method = RequestMethod.GET)
    public ResponseEntity<String> showTestbedName(@PathVariable("testbedId") int testbedId)
            throws TestbedNotFoundException, InvalidTestbedIdException {
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }
        return rawResponse(testbed.getName());
    }

    @RequestMapping(value = "/testbed/{testbedId}/name", method = RequestMethod.POST)
    public ResponseEntity<String> changeTestbedName(@PathVariable("testbedId") int testbedId, @RequestBody String body)
            throws TestbedNotFoundException, InvalidTestbedIdException {
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        try {
            body = URLDecoder.decode(body, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        testbed.setName(body);
        testbedManager.update(testbed);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }
        return rawResponse("Name Changed to '" + body + "'");
    }


    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws InvalidTestbedIdException a InvalidTestbedIDException exception.
     * @throws TestbedNotFoundException  a TestbedNotFoundException exception.
     */
    @Loggable
    @WiseLog(logName = "/testbed/status/")
    @RequestMapping(value = "/testbed/{testbedId}/status", method = RequestMethod.GET)
    public ModelAndView showTestbedStatus(@PathVariable("testbedId") int testbedId) throws TestbedNotFoundException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try {
            long start1 = System.currentTimeMillis();


            LOGGER.info("--------- Get Testbed id: " + (System.currentTimeMillis() - start1));
            start1 = System.currentTimeMillis();

            // look up testbed
            final Testbed testbed = testbedManager.getByID(testbedId);
            if (testbed == null) {
                return new ModelAndView("testbed/404.html", refData);
            }
            LOGGER.info("got testbed " + testbed);

            LOGGER.info("--------- Get Testbed: " + (System.currentTimeMillis() - start1));


            if (nodeCapabilityManager == null) {
                LOGGER.error("nodeCapabilityManager==null");
            }

            long before30Min = new Date().getTime() - 30 * 60 * 1000;
            start1 = System.currentTimeMillis();
            // get a list of node last readings from testbed
            final List<NodeCapability> nodeCapabilities = nodeCapabilityManager.list(testbed.getSetup());
            final Set<String> updated = new HashSet<String>();

            for (NodeCapability nodeCapability : nodeCapabilities) {
                try {
                    if (nodeCapability.getLastNodeReading().getTimestamp().getTime() - before30Min > 0)
                        updated.add(nodeCapability.getNode().getName());
                } catch (NullPointerException e) {
                    LOGGER.error(e + " Node:" + nodeCapability.toString());
                }
            }
            LOGGER.info("--------- list nodeCapabilities: " + (System.currentTimeMillis() - start1));

            start1 = System.currentTimeMillis();

            LOGGER.info("--------- format last node readings: " + (System.currentTimeMillis() - start1));

            start1 = System.currentTimeMillis();
            // get a list of link statistics from testbed
            final List<LinkCapability> linkCapabilities = linkCapabilityManager.list(testbed.getSetup());
            LOGGER.info("--------- List link capabilities: " + (System.currentTimeMillis() - start1));


            // Prepare data to pass to jsp

            refData.put("testbed", testbed);
            refData.put("lastNodeReadings", nodeCapabilities);
            refData.put("updated", updated);

            try {
                start1 = System.currentTimeMillis();
                refData.put("lastLinkReadings", HtmlFormatter.getInstance().formatLastLinkReadings(linkCapabilities));
                LOGGER.info("--------- format link Capabilites: " + (System.currentTimeMillis() - start1));
            } catch (NotImplementedException e) {
                LOGGER.error(e, e);
            }

            LOGGER.info("--------- Total time: " + (System.currentTimeMillis() - start));
            refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
            LOGGER.info("prepared map");
        } catch (Exception e) {
            LOGGER.error(e, e);
        }
        return new ModelAndView("testbed/status.html", refData);

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
