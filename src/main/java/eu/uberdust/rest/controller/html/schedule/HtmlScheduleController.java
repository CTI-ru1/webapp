package eu.uberdust.rest.controller.html.schedule;

import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.Schedule;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller class that returns a list of links for a given testbed in HTML format.
 */
@Controller
public final class HtmlScheduleController extends UberdustSpringController {

    /**
     * Logger persistence manager.
     */
    private static final Logger LOGGER = Logger.getLogger(HtmlScheduleController.class);

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *          an InvalidTestbedIdException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *          an TestbedNotFoundException exception.
     */
    @Loggable
    @RequestMapping(value = "/testbed/{testbedId}/schedule", method = RequestMethod.GET)
    public ModelAndView listSchedules(@PathVariable("testbedId") int testbedId)
            throws TestbedNotFoundException, InvalidTestbedIdException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // Prepare data to pass to jsp


        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);

        LOGGER.info("HERE");
        if (scheduleManager != null) {
            try {
                final List<Schedule> allSchedules = scheduleManager.list(testbed.getSetup(), current_user);
                final List<Schedule> schedules = new ArrayList<Schedule>();
                for (Schedule schedule : allSchedules) {
                    if (nodeManager.getByName(schedule.getNode()).getSetup().getId() == testbedId) {
                        schedule.setLast(quartzJobScheduler.getLastFiredTime(schedule));
                        schedules.add(schedule);
                    }
                }
                refData.put("schedules", schedules);
            } catch (Exception e) {
                LOGGER.error(e, e);
            }
        } else {
            LOGGER.info("HERE not null");
        }

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        refData.put("testbed", testbed);
        LOGGER.info("HERE");
        return new ModelAndView("blockly/schedule/list.html", refData);

    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *          an InvalidTestbedIdException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *          an TestbedNotFoundException exception.
     */
    @Loggable
    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public ModelAndView listAllSchedules()
            throws TestbedNotFoundException, InvalidTestbedIdException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // Prepare data to pass to jsp

        if (scheduleManager != null) {
            try {
                final List<Schedule> schedules = scheduleManager.list(current_user);
                for (Schedule schedule : schedules) {
                    schedule.setLast(quartzJobScheduler.getLastFiredTime(schedule));
                }
                refData.put("schedules", schedules);
            } catch (Exception e) {
                LOGGER.error(e, e);
            }
        }

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        return new ModelAndView("blockly/schedule/list.html", refData);

    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *          an InvalidTestbedIdException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *          an TestbedNotFoundException exception.
     */
    @Loggable
    @RequestMapping(value = "/schedule/add", method = RequestMethod.POST)
    public ResponseEntity<String> addSchedule(@RequestParam("type") String type,
                                              @RequestParam("second") String second,
                                              @RequestParam("minute") String minute,
                                              @RequestParam("hour") String hour,
                                              @RequestParam("dom") String dom,
                                              @RequestParam("month") String month,
                                              @RequestParam("dow") String dow,
                                              @RequestParam("node") String node,
                                              @RequestParam("capability") String capability,
                                              @RequestParam("payload") String payload
    )
            throws TestbedNotFoundException, InvalidTestbedIdException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Exception exception;
        try {
            if ("".equals(current_user)) {
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
                return new ResponseEntity<String>("Unauthorized User. Only logged in users can publish rules.", responseHeaders, HttpStatus.UNAUTHORIZED);
            } else {
                if (second.equals("*")) {
                    HttpHeaders responseHeaders = new HttpHeaders();
                    responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
                    return new ResponseEntity<String>("Rules cannot be scheduled for execution every second.", responseHeaders, HttpStatus.NOT_ACCEPTABLE);
                }
                Schedule shed = new Schedule();
                shed.setType(type);
                shed.setUsername(current_user);
                shed.setSecond(second);
                shed.setMinute(minute);
                shed.setHour(hour);
                shed.setDom(dom);
                shed.setMonth(month);
                shed.setDow(dow);
                shed.setNode(node);
                shed.setCapability(capability);
                shed.setPayload(payload);
                shed.setLast(new Date(0));
                scheduleManager.add(shed);
                quartzJobScheduler.addJob(shed);

                return rawResponse("ok:" + current_user + " "
                        + type + " "
                        + second + " "
                        + minute + " "
                        + hour + " "
                        + dom + " "
                        + month + " "
                        + dow + " = "
                        + node + ","
                        + capability + ","
                        + payload);
            }
        } catch (Exception e) {
            quartzJobScheduler.init();
            exception = e;
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>("An error occured while creating the schedule:" + exception.getMessage(), responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *          an InvalidTestbedIdException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *          an TestbedNotFoundException exception.
     */
    @Loggable
    @RequestMapping(value = "/testbed/{testbedId}/schedule/{ruleId}/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSchedule(
            @PathVariable("testbedId") int testbedId,
            @PathVariable("ruleId") int ruleId,
            @PathVariable("username") String username
    )
            throws TestbedNotFoundException, InvalidTestbedIdException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try {
            if (
                    username.equals(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername())
                            && scheduleManager.getByID(ruleId).getUsername().equals(username)
                    ) {
                quartzJobScheduler.removeJob(ruleId);
                scheduleManager.delete(ruleId);

                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
                return new ResponseEntity<String>("ok", responseHeaders, HttpStatus.OK);
            }
        } catch (Exception e) {
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>("unauthorized", responseHeaders, HttpStatus.UNAUTHORIZED);

    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *          an InvalidTestbedIdException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *          an TestbedNotFoundException exception.
     */
    @Loggable
    @RequestMapping(value = "/testbed/{testbedId}/schedule/{ruleId}/{username}", method = RequestMethod.GET)
    public ResponseEntity<String> runSchedule(
            @PathVariable("testbedId") int testbedId,
            @PathVariable("ruleId") int ruleId,
            @PathVariable("username") String username
    )
            throws TestbedNotFoundException, InvalidTestbedIdException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try {
            if (
                    username.equals(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername())
                            && scheduleManager.getByID(ruleId).getUsername().equals(username)
                    ) {
                quartzJobScheduler.runJob(ruleId);

                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
                return new ResponseEntity<String>("ok", responseHeaders, HttpStatus.OK);
            }
        } catch (Exception e) {
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>("unauthorized", responseHeaders, HttpStatus.UNAUTHORIZED);

    }


    @Loggable
    @RequestMapping(method = RequestMethod.GET, value = "/testbed/{testbedId}/schedule/create")
    public ModelAndView createVirtualNode(@PathVariable("testbedId") int testbedId)
            throws TestbedNotFoundException, InvalidTestbedIdException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());


        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // get testbed's nodes
        final List<Node> allnodes = nodeManager.list(testbed.getSetup());
        final List<Node> nodes = new ArrayList<Node>();
        for (Node node : allnodes) {
            for (Capability capability : capabilityManager.list(node)) {
                if (capability.getName().contains(":lz") || capability.getName().contains(":ac")) {
                    nodes.add(node);
                    break;
                } else if (capability.getName().matches(".*:.r")) {
                    nodes.add(node);
                    break;
                }
            }
        }
        final List<Capability> allcapabilities = capabilityManager.list(testbed.getSetup());
        final List<Capability> capabilities = new ArrayList<Capability>();
        for (Capability capability : allcapabilities) {
            if (capability.getName().contains(":lz") || capability.getName().contains(":ac")) {
                capabilities.add(capability);
            } else if (capability.getName().matches(".*:.r")) {
                capabilities.add(capability);
            }
        }

        // Prepare data to pass to jsp


        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);

        refData.put("nodes", nodes);
        refData.put("capabilities", capabilities);

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        return new ModelAndView("blockly/schedule/create.html", refData);

    }
}
