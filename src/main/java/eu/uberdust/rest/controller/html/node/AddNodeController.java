package eu.uberdust.rest.controller.html.node;

import eu.uberdust.caching.Loggable;
import eu.uberdust.command.NodeCommand;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.NodeNotFoundException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
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

/**
 * Controller class that returns the a web page for a node.
 */
public final class AddNodeController extends AbstractRestController {
    /**
     * Node persistence manager.
     */
    private transient NodeController nodeManager;

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(AddNodeController.class);

    /**
     * Constructor.
     */
    public AddNodeController() {
        super();

        // Make sure to set which method this controller will support.
        this.setSupportedMethods(new String[]{METHOD_PUT});
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
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *          InvalidTestbedIdException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *          TestbedNotFoundException exception.
     * @throws eu.uberdust.rest.exception.NodeNotFoundException
     *          NodeNotFoundException exception.
     */
    @Loggable
    protected ModelAndView handle(final HttpServletRequest req, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws InvalidTestbedIdException, TestbedNotFoundException, NodeNotFoundException, IOException {
        final long start = System.currentTimeMillis();

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

        final Node newNode = new Node();
        newNode.setName(command.getNodeId());
        newNode.setSetup(testbed.getSetup());

        nodeManager.add(newNode);

        // write on the HTTP response
        response.setContentType("text/plain");
        final Writer textOutput = (response.getWriter());

        textOutput.append("node added to the system");

        // flush close output
        textOutput.flush();
        textOutput.close();

        return null;
    }

}
