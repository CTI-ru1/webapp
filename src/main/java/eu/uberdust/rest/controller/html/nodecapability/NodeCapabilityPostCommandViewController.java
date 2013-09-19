package eu.uberdust.rest.controller.html.nodecapability;

import ch.ethz.inf.vs.californium.coap.CodeRegistry;
import ch.ethz.inf.vs.californium.coap.Option;
import ch.ethz.inf.vs.californium.coap.OptionNumberRegistry;
import ch.ethz.inf.vs.californium.coap.Request;
import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.*;
import eu.uberdust.util.CommandDispatcher;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Controller class that returns an HTML page containing a list of the readings for a node/capability.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/node/{nodeName}/capability/{capabilityName}/{payload}/")
public final class NodeCapabilityPostCommandViewController extends UberdustSpringController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NodeCapabilityPostCommandViewController.class);

    private Random rand;

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
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String handle(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, @PathVariable("capabilityName") String capabilityName, @PathVariable("payload") String payload, HttpServletResponse response)
            throws CapabilityNotFoundException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException, InvalidCapabilityNameException, InvalidNodeIdException, InvalidLimitException, IOException {
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
        String payloadString = "";
        if (node.getName().contains("virtual")) {
            List<Node> nodes = nodeManager.getRealNodes(node);
            for (Node node1 : nodes) {
                if (capabilityManager.hasCapability(node1, capability)) {
                    coapSend(node1, capability, payload);
                }
            }
        } else {
            coapSend(node, capability, payload);
        }


        response.setStatus(200);
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        return "OK . Destination : " + node.getName() + "\nPayload : " + payloadString;
    }

    public String coapSend(Node node, Capability capability, String payload) {
        Request coapReq = new Request(CodeRegistry.METHOD_POST, false);
        if (rand == null) {
            rand = new Random();
            rand.setSeed(node.getId() + capability.getName().hashCode() + payload.hashCode());
        }
        coapReq.setMID(rand.nextInt() % 60000);

        if (node.getName().contains("0x")) {
            Option uriHost = new Option(OptionNumberRegistry.URI_HOST);
            uriHost.setStringValue(node.getName().split("0x")[1]);
            coapReq.addOption(uriHost);
        }
        final String capShortName = capability.getName().substring(capability.getName().lastIndexOf(":") + 1);
        coapReq.setURI(capShortName);
        coapReq.setPayload(payload);

        StringBuilder payloadStringBuilder = new StringBuilder();
        for (Byte data : coapReq.toByteArray()) {
            int i = data;
            payloadStringBuilder.append(",");
            payloadStringBuilder.append(Integer.toHexString(i));
        }

        final String payloadString = "33," + payloadStringBuilder.toString().substring(1).replaceAll("ffffff", "");

        CommandDispatcher.getInstance().sendCommand(node.getSetup().getId(), node.getName(), payloadString);
        return payloadString;
    }
}
