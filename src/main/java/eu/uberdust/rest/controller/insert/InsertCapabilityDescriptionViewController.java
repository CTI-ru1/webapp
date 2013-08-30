package eu.uberdust.rest.controller.insert;

import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * Controller class for inserting description of a node.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/capability/{capabilityName}/insert/description/{description}/")
public final class InsertCapabilityDescriptionViewController extends UberdustSpringController{

    /**
     * Logger persistence manager.
     */
    private static final Logger LOGGER = Logger.getLogger(InsertCapabilityDescriptionViewController.class);

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;
    /**
     * Capability persistence manager.
     */
    private transient CapabilityController capabilityManager;

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
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws InvalidTestbedIdException an invalid testbed id exception.
     * @throws TestbedNotFoundException  testbed not found exception.
     * @throws java.io.IOException       IO exception.
     */
    @Loggable
    @RequestMapping("/reading/{readingDOUBLE}")
    public ResponseEntity<String> insertDoubleReading(@PathVariable("testbedId") int testbedId, @PathVariable("capabilityName") String capabilityName, @PathVariable("description") String description) throws TestbedNotFoundException, IOException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // look up capability
        Capability capability = capabilityManager.getByID(capabilityName);
        if (capability == null) {
            // if capability not found create one and store it
            capability = new Capability();
            capability.setName(capabilityName);
        }

        // update description
        capability.setDescription(description);
        capabilityManager.add(capability);

        // make response
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(
                "Desciption \"" + description + "\" inserted for Capability(" + capabilityName
                        + ")" + ") Testbed(" + testbedId + "). OK"
                , responseHeaders, HttpStatus.OK);
    }
}
