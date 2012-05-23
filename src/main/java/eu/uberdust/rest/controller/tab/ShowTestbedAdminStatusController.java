package eu.uberdust.rest.controller.tab;

import eu.uberdust.command.TestbedCommand;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.NodeCapabilityController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.NodeCapability;
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
 * Controller class that returns the status page for the nodes and links of a testbed.
 */
public final class ShowTestbedAdminStatusController extends AbstractRestController {

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Last node reading persistence manager.
     */
    private transient NodeCapabilityController nodeCapabilityManager;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ShowTestbedAdminStatusController.class);

    /**
     * Constructor.
     */
    public ShowTestbedAdminStatusController() {
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

    public void setNodeCapabilityManager(final NodeCapabilityController nodeCapabilityManager) {
        this.nodeCapabilityManager = nodeCapabilityManager;
    }

    /**
     * Handle request and return the appropriate response.
     *
     * @param request    http servlet request.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     a BindException exception.
     * @return http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *          a InvalidTestbedIDException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *          a TestbedNotFoundException exception.
     */
    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws InvalidTestbedIdException, TestbedNotFoundException, IOException {
        try {

            LOGGER.info("showTestbedAdminStatusController(...)");

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

            // look up testbed                                              nodeCapabilityManager
            final Testbed testbed = testbedManager.getByID(Integer.parseInt(command.getTestbedId()));
            if (testbed == null) {
                // if no testbed is found throw exception
                throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
            }
            LOGGER.info("got testbed " + testbed);

            // get a list of node last readings from testbed
            final List<NodeCapability> nodeCapabilities = nodeCapabilityManager.list(testbed.getSetup());

            // Prepare data to pass to jsp
            final Map<String, Object> refData = new HashMap<String, Object>();

            Map<Node, List<NodeCapability>> nodeCapabilityMap = new HashMap<Node, List<NodeCapability>>();

            for (final NodeCapability capability : nodeCapabilities) {
                if (nodeCapabilityMap.containsKey(capability.getNode())) {
                    nodeCapabilityMap.get(capability.getNode()).add(capability);
                } else {
                    nodeCapabilityMap.put(capability.getNode(), new ArrayList<NodeCapability>());
                    nodeCapabilityMap.get(capability.getNode()).add(capability);
                }
            }
            final StringBuilder output = new StringBuilder();
            for (final Node node : nodeCapabilityMap.keySet()) {
                boolean outdated = true;

                for (NodeCapability nodeCapability : nodeCapabilityMap.get(node)) {
                    outdated = outdated && isOutdated(nodeCapability);
                }

                if (outdated) {
                    final NodeCapability roomCap = nodeCapabilityManager.getByID(node, "room");
                    final NodeCapability nodeCap = nodeCapabilityManager.getByID(node, "nodetype");
                    final String room = roomCap == null ? "null" : roomCap.getLastNodeReading().getStringReading();
                    final String nodetype = nodeCap == null ? "null" : nodeCap.getLastNodeReading().getStringReading();

                    if (!node.getName().contains("virtual")) {
                        output.append(node.getName()).append("\t").
                                append(room == null ? "null" : room).append("\t")
                                .append(nodetype == null ? "null" : nodetype).append("\n");
                    }
                }
            }


            // write on the HTTP response
            response.setContentType("text/plain");
            final Writer textOutput = (response.getWriter());

            textOutput.append(output.toString());

            textOutput.flush();
            textOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isOutdated(NodeCapability capability) {
        if (capability.getLastNodeReading().getTimestamp() != null) {
            return (System.currentTimeMillis() - capability.getLastNodeReading().getTimestamp().getTime()) > 86400000;
        }
        return false;
    }
}
