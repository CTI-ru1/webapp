package eu.uberdust.rest.controller.html.capability;

import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.exception.CapabilityNotFoundException;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.LinkController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.Link;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that returns the a web page for a capability.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/capability")
public final class CapabilityViewController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(CapabilityViewController.class);

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Capability persistence manager.
     */
    private transient CapabilityController capabilityManager;

    /**
     * Node persistence manager.
     */
    private transient NodeController nodeManager;

    /**
     * Link persistence manager.
     */
    private transient LinkController linkManager;

    /**
     * Sets capability persistence manager.
     *
     * @param capabilityManager capability persistence manager.
     */

    @Autowired
    public void setCapabilityManager(final CapabilityController capabilityManager) {
        this.capabilityManager = capabilityManager;
    }

    /**
     * Sets testbed persistence manager.
     *
     * @param testbedManager testbed persistence manager.
     */
    @Autowired
    public void setTestbedManager(final TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }

    /**
     * Sets node persistence manager.
     *
     * @param nodeManager node persistence manager.
     */
    @Autowired
    public void setNodeManager(final NodeController nodeManager) {
        this.nodeManager = nodeManager;
    }

    /**
     * Sets link persistence manager.
     *
     * @param linkManager link persistence manager.
     */
    @Autowired
    public void setLinkManager(final LinkController linkManager) {
        this.linkManager = linkManager;
    }

    /**
     * Handle Request and return the appropriate response.
     * System.out.println(req.getRemoteUser());
     *
     * @return response http servlet response.
     * @throws InvalidTestbedIdException an InvalidTestbedIdException exception.
     * @throws TestbedNotFoundException  an TestbedNotFoundException exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listCapabilities(@PathVariable("testbedId") int testbedId) throws InvalidTestbedIdException, TestbedNotFoundException {

        final long start = System.currentTimeMillis();

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }
        // get testbed's capabilities
        final List<Capability> capabilities = capabilityManager.list(testbed.getSetup());


        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        refData.put("testbed", testbed);

        refData.put("capabilities", capabilities);

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));

        return new ModelAndView("capability/list.html", refData);
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @return http servlet response
     * @throws InvalidTestbedIdException   InvalidTestbedIdException exception.
     * @throws TestbedNotFoundException    TestbedNotFoundException exception.
     * @throws CapabilityNotFoundException CapabilityNotFoundExcetion.
     */
    @Loggable
    @RequestMapping(value = "/{capabilityName}", method = RequestMethod.GET)
    public ModelAndView listCapabilities(@PathVariable("testbedId") int testbedId, @PathVariable("capabilityName") String capabilityName) throws TestbedNotFoundException, CapabilityNotFoundException {

        final long start = System.currentTimeMillis();

        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // look up capability
        final Capability capability = capabilityManager.getByID(capabilityName);
        if (capability == null) {
            // if no capability is found throw exception
            throw new CapabilityNotFoundException("Cannot find capability [" + capabilityName + "].");
        }

        // get testbed nodes only
        final List<Node> nodes = nodeManager.list(testbed.getSetup(), capability);
        final List<Link> links = linkManager.list(testbed.getSetup(), capability);

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        refData.put("testbed", testbed);
        refData.put("capability", capability);

        refData.put("nodes", nodes);
        refData.put("links", links);

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));

        return new ModelAndView("capability/show.html", refData);
    }


    /**
     * Handle Request and return the appropriate response.
     *
     * @return http servlet response
     * @throws InvalidTestbedIdException   InvalidTestbedIdException exception.
     * @throws TestbedNotFoundException    TestbedNotFoundException exception.
     * @throws CapabilityNotFoundException CapabilityNotFoundExcetion.
     */
    @Loggable
    @RequestMapping(value = "/{capabilityName}/live", method = RequestMethod.GET)
    public ModelAndView liveCapabilityReadings(@PathVariable("testbedId") int testbedId, @PathVariable("capabilityName") String capabilityName, HttpServletRequest request)
            throws TestbedNotFoundException, CapabilityNotFoundException {
        final long start = System.currentTimeMillis();


        String hostname = request.getRequestURL().substring(0, request.getRequestURL().indexOf("/rest"));
        hostname = hostname.replace("http://", "");


        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // look up capability
        final Capability capability = capabilityManager.getByID(capabilityName);
        if (capability == null) {
            // if no capability is found throw exception
            throw new CapabilityNotFoundException("Cannot find capability [" + capabilityName + "].");
        }

        // Prepare data to pass to jsp
        final Map<String, Object> refData = new HashMap<String, Object>();

        // else put thisNode instance in refData and return index view
        refData.put("testbedId", testbedId);
        refData.put("host", hostname);
        refData.put("testbedUrnPrefix", testbed.getUrnPrefix());
        refData.put("capability", capabilityName);
        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
        // check type of view requested
        return new ModelAndView("capability/live.html", refData);
    }
}
