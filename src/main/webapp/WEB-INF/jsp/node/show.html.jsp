<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="setup" scope="request" class="eu.wisebed.wisedb.model.Setup"/>
<jsp:useBean id="node" scope="request" class="eu.wisebed.wisedb.model.Node"/>
<jsp:useBean id="nodePosition" scope="request" class="eu.wisebed.wisedb.model.Position"/>
<jsp:useBean id="nodeCapabilities" scope="request" class="java.util.ArrayList"/>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>ÜberDust - Show Node : <c:out value="${node.name}"/></title>
    <%@include file="/head.jsp" %>
    <script type="text/javascript" src="<c:url value="/js/qrcode.js"/>"></script>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
</head>
<body>
<%@include file="/header.jsp" %>
<script>
    var map;
    function initialize() {
        var myLatlng = new google.maps.LatLng(<c:out value="${nodePosition.x}"/>, <c:out value="${nodePosition.y}"/>);
        var mapOptions = {
            zoom: 13,
            center: new google.maps.LatLng(<c:out value="${setup.origin.x}"/>, <c:out value="${setup.origin.y}"/>),
            mapTypeId: google.maps.MapTypeId.HYBRID
        };

        map = new google.maps.Map(document.getElementById('map-canvas'),
                mapOptions);
        var marker = new google.maps.Marker({
            position: myLatlng,
            map: map,
            title: "<c:out value="${node.name}"/>"
        });
    }

    google.maps.event.addDomListener(window, 'load', initialize);
</script>


<div class="container">
    <div class="span12">
        <h3>
            <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/"/>">
                <c:out value="${node.name}"/>
            </a>
        </h3>
    </div>

    <div class="span4">
        <table class="table-hover">
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
                <td colspan=2>
                    <div id="qrdiv">
                        <img id="qr" src="" alt="QRCode" width="150px"/>
                        <script type="text/javascript">
                            document.getElementById("qr").src = "http://qrfree.kaywa.com/?s=8&d=" + encodeURIComponent(window.location + "rdf/rdf+xml/");
                        </script>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div class="span6" id="map-canvas">
        <!--- MAP GOES HERE-->
    </div>


    <table class="table table-hover">
        <thead>
        <tr>
            <th>Capability</th>
            <th> Last 10 Readings</th>
            <th> Last Reading</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${nodeCapabilities}" var="capability">
            <tr>
                <td>
                    <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${capability.capability.name}"/>">${capability.capability.name}</a>
                </td>
                <td>
                    <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/html/limit/10"/>">HTML</a>,
                    <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/tabdelimited/limit/10"/>">Tab
                        Delimited</a>,
                    <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/json/limit/10"/>">JSON</a>,
                    <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/wiseml/limit/10"/>">WiseML</a>

                </td>
                <td>
                    <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/latestreading"/>">Tab</a>,
                    <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/latestreading/json"/>">JSON</a>,
                    <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/capability/${capability.capability.name}/rdf/rdf+xml/limit/1"/>">RDF_XML</a>
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
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
