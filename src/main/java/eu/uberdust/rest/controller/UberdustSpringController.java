package eu.uberdust.rest.controller;

import eu.uberdust.formatter.JsonFormatter;
import eu.uberdust.formatter.TextFormatter;
import eu.uberdust.util.QuartzJobScheduler;
import eu.wisebed.wisedb.controller.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amaxilatis
 * Date: 8/26/13
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class UberdustSpringController {
    protected Map<String, Object> refData;
    protected String current_user;
    protected transient TestbedController testbedManager;
    protected transient SetupController setupManager;
    protected transient NodeController nodeManager;
    protected transient LinkController linkManager;
    protected transient CapabilityController capabilityManager;
    protected transient NodeCapabilityController nodeCapabilityManager;
    protected transient LinkCapabilityController linkCapabilityManager;
    protected transient NodeReadingController nodeReadingManager;
    protected transient LinkReadingController linkReadingManager;
    protected transient LastNodeReadingController lastNodeReadingManager;
    protected transient LastLinkReadingController lastLinkReadingManager;
    protected transient ScheduleController scheduleManager;
    protected transient VirtualNodeDescriptionController virtualNodeDescriptionManager;
    protected transient UserController userManager;
    protected transient UserRoleController userRoleManager;
    protected transient QuartzJobScheduler quartzJobScheduler;

    protected transient StatisticsController statisticsManager;

    @Autowired
    public void setTestbedManager(final TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }

    @Autowired
    public void setSetupManager(final SetupController setupManager) {
        this.setupManager = setupManager;
    }

    @Autowired
    public void setNodeManager(final NodeController nodeManager) {
        this.nodeManager = nodeManager;
    }

    @Autowired
    public void setLinkManager(final LinkController linkManager) {
        this.linkManager = linkManager;
    }

    @Autowired
    public void setCapabilityManager(final CapabilityController capabilityManager) {
        this.capabilityManager = capabilityManager;
    }

    @Autowired
    public void setNodeCapabilityManager(final NodeCapabilityController nodeCapabilityManager) {
        this.nodeCapabilityManager = nodeCapabilityManager;
    }

    @Autowired
    public void setLinkCapabilityManager(final LinkCapabilityController linkCapabilityManager) {
        this.linkCapabilityManager = linkCapabilityManager;
    }

    @Autowired
    public void setNodeReadingManager(final NodeReadingController nodeReadingManager) {
        this.nodeReadingManager = nodeReadingManager;
    }

    @Autowired
    public void setLinkReadingManager(final LinkReadingController linkReadingManager) {
        this.linkReadingManager = linkReadingManager;
    }

    @Autowired
    public void setLastNodeReadingManager(final LastNodeReadingController lastNodeReadingManager) {
        this.lastNodeReadingManager = lastNodeReadingManager;
    }

    @Autowired
    public void setLastLinkReadingManager(final LastLinkReadingController lastLinkReadingManager) {
        this.lastLinkReadingManager = lastLinkReadingManager;
    }

    @Autowired
    public void setScheduleManager(final ScheduleController scheduleManager) {
        this.scheduleManager = scheduleManager;
    }

    @Autowired
    public void setVirtualNodeDescriptionManager(final VirtualNodeDescriptionController virtualNodeDescriptionManager) {
        this.virtualNodeDescriptionManager = virtualNodeDescriptionManager;
    }

    @Autowired
    public void setUserManager(final UserController userManager) {
        this.userManager = userManager;
    }

    @Autowired
    public void setUserRoleManager(final UserRoleController userRoleManager) {
        this.userRoleManager = userRoleManager;
    }

    @Autowired
    public void setQuartzJobScheduler(final QuartzJobScheduler quartzJobScheduler) {
        this.quartzJobScheduler = quartzJobScheduler;
    }

    @Autowired
    public void setStatisticsManager(StatisticsController statisticsManager) {
        this.statisticsManager = statisticsManager;
    }

    public StatisticsController getStatisticsManager() {
        return statisticsManager;
    }

    public void initialize(Object user) {
        refData = new HashMap<String, Object>();
        String username;
        if (user instanceof User) {
            username = ((User) user).getUsername();
        } else {
            username = null;
        }
        current_user = username;
        refData.put("username", username);
    }

    protected ResponseEntity<String> jsonResponse(final String jsonString) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        responseHeaders.add("Access-Control-Allow-Methods", "GET, POST");
        return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.OK);
    }

    protected ResponseEntity<String> rawResponse(final String rawString) {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        responseHeaders.add("Access-Control-Allow-Methods", "GET, POST");
        return new ResponseEntity<String>(rawString, responseHeaders, HttpStatus.OK);
    }

    protected ResponseEntity<String> xmlResponse(final String rawString) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(rawString, responseHeaders, HttpStatus.OK);
    }
}
