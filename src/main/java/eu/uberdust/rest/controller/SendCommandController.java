package eu.uberdust.rest.controller;

import eu.uberdust.caching.Loggable;
import eu.uberdust.command.DestinationPayloadCommand;
import eu.uberdust.rest.exception.NodeNotFoundException;
import eu.uberdust.util.CommandDispatcher;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.model.Node;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Controller class for sending a message with certain payload to a destination.
 */
@Controller
@RequestMapping("/sendCommand/destination/{destinationName}/payload/{payloadSTR}")
public final class SendCommandController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(SendCommandController.class);

    /**
     * Node persistence manager.
     */
    private transient NodeController nodeManager;

    /**
     * Sets node persistence manager.
     *
     * @param nodeManager node persistence manager.
     */
    @Autowired
    public void setNodeManager(final NodeController nodeManager) {
        this.nodeManager = nodeManager;
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws NodeNotFoundException NodeNotFoundException exception.
     * @throws IOException           IOException exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> handle(@PathVariable("destinationName") String destinationName, @PathVariable("payloadSTR") String payloadSTR)
            throws NodeNotFoundException, IOException {

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
