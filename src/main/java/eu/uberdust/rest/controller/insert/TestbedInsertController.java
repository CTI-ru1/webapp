package eu.uberdust.rest.controller.insert;

import eu.uberdust.caching.Loggable;
import eu.uberdust.command.TestbedInsertCommand;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.SetupController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Origin;
import eu.wisebed.wisedb.model.Setup;
import eu.wisebed.wisedb.model.Testbed;
import eu.wisebed.wisedb.model.TimeInfo;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractRestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.TimeZone;

/**
 * Controller class for inserting readings for a node capability pair.
 */
public final class TestbedInsertController extends AbstractRestController {

    /**
     * Testbed persistence manager.
     */
    private transient TestbedController testbedManager;
    /**
     * Testbed persistence manager.
     */
    private transient SetupController setupManager;

    /**
     * Looger.
     */
    private static final Logger LOGGER = Logger.getLogger(TestbedInsertController.class);

    /**
     * Constructor.
     */
    public TestbedInsertController() {
        super();

        // Make sure to set which method this controller will support.
        this.setSupportedMethods(new String[]{METHOD_PUT});
    }


    /**
     * Sets Testbed persistence manager.
     *
     * @param testbedManager Testbed persistence manager.
     */
    public void setTestbedManager(final TestbedController testbedManager) {
        this.testbedManager = testbedManager;
    }

    public void setSetupManager(final SetupController setupManager) {
        this.setupManager = setupManager;
    }

    /**
     * Handle Request and return the appropriate response.
     *
     * @param request    http servlet request.
     * @param response   http servlet response.
     * @param commandObj command object.
     * @param errors     BindException exception.
     * @return response http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                             invalid testbed id exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                             testbed not found exception.
     * @throws java.io.IOException IO exception.
     */
    @Loggable
    protected ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object commandObj, final BindException errors)
            throws InvalidTestbedIdException, TestbedNotFoundException, IOException {

        // set commandNode object
        final TestbedInsertCommand command = (TestbedInsertCommand) commandObj;
        try {
            Testbed testbed = new Testbed();
            testbed.setName(command.getName());
            testbed.setDescription(command.getDescription());
            testbed.setFederated(command.getDescription().equals("Yes"));
            testbed.setUrl(command.getUrl());
            testbed.setTimeZone(TimeZone.getTimeZone(command.getZone()));

            testbed.setUrnPrefix(command.getPrefix());
            // set the testbed of the setup to be imported
            Setup setup = new Setup();

            testbed.setSetup(setup);


            Origin origin = new Origin();
            origin.setPhi(command.getPhi());
            origin.setTheta(command.getTheta());
            origin.setX(command.getX());
            origin.setY(command.getY());
            origin.setZ(command.getZ());
            setup.setOrigin(origin);
            setup.setTimeinfo(new TimeInfo());
            setup.setCoordinateType(command.getCoordinate());
            setup.setDescription(command.getDescription());

            testbedManager.add(testbed);

            setup.setTestbed(testbed);

            setupManager.add(setup);

        } catch (Exception e) {
            LOGGER.error(e, e);
        }

        LOGGER.info(command);

        return null;
    }
}
