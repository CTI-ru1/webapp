<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="node" scope="request" class="eu.wisebed.wisedb.model.Node"/>
<jsp:useBean id="nodeCapabilities" scope="request" class="java.util.ArrayList"/>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - Show Node : <c:out value="${node.name}"/></title>
    <%@include file="/head.jsp" %>
    <script type="text/javascript" src="/js/qrcode.js"></script>
</head>
<body>
<%@include file="/header.jsp" %>

<div class="container">
    <table class="table">
        <thead>
        <tr>
            <th>Name</th>
            <th>
                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/"/>">
                    <c:out value="${node.name}"/>
                </a>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>GeoRSS Feed</td>
            <td>
                <a href="http://maps.google.com/maps?q=<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/georss"/>"
                   alt="georss">
                    <img src="<c:url value="/img/google-map-logo.gif"/>">
                </a>
            </td>
        </tr>

        <tr>
            <td>KML Feed</td>
            <td>
                <a href="http://maps.google.com/maps?q=<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/kml"/>"
                   alt="kml">
                    <img src="<c:url value="/img/google-map-logo.gif"/>">
                </a>
            </td>
        </tr>
        <tr>
            <td>Rdf description</td>
            <td>
                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/rdf/rdf+xml/"/>" alt="rdf">
                    <img src="<c:url value="/img/rdf.png"/>">
                </a>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <table>
                    <tbody>
                    <c:forEach items="${nodeCapabilities}" var="capability">
                        <tr>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}"/>">${capability.capability.name}</a>
                            </td>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/html/limit/10"/>">HTML</a>
                            </td>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/tabdelimited/limit/10"/>">Tab
                                    Delimited</a>
                            </td>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/json/limit/10"/>">JSON</a>
                            </td>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/wiseml/limit/10"/>">WiseML</a>
                            </td>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/latestreading"/>">Last
                                    Reading Tab</a>
                            </td>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/latestreading/json"/>">Last
                                    Reading JSON</a>
                            </td>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/rdf/rdf+xml/limit/1"/>">Last
                                    Reading RDF_XML</a>
                            </td>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/chart/limit/10"/>">Chart</a>
                            </td>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/live"/>">Live</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </td>
        </tr>
        </tbody>
    </table>

    <div id="qrdiv" class="qrdiv">
        <img id="qr" src="" alt="QRCode" width="150px"/>
        <script type="text/javascript">
            document.getElementById("qr").src = "http://qrfree.kaywa.com/?s=8&d=" + encodeURIComponent(window.location + "rdf/rdf+xml/");
        </script>
    </div>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
