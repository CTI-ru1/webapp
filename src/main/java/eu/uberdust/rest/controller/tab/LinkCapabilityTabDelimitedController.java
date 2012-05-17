package eu.uberdust.rest.controller.tab;

import eu.uberdust.command.LinkCapabilityCommand;
import eu.uberdust.formatter.HtmlFormatter;
import eu.uberdust.formatter.TextFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.exception.CapabilityNotFoundException;
import eu.uberdust.rest.exception.InvalidCapabilityNameException;
import eu.uberdust.rest.exception.InvalidLimitException;
import eu.uberdust.rest.exception.InvalidNodeIdException;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.NodeNotFoundException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.LinkController;
import eu.wisebed.wisedb.controller.LinkReadingController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.Link;
import eu.wisebed.wisedb.model.LinkReading;
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
 * Controller class that returns an HTML page containing a list of the readings for a node/capability.
 */
public final class LinkCapabilityTabDelimitedController extends AbstractRestController {

    /**
     * Node peristence manager.
     */
    private transient LinkController linkManager;

    /**
     * Capability persistence manager.
     */
    private transient CapabilityController capabilityManager;

    /**
     * NodeReading persistence manager.
     */
    private transient LinkReadingController linkReadingManager;

    /**
     * Testbed peristence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LinkCapabilityTabDelimitedController.class);

    /**
     * Constructor.
     */
    public LinkCapabilityTabDelimitedController() {
        super();

        // Make sure to set which method this controller will support.
        this.setSupportedMethods(new String[]{METHOD_GET});
    }

    /**
     * Sets node persistence manager.
     *
     * @param linkManager node persistence manager.
     */
    public void setLinkManager(final LinkController linkManager) {
        this.linkManager = linkManager;
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
     * Sets NodeReading persistence manager.
     *
     * @param linkReadingManager NodeReading persistence manager.
     */
    public void setLinkReadingManager(final LinkReadingController linkReadingManager) {
        this.linkReadingManager = linkReadingManager;
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
    protected ModelAndView handle(final HttpServletRequest req, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws CapabilityNotFoundException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException, InvalidCapabilityNameException, InvalidNodeIdException, InvalidLimitException {

        LOGGER.info("showNodeCapabilityController(...)");

        HtmlFormatter.getInstance().setBaseUrl(req.getRequestURL().substring(0, req.getRequestURL().indexOf("/rest")));

        final long start = System.currentTimeMillis();

        // set commandNode object
        final LinkCapabilityCommand command = (LinkCapabilityCommand) commandObj;

        // check link id
        if (command.getSourceId() == null || command.getSourceId().isEmpty()) {
            throw new InvalidNodeIdException("Must provide link id");
        }
        if (command.getTargetId() == null || command.getTargetId().isEmpty()) {
            throw new InvalidNodeIdException("Must provide link id");
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

        // retrieve link
        final Link link = linkManager.getByID(command.getSourceId(), command.getTargetId());
        if (link == null) {
            throw new NodeNotFoundException("Cannot find link [" + command.getSourceId() + "-" + command.getTargetId() + "]");
        }

        // retrieve capability
        final Capability capability = capabilityManager.getByID(command.getCapabilityId());
        if (capability == null) {
            throw new CapabilityNotFoundException("Cannot find capability [" + command.getCapabilityId() + "]");
        }

        // retrieve readings based on link/capability
        final List<LinkReading> linkReadings;
        if (command.getReadingsLimit() == null) {
            // no limit is provided
            linkReadings = linkReadingManager.list(link, capability, 0);
        } else {
            int limit;
            try {
                limit = Integer.parseInt(command.getReadingsLimit());
            } catch (NumberFormatException nfe) {
                throw new InvalidLimitException("Limit must have have number format.", nfe);
            }
            linkReadings = linkReadingManager.list(link, capability, limit);
        }
        try {
            // write on the HTTP response
            response.setContentType("text/plain");
            final Writer textOutput;

            textOutput = (response.getWriter());

            try {
                textOutput.append(TextFormatter.getInstance().formatLinkReadings(linkReadings));
            } catch (NotImplementedException e) {
                textOutput.append("not implemented exception");
            }

            textOutput.flush();
            textOutput.close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}
