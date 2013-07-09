package eu.uberdust.rest.controller.html.testbed;

import eu.uberdust.caching.Loggable;
import eu.uberdust.command.TestbedCommand;
import eu.uberdust.formatter.HtmlFormatter;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.Coordinate;
import eu.wisebed.wisedb.controller.*;
import eu.wisebed.wisedb.model.*;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractRestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that returns the a web page for a testbed.
 */
public final class ShowController extends AbstractRestController {

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Capability persistence manager.
     */
    private transient CapabilityController capabilityManager;

    /**
     * Link persistence manager.
     */
    private transient LinkController linkManager;

    /**
     * Node persistence manager.
     */
    private transient NodeController nodeManager;

    /**
     * Node Capability persistence manager.
     */
    private transient NodeCapabilityController nodeCapabilityManager;


    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ShowController.class);

    /**
     * Constructor.
     */
    public ShowController() {
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
     * Sets capability persistence manager.
     *
     * @param capabilityManager capability persistence manager.
     */
    public void setCapabilityManager(final CapabilityController capabilityManager) {
        this.capabilityManager = capabilityManager;
    }

    /**
     * Sets link persistence manager.
     *
     * @param linkManager link persistence manager.
     */
    public void setLinkManager(final LinkController linkManager) {
        this.linkManager = linkManager;
    }

    /**
     * Sets node persistence manager.
     *
     * @param nodeManager node persistence manager.
     */
    public void setNodeManager(final NodeController nodeManager) {
        this.nodeManager = nodeManager;
    }

    public void setNodeCapabilityManager(final NodeCapabilityController nodeCapabilityManager) {
        this.nodeCapabilityManager = nodeCapabilityManager;
    }

    /**
     * Handle req and return the appropriate response.
     *
     * @param req        http servlet req.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     a BindException exception.
     * @return http servlet response
     * @throws TestbedNotFoundException  a TestbedNotFoundException exception.
     * @throws InvalidTestbedIdException a InvalidTestbedException exception.
     */
    @Loggable
    protected ModelAndView handle(final HttpServletRequest req, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws TestbedNotFoundException, InvalidTestbedIdException {
        HtmlFormatter.getInstance().setBaseUrl(req.getRequestURL().substring(0, req.getRequestURL().indexOf("/rest")));

        final long start = System.currentTimeMillis();

        // set command object
        final TestbedCommand command = (TestbedCommand) commandObj;

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


        // get testbed nodes
        final List<Node> allNodes = nodeManager.list(testbed.getSetup());
        final List<Node> nodes = new ArrayList<Node>();
        final List<Node> virtual = new ArrayList<Node>();
        for (Node node : allNodes) {
            if (node.getName().contains("virtual")) {
                virtual.add(node);
            } else {
                nodes.add(node);
            }
        }

        Map<String, Position> nodePositions = new HashMap<String, Position>();
        Map<String, String> nodeTypes = new HashMap<String, String>();
        for (Node node : nodes) {
            Position nodePosition = nodeManager.getPosition(node);

            if (!(testbed.getSetup().getCoordinateType().equals("Absolute"))) {
                // determine testbed origin by the type of coordinates given
                final Origin origin = testbed.getSetup().getOrigin();
                Coordinate originCoordinate = new Coordinate();
                originCoordinate.setX((double) origin.getX());
                originCoordinate.setY((double) origin.getY());
                originCoordinate.setZ((double) origin.getZ());
                originCoordinate.setPhi((double) origin.getPhi());
                originCoordinate.setTheta((double) origin.getTheta());
                Coordinate properOrigin = Coordinate.blh2xyz(originCoordinate);

                Position testbedPosition = new Position();
                testbedPosition.setX(testbed.getSetup().getOrigin().getX());
                testbedPosition.setY(testbed.getSetup().getOrigin().getY());
                testbedPosition.setZ(testbed.getSetup().getOrigin().getZ());
                testbedPosition.setPhi(testbed.getSetup().getOrigin().getPhi());
                testbedPosition.setTheta(testbed.getSetup().getOrigin().getTheta());

                Coordinate nodeCoordinate = new Coordinate();
                nodeCoordinate.setX((double) nodePosition.getX());
                nodeCoordinate.setY((double) nodePosition.getY());
                nodeCoordinate.setZ((double) nodePosition.getZ());

                final Coordinate rotated = Coordinate.rotate(nodeCoordinate, properOrigin.getPhi());
                final Coordinate absolute = Coordinate.absolute(properOrigin, rotated);
                final Coordinate finalNodePosition = Coordinate.xyz2blh(absolute);
                nodePosition.setX(Float.parseFloat(finalNodePosition.getX().toString()));
                nodePosition.setY(Float.parseFloat(finalNodePosition.getY().toString()));
            }
            nodePositions.put(node.getName(), nodePosition);
            NodeCapability cap = nodeCapabilityManager.getByID(node, "nodeType");
            if (cap != null) {
                nodeTypes.put(node.getName(), cap.getLastNodeReading().getStringReading());
            } else {
                nodeTypes.put(node.getName(), "default");
            }

        }

        // get testbed links
        final List<Link> links = linkManager.list(testbed.getSetup());
        // get testbed capabilities
        final List<Capability> capabilities = capabilityManager.list(testbed.getSetup());

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);
        refData.put("setup", testbed.getSetup());
        refData.put("testbed", testbed);
        refData.put("nodes", nodes);
        refData.put("links", links);
        refData.put("virtual", virtual);
        refData.put("capabilities", capabilities);
        refData.put("nodePositions", nodePositions);
        refData.put("nodeTypes", nodeTypes);
        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));

        return new ModelAndView("testbed/show.html", refData);
    }


}