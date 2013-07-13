package eu.uberdust.websockets.commands;

import com.caucho.websocket.WebSocketServletRequest;
import eu.uberdust.communication.websocket.WSIdentifiers;
import eu.uberdust.util.CommandDispatcher;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Validates the initial HTTP request and  dispatches a new WebSocket connection.
 */

@org.springframework.stereotype.Controller
@RequestMapping("/testbedcontroller.ws")
public class CommandPublisherWebSocket
        extends GenericServlet {

    /**
     * Static Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(CommandPublisherWebSocket.class);

    private static final long serialVersionUID = 2111855028625803019L;

    /**
     * Default Constructor.
     */
    public CommandPublisherWebSocket() {
        super();
    }

    /**
     * Services the request.
     *
     * @param servletRequest  the servletRequest
     * @param servletResponse the servletResponse
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @RequestMapping(method = RequestMethod.GET)
    public final ModelAndView handleRequest(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) throws Exception {

        servletRequest.getSession().setMaxInactiveInterval(Integer.MAX_VALUE);
        /*
        * Process the handshake, selecting the protocol to be used.
        * The protocol is Defined by: NodeID:capabilityID
        */
        final String protocol = decode(servletRequest.getHeader("Sec-WebSocket-Protocol"));

        LOGGER.info(protocol);


        if (protocol == null) {
            servletResponse.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return null;
        }

        final CommandWSListener thisListener;
        if (protocol.split(WSIdentifiers.DELIMITER)[0].equals(WSIdentifiers.COMMAND_PROTOCOL)) {

            thisListener = new CommandWSListener(protocol.split(WSIdentifiers.DELIMITER)[1]);

            LOGGER.debug("new TESTBEDCONTROLLER");
            CommandDispatcher.getInstance().add(thisListener);
            servletResponse.setHeader("Sec-WebSocket-Protocol", protocol);

            final WebSocketServletRequest wsRequest = (WebSocketServletRequest) servletRequest;
            wsRequest.startWebSocket(thisListener);
        }
        return null;
    }

    private String decode(final String header) {
        if (header == null) {
            return null;
        } else {
            String decodedProtocol = header;
            if (header.contains(".")) {
                decodedProtocol = decodedProtocol.replaceAll("\\.", "@");
            }
            if (header.contains("-")) {
                decodedProtocol = decodedProtocol.replaceAll("-", ":");
            }
            return decodedProtocol;
        }
    }

    @Override
    public final void service(final ServletRequest servletRequest, final ServletResponse servletResponse) throws ServletException, IOException {
        LOGGER.debug("service");
        try {
            handleRequest((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        } catch (Exception ex) {
            LOGGER.fatal(ex);
            ex.printStackTrace();
        }
    }
}

