package eu.uberdust.rest.controller.rdf;

import com.sun.syndication.io.FeedException;
import eu.uberdust.caching.Loggable;
import eu.uberdust.command.TestbedCommand;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
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

/**
 * Controller class that returns the setup of a testbed in GeoRSS format.
 */
public final class ShowTestbedRdfController extends AbstractRestController {

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ShowTestbedRdfController.class);

    /**
     * Constructor.
     */
    public ShowTestbedRdfController() {
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
     * Handle request and return the appropriate response.
     *
     * @param request    http servlet request.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     a BindException exception.
     * @return http servlet response.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                             a TestbedNotFoundException exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                             a InvalidTestbedIdException exception.
     * @throws java.io.IOException a IOException exception.
     * @throws com.sun.syndication.io.FeedException
     *                             a FeedException exception.
     */
    @SuppressWarnings("unchecked")
    @Loggable
    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws TestbedNotFoundException, InvalidTestbedIdException, IOException, FeedException {

        // set command object
        final TestbedCommand command = (TestbedCommand) commandObj;

        // a specific testbed is requested by testbed Id
        int testbedId;
        try {
            testbedId = Integer.parseInt(command.getTestbedId());

        } catch (NumberFormatException nfe) {
            throw new InvalidTestbedIdException("Testbed IDs have number format.", nfe);
        }

        // look up testbed
        final Testbed testbed = testbedManager.getByID(Integer.parseInt(command.getTestbedId()));
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        String retVal = "";
//        try {
//            Model model = (Model) RdfFormatter.getInstance().formatTestbed(testbed);
//
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            if (command.getFormat().toLowerCase().equals("turtle")) {
//                response.setContentType("text/plain");
//                model.write(bos, "TURTLE");
//            } else if (command.getFormat().toLowerCase().equals("n-triple")) {
//                response.setContentType("text/plain");
//                model.write(bos, "N-TRIPLE");
//            } else {
//                response.setContentType("application/rdf+xml");
//                model.write(bos, "RDF/XML");
//            }
//
//            retVal = bos.toString();
//
//        } catch (NotImplementedException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }


        // set up feed and entries

        final Writer output = (response.getWriter());

        output.write(retVal);
        output.flush();
        output.close();

        return null;
    }
}
