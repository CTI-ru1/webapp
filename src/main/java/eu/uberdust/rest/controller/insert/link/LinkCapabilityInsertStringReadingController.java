package eu.uberdust.rest.controller.insert.link;


import eu.uberdust.command.LinkCapabilityInsertReadingCommand;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.LinkReadingController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.exception.UnknownTestbedException;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractRestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

public final class LinkCapabilityInsertStringReadingController extends AbstractRestController {

    /**
     * LinkReading persistence manager.
     */
    private transient LinkReadingController linkReadingManager;

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Looger.
     */
    private static final Logger LOGGER = Logger.getLogger(LinkCapabilityInsertStringReadingController.class);

    /**
     * Sets testbed persistence manager.
     *
     * @param testbedManager testbed persistence manager.
     */
    public void setTestbedManager(final TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }

    /**
     * Sets link persistence manager.
     *
     * @param linkReadingManager LinkReading persistence manager.
     */
    public void setLinkReadingManager(final LinkReadingController linkReadingManager) {
        this.linkReadingManager = linkReadingManager;
    }

    /**
     * Constructor.
     */
    public LinkCapabilityInsertStringReadingController() {
        super();

        // Make sure to set which method this controller will support.
        this.setSupportedMethods(new String[]{METHOD_GET});
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @param request    http servlet request.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     BindException exception.
     * @return response http servlet response.
     * @throws InvalidTestbedIdException invalid testbed id exception.
     * @throws TestbedNotFoundException  testbed not found exception.
     * @throws java.io.IOException       IO exception.
     */
    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws InvalidTestbedIdException, TestbedNotFoundException, IOException {

        LOGGER.info("Remote address: " + request.getRemoteAddr());
        LOGGER.info("Remote host: " + request.getRemoteHost());

        // set command object object
        final LinkCapabilityInsertReadingCommand command = (LinkCapabilityInsertReadingCommand) commandObj;

        // parse reading and timestamp
        final Date timestamp = new Date(Long.parseLong(command.getTimestamp()));
        final String reading = command.getStringReading();
        final String sourceId = command.getSourceId();
        final String targetId = command.getTargetId();
        final String capabilityId = command.getCapabilityId();

        // insert reading
        try {
            linkReadingManager.insertReading(sourceId, targetId, capabilityId, null, reading, timestamp);
        } catch (UnknownTestbedException e) {
            throw new TestbedNotFoundException(
                    "Cannot find testbed to assosiate with nodes: " + sourceId + "," + targetId + "].", e);
        }

        response.setContentType("text/plain");
        final Writer textOutput = (response.getWriter());
        textOutput.write("Inserted for Link [" + command.getSourceId() + "," + command.getTargetId()
                + "] Capability(" + command.getCapabilityId()
                + ") : " + reading + ". OK");
        textOutput.flush();
        textOutput.close();

//        LOGGER.info("MEMSTAT_1: " + Runtime.getRuntime().totalMemory() + ":" + Runtime.getRuntime().freeMemory()
// + " -- " + Runtime.getRuntime().freeMemory() * 100 / Runtime.getRuntime().totalMemory() + "% free mem");
//        Runtime.getRuntime().gc();
//        LOGGER.info("MEMSTAT_2: " + Runtime.getRuntime().totalMemory() + ":" + Runtime.getRuntime().freeMemory()
// + " -- " + Runtime.getRuntime().freeMemory() * 100 / Runtime.getRuntime().totalMemory() + "% free mem");

        return null;
    }
}
