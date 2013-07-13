package eu.uberdust.rest.controller.insert;

import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.LinkReadingController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.exception.UnknownTestbedException;
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
import java.util.Date;

/**
 * Controller class for inserting readings for a link capability pair.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/link/{sourceName}/{targetName}/capability/{capabilityName}/insert/timestamp/{timestampLONG}")
public final class InsertLinkReadingsViewController {

    /**
     * LinkReading persistence manager.
     */
    private transient LinkReadingController linkReadingManager;

    /**
     * Looger.
     */
    private static final Logger LOGGER = Logger.getLogger(InsertLinkReadingsViewController.class);

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

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
     * Sets link persistence manager.
     *
     * @param linkReadingManager LinkReading persistence manager.
     */
    @Autowired
    public void setLinkReadingManager(final LinkReadingController linkReadingManager) {
        this.linkReadingManager = linkReadingManager;
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws InvalidTestbedIdException invalid testbed id exception.
     * @throws TestbedNotFoundException  testbed not found exception.
     * @throws IOException               IO exception.
     */
    @Loggable
    @RequestMapping("/reading/{readingDOUBLE}")
    public ResponseEntity<String> insertDoubleReading(@PathVariable("testbedId") int testbedId, @PathVariable("sourceName") String sourceName, @PathVariable("targetName") String targetName, @PathVariable("capabilityName") String capabilityName, @PathVariable("timestampLONG") long timestampLONG, @PathVariable("readingSTR") String readingSTR)
            throws InvalidTestbedIdException, TestbedNotFoundException, IOException {

        // parse reading and timestamp
        final Date timestamp = new Date(timestampLONG);
        final Double reading = new Double(readingSTR);

        // insert reading
        try {
            linkReadingManager.insertReading(sourceName, targetName, capabilityName, reading, null, timestamp);
        } catch (UnknownTestbedException e) {
            throw new TestbedNotFoundException(
                    "Cannot find testbed to assosiate with nodes: " + sourceName + "," + targetName + "].", e);
        }


        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(
                "Inserted for Link [" + sourceName + "," + targetName
                        + "] Capability(" + capabilityName
                        + ") : " + reading + ". OK"
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
    public ResponseEntity<String> handles(@PathVariable("testbedId") int testbedId, @PathVariable("sourceName") String sourceName, @PathVariable("targetName") String targetName, @PathVariable("capabilityName") String capabilityName, @PathVariable("timestampLONG") long timestampLONG, @PathVariable("readingDOUBLE") double readingDOUBLE, @PathVariable("readingSTR") String readingSTR) throws InvalidTestbedIdException, TestbedNotFoundException, IOException {

        // parse reading and timestamp
        final Date timestamp = new Date(timestampLONG);

        // insert reading
        try {
            linkReadingManager.insertReading(sourceName, targetName, capabilityName, readingDOUBLE, readingSTR,
                    timestamp);
        } catch (UnknownTestbedException e) {
            throw new TestbedNotFoundException(
                    "Cannot find testbed to assosiate with nodes: " + sourceName + "," + targetName + "].", e);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(
                "Inserted for Link [" + sourceName + "," + targetName
                        + "] Capability(" + capabilityName
                        + ") : [" + readingDOUBLE + "," + readingSTR + "]. OK"
                , responseHeaders, HttpStatus.OK);
    }


    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws InvalidTestbedIdException invalid testbed id exception.
     * @throws TestbedNotFoundException  testbed not found exception.
     * @throws java.io.IOException       IO exception.
     */
    @Loggable
    @RequestMapping(value = "/stringreading/{readingSTR}", method = RequestMethod.GET)
    public ResponseEntity<String> handles(@PathVariable("testbedId") int testbedId, @PathVariable("sourceName") String sourceName, @PathVariable("targetName") String targetName, @PathVariable("capabilityName") String capabilityName, @PathVariable("timestampLONG") long timestampLONG, @PathVariable("readingSTR") String readingSTR) throws InvalidTestbedIdException, TestbedNotFoundException, IOException {

        // parse reading and timestamp
        final Date timestamp = new Date(timestampLONG);

        // insert reading
        try {
            linkReadingManager.insertReading(sourceName, targetName, capabilityName, null, readingSTR, timestamp);
        } catch (UnknownTestbedException e) {
            throw new TestbedNotFoundException(
                    "Cannot find testbed to assosiate with nodes: " + sourceName + "," + targetName + "].", e);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(
                "Inserted for Link [" + sourceName + "," + targetName
                        + "] Capability(" + capabilityName
                        + ") : " + readingSTR + ". OK"
                , responseHeaders, HttpStatus.OK);
    }

}
