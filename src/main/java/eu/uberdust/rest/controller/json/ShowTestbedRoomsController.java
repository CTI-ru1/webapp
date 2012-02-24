package eu.uberdust.rest.controller.json;

import eu.uberdust.command.TestbedCommand;
import eu.uberdust.formatter.JsonFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.NodeCapabilityController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
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
public final class ShowTestbedRoomsController extends AbstractRestController {

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Last node reading persistence manager.
     */
    private transient NodeCapabilityController nodeCapabilityManager;

    /**
     * Last link reading persistence manager.
     */
    private transient CapabilityController capabilityManager;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ShowTestbedRoomsController.class);

    /**
     * Constructor.
     */
    public ShowTestbedRoomsController() {
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

    public void setNodeCapabilityManager(NodeCapabilityController nodeCapabilityManager) {
        this.nodeCapabilityManager = nodeCapabilityManager;
    }

    public void setCapabilityManager(CapabilityController capabilityManager) {
        this.capabilityManager = capabilityManager;
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
            throws InvalidTestbedIdException, TestbedNotFoundException {

        LOGGER.info("Remote address: " + request.getRemoteAddr());
        LOGGER.info("Remote host: " + request.getRemoteHost());
        try {
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

            final Capability capability = capabilityManager.getByID("room");


            // get a list of node last readings from testbed
            List<NodeCapability> nodeCapabilities = nodeCapabilityManager.list(testbed.getSetup(), capability);

            response.setContentType("text/json");
            final Writer textOutput = (response.getWriter());
            try {
                textOutput.append(JsonFormatter.getInstance().formatUniqueLastNodeReadings(nodeCapabilities));
            } catch (NotImplementedException e) {
                textOutput.append("not implemented exception");
            }
            textOutput.flush();
            textOutput.close();
        } catch (IOException e) {
            LOGGER.fatal(e);
        }
        return null;
    }
}
