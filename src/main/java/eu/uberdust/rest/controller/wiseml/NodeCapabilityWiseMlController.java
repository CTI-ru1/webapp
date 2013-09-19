package eu.uberdust.rest.controller.wiseml;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.*;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.NodeReadingController;
import eu.wisebed.wisedb.controller.TestbedController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller class that returns the readings of a node capability pair in WiseML format.
 */
@Controller
public final class NodeCapabilityWiseMlController extends UberdustSpringController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NodeCapabilityWiseMlController.class);

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     */
    // TODO make this controller
    @Loggable
    @RequestMapping(value = "/testbed/{testbedId}/wiseml", method = RequestMethod.GET)
    public ModelAndView getTestbedWiseML(@PathVariable("testbedId") int testbedId) throws NotImplementedException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        LOGGER.info("showTestbedWiseMLController(...)");
        throw new NotImplementedException();
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws InvalidNodeIdException         a InvalidNodeIdException exception.
     * @throws InvalidCapabilityNameException a InvalidCapabilityNameException exception.
     * @throws InvalidTestbedIdException      a InvalidTestbedIdException exception.
     * @throws TestbedNotFoundException       a TestbedNotFoundException exception.
     * @throws NodeNotFoundException          a NodeNotFoundExeption exception.
     * @throws CapabilityNotFoundException    a CapabilityNotFoundException exception.
     * @throws InvalidLimitException          a InvalidLimitException exception.
     */
    @Loggable
    @RequestMapping(value = "/testbed/{testbedId}/node/{nodeName}/capability/{capabilityName}/wiseml/limit/{limit}", method = RequestMethod.GET)
    public ModelAndView getNodeWiseML(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, @PathVariable("capabilityName") String capabilityName, @PathVariable("limit") int limit)
            throws InvalidNodeIdException, InvalidCapabilityNameException, InvalidTestbedIdException,
            TestbedNotFoundException, NodeNotFoundException, CapabilityNotFoundException, InvalidLimitException, NotImplementedException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

//        // look up testbed
//        final Testbed testbed = testbedManager.getByID(testbedId);
//        if (testbed == null) {
//            // if no testbed is found throw exception
//            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
//        }
//
//        // retrieve node
//        final Node node = nodeManager.getByName(nodeName);
//        if (node == null) {
//            throw new NodeNotFoundException("Cannot find node [" + nodeName + "]");
//        }
//
//        // retrieve capability
//        final Capability capability = capabilityManager.getByID(capabilityName);
//        if (capability == null) {
//            throw new CapabilityNotFoundException("Cannot find capability [" + capabilityName + "]");
//        }
//
//        // retrieve readings based on node/capability
//        final List<NodeReading> nodeReadings = nodeReadingManager.listNodeReadings(node, capability, limit);

        throw new NotImplementedException();
    }
}

