package eu.uberdust.rest.controller.json;

import eu.uberdust.command.NodeCommand;
import eu.uberdust.formatter.JsonFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.exception.*;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
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
import java.util.List;

/**
 * Controller class that returns an HTML page containing a list of the readings for a node/capability.
 */
public final class ListNodeCapabilitiesController extends AbstractRestController {

    /**
     * Node peristence manager.
     */
    private transient NodeController nodeManager;

    /**
     * Testbed peristence manager.
     */
    private transient TestbedController testbedManager;

    private transient eu.wisebed.wisedb.controller.NodeCapabilityController capabilityManager;

    public void setNodeCapabilityManager(final eu.wisebed.wisedb.controller.NodeCapabilityController capabilityManager) {
        this.capabilityManager = capabilityManager;
    }

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ListNodeCapabilitiesController.class);

    /**
     * Constructor.
     */
    public ListNodeCapabilitiesController() {
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
     * Sets Testbed persistence manager.
     *
     * @param testbedManager Testbed persistence manager.
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
     * @throws eu.uberdust.rest.exception.InvalidNodeIdException
     *          invalid node id exception.
     * @throws eu.uberdust.rest.exception.InvalidCapabilityNameException
     *          invalid capability name exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *          invalid testbed id exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *          testbed not found exception.
     * @throws eu.uberdust.rest.exception.NodeNotFoundException
     *          node not found exception.
     * @throws eu.uberdust.rest.exception.CapabilityNotFoundException
     *          capability not found exception.
     * @throws eu.uberdust.rest.exception.InvalidLimitException
     *          invalid limit exception.
     */
    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws CapabilityNotFoundException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException, InvalidCapabilityNameException, InvalidNodeIdException, InvalidLimitException {

        LOGGER.info("listNodeCapabilitiesController(...)");
        try {
            // set commandNode object
            final NodeCommand command = (NodeCommand) commandObj;

            // check node id
            if (command.getNodeId() == null || command.getNodeId().isEmpty()) {
                throw new InvalidNodeIdException("Must provide node id");
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
            final List<Capability> capabilities = new ArrayList<Capability>();
            for (final NodeCapability capability : capabilityManager.list(node)) {
                capabilities.add(capability.getCapability());
            }

            response.setContentType("text/json");
            final Writer output;
            try {
                output = (response.getWriter());
                output.append(JsonFormatter.getInstance().formatCapabilities(testbed, capabilities));
                output.flush();
                output.close();
            } catch (final NotImplementedException e) {
                LOGGER.error(e);
            } catch (final IOException e) {
                LOGGER.fatal(e);
            }
            // check type of view requested
            return null;
        } catch (final Exception e) {
            LOGGER.error(e);
        }
        return null;
    }

}
