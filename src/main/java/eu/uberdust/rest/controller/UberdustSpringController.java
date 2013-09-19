package eu.uberdust.rest.controller;

import eu.uberdust.util.QuartzJobScheduler;
import eu.wisebed.wisedb.controller.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public void setTestbedManager(TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }

    @Autowired
    public void setSetupManager(SetupController setupManager) {
        this.setupManager = setupManager;
    }

    @Autowired
    public void setNodeManager(NodeController nodeManager) {
        this.nodeManager = nodeManager;
    }

    @Autowired
    public void setLinkManager(LinkController linkManager) {
        this.linkManager = this.linkManager;
    }

    @Autowired
    public void setCapabilityManager(CapabilityController capabilityManager) {
        this.capabilityManager = capabilityManager;
    }

    @Autowired
    public void setNodeCapabilityManager(NodeCapabilityController nodeCapabilityManager) {
        this.nodeCapabilityManager = nodeCapabilityManager;
    }

    @Autowired
    public void setLinkCapabilityManager(LinkCapabilityController linkCapabilityManager) {
        this.linkCapabilityManager = linkCapabilityManager;
    }

    @Autowired
    public void setNodeReadingManager(NodeReadingController nodeReadingManager) {
        this.nodeReadingManager = nodeReadingManager;
    }

    @Autowired
    public void setLinkReadingManager(LinkReadingController linkReadingManager) {
        this.linkReadingManager = linkReadingManager;
    }

    @Autowired
    public void setLastNodeReadingManager(LastNodeReadingController lastNodeReadingManager) {
        this.lastNodeReadingManager = lastNodeReadingManager;
    }

    @Autowired
    public void setLastLinkReadingManager(LastLinkReadingController lastLinkReadingManager) {
        this.lastLinkReadingManager = lastLinkReadingManager;
    }

    @Autowired
    public void setScheduleManager(ScheduleController scheduleManager) {
        this.scheduleManager = scheduleManager;
    }

    @Autowired
    public void setVirtualNodeDescriptionManager(VirtualNodeDescriptionController virtualNodeDescriptionManager) {
        this.virtualNodeDescriptionManager = virtualNodeDescriptionManager;
    }

    @Autowired
    public void setUserManager(UserController userManager) {
        this.userManager = userManager;
    }

    @Autowired
    public void setUserRoleManager(UserRoleController userRoleManager) {
        this.userRoleManager = userRoleManager;
    }

    @Autowired
    public void setQuartzJobScheduler(QuartzJobScheduler quartzJobScheduler) {
        this.quartzJobScheduler = quartzJobScheduler;
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
}
