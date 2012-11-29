package eu.uberdust.websockets.readings;

import com.caucho.websocket.AbstractWebSocketListener;
import com.caucho.websocket.WebSocketContext;
import eu.uberdust.communication.protobuf.Message;
import eu.uberdust.formatter.TextFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.wisebed.wisedb.listeners.AbstractNodeReadingListener;
import eu.wisebed.wisedb.model.NodeReading;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: akribopo
 * Date: 3/20/12
 * Time: 5:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class LastReadingWSListener extends AbstractWebSocketListener implements AbstractNodeReadingListener {

    /**
     * Static Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LastReadingWSListener.class);

    /**
     * A List with the connected users.
     */
    private final transient CopyOnWriteArrayList<WebSocketContext> users = new CopyOnWriteArrayList<WebSocketContext>();

    /**
     * The Node id..
     */
    private final transient String nodeID;

    /**
     * The capaility id.
     */
    private final transient String capabilityID;

    /**
     * Constructor.
     *
     * @param nodeID       the node ID.
     * @param capabilityID the capability ID.
     */
    public LastReadingWSListener(final String nodeID, final String capabilityID) {
        super();
        this.nodeID = nodeID;
        this.capabilityID = capabilityID;
//        String thisProtocol = new StringBuilder().append(nodeID).append(":").append(capabilityID).toString();

    }

    @Override
    public final void onStart(final WebSocketContext context) throws IOException {
        super.onStart(context);
        LOGGER.info("onStart");
        users.add(context);
        LOGGER.debug(users.size());
        context.setTimeout(-1);
    }

    @Override
    public final void onReadBinary(final WebSocketContext context, final InputStream inputStream) throws IOException {
        super.onReadBinary(context, inputStream);
    }

    @Override
    public final void onReadText(final WebSocketContext context, final Reader reader) throws IOException {

        final char[] arr = new char[1024]; // 1K at a time
        final StringBuffer buf = new StringBuffer();
        int numChars;

        while ((numChars = reader.read(arr, 0, arr.length)) > 0) {
            buf.append(arr, 0, numChars);
        }
        LOGGER.info("onReadText() : " + buf.toString());

        final PrintWriter writer = context.startTextMessage();
        writer.append("pong");
        writer.flush();
        writer.close();

        super.onReadText(context, reader);
    }

    @Override
    public final void onClose(final WebSocketContext context) throws IOException {
        super.onClose(context);
        LOGGER.info("onClose");
        users.remove(context);
        LOGGER.debug(users.size());
        /*if (users.size() == 0) {
            LastNodeReadingConsumer.getInstance().removeListener(nodeID, capabilityID);
        }*/
    }

    @Override
    public final void onDisconnect(final WebSocketContext context) throws IOException {
        super.onDisconnect(context);
        LOGGER.info("onDisconnect");
        users.remove(context);
        LOGGER.debug(users.size());
        /*   if (users.size() == 0) {
            LastNodeReadingConsumer.getInstance().removeListener(nodeID, capabilityID);
        }*/

    }

    @Override
    public final void onTimeout(final WebSocketContext context) throws IOException {
        super.onTimeout(context);
        LOGGER.debug("onTimeout");
    }

    @Override
    public final void update(final NodeReading lastReading) {
        LOGGER.info("updating for " + lastReading);
        LOGGER.info(lastReading.getCapability().getNode().getName()
                + "--" + lastReading.getCapability().getCapability().getName());
        if ((nodeID.contains("virtual") || lastReading.getCapability().getNode().getName().equals(nodeID))
                && lastReading.getCapability().getCapability().getName().equals(capabilityID)) {
            LOGGER.info("is for me");
            //create the protobuf
            final Message.NodeReadings.Reading.Builder reading = Message.NodeReadings.Reading.newBuilder();
            LOGGER.error("initialized?: " + reading.isInitialized());
            reading.setNode(lastReading.getCapability().getNode().getName())
                    .setCapability(lastReading.getCapability().getCapability().getName())
                    .setTimestamp(lastReading.getTimestamp().getTime());
            if (lastReading.getReading() != null) {
                reading.setDoubleReading(lastReading.getReading());
            } else {
                reading.setStringReading(lastReading.getStringReading());
            }
            final Message.NodeReadings readings = Message.NodeReadings.newBuilder().addReading(reading.build()).build();
            final Message.Envelope envelope = Message.Envelope.newBuilder()
                    .setType(Message.Envelope.Type.NODE_READINGS)
                    .setNodeReadings(readings)
                    .build();
            LOGGER.info("envelope built");

            String readingToString = "";
            try {
                readingToString = TextFormatter.getInstance().formatNodeReading(lastReading);
            } catch (NotImplementedException e) {
                LOGGER.error(e, e);
            }

            LOGGER.info("Updating " + users.size() + " listeners");

            for (final WebSocketContext user : users) {
                try {
                    final OutputStream thisWriter = user.startBinaryMessage();
                    thisWriter.write(envelope.toByteArray());
                    thisWriter.flush();
                    thisWriter.close();

                    final PrintWriter thisMessageWriter = user.startTextMessage();
                    thisMessageWriter.write(readingToString);
                    thisMessageWriter.flush();
                    thisMessageWriter.close();

                } catch (final IOException ioe) {
                    LOGGER.error(ioe, ioe);
                } catch (final IllegalStateException ise) {
                    LOGGER.error(ise, ise);
                }
            }
        }

    }
}

