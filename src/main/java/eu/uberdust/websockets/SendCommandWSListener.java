package eu.uberdust.websockets;

import com.caucho.websocket.AbstractWebSocketListener;
import com.caucho.websocket.WebSocketContext;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akribopo
 * Date: 11/8/11
 * Time: 12:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class SendCommandWSListener extends AbstractWebSocketListener {

    /**
     * Static Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(SendCommandWSListener.class);

    /**
     * A List with the connected users.
     */
    private final transient List<WebSocketContext> users = new ArrayList<WebSocketContext>();

    private int testbedId;

    public int getTestbedId() {
        return testbedId;
    }

    /**
     * Constructor.
     */
    public SendCommandWSListener(final String testbed) {
        super();
        testbedId = Integer.parseInt(testbed);
//        String thisProtocol = new StringBuilder().append(nodeID).append(":").append(capabilityID).toString();

    }

    @Override
    public final void onStart(final WebSocketContext context) throws IOException {
        super.onStart(context);
        LOGGER.info("onStart-SendCommand");
        users.add(context);
        LOGGER.info(users.size());
        context.setTimeout(-1);
    }

    @Override
    public final void onReadBinary(final WebSocketContext context, final InputStream inputStream) throws IOException {
        super.onReadBinary(context, inputStream);
    }

    @Override
    public final void onReadText(final WebSocketContext context, final Reader reader) throws IOException {
        super.onReadText(context, reader);
    }

    @Override
    public final void onClose(final WebSocketContext context) throws IOException {
        super.onClose(context);
        LOGGER.info("onClose");
        users.remove(context);
        LOGGER.info(users.size());
        /*if (users.size() == 0) {
            LastNodeReadingConsumer.getInstance().removeListener(nodeID, capabilityID);
        }*/
    }

    @Override
    public final void onDisconnect(final WebSocketContext context) throws IOException {
        super.onDisconnect(context);
        LOGGER.info("onDisconnect");
        users.remove(context);
        LOGGER.info(users.size());
        /*   if (users.size() == 0) {
            LastNodeReadingConsumer.getInstance().removeListener(nodeID, capabilityID);
        }*/

    }

    @Override
    public final void onTimeout(final WebSocketContext context) throws IOException {
        super.onTimeout(context);
        LOGGER.info("onTimeout");
    }


    public final void update(final String nodeId, final String command) {
        LOGGER.info("Update");
        for (final WebSocketContext user : users) {
            try {
                final PrintWriter thisWriter = user.startTextMessage();
                thisWriter.println(nodeId + "@" + command);
                thisWriter.close();
            } catch (final IOException e) {
                LOGGER.error(e);
            }
        }
    }
}
