package eu.uberdust.rest.controller.html.node;

import eu.uberdust.caching.Loggable;
import eu.uberdust.command.NodeCommand;
import eu.uberdust.formatter.HtmlFormatter;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.NodeNotFoundException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.Coordinate;
import eu.wisebed.wisedb.controller.NodeCapabilityController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.*;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractRestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that returns the a web page for a node.
 */
public final class GetController extends AbstractRestController {
    /**
     * Node persistence manager.
     */
    private transient NodeController nodeManager;

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    public void setNodeCapabilityManager(final NodeCapabilityController nodeCapabilityManager) {
        this.nodeCapabilityManager = nodeCapabilityManager;
    }

    private transient NodeCapabilityController nodeCapabilityManager;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(GetController.class);

    /**
     * Constructor.
     */
    public GetController() {
        super();

        // Make sure to set which method this controller will support.
        this.setSupportedMethods(new String[]{METHOD_GET});
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
     * Sets testbed persistence manager.
     *
     * @param testbedManager testbed persistence manager.
     */
    public void setTestbedManager(final TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }

    /**
     * Handle req and return the appropriate response.
     *
     * @param req        http servlet req.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     BindException errors.
     * @return http servlet response.
     * @throws InvalidTestbedIdException InvalidTestbedIdException exception.
     * @throws TestbedNotFoundException  TestbedNotFoundException exception.
     * @throws NodeNotFoundException     NodeNotFoundException exception.
     */
    @Loggable
    protected ModelAndView handle(final HttpServletRequest req, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws InvalidTestbedIdException, TestbedNotFoundException, NodeNotFoundException {
        final long start = System.currentTimeMillis();


        HtmlFormatter.getInstance().setBaseUrl(req.getRequestURL().substring(0, req.getRequestURL().indexOf("/rest")));

        // set command object
        final NodeCommand command = (NodeCommand) commandObj;

        // a specific testbed is requested by testbed Id
        int testbedId;
        try {
            testbedId = Integer.parseInt(command.getTestbedId());

        } catch (NumberFormatException nfe) {
            throw new InvalidTestbedIdException("Testbed IDs have number format.", nfe);
        }
        final Testbed testbed = testbedManager.getByID(Integer.parseInt(command.getTestbedId()));
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // look up node
        final Node node = nodeManager.getByName(command.getNodeId());
        if (node == null) {
            // if no testbed is found throw exception
            throw new NodeNotFoundException("Cannot find testbed [" + command.getNodeId() + "].");
        }

        List<NodeCapability> nodeCapabilities = nodeCapabilityManager.list(node);


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
        String nodeType = "default";
        NodeCapability cap = nodeCapabilityManager.getByID(node, "nodeType");
        if (cap != null) {
            nodeType = cap.getLastNodeReading().getStringReading();
        }


        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);
        refData.put("setup", testbed.getSetup());
        refData.put("node", node);
        refData.put("nodePosition", nodePosition);
        refData.put("nodeType", nodeType);
        refData.put("nodeCapabilities", nodeCapabilities);

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        return new ModelAndView("node/show.html", refData);
    }
}
