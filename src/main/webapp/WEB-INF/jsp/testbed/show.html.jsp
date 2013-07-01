<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="nodes" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="virtual" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="links" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="capabilities" scope="request" class="java.util.ArrayList"/>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - Show testbed : <c:out value="${testbed.name}"/></title>
    <%@include file="/head.jsp" %>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
</head>
<body>
<%@include file="/header.jsp" %>
<script>
    var map;
    function initialize() {
        var myLatlng = new google.maps.LatLng(<c:out value="${setup.origin.x}"/>, <c:out value="${setup.origin.y}"/>);
        var mapOptions = {zoom: 13, center: myLatlng, mapTypeId: google.maps.MapTypeId.HYBRID};
        map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
        var marker = new google.maps.Marker({position: myLatlng, map: map, title: "<c:out value="${node.name}"/>"});
    }
    google.maps.event.addDomListener(window, 'load', initialize);
</script>

<div class="container">
<div class="span12">
    <h2>
        <a href="<c:url value="/rest/testbed/${testbed.id}"/>"><c:out value="${testbed.name}"/></a>
    </h2>
</div>


<div id="testbed_info" class="span6">

    <table class="table-hover">
        <tbody>
        <tr>
            <td>Testbed ID</td>
            <td><c:out value="${testbed.id}"/></td>
        </tr>
        <tr>
            <td colspan="2"><c:out value="${testbed.description}"/></td>
        </tr>
        <tr>
            <td>Testbed Name</td>
            <td><c:out value="${testbed.name}"/></td>
        </tr>
        <tr>
            <td>Testbed URN prefix</td>
            <td><c:out value="${testbed.urnPrefix}"/></td>
        </tr>
        <tr>
            <td>Testbed Timezone</td>
            <c:set var="testbedZone" value="<%= testbed.getTimeZone().getDisplayName() %>"/>
            <td><c:out value="${testbedZone}"/></td>
        </tr>
        <tr>
            <td>Testbed URL</td>
            <td><a href="<c:out value="${testbed.url}"/>"><c:out value="${testbed.url}"/></a></td>
        </tr>
        <tr>
            <td>Testbed SNAA URL</td>
            <td><a href="<c:out value="${testbed.snaaUrl}"/>"><c:out value="${testbed.snaaUrl}"/></a></td>
        </tr>
        <tr>
            <td>Testbed RS URL</td>
            <td><a href="<c:out value="${testbed.rsUrl}"/>"><c:out value="${testbed.rsUrl}"/></a></td>
        </tr>
        <tr>
            <td>Testbed Session Management URL</td>
            <td><a href="<c:out value="${testbed.sessionUrl}"/>"><c:out value="${testbed.sessionUrl}"/></a></td>
        </tr>
        <tr>
            <td>Federated Testbed</td>
            <td><c:out value="${testbed.federated}"/></td>
        </tr>
        <tr>
            <td>Testbed Status Page</td>
            <td>
                <a href="<c:url value="/rest/testbed/${testbed.id}/status"/>">Status
                    page</a></td>
        </tr>
        <tr>
            <td>Testbed GeoRSS feed</td>
            <td>
                <a href="<c:url value="/rest/testbed/${testbed.id}/georss"/>">GeoRSS feed</a>
                (<a href="http://maps.google.com/maps?q=<c:url value="${baseURL}/rest/testbed/${testbed.id}/georss"/>">View
                On Google
                Maps</a>)
            </td>
        </tr>
        <tr>
            <td>Testbed KML feed</td>
            <td>
                <a href="<c:url value="/rest/testbed/${testbed.id}/kml"/>">KML feed</a>
                (<a href="http://maps.google.com/maps?q=<c:url value="${baseURL}/rest/testbed/${testbed.id}/kml"/>">View
                On Google
                Maps</a>)
                <span style="color : red">not implemented yet</span>
            </td>

        </tr>
        <tr>
            <td>Testbed WiseML</td>
            <td>
                <a href="<c:url value="/rest/testbed/${testbed.id}/wiseml"/>">WiseML</a>
                <span style="color : red">not implemented yet</span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div class="span6" id="map-canvas" style="height: 400px;">

</div>

<ul class="nav nav-tabs">
    <li class="active"><a href="#nodestab" data-toggle="tab">Nodes</a></li>
    <li><a href="#virtualnodestab" data-toggle="tab">Virtual Nodes</a></li>
    <li><a href="#capabilitiestab" data-toggle="tab">Capabilities</a></li>
    <li><a href="#linkstab" data-toggle="tab">Links</a></li>
</ul>

<div class="tab-content">
    <div class="tab-pane active" id="nodestab">
        <table class="table-hover table table-condensed">
            <thead>
            <tr>
                <th>
                    <a href="<c:url value="/rest/testbed/${testbed.id}/node"/>">Nodes</a>
                    (
                    <a href="<c:url value="/rest/testbed/${testbed.id}/node/raw"/>">raw</a>,
                    <a href="<c:url value="/rest/testbed/${testbed.id}/node/json"/>">json</a>
                    )
                </th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${nodes == null || fn:length(nodes) == 0}">
                    <tr>
                        <td style="color : red">No nodes found for testbed <c:out value="${testbed.name}"/></td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${nodes}" var="node">
                        <tr>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/"/>"><c:out
                                        value="${node.name}"/></a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <div class="tab-pane" id="virtualnodestab">
        <table class="table-hover table table-condensed">
            <thead>
            <tr>
                <th>
                    <a href="<c:url value="/rest/testbed/${testbed.id}/virtualnode"/>">Virtual Nodes</a>
                    (
                    <a href="<c:url value="/rest/testbed/${testbed.id}/virtualnode/raw"/>">raw</a>,
                    <a href="<c:url value="/rest/testbed/${testbed.id}/virtualnode/json"/>">json</a>
                    )
                </th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${virtual == null || fn:length(virtual) == 0}">
                    <tr>
                        <td style="color : red">No virtual nodes found for testbed <c:out
                                value="${testbed.name}"/></td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${virtual}" var="node">
                        <tr>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/"/>"><c:out
                                        value="${node.name}"/></a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <div class="tab-pane" id="capabilitiestab">
        <table class="table-hover table table-condensed">
            <thead>
            <tr>
                <th>
                    <a href="<c:url value="/rest/testbed/${testbed.id}/capability"/>">Capabilities</a>
                    (
                    <a href="<c:url value="/rest/testbed/${testbed.id}/capability/raw"/>">raw</a>,
                    <a href="<c:url value="/rest/testbed/${testbed.id}/capability/json"/>">json</a>
                    )
                </th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${capabilities == null || fn:length(capabilities) == 0 }">
                    <tr>
                        <td style="color : red">No capabilities found for <c:out value="${testbed.name}"/></td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${capabilities}" var="capability">
                        <tr>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${capability.name}"/>"><c:out
                                        value="${capability.name}"/></a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <div class="tab-pane" id="linkstab">
        <table class="table-hover table table-condensed">
            <thead>
            <tr>
                <th>
                    <a href="<c:url value="/rest/testbed/${testbed.id}/link"/>">Links</a>
                    (
                    <a href="<c:url value="/rest/testbed/${testbed.id}/link/raw"/>">raw</a>,
                    <a href="<c:url value="/rest/testbed/${testbed.id}/link/json"/>">json</a>
                    )
                </th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${links == null || fn:length(links) == 0 }">
                    <tr>
                        <td style="color : red">No links found for <c:out value="${testbed.name}"/></td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${links}" var="link">
                        <tr>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/link/${link.source.name}/${link.target.name}"/>"><c:out
                                        value="${link.source.name},${link.target.name}"/></a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>

</div>
<%@include file="/footer.jsp" %>
</body>
</html>
