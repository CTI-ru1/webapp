package eu.uberdust.rest.controller.json;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.JsonFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
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
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;

/**
 * Controller class that returns a list of capabilities for a given testbed in JSON format.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/capability/json")
public final class ListCapabilitiesController extends UberdustSpringController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ListCapabilitiesController.class);

    /**
     * Handle Request and return the appropriate response.
     * System.out.println(request.getRemoteUser());
     *
     * @return response http servlet response.
     * @throws InvalidTestbedIdException an {@link InvalidTestbedIdException} exception.
     * @throws TestbedNotFoundException  an {@link TestbedNotFoundException} exception.
     * @throws IOException               IO exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> handle(@PathVariable("testbedId") int testbedId)
            throws InvalidTestbedIdException, TestbedNotFoundException, IOException, NotImplementedException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());


        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // get Testbed capabilities
        final List<Capability> capabilities = capabilityManager.list(testbed.getSetup());

        return jsonResponse(JsonFormatter.getInstance().formatCapabilities(testbed, capabilities));
    }

}
