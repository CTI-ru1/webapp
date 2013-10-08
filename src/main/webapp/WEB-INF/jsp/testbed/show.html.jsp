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
<jsp:useBean id="nodePositions" scope="request" class="java.util.HashMap"/>
<jsp:useBean id="nodeTypes" scope="request" class="java.util.HashMap"/>


<html>
<head>
    <META NAME="Description" CONTENT="Überdust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Überdust - Show testbed : <c:out value="${testbed.name}"/></title>
    <%@include file="/head.jsp" %>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
</head>
<body>
<%@include file="/header.jsp" %>
<script>
    var map;
    function initialize() {
        var myLatlng = new google.maps.LatLng(<c:out value="${setup.origin.x}"/>, <c:out value="${setup.origin.y}"/>);
        var mapOptions = {zoom: 13, center: myLatlng, mapTypeId: google.maps.MapTypeId.HYBRID, disableDefaultUI: true};
        map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
        var bounds = new google.maps.LatLngBounds();

        var marker = new google.maps.Marker({position: myLatlng, map: map, title: "<c:out value="${testbed.name}"/>"});
        bounds.extend(marker.getPosition());

        var infoWindow = new google.maps.InfoWindow({
            content: "${testbed.description}",
            disableAutoPan: false
        });
        infoWindow.open(map, marker);


        <c:forEach items="${nodes}" var="node">
        <c:if test="${nodePositions[node.name].x!=0}">
        var iconLink = '<c:url value="/img/markers/${nodeTypes[node.name]}.png"/>';
        var myLatlng<c:out value="${node.id}"/> = new google.maps.LatLng(<c:out value="${nodePositions[node.name].x}"/>, <c:out value="${nodePositions[node.name].y}"/>);
        var marker<c:out value="${node.id}"/> = new google.maps.Marker({position: myLatlng<c:out value="${node.id}"/>, map: map, title: "<c:out value="${node.name}"/>", icon: iconLink});
        bounds.extend(marker<c:out value="${node.id}"/>.getPosition());
        </c:if>
        </c:forEach>
        map.fitBounds(bounds);
    }
    google.maps.event.addDomListener(window, 'load', initialize);
</script>

<c:choose>
    <c:when test="${admin}">
        <script type="text/javascript">
            function updateTestbedInfo() {
                $.post("<c:url value="/rest/testbed/${testbed.id}/name"/>", encodeURIComponent($("#aboutName").val()), function () {
                    window.location.reload();
                })
            }
        </script>
    </c:when>
</c:choose>
<div class="container">
<div class="col-md-12">
    <h2>
        You are looking at: <a href="<c:url value="/rest/testbed/${testbed.id}"/>"><c:out
            value="${testbed.name}"/></a>
    </h2>
</div>

<div class="col-md-12" style="height:450px">
    <div class="col-lg-4">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4 class="panel-title">
                    <a class="accordion-toggle" data-parent="#accordion2" id="clickable1">
                        About:Testbed
                    </a>
                    <c:choose>
                        <c:when test="${admin}">
                            <button type="submit" class="btn btn-xs btn-primary pull-right"
                                    onclick="updateTestbedInfo()">Save Changes
                            </button>
                        </c:when>
                    </c:choose>

                </h4>
            </div>

            <div id="collapse1" class="panel-collapse in" style="height: auto;">
                <div class="panel-body">
                    <form role="form">
                        <div class="form-group">
                            <label class="col-lg-2 control-label">ID</label>
                            <label class="col-lg-10 control-label"><c:out value="${testbed.id}"/></label>
                        </div>
                        <div class="form-group">
                            <label for="aboutName" class="col-lg-6 control-label">Name</label>
                            <input type="text" class="col-lg-6 form-control" id="aboutName"
                            <c:choose>
                            <c:when test="${not admin}">
                                   disabled
                            </c:when>
                            </c:choose>
                                   value="<c:out value="${testbed.name}"/>">
                        </div>
                        <div class="form-group">
                            <label for="aboutNodePrefix" class="col-lg-6 control-label">Node prefix</label>
                            <input disabled type="text" class="col-lg-6 form-control" id="aboutNodePrefix"
                                   value="<c:out value="${testbed.urnPrefix}"/>">
                        </div>
                        <div class="form-group">
                            <label for="aboutCapabilityPrefix" class="col-lg-6 control-label">Capability prefix</label>
                            <input disabled type="text" class="col-lg-6 form-control" id="aboutCapabilityPrefix"
                                   value="<c:out value="${testbed.urnCapabilityPrefix}"/>">
                        </div>
                        <div class="form-group">
                            <label for="aboutLong" class="col-lg-6 control-label">Long</label>
                            <label for="aboutLat" class="col-lg-6 control-label">Lat</label>
                            <input type="text" class="col-lg-6 form-control" id="aboutLong"
                            <c:choose>
                            <c:when test="${not admin}">
                                   disabled
                            </c:when>
                            </c:choose>
                                   value="<c:out value="${testbed.setup.origin.x}"/>" style="width: 50%">
                            <input type="text" class="col-lg-6 form-control" id="aboutLat"
                            <c:choose>
                            <c:when test="${not admin}">
                                   disabled
                            </c:when>
                            </c:choose>
                                   value="<c:out value="${testbed.setup.origin.y}"/>" style="width: 50%">
                        </div>
                        <div class="form-group">
                            <label class="col-lg-12 control-label">
                                <a href="<c:url value="/rest/testbed/${testbed.id}/georss"/>">GeoRSS</a>,
                                <a href="<c:url value="/rest/testbed/${testbed.id}/geojson"/>">GeoJSON</a>,
                                <a href="<c:url value="/rest/testbed/${testbed.id}/wiseml"/>">WiseML,</a>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/kml"/>">KML</a>
                            </label>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-8" id="map-canvas" style="height: 500px;">

    </div>
</div>
<br/>
<br/>

<div class="col-md-12">
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

</div>
<%@include file="/footer.jsp" %>
</body>
</html>
