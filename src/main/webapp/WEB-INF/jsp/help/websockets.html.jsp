<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
<head>
    <META NAME="Description" CONTENT="Überdust"/>
    <META http-equiv="Content-Language" content="en"/>
    <title>Überdust Man Page</title>
    <%@include file="/head.jsp" %>
</head>

<jsp:useBean id="text" scope="request" class="java.lang.String"/>

<body>
<%@include file="/header.jsp" %>

<div class="container">
    <h1>Überdust Websockets Man Page</h1>

    <p>
        To receive continous flow of node readings you should use th WebSockets protocol.
        The easiest way to do so is throught the JavaScript API using the
        <a href="<c:url value="/js/ws/uberdustws.js"/>"> JavaScript Library </a>.
    </p>

    <p>
        You can check out two live example pages using the WebSocket API <a href=""> here </a> and <a href=""> here</a>.
    </p>

    <p>
        Specifically you can access live readings in the following formats:
    <ul>
        <li> per NodeCapability (e.g., for a specific capability of a node)
        <li> per Node (e.g., for all capabilities of a specific node)
        <li> per Capability (e.g., for all nodes of a specific capability)
        <li> per Capability per Testbed (e.g., for all nodes of a specific capability in a specific Testbed)
        <li> per Testbed (e.g., for all nodes and capabilities of a specific Testbed)
    </ul>

    </p>

    <p>
        In more detail :
    <ul>
        <li>
            For a specific node you need to provide the nodeName and the capabilityName you want to observe:
<pre>
 connect("uberdust.cti.gr", "urn:wisebed:ctitestbed:0x9979", "urn:wisebed:node:capability:pir", 
	function message(value2add) {
            alert(reading);
        }
  );
</pre>
        </li>
        <li>
            For all the capabilities of a specific node you need to provide the nodeName and "*" as the capabilityName :
<pre>
 connect("uberdust.cti.gr", "urn:wisebed:ctitestbed:0x9979", "*", 
	function message(value2add) {
            alert(reading);
        }
  );
</pre>
        </li>
        <li>
            For all nodes of a specific capability you need to provide the capabilityName and "*" as the nodeName :
<pre>
 connect("uberdust.cti.gr", "*", "urn:wisebed:node:capability:pir", 
	function message(value2add) {
            alert(reading);
        }
  );
</pre>
        </li>
        <li>
            For all nodes of a specific capability in a specific testbed you need to provide the capabilityName and
            "urnPrefix*" as the nodeName,
            where urnPrefix is the prefix defined for the testbed you want to observe:
<pre>
 connect("uberdust.cti.gr", "urn:wisebed:ctitestbed:*", "urn:wisebed:node:capability:pir", 
	function message(value2add) {
            alert(reading);
        }
  );
</pre>
        </li>
        <li>
            For all nodes and capabilities in a specific testbed you need to provide the "urnCapabilityPrefix*" as
            capabilityName and "urnPrefix*" as the nodeName,
            where urnPrefix and urnCapabilityPrefix are the prefixes defined for the testbed you want to observe:
<pre>
 connect("uberdust.cti.gr", "urn:wisebed:ctitestbed:*", "urn:wisebed:node:capability:*", 
	function message(value2add) {
            alert(reading);
        }
  );
</pre>
        </li>
    </ul>
    </p>


    </pre>
</div>

<%@include file="/footer.jsp" %>
</body>
</html>
