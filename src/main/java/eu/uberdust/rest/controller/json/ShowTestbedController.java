package eu.uberdust.rest.controller.json;

import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
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

/**
 * Controller class that returns the a web page for a testbed.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/json")
public final class ShowTestbedController extends UberdustSpringController{

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ShowTestbedController.class);

    /**
     * Handle req and return the appropriate response.
     *
     * @return http servlet response
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *          a TestbedNotFoundException exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *          a InvalidTestbedException exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> showReadings(@PathVariable("testbedId") int testbedId)
            throws TestbedNotFoundException, InvalidTestbedIdException, JSONException, IOException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        JSONObject jobj = new JSONObject();
        jobj.put("testbedID", testbed.getId());
        jobj.put("testbedName", testbed.getName());
        jobj.put("urnPrefix", testbed.getUrnPrefix());
        jobj.put("urnCapabilityPrefix", testbed.getUrnCapabilityPrefix());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(jobj.toString(), responseHeaders, HttpStatus.OK);
    }


}
