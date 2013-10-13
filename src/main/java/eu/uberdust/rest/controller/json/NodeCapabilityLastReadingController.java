package eu.uberdust.rest.controller.json;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.JsonFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.*;
import eu.wisebed.wisedb.controller.*;
import eu.wisebed.wisedb.controller.NodeCapabilityController;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.LastNodeReading;
import eu.wisebed.wisedb.model.Node;
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

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/testbed/{testbedId}/node/{nodeName}/capability/{capabilityName}/latestreading/json")
public final class NodeCapabilityLastReadingController extends UberdustSpringController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NodeCapabilityLastReadingController.class);

    /**
     * Handle Request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws InvalidTestbedIdException      invalid testbed id exception.
     * @throws TestbedNotFoundException       testbed not found exception.
     * @throws NodeNotFoundException          node not found exception.
     * @throws CapabilityNotFoundException    capability not found exception.
     * @throws InvalidCapabilityNameException invalid capability name exception.
     * @throws InvalidNodeIdException         invalid node id exception.
     * @throws IOException                    IO exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> showReadings(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, @PathVariable("capabilityName") String capabilityName)
            throws InvalidTestbedIdException, TestbedNotFoundException, NodeNotFoundException,
            CapabilityNotFoundException, InvalidCapabilityNameException, InvalidNodeIdException, IOException, NotImplementedException {
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
        // retrieve last node rading for this node/capability
        final LastNodeReading lnr = lastNodeReadingManager.getByNodeCapability(node, capability);

        System.out.println("Reached here " + lnr.toString());

        return jsonResponse(JsonFormatter.getInstance().formatNodeReading(lnr));
    }

    @PostConstruct
    public void initializeCaching() {
        LOGGER.info("initializeCaching--lastnodereadingcontroller");
        final List<Testbed> testbeds = testbedManager.list();
        for (Testbed testbed : testbeds) {
            if (testbed.getId() == 2) continue;
            final List<Node> allNodes = nodeManager.list();
            final Capability roomCapability = capabilityManager.getByID("room");
            final Capability workstationCapability = capabilityManager.getByID("workstation");
            final Capability nameCapability = capabilityManager.getByID("name");
            for (Node node : allNodes) {
                if (node.getName().contains("virtual")) continue;
                lastNodeReadingManager.getByNodeCapability(node, roomCapability);
                lastNodeReadingManager.getByNodeCapability(node, workstationCapability);
//                lastNodeReadingManager.getByNodeCapability(node, nameCapability);
            }
        }
        LOGGER.info("done--initializeCaching--lastnodereadingcontroller");
    }
}
