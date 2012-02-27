package eu.uberdust.rest.controller.tab;

import eu.uberdust.formatter.TextFormatter;
import eu.uberdust.formatter.exception.NotImplementedException;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractRestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Controller class that returns a list of testbed in Raw text format.
 */
public final class ListTestbedsController extends AbstractRestController {

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ListTestbedsController.class);

    /**
     * Constructor.
     */
    public ListTestbedsController() {
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

    /**
     * Handle Request and return the appropriate response.
     *
     * @param request    http servlet request.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     BindException exception.
     * @return response http servlet response.
     * @throws IOException IO exception.
     */
    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors) throws IOException {

        LOGGER.info("listTestbedsController(...)");


        // testbed list
        final List<Testbed> testbeds = testbedManager.list();


        // write on the HTTP response
        response.setContentType("text/plain");
        final Writer textOutput = (response.getWriter());

        try {
            textOutput.append(TextFormatter.getInstance().formatTestbeds(testbeds));
        } catch (NotImplementedException e) {
            LOGGER.error(e);
        }

        // flush close output
        textOutput.flush();
        textOutput.close();

        return null;
    }


}
