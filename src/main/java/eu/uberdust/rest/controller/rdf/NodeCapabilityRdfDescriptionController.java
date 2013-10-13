package eu.uberdust.rest.controller.rdf;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.RdfFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.NodeNotFoundException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.NodeCapability;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class that returns the position of a node in GeoRSS format.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/node/{nodeName}/capability/{capabilityName}/rdf/{rdfEncoding}")
public final class NodeCapabilityRdfDescriptionController extends UberdustSpringController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NodeCapabilityRdfDescriptionController.class);

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws java.io.IOException an IOException exception.
     *                             a FeedException exception.
     * @throws eu.uberdust.rest.exception.NodeNotFoundException
     *                             NodeNotFoundException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                             TestbedNotFoundException exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                             InvalidTestbedIdException exception.
     */
    @Loggable
    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> handle(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, @PathVariable("capabilityName") String capabilityName, @PathVariable("rdfEncoding") String rdfEncoding)
            throws NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException, NotImplementedException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        LOGGER.info("nodeCapabilityRdfController(...)");

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


        final Capability capability = capabilityManager.getByID(capabilityName);

        List<NodeCapability> nodeCapabilitiesList = new ArrayList<NodeCapability>();
        nodeCapabilitiesList.add(nodeCapabilityManager.getByID(node, capability));

        String retVal = "";

        retVal = RdfFormatter.getInstance().describeNodeCapability(nodeCapabilitiesList);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/rdf+xml; charset=UTF-8");
        return new ResponseEntity<String>(retVal, responseHeaders, HttpStatus.OK);
    }
}
