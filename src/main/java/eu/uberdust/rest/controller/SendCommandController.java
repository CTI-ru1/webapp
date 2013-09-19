package eu.uberdust.rest.controller;

import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.exception.NodeNotFoundException;
import eu.uberdust.util.CommandDispatcher;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.model.Node;
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

/**
 * Controller class for sending a message with certain payload to a destination.
 */
@Controller
@RequestMapping("/sendCommand/destination/{destinationName}/payload/{payloadSTR}")
public final class SendCommandController extends UberdustSpringController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(SendCommandController.class);

    /**
     * Handle Request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws NodeNotFoundException NodeNotFoundException exception.
     * @throws IOException           IOException exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getCommand(@PathVariable("destinationName") String destinationName, @PathVariable("payloadSTR") String payloadSTR)
            throws NodeNotFoundException, IOException {
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look for destination node
        final Node destinationNode = nodeManager.getByName(destinationName);
        if (destinationNode == null) {
            throw new NodeNotFoundException("Destination Node [" + destinationName + "] is not stored.");
        }


        CommandDispatcher.getInstance().sendCommand(destinationNode.getSetup().getId(), destinationName, payloadSTR);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(
                "OK . Destination : " + destinationName + "\nPayload : " + payloadSTR
                , responseHeaders, HttpStatus.OK);
    }
}
