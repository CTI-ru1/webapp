package eu.uberdust.rest.controller.rdf;

import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Testbed;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * Controller class that returns the setup of a testbed in GeoRSS format.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/rdf/{rdfEncoding}")
public final class ShowTestbedRdfController extends UberdustSpringController{

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ShowTestbedRdfController.class);

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
    @Loggable
    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> handle(@PathVariable("testbedId") int testbedId, @PathVariable("rdfEncoding") String rdfEncoding)
            throws TestbedNotFoundException, InvalidTestbedIdException, IOException{
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
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
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/rdf+xml; charset=UTF-8");
        return new ResponseEntity<String>(retVal, responseHeaders, HttpStatus.OK);
    }
}
