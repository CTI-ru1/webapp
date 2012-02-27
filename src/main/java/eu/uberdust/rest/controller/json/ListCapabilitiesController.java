package eu.uberdust.rest.controller.json;

import eu.uberdust.command.CapabilityCommand;
import eu.uberdust.formatter.JsonFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
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
 * Controller class that returns a list of capabilities for a given testbed in JSON format.
 */
public final class ListCapabilitiesController extends AbstractRestController {

    /**
     * {@link Testbed} persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * {@link Capability} persistence manager.
     */
    private transient CapabilityController capabilityManager;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ListCapabilitiesController.class);

    /**
     * Constructor.
     */
    public ListCapabilitiesController() {
        super();

        // Make sure to set which method this controller will support.
        this.setSupportedMethods(new String[]{METHOD_GET});
    }

    /**
     * Sets testbed persistence manager.
     *
     * @param testbedManager testbed peristence manager.
     */
    public void setTestbedManager(final TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }

    /**
     * Sets capability peristence manager.
     *
     * @param capabilityManager capability persistence manager.
     */
    public void setCapabilityManager(final CapabilityController capabilityManager) {
        this.capabilityManager = capabilityManager;
    }

    /**
     * Handle Request and return the appropriate response.
     * System.out.println(request.getRemoteUser());
     *
     * @param request    http servlet request.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     BindException exception.
     * @return response http servlet response.
     * @throws InvalidTestbedIdException an {@link InvalidTestbedIdException} exception.
     * @throws TestbedNotFoundException  an {@link TestbedNotFoundException} exception.
     * @throws IOException               IO exception.
     */
    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws InvalidTestbedIdException, TestbedNotFoundException, IOException {
        LOGGER.info("listCapabilitiesController(...)");

        // get command
        final CapabilityCommand command = (CapabilityCommand) commandObj;

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

        // get Testbed capabilities
        final List<Capability> capabilities = capabilityManager.list(testbed.getSetup());

        // write on the HTTP response
        response.setContentType("text/json");
        final Writer textOutput = (response.getWriter());
        try {
            textOutput.append(JsonFormatter.getInstance().formatCapabilities(capabilities));
        } catch (NotImplementedException e) {
            textOutput.append("not implemented exception");
        }
        textOutput.flush();
        textOutput.close();

        return null;
    }
}
