package eu.uberdust.websockets.readings;

import com.caucho.websocket.WebSocketServletRequest;
import eu.uberdust.communication.websocket.WSIdentifiers;
import eu.wisebed.wisedb.controller.*;
import eu.wisebed.wisedb.listeners.LastNodeReadingConsumer;
import eu.wisebed.wisedb.model.Link;
import eu.wisebed.wisedb.model.LinkCapability;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: akribopo
 * Date: 3/20/12
 * Time: 1:07 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/readings.ws")
public class ReadingsWebSocket {

    /**
     * Static Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ReadingsWebSocket.class);

    /**
     * Serial Version Unique ID.
     */
    private static final long serialVersionUID = -279704326229266519L;

    /**
     * Link persistence manager.
     */
    private transient NodeController nodeManager;
    private transient LinkController linkManager;
    private transient LinkCapabilityController linkCapabilityManager;

    @Autowired
    public void setNodeManager(final NodeController nodeManager) {
        this.nodeManager = nodeManager;
    }

    /**
     * Sets link persistence manager.
     *
     * @param linkManager link persistence manager.
     */
    @Autowired
    public void setLinkManager(final LinkController linkManager) {
        this.linkManager = linkManager;
    }

    @Autowired
    public void setLinkCapabilityManager(final LinkCapabilityController linkCapabilityManager) {
        this.linkCapabilityManager = linkCapabilityManager;
    }

    /**
     * Insert Reading Web Socket Listener.
     */
    private InsertReadingWSListener insertReadingWSListener;
    /**
     * A HashMap<CapabilityID:NodeID>.
     */
    private final transient Map<String, LastReadingWSListener> listeners;

    /**
     * Sets the web socket listener.
     *
     * @param insertReadingWSListener reading web socket listener.
     */
    @Autowired
    public void setInsertReadingWSListener(final InsertReadingWSListener insertReadingWSListener) {
        this.insertReadingWSListener = insertReadingWSListener;
    }

    /**
     * Default Constructor.
     */
    public ReadingsWebSocket() {
        // empty constructor.
        listeners = new HashMap<String, LastReadingWSListener>();
//        LOGGER.setLevel(Level.DEBUG);
    }

    /**
     * Handles the request.
     *
     * @param servletRequest  the servletRequest.
     * @param servletResponse the servletResponse.
     * @return servlet response.
     * @throws javax.servlet.ServletException ServletException exception.
     * @throws java.io.IOException            IOException exception.
     */
    @RequestMapping()
    public void handleRequest(final HttpServletRequest servletRequest,
                              final HttpServletResponse servletResponse) throws ServletException, IOException {

        servletRequest.getSession().setMaxInactiveInterval(Integer.MAX_VALUE);

        //Process the handshake, selecting the protocol to be used.
        String protocol = servletRequest.getHeader("Sec-WebSocket-Protocol");
        LOGGER.info(protocol);

        if (protocol == null) {
            servletResponse.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }


//        if (protocol.contains("virtual")) {
//            protocol = protocol.substring(0, protocol.indexOf(".")) +
//                    "@" + protocol.substring(protocol.indexOf(".") + 1, protocol.lastIndexOf(".")) +
//                    "@" + protocol.substring(protocol.lastIndexOf(".") + 1);
//
//            protocol = protocol.replaceAll("-", ":");
//
//        } else {
        protocol = protocol.replaceAll("\\.", "@").replaceAll("--", ":");
//        }


        if (protocol.equals(WSIdentifiers.INSERT_PROTOCOL)) {
            LOGGER.info("WSIdentifiers.INSERT_PROTOCOL");
            LOGGER.debug(insertReadingWSListener == null);

//                servletResponse.setHeader("Sec-WebSocket-Protocol", protocol);

            //Initialize Insert WebSocket Client
            final WebSocketServletRequest wsRequest = (WebSocketServletRequest) servletRequest;
            wsRequest.startWebSocket(insertReadingWSListener);

            return;
        }

        if (protocol.startsWith(WSIdentifiers.SUBSCRIBE_PROTOCOL_PREFIX)) {
            LOGGER.info("WSIdentifiers.SUBSCRIBE_PROTOCOL_PREFIX");
            String nodeName = protocol.substring(protocol.indexOf(WSIdentifiers.DELIMITER) + 1, protocol.lastIndexOf(WSIdentifiers.DELIMITER)).replaceAll("@", "\\.");
            String capabilityName = protocol.substring(protocol.lastIndexOf(WSIdentifiers.DELIMITER) + 1);
            LOGGER.info("nodeName=" + nodeName);
            LOGGER.info("capabilityName=" + capabilityName);

            LastReadingWSListener lastReadingWSListener;
            LOGGER.info("listeners:" + listeners.size());

            if (listeners.containsKey(protocol) && (listeners.get(protocol).userCount() > 0)) {
//                servletResponse.setHeader("Sec-WebSocket-Protocol", protocol);
                lastReadingWSListener = listeners.get(protocol);
                LOGGER.debug("registered in existing listener");

            } else {
                lastReadingWSListener = new LastReadingWSListener(nodeName, capabilityName);
                LastNodeReadingConsumer.getInstance().registerListener(nodeName, capabilityName, lastReadingWSListener);

                LOGGER.debug("new listener");
                listeners.put(protocol, lastReadingWSListener);

//                if (protocol.split(WSIdentifiers.DELIMITER)[1].contains("virtual")) {
                //Get all links as nodes may be connected programmatically
                List<Link> links = linkManager.getBySource(nodeManager.getByName(nodeName));
                for (Link link : links) {
                    LOGGER.info(link);
                    LinkCapability vcap = linkCapabilityManager.getByID(link, "virtual");
                    if (vcap != null && vcap.getLastLinkReading().getReading() == 1.0) {
                        LOGGER.info("registerListenerAlso@" + link.getTarget().getName());
                        LastNodeReadingConsumer.getInstance().registerListener(
                                link.getTarget().getName(), capabilityName, lastReadingWSListener);
                    }
                }

//                }

//                servletResponse.setHeader("Sec-WebSocket-Protocol", protocol);
            }

            //Initialize LastReading WebSocket Client
            final WebSocketServletRequest wsRequest = (WebSocketServletRequest) servletRequest;
            wsRequest.startWebSocket(lastReadingWSListener);

            return;
        }
        LOGGER.debug("HttpServletResponse.SC_NOT_ACCEPTABLE");
        servletResponse.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
        return;
    }


//    @Override
//    public final void service(final ServletRequest servletRequest, final ServletResponse servletResponse) throws
//            ServletException, IOException {
//        LOGGER.debug("service");
//        try {
//            handleRequest((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
//        } catch (Exception ex) {
//            LOGGER.fatal(ex);
//            ex.printStackTrace();
//        }
//    }
}
