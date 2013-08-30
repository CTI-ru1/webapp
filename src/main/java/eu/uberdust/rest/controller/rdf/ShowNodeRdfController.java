package eu.uberdust.rest.controller.rdf;

import com.sun.syndication.io.FeedException;
import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.NodeNotFoundException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.LastNodeReadingController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
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

import java.io.IOException;

/**
 * Controller class that returns the position of a node in GeoRSS format.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/node/{nodeName}/rdf/{rdfEncoding}")
public final class ShowNodeRdfController extends UberdustSpringController{

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ShowNodeRdfController.class);

    /**
     * Tested persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Node persistence manager.
     */
    private transient NodeController nodeManager;

    private transient LastNodeReadingController lastNodeReadingManager;

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
     * Sets node persistence manager.
     *
     * @param nodeManager node persistence manager.
     */
    @Autowired
    public void setNodeManager(final NodeController nodeManager) {
        this.nodeManager = nodeManager;
    }

    @Autowired
    public void setLastNodeReadingManager(final LastNodeReadingController lastNodeReadingManager) {
        this.lastNodeReadingManager = lastNodeReadingManager;
    }

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
    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> handle(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, @PathVariable("rdfEncoding") String rdfEncoding)
            throws IOException, FeedException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        LOGGER.info("showNodeRdfController(...)");


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

        // current host base URL

        String retVal = "";
//        try {
//            Model model = (Model) RdfFormatter.getInstance().formatNode(node);
//
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            if (command.getFormat().toLowerCase().equals("turtle")) {
//                response.setContentType("text/turtle");
//                model.write(bos, "TURTLE");
//            } else if (command.getFormat().toLowerCase().equals("n-triple")) {
//                response.setContentType("text/plain");
//                model.write(bos, "N-TRIPLE");
//            } else {
//                response.setContentType("application/rdf+xml");
//                model.write(bos, "RDF/XML");
//            }
//
//            retVal = bos.toString();
//
//        } catch (NotImplementedException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }


        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/rdf+xml; charset=UTF-8");
        return new ResponseEntity<String>(retVal, responseHeaders, HttpStatus.OK);
    }
}
