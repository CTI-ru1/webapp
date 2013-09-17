package eu.uberdust.util;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: amaxilatis
 * Date: 8/27/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class RebuildEntitiesJob implements Job {
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(RebuildEntitiesJob.class);


    public RebuildEntitiesJob() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
