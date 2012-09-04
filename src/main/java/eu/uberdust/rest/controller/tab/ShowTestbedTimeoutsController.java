package eu.uberdust.rest.controller.tab;

import eu.uberdust.caching.Loggable;
import eu.uberdust.command.TestbedCommand;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.NodeCapabilityController;
import eu.wisebed.wisedb.controller.NodeController;
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
import java.util.List;

/**
 * Controller class that returns the status page for the nodes and links of a testbed.
 */
public final class ShowTestbedTimeoutsController extends AbstractRestController {

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Node persistence manager.
     */
    private transient NodeController nodeManager;

    /**
     * Last node reading persistence manager.
     */
    private transient NodeCapabilityController nodeCapabilityManager;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ShowTestbedTimeoutsController.class);

    /**
     * Constructor.
     */
    public ShowTestbedTimeoutsController() {
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
     * @param nodeManager nodepersistence manager.
     */
    public void setNodeManager(final NodeController nodeManager) {
        this.nodeManager = nodeManager;
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
    @Loggable
    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws InvalidTestbedIdException, TestbedNotFoundException, IOException {

        try {

            LOGGER.info("showTestbedAdminStatusController(...)");

            TestbedCommand command = (TestbedCommand) commandObj;

            Testbed testbed = testbedManager.getByID(Integer.parseInt(command.getTestbedId()));
            List<Node> nodes = nodeManager.list(testbed.getSetup());

            int counter = 0;

            StringBuilder responseSB = new StringBuilder("Running nodes :\n");
            String type = "";
            for (Node node : nodes) {
                if (node.getName().contains("virtual")) continue;
                List<NodeCapability> ncaps = nodeCapabilityManager.list(node);
                long max = -1;
                for (NodeCapability ncap : ncaps) {
                    if (ncap.getCapability().getName().contains("nodetype")) {
                        type = ncap.getLastNodeReading().getStringReading();
                    }
                    long timediff = System.currentTimeMillis() - ncap.getLastNodeReading().getTimestamp().getTime();
                    if (max == -1) {
                        max = timediff;
                    } else if (timediff < max) {
                        max = timediff;
                    }
                }
                long secs = max / 1000;
                long min = secs / 60;
                long hours = min / 60;
                long days = hours / 24;
                if (secs < 60) {
                    responseSB.append(node + "," + type + " @ " + min + " mins ago\n");
                } else if (min < 10) {
                    responseSB.append(node + "," + type + " @ " + min + " mins ago\n");
                } else if (min < 60) {
                    responseSB.insert(0, node + "," + type + " @ " + min + " mins ago\n");
                } else if (hours < 24) {
                    counter++;
                    responseSB.insert(0, node + "," + type + " @ " + hours + " hours ago\n");
                } else {
                    counter++;
                    responseSB.insert(0, node + "," + type + " @ " + days + " days ago\n");
                }
            }
            responseSB.insert(0, "Late Nodes :\n");
            responseSB.insert(0, "Total Nodes: " + nodes.size());


            // write on the HTTP response
            response.setContentType("text/plain");
            final Writer textOutput = (response.getWriter());

            textOutput.append(responseSB.toString());

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
