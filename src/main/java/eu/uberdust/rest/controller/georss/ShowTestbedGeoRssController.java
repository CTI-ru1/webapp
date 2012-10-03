package eu.uberdust.rest.controller.georss;

import com.sun.syndication.io.FeedException;
import eu.uberdust.caching.Loggable;
import eu.uberdust.command.TestbedCommand;
import eu.uberdust.formatter.GeoRssFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.NodeCapabilityController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.NodeCapability;
import eu.wisebed.wisedb.model.Position;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractRestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that returns the setup of a testbed in GeoRSS format.
 */
public final class ShowTestbedGeoRssController extends AbstractRestController {

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;


    public NodeController getNodeManager() {
        return nodeManager;
    }

    public void setNodeManager(final NodeController nodeManager) {
        this.nodeManager = nodeManager;
    }

    private transient NodeController nodeManager;

    private transient NodeCapabilityController nodeCapabilityManager;

    public void setNodeCapabilityManager(final NodeCapabilityController nodeCapabilityManager) {
        this.nodeCapabilityManager = nodeCapabilityManager;
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ShowTestbedGeoRssController.class);

    /**
     * Constructor.
     */
    public ShowTestbedGeoRssController() {
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
     * Handle request and return the appropriate response.
     *
     * @param request    http servlet request.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     a BindException exception.
     * @return http servlet response.
     * @throws TestbedNotFoundException  a TestbedNotFoundException exception.
     * @throws InvalidTestbedIdException a InvalidTestbedIdException exception.
     * @throws IOException               a IOException exception.
     * @throws FeedException             a FeedException exception.
     */
    @SuppressWarnings("unchecked")
    @Loggable
    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws TestbedNotFoundException, InvalidTestbedIdException, IOException, FeedException {

        // set command object
        final TestbedCommand command = (TestbedCommand) commandObj;

        // a specific testbed is requested by testbed Id
        int testbedId;
        try {
            testbedId = Integer.parseInt(command.getTestbedId());

        } catch (NumberFormatException nfe) {
            throw new InvalidTestbedIdException("Testbed IDs have number format.", nfe);
        }
        Testbed testbed = testbedManager.getByID(testbedId);

        final List<Node> nodes = nodeManager.list(testbed.getSetup());
        final List<Node> nodesList = new ArrayList<Node>();
        final Map<Node, String> descriptionMap = new HashMap<Node, String>();
        final Map<Node, List<NodeCapability>> capabilityMap = new HashMap<Node, List<NodeCapability>>();
        final Map<Node, Position> originMap = new HashMap<Node, Position>();
        for (final Node node : nodes) {
            if (node.getName().contains("virtual")) {
                continue;
            }
            String desc = null;
            List<NodeCapability> caps;
            Position origins;
            try {
                desc = nodeManager.getDescription(node);
                caps = nodeCapabilityManager.list(node);
                origins = nodeManager.getPosition(node);
                if ((desc == null) || (caps == null) || (origins == null)) {
                    continue;
                }
            } catch (Exception e) {
                continue;
            }

            nodesList.add(node);
            descriptionMap.put(node, desc);
            capabilityMap.put(node, caps);
            originMap.put(node, origins);

        }

        String output = "";

        try {
            output = GeoRssFormatter.getInstance().describeTestbed(testbed,
                    request.getRequestURL().toString(),
                    request.getRequestURI(),
                    nodesList, descriptionMap, capabilityMap, originMap);
        } catch (NotImplementedException e) {
            output = e.getMessage();
        }


        // set up feed and entries
        response.setContentType("application/xml; charset=UTF-8");

        final Writer textOutput = (response.getWriter());
        response.getWriter().append(output);
        textOutput.flush();
        textOutput.close();

        return null;
    }
}
