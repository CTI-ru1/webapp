package eu.uberdust.rest.controller.tab;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.TextFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.exception.CapabilityNotFoundException;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.LastLinkReadingController;
import eu.wisebed.wisedb.controller.LastNodeReadingController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.LastLinkReading;
import eu.wisebed.wisedb.model.LastNodeReading;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;

/**
 * Controller class that returns readings of a specific capability in a tab delimited format.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/capability")
public final class CapabilityTabDelimitedController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(CapabilityTabDelimitedController.class);

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Capability persistence manager.
     */
    private transient CapabilityController capabilityManager;

    /**
     * Last node reading persistence manager.
     */
    private transient LastNodeReadingController lastNodeReadingManager;

    /**
     * Last link reading persistence manager.
     */
    private transient LastLinkReadingController lastLinkReadingManager;

    /**
     * Sets testbed persistence manager.
     *
     * @param testbedManager testbed persistence manager.
     */
    @Autowired
    public void setTestbedManager(final TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }

    /**
     * Sets capability persistence manager.
     *
     * @param capabilityManager capability persistence manager.
     */
    @Autowired
    public void setCapabilityManager(final CapabilityController capabilityManager) {
        this.capabilityManager = capabilityManager;
    }

    /**
     * Sets last node reading persistence manager.
     *
     * @param lastNodeReadingManager last node reading persistence manager.
     */
    @Autowired
    public void setLastNodeReadingManager(final LastNodeReadingController lastNodeReadingManager) {
        this.lastNodeReadingManager = lastNodeReadingManager;
    }

    /**
     * Sets last link reading persistence manager.
     *
     * @param lastLinkReadingManager last link reading persistence manager.
     */
    @Autowired
    public void setLastLinkReadingManager(final LastLinkReadingController lastLinkReadingManager) {
        this.lastLinkReadingManager = lastLinkReadingManager;
    }

    /**
     * Handle Request and return the appropriate response.
     * System.out.println(request.getRemoteUser());
     *
     * @return response http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                     an InvalidTestbedIdException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                     an TestbedNotFoundException exception.
     * @throws IOException IO exception.
     */
    @Loggable
    @RequestMapping(value = {"/raw", "/tabdelimited"}, method = RequestMethod.GET)
    public ResponseEntity<String> listCapabilities(@PathVariable("testbedId") int testbedId)
            throws InvalidTestbedIdException, TestbedNotFoundException, IOException, CapabilityNotFoundException, NotImplementedException {

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // get testbed's capabilities
        final List<Capability> capabilities = capabilityManager.list(testbed.getSetup());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(TextFormatter.getInstance().formatCapabilities(testbed, capabilities), responseHeaders, HttpStatus.OK);

    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws InvalidTestbedIdException   a InvalidTestbedIdException exception.
     * @throws TestbedNotFoundException    a TestbedNotFoundException exception.
     * @throws IOException                 an IOException exception.
     * @throws CapabilityNotFoundException a CapabilityNotFoundException exception.
     */
    @Loggable
    @RequestMapping(value = {"/{capabilityName}/tabdelimited","/{capabilityName}/raw"}, method = RequestMethod.GET)
    public ResponseEntity<String> handle(@PathVariable("testbedId") int testbedId, @PathVariable("capabilityName") String capabilityName)
            throws InvalidTestbedIdException, TestbedNotFoundException, IOException, CapabilityNotFoundException, NotImplementedException {

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // look up capability
        final Capability capability = capabilityManager.getByID(capabilityName);
        if (capability == null) {
            // if no capability is found throw exception
            throw new CapabilityNotFoundException("Cannot find capability [" + capabilityName + "].");
        }

        final List<LastNodeReading> lnrs = lastNodeReadingManager.getByCapability(testbed.getSetup(), capability);
        final List<LastLinkReading> llrs = lastLinkReadingManager.getByCapability(testbed.getSetup(), capability);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(TextFormatter.getInstance().formatLastReadings(lnrs, llrs), responseHeaders, HttpStatus.OK);
    }
}
