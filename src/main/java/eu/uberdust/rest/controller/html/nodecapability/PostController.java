package eu.uberdust.rest.controller.html.nodecapability;

import ch.ethz.inf.vs.californium.coap.CodeRegistry;
import ch.ethz.inf.vs.californium.coap.Option;
import ch.ethz.inf.vs.californium.coap.OptionNumberRegistry;
import ch.ethz.inf.vs.californium.coap.Request;
import eu.uberdust.caching.Loggable;
import eu.uberdust.command.NodeCapabilityCommand;
import eu.uberdust.rest.exception.*;
import eu.uberdust.util.CommandDispatcher;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
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
import java.util.Random;

/**
 * Controller class that returns an HTML page containing a list of the readings for a node/capability.
 */
public final class PostController extends AbstractRestController {

    /**
     * Node peristence manager.
     */
    private transient NodeController nodeManager;

    /**
     * Capability persistence manager.
     */
    private transient CapabilityController capabilityManager;

    /**
     * Testbed peristence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(PostController.class);

    /**
     * Constructor.
     */
    public PostController() {
        super();

        // Make sure to set which method this controller will support.
        this.setSupportedMethods(new String[]{METHOD_POST});
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
     * @param req        http servlet req.
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
    @Loggable
    protected ModelAndView handle(final HttpServletRequest req, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws CapabilityNotFoundException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException, InvalidCapabilityNameException, InvalidNodeIdException, InvalidLimitException, IOException {


        // set commandNode object
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


        Request coapReq = new Request(CodeRegistry.METHOD_POST, false);
        coapReq.setMID((new Random()).nextInt() % 60000);
        if (node.getName().contains("0x")) {
            Option uriHost = new Option(OptionNumberRegistry.URI_HOST);
            uriHost.setStringValue(node.getName().split("0x")[1]);
            coapReq.addOption(uriHost);
        }
        final String capShortName = capability.getName().substring(capability.getName().lastIndexOf(":") + 1);
        coapReq.setURI(capShortName);
        coapReq.setPayload(command.getStringReading());

        StringBuilder payloadStringBuilder = new StringBuilder();
        for (Byte data : coapReq.toByteArray()) {
            int i = data;
            payloadStringBuilder.append(",");
            payloadStringBuilder.append(Integer.toHexString(i));
        }

        final String payloadString = "33," + payloadStringBuilder.toString().substring(1).replaceAll("ffffff", "");

        CommandDispatcher.getInstance().sendCommand(node.getSetup().getId(), node.getName(), payloadString);

        response.setContentType("text/plain");
        final Writer textOutput = (response.getWriter());
        textOutput.write("OK . Destination : " + node.getName() + "\nPayload : " + payloadString);
        coapReq.prettyPrint();

        return null;
    }
}
