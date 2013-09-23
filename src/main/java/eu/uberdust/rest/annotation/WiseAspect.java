package eu.uberdust.rest.annotation;

import eu.uberdust.caching.EvictCache;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.wisebed.wisedb.model.Statistics;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class WiseAspect {
    private static final Logger LOGGER = Logger.getLogger(WiseAspect.class);

    /**
     * This pointcut matches all methods with a <code>@WiseLog</code> annotation.
     */
    @Pointcut("@annotation( thisLogName)")
    @SuppressWarnings("unused")
    private void doLog(WiseLog thisLogName) {
    }

    /**
     * Returns objects from the cache if necessary.
     */
    @Around("doLog(thisLogName)")
    public Object processRequest(final ProceedingJoinPoint thisJoinPoint, WiseLog thisLogName) throws Throwable {
        System.out.println("args:");
        for (Object o : thisJoinPoint.getArgs()) {
            System.out.println("ARG:" + o.getClass());
        }
//    @Around("wiseLog()")
//    public Object processRequest(final ProceedingJoinPoint thisJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object val = thisJoinPoint.proceed();
        long timediff = (System.currentTimeMillis() - start);
        System.out.println("TIMEDIFF:" + timediff);
        try {
            final UberdustSpringController thisController = (UberdustSpringController) thisJoinPoint.getTarget();
            thisController.getStatisticsManager().add(new Statistics(thisLogName.logName(), timediff));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }
}