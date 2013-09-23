package eu.uberdust.rest.controller.html.node;

import eu.uberdust.caching.Cachable;
import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.annotation.WiseLog;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class that returns the a web page for a node.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/node")
public final class HtmlNodeController extends UberdustSpringController {
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(HtmlNodeController.class);

    /**
     * Handle req and return the appropriate response.
     *
     * @return http servlet response.
     * @throws InvalidTestbedIdException InvalidTestbedIdException exception.
     * @throws TestbedNotFoundException  TestbedNotFoundException exception.
     * @throws NodeNotFoundException     NodeNotFoundException exception.
     */
    @Loggable
    @WiseLog(logName = "/testbed/node/show/")
    @RequestMapping(value = "/{nodeName}", method = RequestMethod.GET)
    public ModelAndView getNode(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName) throws TestbedNotFoundException, NodeNotFoundException {

        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());


        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // look up node
        final Node node = nodeManager.getByName(nodeName);
        if (node == null) {
            // if no testbed is found throw exception
            throw new NodeNotFoundException("Cannot find node [" + nodeName + "].");
        }

        List<NodeCapability> nodeCapabilities = nodeCapabilityManager.list(node);


        Position nodePosition = nodeManager.getAbsolutePosition(node);
        String nodeType = getNodeType(node);


        // Prepare data to pass to jsp
        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);
        refData.put("setup", testbed.getSetup());
        refData.put("node", node);
        refData.put("nodePosition", nodePosition);
        refData.put("nodeType", nodeType);
        refData.put("nodeCapabilities", nodeCapabilities);

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        return new ModelAndView("node/show.html", refData);
    }

    @Loggable
    @RequestMapping(value = "{nodeName}/", method = RequestMethod.PUT)
    @Transactional
    @ResponseBody
    public String putNode(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, HttpServletResponse response)
            throws InvalidTestbedIdException, TestbedNotFoundException, NodeNotFoundException, IOException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        if (nodeManager.getByName(nodeName) != null) {
            response.setStatus(200);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            return "Node Already Exists";
        }

        final Node newNode = new Node();
        newNode.setName(nodeName);
        newNode.setSetup(testbed.getSetup());

        nodeManager.add(newNode);
        // write on the HTTP response
        response.setStatus(200);
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        return "Node added to the system";
    }


    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws InvalidTestbedIdException an InvalidTestbedIdException exception.
     * @throws TestbedNotFoundException  an TestbedNotFoundException exception.
     */
    @Loggable
    @WiseLog(logName = "/testbed/node/")
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listNodes(@PathVariable("testbedId") int testbedId) throws TestbedNotFoundException {


        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // get testbed's nodes
        final List<Node> nodes = new ArrayList<Node>();
        for (Node node : nodeManager.list(testbed.getSetup())) {
            if (!node.getName().contains(":virtual:")) {
                nodes.add(node);
            }
        }

        // Prepare data to pass to jsp


        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);

        refData.put("nodes", nodes);

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        return new ModelAndView("node/list.html", refData);

    }

    @Cachable
    String getNodeType(Node node) {
        String nodeType = "default";
        NodeCapability cap = nodeCapabilityManager.getByID(node, "nodeType");
        if (cap != null) {
            nodeType = cap.getLastNodeReading().getStringReading();
        }
        return nodeType;
    }

}
