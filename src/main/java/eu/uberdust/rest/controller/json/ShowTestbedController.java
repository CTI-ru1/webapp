package eu.uberdust.rest.controller.json;

import eu.uberdust.caching.Loggable;
import eu.uberdust.command.TestbedCommand;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.LinkController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractRestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Controller class that returns the a web page for a testbed.
 */
public final class ShowTestbedController extends AbstractRestController {

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Capability persistence manager.
     */
    private transient CapabilityController capabilityManager;

    /**
     * Link persistence manager.
     */
    private transient LinkController linkManager;

    /**
     * Node persistence manager.
     */
    private transient NodeController nodeManager;
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ShowTestbedController.class);

    /**
     * Constructor.
     */
    public ShowTestbedController() {
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
     * Sets capability persistence manager.
     *
     * @param capabilityManager capability persistence manager.
     */
    public void setCapabilityManager(final CapabilityController capabilityManager) {
        this.capabilityManager = capabilityManager;
    }

    /**
     * Sets link persistence manager.
     *
     * @param linkManager link persistence manager.
     */
    public void setLinkManager(final LinkController linkManager) {
        this.linkManager = linkManager;
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
     * Handle req and return the appropriate response.
     *
     * @param req        http servlet req.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     a BindException exception.
     * @return http servlet response
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *          a TestbedNotFoundException exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *          a InvalidTestbedException exception.
     */
    @Loggable
    protected ModelAndView handle(final HttpServletRequest req, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws TestbedNotFoundException, InvalidTestbedIdException, JSONException, IOException {
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

        // look up testbed
        final Testbed testbed = testbedManager.getByID(Integer.parseInt(command.getTestbedId()));
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        JSONObject jobj = new JSONObject();
        jobj.put("testbedID", testbed.getId());
        jobj.put("testbedName", testbed.getName());
        jobj.put("urnPrefix", testbed.getUrnPrefix());
        jobj.put("urnCapabilityPrefix", testbed.getUrnCapabilityPrefix());
        response.setContentType("text/json");
        final Writer textOutput = (response.getWriter());
        textOutput.append(jobj.toString());
        textOutput.flush();
        textOutput.close();
        return null;

    }


}
