package eu.uberdust.rest.controller.html.virtualnode;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.annotation.WiseLog;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.*;
import eu.wisebed.wisedb.model.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

/**
 * Controller class that returns a list of links for a given testbed in HTML format.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/virtualnode")
public final class HtmlVirtualNodeController extends UberdustSpringController {

    /**
     * Logger persistence manager.
     */
    private static final Logger LOGGER = Logger.getLogger(HtmlVirtualNodeController.class);

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *          an InvalidTestbedIdException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *          an TestbedNotFoundException exception.
     */
    @Loggable
    @WiseLog(logName = "/testbed/virtualnode/")
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listVirtualNodes(@PathVariable("testbedId") int testbedId)
            throws TestbedNotFoundException, InvalidTestbedIdException {

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
            if (node.getName().contains(":virtual:")) {
                nodes.add(node);
            }
        }

        // Prepare data to pass to jsp


        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);

        refData.put("nodes", nodes);

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        return new ModelAndView("virtualnode/list.html", refData);

    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *          an InvalidTestbedIdException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *          an TestbedNotFoundException exception.
     */
    @Loggable
    @WiseLog(logName = "/testbed/virtualnode/add/")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ModelAndView add(@PathVariable("testbedId") int testbedId,
                            @RequestParam("name") String name,
                            @RequestParam("conditions") String conditions)
            throws TestbedNotFoundException, InvalidTestbedIdException {

        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        Set<Node> nodesFound = null;
        try {
            JSONArray conditionsJSON = new JSONArray(conditions);
            LOGGER.info("Conditions:");
            for (int i = 0; i < conditionsJSON.length(); i++) {
                JSONObject curCondition = new JSONObject(conditionsJSON.get(i).toString());
                HashSet<Node> curNodesFound = new HashSet<Node>();
                try {
                    LOGGER.info(curCondition.toString());
                    String capabilityName = curCondition.getString("capability");
                    Capability capability = capabilityManager.getByID(capabilityName);
                    String capabilityValue = curCondition.getString("value");
                    final List<NodeCapability> nodeCapabilities = nodeCapabilityManager.list(testbed.getSetup(), capability);
                    if ("*".equals(capabilityValue)) {
                        for (NodeCapability nodeCapability : nodeCapabilities) {
                            curNodesFound.add(nodeCapability.getNode());
                        }
                    } else {
                        for (NodeCapability nodeCapability : nodeCapabilities) {
                            if (nodeCapability.getLastNodeReading().getStringReading().equals(capabilityValue)) {
                                curNodesFound.add(nodeCapability.getNode());
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                if (nodesFound == null) {
                    nodesFound = curNodesFound;
                } else {
                    nodesFound.retainAll(curNodesFound);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        LOGGER.info("Should Include : ");
        for (Node node : nodesFound) {
            LOGGER.info(node.getName());
        }

        // get testbed's nodes
        final List<Node> nodes = new ArrayList<Node>();
        for (Node node : nodeManager.list(testbed.getSetup())) {
            if (node.getName().contains(":virtual:")) {
                nodes.add(node);
            }
        }

        // Prepare data to pass to jsp


        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);

        refData.put("nodes", nodes);

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        return new ModelAndView("virtualnode/list.html", refData);

    }


    @Loggable
    @WiseLog(logName = "/testbed/virtualnode/create/")
    @RequestMapping(method = RequestMethod.GET, value = "/create")
    public ModelAndView createVirtualNode(@PathVariable("testbedId") int testbedId)
            throws TestbedNotFoundException, InvalidTestbedIdException {

        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // get testbed's nodes
        final List<Node> nodes = nodeManager.list(testbed.getSetup());
        final List<Capability> capabilities = capabilityManager.list(testbed.getSetup());
        // Prepare data to pass to jsp


        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);

        refData.put("nodes", nodes);
        refData.put("capabilities", capabilities);

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        return new ModelAndView("blockly/virtualnode/create.html", refData);

    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws java.io.IOException IO exception.
     */
    @Loggable
    @WiseLog(logName = "/testbed/virtualnode/rebuild/")
    @RequestMapping(value = "/rebuild", method = RequestMethod.GET)
    public ResponseEntity<String> rebuild(@PathVariable("testbedId") int testbedId) throws IOException, NotImplementedException {
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        final Testbed testbed = testbedManager.getByID(testbedId);
        final String response = virtualNodeDescriptionManager.rebuild(testbed.getId());
//        StringBuilder response = new StringBuilder("+---------------------------------------------------------------+\n");
//        List<VirtualNodeDescription> virtualNodes = virtualNodeDescriptionManager.list();
//        for (VirtualNodeDescription virtualNode : virtualNodes) {
//            response.append("VirtualNode:" + virtualNode.getNode().getName()).append("\n");
//
//            Set<Node> nodesFound = null;
//            try {
//                JSONArray conditionsJSON = new JSONArray(virtualNode.getDescription());
//                response.append("Conditions:").append("\n");
//                for (int i = 0; i < conditionsJSON.length(); i++) {
//                    JSONObject curCondition = new JSONObject(conditionsJSON.get(i).toString());
//                    HashSet<Node> curNodesFound = new HashSet<Node>();
//                    try {
//                        response.append(curCondition.toString()).append("\n");
//                        String capabilityName = curCondition.getString("capability");
//                        Capability capability = capabilityManager.getByID(capabilityName);
//                        String capabilityValue = curCondition.getString("value");
//                        final List<NodeCapability> nodeCapabilities = nodeCapabilityManager.list(virtualNode.getNode().getSetup(), capability);
//                        if ("*".equals(capabilityValue)) {
//                            for (NodeCapability nodeCapability : nodeCapabilities) {
//                                curNodesFound.add(nodeCapability.getNode());
//                            }
//                        } else {
//                            for (NodeCapability nodeCapability : nodeCapabilities) {
//                                try {
//                                    if (nodeCapability.getLastNodeReading().getStringReading().equals(capabilityValue)) {
//                                        curNodesFound.add(nodeCapability.getNode());
//                                    }
//                                } catch (Exception e) {
//                                    LOGGER.error(e, e);
//                                }
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                    }
//                    if (nodesFound == null) {
//                        nodesFound = curNodesFound;
//                    } else {
//                        nodesFound.retainAll(curNodesFound);
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//            response.append("Should Include : ").append("\n");
//            Capability virtual = capabilityManager.getByID("virtual");
//            for (Node node : nodesFound) {
//                response.append(node.getName());
//                final Link link = linkManager.getByID(virtualNode.getNode().getName(), node.getName());
//                if (link != null) {
//                    final LinkCapability linkCapability = linkCapabilityManager.getByID(link, virtual);
//                    if (linkCapability != null) {
//                        if (linkCapability.getLastLinkReading().getReading() != 1.0) {
//                            addLinkReading(virtualNode.getNode().getName(), node.getName(), 1.0);
//                            response.append("<--");
//                        }
//                    } else {
//                        addLinkReading(virtualNode.getNode().getName(), node.getName(), 1.0);
//                        response.append("<--");
//                    }
//                } else {
//                    addLinkReading(virtualNode.getNode().getName(), node.getName(), 1.0);
//                    response.append("<--");
//                }
//                response.append("\n");
//            }
//            List<Link> links = linkManager.getBySource(virtualNode.getNode());
//            response.append("Should Disconnect: ").append("\n");
//            for (Link link : links) {
//                final LinkCapability linkCapability = linkCapabilityManager.getByID(link, virtual);
//                if (linkCapability.getLastLinkReading().getReading()!=0.0 && !nodesFound.contains(link.getTarget())){
//                    addLinkReading(virtualNode.getNode().getName(), link.getTarget().getName(), 0.0);
//                    response.append(link.getTarget().getName()).append("\n");
//                }
//            }
//             response.append("+---------------------------------------------------------------+\n");
//        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");


        return new ResponseEntity<String>(response.toString(), responseHeaders, HttpStatus.OK);
    }

}
