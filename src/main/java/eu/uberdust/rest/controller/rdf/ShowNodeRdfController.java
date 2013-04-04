package eu.uberdust.rest.controller.rdf;

import com.sun.syndication.io.FeedException;
import eu.uberdust.command.NodeCommand;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.NodeNotFoundException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.LastNodeReadingController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractRestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

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

        LOGGER.info("showNodeRdfController(...)");

        // set command object
        final NodeCommand command = (NodeCommand) commandObj;

        // a specific testbed is requested by testbed Id
        int testbedId;
        try {
            testbedId = Integer.parseInt(command.getTestbedId());

        } catch (NumberFormatException nfe) {
            throw new InvalidTestbedIdException("Testbed IDs have number format.", nfe);
        }

        // look up testbed
        final Testbed testbed = testbedManager.getByID(Integer.parseInt(command.getTestbedId()));
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // look up node
        final Node node = nodeManager.getByName(command.getNodeId());
        if (node == null) {
            // if no node is found throw exception
            throw new NodeNotFoundException("Cannot find testbed [" + command.getNodeId() + "].");
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


        // set up feed and entries
        response.setContentType("application/rdf+xml");
        final Writer output = (response.getWriter());

        output.write(retVal);
        output.flush();
        output.close();

        return null;
    }
}
