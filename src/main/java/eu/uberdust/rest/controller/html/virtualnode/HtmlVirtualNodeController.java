package eu.uberdust.rest.controller.html.virtualnode;

import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.uberdust.rest.annotation.WiseLog;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.exception.UnknownTestbedException;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.Testbed;
import eu.wisebed.wisedb.model.VirtualNodeDescription;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class that returns a list of links for a given testbed in HTML format.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/virtualnode")
public final class HtmlVirtualNodeController extends UberdustSpringController {

    /**
     * Logger persistence manager.
     */
    private static final Logger LOGGER = Logger.getLogger(HtmlVirtualNodeController.class);

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
    @WiseLog(logName = "/testbed/virtualnode/")
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listVirtualNodes(@PathVariable("testbedId") int testbedId)
            throws TestbedNotFoundException, InvalidTestbedIdException {

        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // get testbed's nodes
        final List<Node> nodes = new ArrayList<Node>();
        for (Node node : nodeManager.list(testbed.getSetup())) {
            if (node.getName().contains(":virtual:")) {
                nodes.add(node);
            }
        }

        // Prepare data to pass to jsp


        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);

        refData.put("nodes", nodes);

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        return new ModelAndView("virtualnode/list.html", refData);

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
    @WiseLog(logName = "/testbed/virtualnode/add/")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ModelAndView add(@PathVariable("testbedId") int testbedId,
                            @RequestParam("name") String name,
                            @RequestParam("conditions") String conditions)
            throws TestbedNotFoundException, InvalidTestbedIdException, UnknownTestbedException {

        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        VirtualNodeDescription vnd = new VirtualNodeDescription();
        vnd.setUser(userManager.getByUsername(current_user));
        vnd.setDescription(conditions);
        Node node = nodeManager.getByName(name);
        if (node == null) {
            nodeManager.prepareInsertNode(name);
        }
        node = nodeManager.getByName(name);
        vnd.setNode(node);

        virtualNodeDescriptionManager.add(vnd);
        virtualNodeDescriptionManager.rebuild(testbedId);


        // get testbed's nodes
        final List<Node> nodes = new ArrayList<Node>();
        for (Node anode : nodeManager.list(testbed.getSetup())) {
            if (node.getName().contains(":virtual:")) {
                nodes.add(anode);
            }
        }

        refData.put("testbed", testbed);
        refData.put("nodes", nodes);
        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));

        return new ModelAndView("virtualnode/list.html", refData);
    }


    @Loggable
    @WiseLog(logName = "/testbed/virtualnode/create/")
    @RequestMapping(method = RequestMethod.GET, value = "/create")
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
        final List<Node> nodes = nodeManager.list(testbed.getSetup());
        final List<Capability> capabilities = capabilityManager.list(testbed.getSetup());
        // Prepare data to pass to jsp


        // else put thisNode instance in refData and return index view
        refData.put("testbed", testbed);
        refData.put("nodes", nodes);
        refData.put("capabilities", capabilities);
        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        return new ModelAndView("blockly/virtualnode/create.html", refData);

    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws java.io.IOException IO exception.
     */
    @Loggable
    @WiseLog(logName = "/testbed/virtualnode/rebuild/")
    @RequestMapping(value = "/rebuild", method = RequestMethod.GET)
    public ResponseEntity<String> rebuild(@PathVariable("testbedId") int testbedId) throws IOException, NotImplementedException {
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        final Testbed testbed = testbedManager.getByID(testbedId);
        final String response = virtualNodeDescriptionManager.rebuild(testbed.getId());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");

        return new ResponseEntity<String>(response.toString(), responseHeaders, HttpStatus.OK);
    }

}
