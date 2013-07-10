package eu.uberdust.rest.controller.html.testbed;

import eu.uberdust.caching.Cachable;
import eu.uberdust.caching.Loggable;
import eu.uberdust.formatter.HtmlFormatter;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.Origin;
import eu.wisebed.wisedb.model.Position;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractRestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that returns a list of testbed in HTML format.
 */
public final class ListController extends AbstractRestController {

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;
    private transient NodeController nodeManager;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ListController.class);

    /**
     * Constructor.
     */
    public ListController() {
        super();

        // Make sure to set which method this controller will support.
        this.setSupportedMethods(new String[]{METHOD_GET});
    }

    /**
     * Sets testbed persistence manager.
     *
     * @param testbedManager testbed persistence manager.
     */
    public void setTestbedManager(final TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }

    public void setNodeManager(final NodeController nodeManager) {
        this.nodeManager = nodeManager;
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @param req        http servlet req.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     BindException exception.
     * @return response http servlet response.
     */
    @Loggable
    protected ModelAndView handle(final HttpServletRequest req, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors) {
        try {

            HtmlFormatter.getInstance().setBaseUrl(req.getRequestURL().substring(0, req.getRequestURL().indexOf("/rest")));

            final long start = System.currentTimeMillis();

            // Prepare data to pass to jsp
            final Map<String, Object> refData = new HashMap<String, Object>();


            // testbed list
            final List<Testbed> testbeds = testbedManager.list();
            if (testbeds.size() == 0) {
                return new ModelAndView("testbed/add.html", refData);
            }

            Map<Integer, Origin> origins = new HashMap<Integer, Origin>();
            List<Position> nodePositions = new ArrayList<Position>();
            for (Testbed testbed : testbeds) {
                origins.put(testbed.getId(), testbed.getSetup().getOrigin());
                Map<String, Position> testbedNodePositions = getNodePositions(testbed);
                nodePositions.addAll(testbedNodePositions.values());
            }


            final Map<String, Long> nodesCount = testbedManager.countNodes();
            final Map<String, Long> linksCount = testbedManager.countLinks();

            refData.put("testbeds", testbeds);
            refData.put("nodes", nodesCount);
            refData.put("links", linksCount);
            refData.put("origins", origins);
            refData.put("nodePositions", nodePositions);

            refData.put("time", String.valueOf((System.currentTimeMillis() - start)));
            return new ModelAndView("testbed/list.html", refData);
        } catch (Exception e) {
            LOGGER.error("e", e);
        }
        return null;
    }

    @Cachable
    private Map<String, Position> getNodePositions(Testbed testbed) {
        final List<Node> nodes = nodeManager.list(testbed.getSetup());
        Map<String, Position> nodePositions = new HashMap<String, Position>();
        for (Node node : nodes) {
            Position nodePosition = nodeManager.getAbsolutePosition(node);
            nodePositions.put(node.getName(), nodePosition);
        }
        return nodePositions;
    }
}
