package eu.uberdust.rest.controller.html.link;

import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.LinkNotFoundException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.LinkCapabilityController;
import eu.wisebed.wisedb.controller.LinkController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Link;
import eu.wisebed.wisedb.model.LinkCapability;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that returns the a web page for a node.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/link")
public final class LinkViewController extends UberdustSpringController{

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LinkViewController.class);

    /**
     * Link persistence manager.
     */
    private transient LinkController linkManager;

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    private transient LinkCapabilityController linkCapabilityManager;

    @Autowired
    public void setLinkCapabilityManager(final LinkCapabilityController linkCapabilityManager) {
        this.linkCapabilityManager = linkCapabilityManager;
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
     * Sets testbed persistence manager.
     *
     * @param testbedManager testbed persistence manager.
     */
    @Autowired
    public void setTestbedManager(final TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }


    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws InvalidTestbedIdException an InvalidTestbedIdException exception.
     * @throws TestbedNotFoundException  an TestbedNotFoundException exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listLinks(@PathVariable("testbedId") int testbedId) throws TestbedNotFoundException, InvalidTestbedIdException {

        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }
        final List<Link> links = linkManager.list(testbed.getSetup());

        // Prepare data to pass to jsp


        refData.put("testbed", testbed);

        refData.put("links", links);

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));

        return new ModelAndView("link/list.html", refData);
    }

    /**
     * Handle req and return the appropriate response.
     *
     * @return http servlet response.
     * @throws InvalidTestbedIdException InvalidTestbedIdException exception.
     * @throws TestbedNotFoundException  TestbedNotFoundException exception.
     * @throws LinkNotFoundException     LinkNotFoundException exception.
     */
    @Loggable
    @RequestMapping(value = "{sourceName}/{targetName}", method = RequestMethod.GET)
    public ModelAndView getLink(@PathVariable("testbedId") int testbedId, @PathVariable("sourceName") String sourceName, @PathVariable("targetName") String targetName) throws InvalidTestbedIdException, TestbedNotFoundException, LinkNotFoundException {


        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // a link instance  and it' inverse
        final Link link = linkManager.getByID(sourceName, targetName);
        final Link linkInv = linkManager.getByID(targetName, sourceName);
        final Map<Link, List<LinkCapability>> linkCapabilityMap = new HashMap<Link, List<LinkCapability>>();

        // if no link or inverse link found return error view
        if (link == null && linkInv == null) {
            throw new LinkNotFoundException("Cannot find link [" + sourceName + "," + targetName
                    + "] or the inverse link [" + targetName + "," + sourceName + "]");
        }
        List<LinkCapability> capabilities = linkCapabilityManager.list(link);

        // Prepare data to pass to jsp


        refData.put("testbed", testbed);
        refData.put("link", link);

        refData.put("capabilities", capabilities);

        refData.put("time", String.valueOf((System.currentTimeMillis() - start)));

        return new ModelAndView("link/show.html", refData);
    }
}
