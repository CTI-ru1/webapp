package eu.uberdust.rest.controller.rdf;

import com.sun.syndication.io.FeedException;
import eu.uberdust.command.NodeCapabilityCommand;
import eu.uberdust.formatter.RdfFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.NodeNotFoundException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.NodeReadingController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.NodeReading;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractRestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Controller class that returns the position of a node in GeoRSS format.
 */
public final class NodeCapabilityRdfController extends AbstractRestController {

    /**
     * Tested persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Node persistence manager.
     */
    private transient NodeController nodeManager;
    /**
     * NodeReading persistence manager.
     */
    private transient NodeReadingController nodeReadingManager;
    /**
     * Capability persistence manager.
     */
    private transient CapabilityController capabilityManager;
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NodeCapabilityRdfController.class);

    /**
     * Constructor.
     */
    public NodeCapabilityRdfController() {
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

    /**
     * Sets NodeReading persistence manager.
     *
     * @param nodeReadingManager NodeReading persistence manager.
     */
    public void setNodeReadingManager(final NodeReadingController nodeReadingManager) {
        this.nodeReadingManager = nodeReadingManager;
    }

    /**
     * Sets capability persistence manager.
     *
     * @param capabilityManager capability persistence manager.
     */
    public void setCapabilityManager(final CapabilityController capabilityManager) {
        this.capabilityManager = capabilityManager;
    }

    /**
     * Handle request and return the appropriate response.
     *
     * @param request    http servlet request.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     BindException exception.
     * @return http servlet response.
     * @throws java.io.IOException an IOException exception.
     * @throws com.sun.syndication.io.FeedException
     *                             a FeedException exception.
     * @throws eu.uberdust.rest.exception.NodeNotFoundException
     *                             NodeNotFoundException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                             TestbedNotFoundException exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                             InvalidTestbedIdException exception.
     */
    @SuppressWarnings("unchecked")
    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws IOException, FeedException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException {

        LOGGER.info("nodeCapabilityRdfController(...)");

        // set command object
        final NodeCapabilityCommand command = (NodeCapabilityCommand) commandObj;

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

        final Capability capability = capabilityManager.getByID(command.getCapabilityId());
//        final Capability capabilityRoom = capabilityManager.getByID("room");

        List<NodeReading> readings = nodeReadingManager.listNodeReadings(node, capability, Integer.parseInt(command.getReadingsLimit()));
//        List<NodeReading> roomReading = nodeReadingManager.listNodeReadings(node, capabilityRoom, 1);
//        readings.add(1, roomReading.get(0));

        // current host base URL

        String retVal = "";
        try {
//            Model model = ModelFactory.createDefaultModel();
//            model.read((String) RdfFormatter.getInstance().formatNodeReadings(readings));
            retVal = ((String) RdfFormatter.getInstance().formatNodeReadings(readings));
//
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            if (command.getFormat().toLowerCase().equals("turtle")) {
//                response.setContentType("text/turtle");
//                model.write(bos, "TURTLE");
//                retVal = bos.toString().replaceAll("rdf-xml", "turtle");
//
//            } else if (command.getFormat().toLowerCase().equals("n-triple")) {
//                response.setContentType("text/plain");
//                model.write(bos, "N-TRIPLE");
//                retVal = bos.toString().replaceAll("rdf-xml", "n-triple");
//            } else {
//                response.setContentType("application/rdf+xml");
//                model.write(bos, "RDF/XML");
//                retVal = bos.toString();
//            }


        } catch (NotImplementedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        // set up feed and entries
        response.setContentType("application/rdf+xml");
        final Writer output = (response.getWriter());

        output.write(retVal);
        output.flush();
        output.close();

        return null;
    }
}
