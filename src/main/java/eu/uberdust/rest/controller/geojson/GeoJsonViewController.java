package eu.uberdust.rest.controller.geojson;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.NodeNotFoundException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.NodeCapabilityController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller class that returns the position of a node in GeoRSS format.
 */
@Controller
public final class GeoJsonViewController extends UberdustSpringController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(GeoJsonViewController.class);

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws java.io.IOException an IOException exception.
     *                             a FeedException exception.
     * @throws eu.uberdust.rest.exception.NodeNotFoundException
     *                             NodeNotFoundException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                             TestbedNotFoundException exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                             InvalidTestbedIdException exception.
     */
    @Loggable
    @RequestMapping(value = "/testbed/{testbedId}/node/{nodeName}/geojson", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> showNodeGeoJSONFeed(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, HttpServletRequest request, HttpServletResponse response)
            throws IOException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException, NotImplementedException {
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // look up node
        final Node node = nodeManager.getByName(nodeName);

        JSONObject geoJsonObject = new JSONObject();
        JSONArray nodeDescriptions = new JSONArray();
        try {
            geoJsonObject.put("type", "FeatureCollection");

            JSONObject nodeDescription = new JSONObject();
            nodeDescription.put("type", "Feature");
            JSONObject geometry = new JSONObject();
            geometry.put("type", "point");
            Position position = nodeManager.getAbsolutePosition(node);
            JSONArray coords = new JSONArray();
            coords.put(position.getX());
            coords.put(position.getY());
            geometry.put("coordinates", coords);
            nodeDescription.put("geometry", geometry);
            JSONObject properties = new JSONObject();
            List<NodeCapability> nCaps = nodeCapabilityManager.list(node);
            for (NodeCapability nCap : nCaps) {
                if (nCap.getLastNodeReading().getStringReading() != null) {
                    properties.put(nCap.getCapability().getName(), nCap.getLastNodeReading().getStringReading());
                } else {
                    properties.put(nCap.getCapability().getName(), nCap.getLastNodeReading().getReading());
                }
            }
            nodeDescription.put("properties", properties);
            nodeDescriptions.put(nodeDescription);

            geoJsonObject.put("features", nodeDescriptions);
        } catch (JSONException e) {
            LOGGER.error(e, e);
        }

        String output = geoJsonObject.toString();

        return jsonResponse(output);
    }

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                             a TestbedNotFoundException exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                             a InvalidTestbedIdException exception.
     * @throws java.io.IOException a IOException exception.
     *                             a FeedException exception.
     */
    @SuppressWarnings("unchecked")
    @Loggable
    @RequestMapping(value = "/testbed/{testbedId}/geojson", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> showTestbedGeoJSON(@PathVariable("testbedId") int testbedId, HttpServletRequest request, HttpServletResponse response)
            throws TestbedNotFoundException, InvalidTestbedIdException, IOException,NotImplementedException {
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // look up node
        final List<Node> nodes = nodeManager.list(testbed.getSetup());

        JSONObject geoJsonObject = new JSONObject();
        JSONArray nodeDescriptions = new JSONArray();
        try {
            geoJsonObject.put("type", "FeatureCollection");
            for (Node node : nodes) {
                JSONObject nodeDescription = new JSONObject();
                nodeDescription.put("type", "Feature");
                JSONObject geometry = new JSONObject();
                geometry.put("type", "point");
                Position position = nodeManager.getAbsolutePosition(node);
                JSONArray coords = new JSONArray();
                coords.put(position.getX());
                coords.put(position.getY());
                geometry.put("coordinates", coords);
                nodeDescription.put("geometry", geometry);
                JSONObject properties = new JSONObject();
                List<NodeCapability> nCaps = nodeCapabilityManager.list(node);
                for (NodeCapability nCap : nCaps) {
                    if (nCap.getLastNodeReading().getStringReading() != null) {
                        properties.put(nCap.getCapability().getName(), nCap.getLastNodeReading().getStringReading());
                    } else {
                        properties.put(nCap.getCapability().getName(), nCap.getLastNodeReading().getReading());
                    }
                }
                nodeDescription.put("properties", properties);
                nodeDescriptions.put(nodeDescription);
            }
            geoJsonObject.put("features", nodeDescriptions);
        } catch (JSONException e) {
            LOGGER.error(e, e);
        }

        String output = geoJsonObject.toString();

        return jsonResponse(output);
    }

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                             a TestbedNotFoundException exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                             a InvalidTestbedIdException exception.
     * @throws java.io.IOException a IOException exception.
     *                             a FeedException exception.
     */
    @SuppressWarnings("unchecked")
    @Loggable
    @RequestMapping(value = "/testbed/geojson", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> showTestbedsGeoJSON(HttpServletRequest request, HttpServletResponse response)
            throws TestbedNotFoundException, InvalidTestbedIdException, IOException,NotImplementedException {
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up node
        final List<Testbed> testbeds = testbedManager.list();

        JSONObject geoJsonObject = new JSONObject();
        JSONArray nodeDescriptions = new JSONArray();
        try {
            geoJsonObject.put("type", "FeatureCollection");
            for (Testbed testbed : testbeds) {
                JSONObject nodeDescription = new JSONObject();
                nodeDescription.put("type", "Feature");
                JSONObject geometry = new JSONObject();
                geometry.put("type", "point");
                Origin position = testbed.getSetup().getOrigin();
                JSONArray coords = new JSONArray();
                coords.put(position.getX());
                coords.put(position.getY());
                geometry.put("coordinates", coords);
                nodeDescription.put("geometry", geometry);
                JSONObject properties = new JSONObject();
                nodeDescription.put("properties", properties);
                nodeDescriptions.put(nodeDescription);
            }
            geoJsonObject.put("features", nodeDescriptions);
        } catch (JSONException e) {
            LOGGER.error(e, e);
        }

        String output = geoJsonObject.toString();

        return jsonResponse(output);
    }
}
