package eu.uberdust.util;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import eu.wisebed.wisedb.controller.LastNodeReadingController;
import eu.wisebed.wisedb.controller.NodeCapabilityController;
import eu.wisebed.wisedb.model.Node;
import eu.wisebed.wisedb.model.NodeCapability;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: amaxilatis
 * Date: 1/24/12
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class RdfConverter {
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(RdfConverter.class);

    private LastNodeReadingController lastNodeReadingManager;

    private NodeCapabilityController nodeCapabilityManager;

    @Autowired
    public void setNodeCapabilityManager(final NodeCapabilityController nodeCapabilityManager) {
        this.nodeCapabilityManager = nodeCapabilityManager;
    }

    @Autowired
    public void setLastNodeReadingManager(final LastNodeReadingController lastNodeReadingManager) {
        this.lastNodeReadingManager = lastNodeReadingManager;
    }

//    private Map<String, Map<String, Double>> sensorValues;

    private Map rdfMapping;

    public RdfConverter() {
        rdfMapping = new HashMap<String, String>();
        rdfMapping.put("urn:wisebed:node:capability:light", "luminance");
        rdfMapping.put("urn:wisebed:node:capability:temperature", "Temperature");
        rdfMapping.put("urn:wisebed:node:capability:pir", "pir");
        rdfMapping.put("urn:wisebed:node:capability:humidity", "relativeHumidity");
        rdfMapping.put("urn:wisebed:node:capability:light1", "Lamp1");
        rdfMapping.put("urn:wisebed:node:capability:light2", "Lamp2");
        rdfMapping.put("urn:wisebed:node:capability:light3", "Lamp3");
        rdfMapping.put("urn:wisebed:node:capability:light4", "Lamp4");
        rdfMapping.put("urn:wisebed:node:capability:co", "carbonMonoxide");
        rdfMapping.put("urn:wisebed:node:capability:co2", "catbonDioxide");
        rdfMapping.put("urn:wisebed:node:capability:ch4", "methane");
        rdfMapping.put("urn:wisebed:node:capability:barometricpressure", "barometricPressure");
        rdfMapping.put("urn:wisebed:node:capability:ir", "infraredLuminosity");
        rdfMapping.put("urn:wisebed:node:capability:batterycharge", "batteryCharge");

    }

    public String map(final String key) {
        return (String) rdfMapping.get(key);
    }

    public String getRdf(final Node node, final String uri) {

        final StringBuilder rdfOutput = new StringBuilder();
        //headers
        rdfOutput.append("@prefix ssn: <http://purl.oclc.org/NET/ssnx/ssn#> .\n"
                + "@prefix dul: <http://www.loa-cnr.it/ontologies/DUL.owl#> .\n"
                + "@prefix dc: <http://purl.org/dc/terms/> . \n"
                + "@prefix spitfire: <http://spitfire-project.eu/cc/spitfireCC_n3.owl#> . \n"
                + "\n"
                + "<#>\n"
                + "\n");
//        rdfOutput.append("@prefix dul: <http://www.loa-cnr.it/ontologies/DUL.owl#> .\n" +
//                "@prefix ssn: <http://purl.oclc.org/NET/ssnx/ssn#> .\n" +
//                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
//                "@prefix sweet: <http://sweet.jpl.nasa.gov/2.2/sweetAll.owl#> .\n" +
//                "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
//                "@prefix : <http://spitfire.ibr.cs.tu-bs.de/static/ontology.owl#> .\n" +
//                "\n" +
//                "<#>\n" +
//                "\n");

        final Model model = ModelFactory.createDefaultModel();
//        rdfOutput.append("@prefix ").append("\n");
//        rdfOutput.append("@prefix ns0: <http://purl.org/rss/1.0/modules/dcterms/>.").append("\n");
//        rdfOutput.append("@prefix ns1: <http://xmlns.com/foaf/0.1/>.").append("\n");
//        rdfOutput.append("@prefix ns2: <http://spitfire.ibr.cs.tu-bs.de/static/descriptions#>.").append("\n");
//        rdfOutput.append("@prefix dul: <http://www.loa-cnr.it/ontologies/DUL.owl#>.").append("\n");
//        rdfOutput.append("@prefix xsd: <http://www.w3.org/2001/XMLSchema#>.").append("\n");
//        rdfOutput.append("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.").append("\n");
//        rdfOutput.append("@prefix ns4: <http://sweet.jpl.nasa.gov/2.2/reprSciUnits.owl#>.").append("\n");
//        rdfOutput.append("@prefix ns5: <http://purl.oclc.org/NET/ssnx/ssn#>.").append("\n");
//        rdfOutput.append("\n").append("<#>").append("\n").append("\n");

        //capabilities
        boolean first = true;
        for (final NodeCapability capability : (List<NodeCapability>) nodeCapabilityManager.list(node)) {
            if (!first) {
                rdfOutput.append(";\n");
            }
            first = false;
            rdfOutput.append(" ssn:attachedSystem [\n"
                    + "  a ssn:Sensor ;\n"
                    + "  ssn:observedProperty <" + capability.getCapability().getDescription()
                    + "> ; \n");
            rdfOutput.append("\t\t]");
        }
        rdfOutput.append(".");

        StringWriter output;
        try {
            final StringReader stringReader = new StringReader(rdfOutput.toString());

            model.read(stringReader, uri, "N3");
            output = new StringWriter();
            model.write(output);
        } catch (Exception e) {
            return rdfOutput.toString();
        }
        return output.toString();  //To change body of created methods use File | Settings | File Templates.
    }

    public String encode(final String inStr) {
        String input = inStr;
        input = input.replaceAll("_", "\\_U");
        input = inStr.replaceAll("/", "\\_S");
        input = inStr.replaceAll(":", "\\_C");
        return inStr;
    }

}
