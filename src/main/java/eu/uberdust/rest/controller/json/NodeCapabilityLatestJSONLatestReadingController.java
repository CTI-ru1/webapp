package eu.uberdust.rest.controller.json;

import com.google.gson.Gson;
import eu.uberdust.command.NodeCapabilityCommand;
import eu.uberdust.rest.exception.CapabilityNotFoundException;
import eu.uberdust.rest.exception.InvalidCapabilityNameException;
import eu.uberdust.rest.exception.InvalidNodeIdException;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.NodeNotFoundException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.uberdust.util.NodeReadingJson;
import eu.uberdust.util.ReadingJson;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.NodeCapabilityController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.LastNodeReading;
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class NodeCapabilityLatestJSONLatestReadingController extends AbstractRestController {

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Node persistence manager.
     */
    private transient NodeController nodeManager;

    /**
     * Capability persistence manager.
     */
    private transient CapabilityController capabilityManager;

    /**
     * LastNodeReading persistence manager.
     */
    private transient NodeCapabilityController nodeCapabilityManager;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NodeCapabilityLatestJSONLatestReadingController.class);

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
     * Sets capability persistence manager.
     *
     * @param capabilityManager capability persistence manager.
     */
    public void setCapabilityManager(final CapabilityController capabilityManager) {
        this.capabilityManager = capabilityManager;
    }

    public void setNodeCapabilityManager(NodeCapabilityController nodeCapabilityManager) {
        this.nodeCapabilityManager = nodeCapabilityManager;
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @param request    http servlet request.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     BindException exception.
     * @return http servlet response.
     * @throws InvalidTestbedIdException      invalid testbed id exception.
     * @throws TestbedNotFoundException       testbed not found exception.
     * @throws NodeNotFoundException          node not found exception.
     * @throws CapabilityNotFoundException    capability not found exception.
     * @throws InvalidCapabilityNameException invalid capability name exception.
     * @throws InvalidNodeIdException         invalid node id exception.
     * @throws IOException                    IO exception.
     */
    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws InvalidTestbedIdException, TestbedNotFoundException, NodeNotFoundException,
            CapabilityNotFoundException, InvalidCapabilityNameException, InvalidNodeIdException, IOException {

        LOGGER.info("Remote address: " + request.getRemoteAddr());
        LOGGER.info("Remote host: " + request.getRemoteHost());

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
            throw new InvalidTestbedIdException("Testbed IDs have number format.");
        }

        // look up testbed
        final Testbed testbed = testbedManager.getByID(Integer.parseInt(command.getTestbedId()));
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // retrieve node
        final Node node = nodeManager.getByID(command.getNodeId());
        if (node == null) {
            throw new NodeNotFoundException("Cannot find node [" + command.getNodeId() + "]");
        }

        // retrieve capability
        final Capability capability = capabilityManager.getByID(command.getCapabilityId());
        if (capability == null) {
            throw new CapabilityNotFoundException("Cannot find capability [" + command.getCapabilityId() + "]");
        }

        // retrieve last node reading for this node/capability
        final LastNodeReading lnr = nodeCapabilityManager.getByID(node, capability).getLastNodeReading();

        // convert results to JSON format
        final ReadingJson readingJson = new ReadingJson(lnr.getTimestamp().getTime(), lnr.getReading());
        final List<ReadingJson> readingJsons = new ArrayList<ReadingJson>();
        readingJsons.add(readingJson);
        final NodeReadingJson nodeReadingInJson = new NodeReadingJson(lnr.getNodeCapability().getNode().getName(),
                lnr.getNodeCapability().getCapability().getName(), readingJsons);

        // write on the HTTP response
        response.setContentType("text/json");
        final Writer jsonOutput = (response.getWriter());

        // init GSON
        final Gson gson = new Gson();
        gson.toJson(nodeReadingInJson, nodeReadingInJson.getClass(), jsonOutput);

        jsonOutput.flush();
        jsonOutput.close();

        return null;
    }
}
