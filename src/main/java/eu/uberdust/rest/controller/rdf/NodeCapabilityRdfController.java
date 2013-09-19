package eu.uberdust.rest.controller.rdf;

import com.sun.syndication.io.FeedException;
import eu.uberdust.caching.Loggable;
import eu.uberdust.rest.controller.UberdustSpringController;
import eu.uberdust.rest.exception.InvalidTestbedIdException;
import eu.uberdust.rest.exception.NodeNotFoundException;
import eu.uberdust.rest.exception.TestbedNotFoundException;
import eu.wisebed.wisedb.controller.CapabilityController;
import eu.wisebed.wisedb.controller.NodeController;
import eu.wisebed.wisedb.controller.NodeReadingController;
import eu.wisebed.wisedb.controller.TestbedController;
import eu.wisebed.wisedb.model.Capability;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.NodeReading;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * Controller class that returns the position of a node in GeoRSS format.
 */
@Controller
@RequestMapping("/testbed/{testbedId}/node/{nodeName}/capability/{capabilityName}/rdf/{rdfEncoding}/limit/{limit}")
public final class NodeCapabilityRdfController extends UberdustSpringController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NodeCapabilityRdfController.class);

    /**
     * Handle request and return the appropriate response.
     *
     * @return http servlet response.
     * @throws java.io.IOException an IOException exception.
     * @throws com.sun.syndication.io.FeedException
     *                             a FeedException exception.
     * @throws eu.uberdust.rest.exception.NodeNotFoundException
     *                             NodeNotFoundException exception.
     * @throws eu.uberdust.rest.exception.TestbedNotFoundException
     *                             TestbedNotFoundException exception.
     * @throws eu.uberdust.rest.exception.InvalidTestbedIdException
     *                             InvalidTestbedIdException exception.
     */
    @Loggable
    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> handle(@PathVariable("testbedId") int testbedId, @PathVariable("nodeName") String nodeName, @PathVariable("capabilityName") String capabilityName, @PathVariable("rdfEncoding") String rdfEncoding, @PathVariable("limit") int limit, HttpServletRequest request)
            throws IOException, FeedException, NodeNotFoundException, TestbedNotFoundException,
            InvalidTestbedIdException {
        final long start = System.currentTimeMillis();
        initialize(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // look up testbed
        final Testbed testbed = testbedManager.getByID(testbedId);
        if (testbed == null) {
            // if no testbed is found throw exception
            throw new TestbedNotFoundException("Cannot find testbed [" + testbedId + "].");
        }

        // look up node
        final Node node = nodeManager.getByName(nodeName);
        if (node == null) {
            // if no node is found throw exception
            throw new NodeNotFoundException("Cannot find testbed [" + nodeName + "].");
        }

        final Capability capability = capabilityManager.getByID(capabilityName);
//        final Capability capabilityRoom = capabilityManager.getByID("room");

        StringBuilder rdfDescription = new StringBuilder("");

        rdfDescription.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                .append("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n")
                .append("  xmlns:ns0=\"http://www.w3.org/2000/01/rdf-schema#\"\n")
                .append("  xmlns:ns1=\"http://purl.oclc.org/NET/ssnx/ssn#\"\n")
                .append("  xmlns:ns2=\"http://spitfire-project.eu/cc/spitfireCC_n3.owl#\"\n")
                .append("  xmlns:ns3=\"http://www.loa-cnr.it/ontologies/DUL.owl#\"\n")
                .append("  xmlns:ns4=\"http://purl.org/dc/terms/\">\n").append("\n");

        String localname = request.getRequestURL().substring(0, request.getRequestURL().indexOf("/rest/"));

        List<NodeReading> readings = nodeReadingManager.listNodeReadings(node, capability, limit);
        for (NodeReading reading : readings) {
            rdfDescription.append("  <rdf:Description rdf:about=\"" + localname + "/rest/testbed/" + node.getSetup().getId() + "/node/" + reading.getCapability().getNode().getName() + "/rdf/rdf+xml/\">\n")
                    .append("    <ns0:type rdf:resource=\"http://purl.oclc.org/NET/ssnx/ssn#Sensor\"/>\n")
                    .append("    <ns1:observedProperty rdf:resource=\"" + localname + "/rest/testbed/" + node.getSetup().getId() + "/capability/" + reading.getCapability().getCapability().getName() + "/rdf/rdf+xml/\"/>\n");
            if ((reading.getStringReading() != null) && !"".equals(reading.getStringReading())) {
                rdfDescription.append("    <ns3:hasValue>" + reading.getStringReading() + "</ns3:hasValue>\n");
            } else {
                rdfDescription.append("    <ns3:hasValue>" + reading.getReading() + "</ns3:hasValue>\n");
            }
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yy-MM-dd'T'HH:mm:ss'Z'");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            rdfDescription.append("    <ns4:date>" + dateFormatGmt.format(reading.getTimestamp()) + "</ns4:date>\n")
                    .append("  </rdf:Description>\n");
        }


//        List<NodeReading> roomReading = nodeReadingManager.listNodeReadings(node, capabilityRoom, 1);
//        readings.add(1, roomReading.get(0));

        rdfDescription.append("\n")
                .append("</rdf:RDF>");
        // current host base URL


//        try {
//            retVal = ((String) RdfFormatter.getInstance().formatNodeReadings(readings));
//        } catch (NotImplementedException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/rdf+xml; charset=UTF-8");
        return new ResponseEntity<String>(rdfDescription.toString(), responseHeaders, HttpStatus.OK);
    }
}
