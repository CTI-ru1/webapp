package eu.uberdust.rest.controller.rdf;

import com.sun.syndication.io.FeedException;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.NodeNotFoundException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.LastNodeReadingController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractRestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller class that returns the position of a node in GeoRSS format.
 */
public final class ShowNodeRdfController extends AbstractRestController {

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
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ShowNodeRdfController.class);

    /**
     * Constructor.
     */
    public ShowNodeRdfController() {
        super();

        // Make sure to set which method this controller will support.
        this.setSupportedMethods(new String[]{METHOD_GET});
    }

    /**
     * Sets testbed persistence manager.
     *
     * @param testbedManager testbed persistence manager.
     */
    public void setTestbedManager(final TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }

    /**
     * Sets node persistence manager.
     *
     * @param nodeManager node persistence manager.
     */
    public void setNodeManager(final NodeController nodeManager) {
        this.nodeManager = nodeManager;
    }

    public void setLastNodeReadingManager(final LastNodeReadingController lastNodeReadingManager) {
        this.lastNodeReadingManager = lastNodeReadingManager;
    }

    /**
     * Handle request and return the appropriate response.
     *
     * @param request    http servlet request.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     BindException exception.
     * @return http servlet response.
     * @throws IOException               an IOException exception.
     * @throws FeedException             a FeedException exception.
     * @throws NodeNotFoundException     NodeNotFoundException exception.
     * @throws TestbedNotFoundException  TestbedNotFoundException exception.
     * @throws InvalidTestbedIdException InvalidTestbedIdException exception.
     */
    @SuppressWarnings("unchecked")
    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws IOException, FeedException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException {

//        // set command object
//        final NodeCommand command = (NodeCommand) commandObj;
//
//        // a specific testbed is requested by testbed Id
//        int testbedId;
//        try {
//            testbedId = Integer.parseInt(command.getTestbedId());
//
//        } catch (NumberFormatException nfe) {
//            throw new InvalidTestbedIdException("Testbed IDs have number format.", nfe);
//        }
//
//        // look up testbed
//        final Testbed testbed = testbedManager.getByID(Integer.parseInt(command.getTestbedId()));
//        if (testbed == null) {
//            // if no testbed is found throw exception
//            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
//        }
//
//        // look up node
//        final Node node = nodeManager.getByID(command.getNodeId());
//        if (node == null) {
//            // if no node is found throw exception
//            throw new NodeNotFoundException("Cannot find testbed [" + command.getNodeId() + "].");
//        }
//
//        // current host base URL
//        final String baseUrl = (request.getRequestURL().toString()).replace(request.getRequestURI(), "");
//        LOGGER.info("baseUrl : " + baseUrl);
//
//        RdfConverter.setLastNodeReadingManager(lastNodeReadingManager);
//
//
//        // set up feed and entries
//        response.setContentType("text/plain");
//        final Writer output = (response.getWriter());
//
//
//        final List<Semantic> semanticList = semanticManager.listByNode(node);
//
//        final String rdfString = RdfConverter.getRdf(node, request.getRequestURL().toString(), semanticList);
//
//        output.write(rdfString);
//        output.flush();
//        output.close();

        return null;
    }
}
