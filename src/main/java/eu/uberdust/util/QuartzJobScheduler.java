package eu.uberdust.util;

import eu.wisebed.wisedb.controller.NodeReadingController;
import eu.wisebed.wisedb.controller.ScheduleController;
import eu.wisebed.wisedb.model.Schedule;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created with IntelliJ IDEA.
 * User: amaxilatis
 * Date: 8/27/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller

public final class QuartzJobScheduler {
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(QuartzJobScheduler.class);

    private static QuartzJobScheduler ourInstance = new QuartzJobScheduler();
    private Scheduler sched;
    private final StdSchedulerFactory schedFact;
    private boolean initialized;

    private transient ScheduleController scheduleManager;
    private transient NodeReadingController nodeReadingManager;

    @Autowired
    public void setScheduleManager(final ScheduleController scheduleManager) {
        System.out.println("SET scheduleManager");
        this.scheduleManager = scheduleManager;
    }

    @Autowired
    public void setNodeReadingManager(final NodeReadingController nodeReadingManager) {
        System.out.println("SET nodeReadingManager");
        this.nodeReadingManager = nodeReadingManager;
    }

    public static QuartzJobScheduler getInstance() {
        return ourInstance;
    }

    public QuartzJobScheduler() {
        schedFact = new org.quartz.impl.StdSchedulerFactory();
        initialized = false;
    }

    public void init() {


        if (!initialized) {
            try {
                sched = schedFact.getScheduler();
                sched.start();
            } catch (SchedulerException e) {
                sched = null;
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            LOGGER.info("Quartz Initailized!:" + scheduleManager + "-" + nodeReadingManager);

            for (Schedule schedule : scheduleManager.list()) {
                LOGGER.info(schedule.toString());
                try {
                    addJob(schedule);
                } catch (SchedulerException e) {
                    LOGGER.error(e, e);
                }
            }
            initialized = true;
        }
    }

    public Date getLastFiredTime(final Schedule schedule) throws SchedulerException {
        return (((List<Trigger>) sched.getTriggersOfJob(new JobKey("myJob" + schedule.getId()))).get(0).getPreviousFireTime());
    }

    public void addJob(Schedule schedule) throws SchedulerException {
        // define the job and tie it to our HelloJob class
        JobDetail job = newJob(ScheduleJob.class)
                .withIdentity("myJob" + schedule.getId()) // name "myJob", group "group1"
                .usingJobData("setup", 1)
                .usingJobData("node", schedule.getNode())
                .usingJobData("capability", schedule.getCapability())
                .usingJobData("payload", schedule.getPayload())
                .build();

        // Trigger the job to run now, and then every 40 seconds
        Trigger trigger = newTrigger()
                .withIdentity("myTrigger" + schedule.getId())
                .startNow()
                .withSchedule(cronSchedule(schedule.getSecond() + " "
                        + schedule.getMinute() + " "
                        + schedule.getHour() + " "
                        + schedule.getDom() + " "
                        + schedule.getMonth() + " "
                        + schedule.getDow()
                )).build();

        // Tell quartz to schedule the job using our trigger
        sched.scheduleJob(job, trigger);

        listAllJobs();
    }

    public void removeJob(int scheduleId) throws SchedulerException {
        sched.deleteJob(new JobKey("myJob" + scheduleId));
        listAllJobs();
    }

    public void runJob(int scheduleId) throws SchedulerException {
        sched.triggerJob(new JobKey("myJob" + scheduleId));
    }

    private void listAllJobs() throws SchedulerException {
        for (String groupName : sched.getJobGroupNames()) {

            for (JobKey jobKey : sched.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                String jobName = jobKey.getName();
                String jobGroup = jobKey.getGroup();

                //get job's trigger
                List<Trigger> triggers = (List<Trigger>) sched.getTriggersOfJob(jobKey);
                Date nextFireTime = triggers.get(0).getNextFireTime();


                LOGGER.info("[jobName] : " + jobName + " [groupName] : " + jobGroup + " - " + nextFireTime);

            }

        }
    }
}
