package eu.uberdust.rest.controller.insert;

import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.NodeReadingController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.exception.UnknownTestbedException;
import eu.wisebed.wisedb.model.NodeReading;
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
import java.util.Date;

/**
 * Controller class for inserting readings for a node capability pair.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/node/{nodeName}/capability/{capabilityName}/insert/timestamp/{timestampLONG}")
public final class InsertNodeReadingsViewController extends UberdustSpringController {

    /**
     * Looger.
     */
    private static final Logger LOGGER = Logger.getLogger(InsertNodeReadingsViewController.class);

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws InvalidTestbedIdException invalid testbed id exception.
     * @throws TestbedNotFoundException  testbed not found exception.
     * @throws IOException               IO exception.
     */
    @Loggable
    @RequestMapping(value = "/reading/{readingSTR}", method = RequestMethod.GET)
    public ResponseEntity<String> handle(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, @PathVariable("capabilityName") String capabilityName, @PathVariable("timestampLONG") long timestampLONG, @PathVariable("readingSTR") String readingSTR)
            throws InvalidTestbedIdException, TestbedNotFoundException, IOException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // parse reading and timestamp
        final Double reading = new Double(readingSTR);
        final Date timestamp = new Date(timestampLONG);

        // insert reading
        NodeReading addedReading;
        try {
            addedReading = nodeReadingManager.insertReading(nodeName, capabilityName, reading, null, timestamp);
        } catch (UnknownTestbedException e) {
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].", e);
        }


        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(
                "Inserted for Node(" + nodeName + ") Capability(" + capabilityName + ") : " + reading + ". OK"
                , responseHeaders, HttpStatus.OK);

    }


    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                             invalid testbed id exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                             testbed not found exception.
     * @throws java.io.IOException IO exception.
     */
    @Loggable
    @RequestMapping(value = "/reading/{readingDOUBLE}/stringreading/{readingSTR}", method = RequestMethod.GET)
    public ResponseEntity<String> handles(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, @PathVariable("capabilityName") String capabilityName, @PathVariable("timestampLONG") long timestampLONG, @PathVariable("readingDOUBLE") double readingDOUBLE, @PathVariable("readingSTR") String readingSTR) throws InvalidTestbedIdException, TestbedNotFoundException, IOException {

        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // parse reading and timestamp
        final Date timestamp = new Date(timestampLONG);
        final String nodeId = nodeName;
        final String capabilityId = capabilityName;

        // insert reading
        try {
            nodeReadingManager.insertReading(nodeId, capabilityId, readingDOUBLE, readingSTR, timestamp);
        } catch (UnknownTestbedException e) {
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].", e);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(
                "Inserted for Node(" + nodeName + ") Capability(" + capabilityName + ") : [" + readingDOUBLE + "," + readingSTR + "]. OK"
                , responseHeaders, HttpStatus.OK);
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                             invalid testbed id exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                             testbed not found exception.
     * @throws java.io.IOException IO exception.
     */
    @Loggable
    @RequestMapping(value = "/stringreading/{readingSTR}", method = RequestMethod.GET)
    public ResponseEntity<String> handles(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, @PathVariable("capabilityName") String capabilityName, @PathVariable("timestampLONG") long timestampLONG, @PathVariable("readingSTR") String readingSTR) throws InvalidTestbedIdException, TestbedNotFoundException, IOException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // parse reading and timestamp
        final Date timestamp = new Date(timestampLONG);

        // insert reading
        try {
            nodeReadingManager.insertReading(nodeName, capabilityName, null, readingSTR, timestamp);
        } catch (UnknownTestbedException e) {
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].", e);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(
                "Inserted for Node(" + nodeName + ") Capability(" + capabilityName
                        + ") : " + readingSTR + ". OK"
                , responseHeaders, HttpStatus.OK);
    }
}
