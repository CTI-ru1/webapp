package eu.uberdust.rest.controller.insert;

import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.model.Origin;
import eu.wisebed.wisedb.model.Setup;
import eu.wisebed.wisedb.model.Testbed;
import eu.wisebed.wisedb.model.TimeInfo;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.TimeZone;

/**
 * Controller class for inserting readings for a node capability pair.
 */
@Controller
@RequestMapping("/testbed/insert/name/{testbedName}/prefix/{prefix}/federated/{federated}/timezone/{timezone}/description/{description}/url/{url}/x/{x}/y/{y}/z/{z}/phi/{phi}/theta/{theta}/ctype/{ctype}/")
public final class InsertTestbedViewController extends UberdustSpringController {

    /**
     * Looger.
     */
    private static final Logger LOGGER = Logger.getLogger(InsertTestbedViewController.class);

    /**
     * Handle Request and return the appropriate response.
     *
     * @return response http servlet response.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                             invalid testbed id exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                             testbed not found exception.
     * @throws java.io.IOException IO exception.
     */
    @Loggable
    @RequestMapping(method = RequestMethod.PUT)
    //@RequestMapping("///theta/{theta}/ctype/{ctype}/")
    public ResponseEntity<String> handle(
            @PathVariable("testbedName") String testbedName,
            @PathVariable("prefix") String prefix,
            @PathVariable("federated") Boolean federated,
            @PathVariable("timezone") String timezone,
            @PathVariable("description") String description,
            @PathVariable("url") String url,
            @PathVariable("x") float x,
            @PathVariable("y") float y,
            @PathVariable("z") float z,
            @PathVariable("phi") float phi,
            @PathVariable("theta") float theta,
            @PathVariable("ctype") String ctype
    )
            throws InvalidTestbedIdException, TestbedNotFoundException, IOException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Testbed testbed = new Testbed();
        testbed.setName(testbedName);
        testbed.setDescription(description);
        testbed.setTimeZone(TimeZone.getTimeZone(timezone));

        testbed.setUrnPrefix(prefix);
        // set the testbed of the setup to be imported
        Setup setup = new Setup();

        testbed.setSetup(setup);


        Origin origin = new Origin();
        origin.setPhi(phi);
        origin.setTheta(theta);
        origin.setX(x);
        origin.setY(y);
        origin.setZ(z);
        setup.setOrigin(origin);
        setup.setTimeinfo(new TimeInfo());
        setup.setCoordinateType(ctype);
        setup.setDescription(description);

        testbedManager.add(testbed);

        setup.setTestbed(testbed);

        setupManager.add(setup);


        LOGGER.info(testbed);

        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<String>(
                "Inserted " + testbed.toString() + " and " + setup.toString() + ". OK"
                , responseHeaders, HttpStatus.OK);
    }
}
