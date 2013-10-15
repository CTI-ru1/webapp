package eu.uberdust.rest.controller.json;

import com.google.gson.Gson;
import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.model.Schedule;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * Controller class that returns a information about the defined schedules in JSON format.
 */
@Controller
@RequestMapping()
public class JsonScheduleController extends UberdustSpringController {
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ListCapabilitiesController.class);


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
    @RequestMapping(value = "/testbed/{testbedId}/schedule/json", method = RequestMethod.GET)
    public ResponseEntity<String> listSchedules(@PathVariable("testbedId") int testbedId)
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
        final JSONArray jsonSchedules = new JSONArray();
        LOGGER.info("HERE");
        if (scheduleManager != null) {
            try {
                final List<Schedule> schedules = scheduleManager.list(testbed.getSetup(), current_user);
                for (Schedule schedule : schedules) {
                    if (nodeManager.getByName(schedule.getNode()).getSetup().getId() == testbedId) {
                        schedule.setLast(quartzJobScheduler.getLastFiredTime(schedule));
                        Gson gson = new Gson();
                        final JSONObject jsonSchedule = new JSONObject(gson.toJson(schedule, Schedule.class));
                        jsonSchedules.put(jsonSchedule);
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
        return jsonResponse(jsonSchedules.toString());

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
    @RequestMapping(value = "/schedule/json", method = RequestMethod.GET)
    public ResponseEntity<String> listAllSchedules()
            throws TestbedNotFoundException, InvalidTestbedIdException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // Prepare data to pass to jsp


        final JSONArray jsonSchedules = new JSONArray();
        if (scheduleManager != null) {
            try {
                final List<Schedule> schedules = scheduleManager.list(current_user);
                for (Schedule schedule : schedules) {
                    schedule.setLast(quartzJobScheduler.getLastFiredTime(schedule));
                    Gson gson = new Gson();
                    final JSONObject jsonSchedule = new JSONObject(gson.toJson(schedule, Schedule.class));
                    jsonSchedules.put(jsonSchedule);
                }
            } catch (Exception e) {
                LOGGER.error(e, e);
            }
        }

        LOGGER.info("HERE");
        return jsonResponse(jsonSchedules.toString());

    }

}
