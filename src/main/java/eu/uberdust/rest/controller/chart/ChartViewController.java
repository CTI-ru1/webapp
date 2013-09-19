package eu.uberdust.rest.controller.chart;

import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.*;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.NodeReadingController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.NodeReading;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that returns a graphical chart of the readings for a node/capability.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/node/{nodeName}/capability/{capabilityName}/chart")
public final class ChartViewController extends UberdustSpringController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ChartViewController.class);

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws InvalidNodeIdException         invalid node id exception.
     * @throws InvalidCapabilityNameException invalid capability name exception.
     * @throws InvalidTestbedIdException      invalid testbed id exception.
     * @throws TestbedNotFoundException       testbed not found exception.
     * @throws NodeNotFoundException          node not found exception.
     * @throws CapabilityNotFoundException    capability not found exception.
     */
    @Loggable
    @RequestMapping("/limit/{limit}")
    public ModelAndView showReadings(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, @PathVariable("capabilityName") String capabilityName, @PathVariable("limit") int limit, final HttpServletRequest servletRequest) throws InvalidNodeIdException, InvalidCapabilityNameException, InvalidTestbedIdException,
            TestbedNotFoundException, NodeNotFoundException, CapabilityNotFoundException {
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


        List<NodeReading> readings = new ArrayList<NodeReading>();
        List<NodeReading> unsortedReadings;
        if (limit > 0) {
            unsortedReadings = nodeReadingManager.listNodeReadings(node, capability, limit);
        } else {
            unsortedReadings = nodeReadingManager.listNodeReadings(node, capability);
        }

        for (int i = unsortedReadings.size() - 1; i >= 0; i--) {
            readings.add(unsortedReadings.get(i));
        }
        final StringBuilder data = new StringBuilder();
        for (final NodeReading reading : readings) {
            data.append(",[").append(reading.getTimestamp().getTime()).append(",");
            data.append(reading.getReading()).append("]").append("\n");
        }

        // Prepare data to pass to jsp


        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);
        refData.put("node", node);
        refData.put("capability", capability);
        refData.put("readings", data.toString().substring(1));
        refData.put("limit", limit);
        refData.put("myrequest", servletRequest.getRequestURL().toString());

        // check type of view requested
        return new ModelAndView("nodecapability/chart.html", refData);
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws InvalidNodeIdException         invalid node id exception.
     * @throws InvalidCapabilityNameException invalid capability name exception.
     * @throws InvalidTestbedIdException      invalid testbed id exception.
     * @throws TestbedNotFoundException       testbed not found exception.
     * @throws NodeNotFoundException          node not found exception.
     * @throws CapabilityNotFoundException    capability not found exception.
     */
    @Loggable
    @RequestMapping("/limit/{limit}/bare")
    public ModelAndView showReadingsBare(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, @PathVariable("capabilityName") String capabilityName, @PathVariable("limit") int limit) throws InvalidNodeIdException, InvalidCapabilityNameException, InvalidTestbedIdException,
            TestbedNotFoundException, NodeNotFoundException, CapabilityNotFoundException {
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


        List<NodeReading> readings = new ArrayList<NodeReading>();
        List<NodeReading> unsortedReadings;
        if (limit > 0) {
            unsortedReadings = nodeReadingManager.listNodeReadings(node, capability, limit);
        } else {
            unsortedReadings = nodeReadingManager.listNodeReadings(node, capability);
        }

        for (int i = unsortedReadings.size() - 1; i >= 0; i--) {
            readings.add(unsortedReadings.get(i));
        }
        final StringBuilder data = new StringBuilder();
        for (final NodeReading reading : readings) {
            data.append(",[").append(reading.getTimestamp().getTime()).append(",");
            data.append(reading.getReading()).append("]").append("\n");
        }

        // Prepare data to pass to jsp


        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);
        refData.put("node", node);
        refData.put("capability", capability);
        refData.put("readings", data.toString().substring(1));
        refData.put("limit", limit);

        // check type of view requested
        return new ModelAndView("nodecapability/chart-bare.html", refData);
    }
}
