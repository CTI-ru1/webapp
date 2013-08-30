package eu.uberdust.util;

import ch.ethz.inf.vs.californium.coap.CodeRegistry;
import ch.ethz.inf.vs.californium.coap.Option;
import ch.ethz.inf.vs.californium.coap.OptionNumberRegistry;
import ch.ethz.inf.vs.californium.coap.Request;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: amaxilatis
 * Date: 8/27/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScheduleJob implements Job {
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ScheduleJob.class);
    private final Random rand;


    public ScheduleJob() {
        rand = new Random();
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int setup_id = dataMap.getInt("setup");
        String node = dataMap.getString("node");
        String capability = dataMap.getString("capability");
        String payload = dataMap.getString("payload");
        LOGGER.info("ScheduleJob:" + node + "@" + capability);


        Request coapReq = new Request(CodeRegistry.METHOD_POST, false);

        coapReq.setMID(rand.nextInt() % 60000);

        if (node.contains("0x")) {
            Option uriHost = new Option(OptionNumberRegistry.URI_HOST);
            uriHost.setStringValue(node.split("0x")[1]);
            coapReq.addOption(uriHost);
        }
        final String capShortName = capability.substring(capability.lastIndexOf(":") + 1);
        coapReq.setURI(capShortName);
        coapReq.setPayload(payload);

        StringBuilder payloadStringBuilder = new StringBuilder();
        for (Byte data : coapReq.toByteArray()) {
            int i = data;
            payloadStringBuilder.append(",");
            payloadStringBuilder.append(Integer.toHexString(i));
        }

        final String payloadString = "33," + payloadStringBuilder.toString().substring(1).replaceAll("ffffff", "");

        CommandDispatcher.getInstance().sendCommand(setup_id, node, payloadString);

    }
}
