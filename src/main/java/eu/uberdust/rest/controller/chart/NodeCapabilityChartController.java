package eu.uberdust.rest.controller.chart;

import eu.uberdust.caching.Loggable;
import eu.uberdust.command.NodeCapabilityCommand;
import eu.uberdust.rest.controller.json.NodeCapabilityController;
import eu.uberdust.rest.exception.CapabilityNotFoundException;
import eu.uberdust.rest.exception.InvalidCapabilityNameException;
import eu.uberdust.rest.exception.InvalidNodeIdException;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that returns a graphical chart of the readings for a node/capability.
 */
public final class NodeCapabilityChartController extends AbstractRestController {

    /**
     * NodeReadings persistence manager.
     */
    private transient NodeReadingController nodeReadingManager;
    /**
     * Node persistence manager.
     */
    private transient NodeController nodeManager;

    /**
     * Capability persistence manager.
     */
    private transient CapabilityController capabilityManager;

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NodeCapabilityController.class);

    /**
     * Constructor.
     */
    public NodeCapabilityChartController() {
        super();

        // Make sure to set which method this controller will support.
        this.setSupportedMethods(new String[]{METHOD_GET});
    }

    /**
     * Sets nodeReadings persistence manager.
     *
     * @param nodeReadingManager nodeReadings persistence manager.
     */
    public void setNodeReadingManager(final NodeReadingController nodeReadingManager) {
        this.nodeReadingManager = nodeReadingManager;
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
     * Sets capability persistence manager.
     *
     * @param capabilityManager capability persistence manager.
     */
    public void setCapabilityManager(final CapabilityController capabilityManager) {
        this.capabilityManager = capabilityManager;
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
     * Handle Request and return the appropriate response.
     *
     * @param request    http servlet request.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     BindException exception.
     * @return response http servlet response.
     * @throws InvalidNodeIdException         invalid node id exception.
     * @throws InvalidCapabilityNameException invalid capability name exception.
     * @throws InvalidTestbedIdException      invalid testbed id exception.
     * @throws TestbedNotFoundException       testbed not found exception.
     * @throws NodeNotFoundException          node not found exception.
     * @throws CapabilityNotFoundException    capability not found exception.
     */
    @Loggable
    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws InvalidNodeIdException, InvalidCapabilityNameException, InvalidTestbedIdException,
            TestbedNotFoundException, NodeNotFoundException, CapabilityNotFoundException {


        // set command object
        final NodeCapabilityCommand command = (NodeCapabilityCommand) commandObj;

        // check node id
        if (command.getNodeId() == null || command.getNodeId().isEmpty()) {
            throw new InvalidNodeIdException("Must provide node id");
        }

        // check capability name
        if (command.getCapabilityId() == null || command.getCapabilityId().isEmpty()) {
            throw new InvalidCapabilityNameException("Must provide capability name");
        }

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

        // retrieve node
        final Node node = nodeManager.getByName(command.getNodeId());
        if (node == null) {
            throw new NodeNotFoundException("Cannot find node [" + command.getNodeId() + "]");
        }

        // retrieve capability
        final Capability capability = capabilityManager.getByID(command.getCapabilityId());
        if (capability == null) {
            throw new CapabilityNotFoundException("Cannot find capability [" + command.getCapabilityId() + "]");
        }


        // check if limit is provided
        int limit = 0;
        if (command.getReadingsLimit() != null) {
            limit = Integer.valueOf(command.getReadingsLimit());
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
        final Map<String, Object> refData = new HashMap<String, Object>();

        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);
        refData.put("node", node);
        refData.put("capability", capability);
        refData.put("readings", data.toString().substring(1));
        refData.put("limit", limit);

        // check type of view requested
        return new ModelAndView("nodecapability/chart.html", refData);
    }
}
