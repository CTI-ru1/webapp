package eu.uberdust.rest.controller.tab;


import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.TextFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.*;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.LinkController;
import eu.wisebed.wisedb.controller.LinkReadingController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.Link;
import eu.wisebed.wisedb.model.LinkReading;
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
 * Controller class that returns a list of links for a given testbed in Raw Text format.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/link")
public final class LinkTabDelimitedViewController extends UberdustSpringController{


    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LinkTabDelimitedViewController.class);

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                     an InvalidTestbedIdException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                     an TestbedNotFoundException exception.
     * @throws IOException IO Exception.
     */
    @Loggable
    @RequestMapping(value = "/raw", method = RequestMethod.GET)
    public ResponseEntity<String> showReadings(@PathVariable("testbedId") int testbedId) throws InvalidTestbedIdException, TestbedNotFoundException, IOException, NotImplementedException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }
        final List<Link> links = linkManager.list(testbed.getSetup());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");

        return new ResponseEntity<String>(TextFormatter.getInstance().formatLinks(links), responseHeaders, HttpStatus.OK);

    }


    /**
     * Handle Request and return the appropriate response.
     *
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
    @Loggable
    @RequestMapping(value = "/{sourceName}/{targetName}/capability/{capabilityName}/tabdelimited/limit/{limit}", method = RequestMethod.GET)
    public ResponseEntity<String> showReadings(@PathVariable("testbedId") int testbedId, @PathVariable("sourceName") String sourceName, @PathVariable("targetName") String targetName, @PathVariable("capabilityName") String capabilityName, @PathVariable("limit") int limit)
            throws CapabilityNotFoundException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException, InvalidCapabilityNameException, InvalidNodeIdException, InvalidLimitException, NotImplementedException {

        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // retrieve link
        final Link link = linkManager.getByID(sourceName, targetName);
        if (link == null) {
            throw new NodeNotFoundException("Cannot find link [" + sourceName + "-" + targetName + "]");
        }

        // retrieve capability
        final Capability capability = capabilityManager.getByID(capabilityName);
        if (capability == null) {
            throw new CapabilityNotFoundException("Cannot find capability [" + capabilityName + "]");
        }

        // retrieve readings based on link/capability
        final List<LinkReading> linkReadings;

        linkReadings = linkReadingManager.list(link, capability, limit);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(TextFormatter.getInstance().formatLinkReadings(linkReadings), responseHeaders, HttpStatus.OK);

    }
}
