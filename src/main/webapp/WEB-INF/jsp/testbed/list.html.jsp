<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <title>ÜberDust - List Testbeds</title>
    <%@include file="/head.jsp" %>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
</head>

<jsp:useBean id="text" scope="request" class="java.lang.String"/>
<jsp:useBean id="testbeds" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="nodes" scope="request" class="java.util.HashMap"/>
<jsp:useBean id="links" scope="request" class="java.util.HashMap"/>
<jsp:useBean id="origins" scope="request" class="java.util.HashMap"/>
<jsp:useBean id="nodePositions" scope="request" class="java.util.ArrayList"/>

<body>
<%@include file="/header.jsp" %>
<script>
    var map;
    function initialize() {
        var myLatlng = new google.maps.LatLng(0, 0);
        var mapOptions = {zoom: 13, center: myLatlng, mapTypeId: google.maps.MapTypeId.HYBRID, disableDefaultUI: true};
        map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
        var bounds = new google.maps.LatLngBounds();

        <%--var marker = new google.maps.Marker({position: myLatlng, map: map, title: "<c:out value="${testbed.name}"/>"});--%>
        <%--bounds.extend(marker.getPosition());--%>
        <c:set var="count" value="0" scope="page" />
        var iconLink = '<c:url value="/img/markers/default.png"/>';
        <c:forEach items="${nodePositions}" var="entry" >
        <c:set var="count" value="${count+1}" />
        <c:out value="${count}" />
        <c:if test="${entry.x!=0}">
        var myLatlng<c:out value="${count}"/> = new google.maps.LatLng(<c:out value="${entry.x}"/>, <c:out value="${entry.y}"/>);
        var marker<c:out value="${count}"/> = new google.maps.Marker({position: myLatlng<c:out value="${count}"/>, map: map, icon: iconLink ,zIndex:0});
        bounds.extend(marker<c:out value="${count}"/>.getPosition());
        </c:if>
        </c:forEach>


        <c:forEach items="${testbeds}" var="testbed">
        <%--<c:if test="${nodePositions[node.name].x!=0}">--%>
        <%--var iconLink = '<c:url value="/img/markers/${nodeTypes[node.name]}.png"/>';--%>
        var myLatlng<c:out value="${testbed.id}"/> = new google.maps.LatLng(<c:out value="${origins[testbed.id].x}"/>, <c:out value="${origins[testbed.id].y}"/>);
        var marker<c:out value="${testbed.id}"/> = new google.maps.Marker({position: myLatlng<c:out value="${testbed.id}"/>, map: map, title: "<c:out value="${testbed.name}"/>"});
        bounds.extend(marker<c:out value="${testbed.id}"/>.getPosition());
        <%--</c:if>--%>
        </c:forEach>


        map.fitBounds(bounds);
    }
    google.maps.event.addDomListener(window, 'load', initialize);
</script>


<div class="container">
    <h1>Welcome to Überdust</h1>

    <div id="map-canvas" style="height: 100%;" class="span8">
    </div>
    <div class="span4">
        <%--<div class="accordion" id="accordion2">--%>
        <c:forEach items="${testbeds}" var="testbed">
            <%--<div class="accordion-group">--%>
            <div class="accordion-heading">
                <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2"
                   href="#collapse<c:out value="${testbed.id}"/>">
                    <img src="<c:url value="/img/wsn.jpg"/>">
                    <c:out value="${testbed.name}"/>
                </a>
            </div>
            <div id="collapse<c:out value="${testbed.id}"/>" class="accordion-body collapse in">
                <div class="accordion-inner">
                    <a href="<c:url value="/rest/testbed/${testbed.id}"/>">View More</a> <br/>
                    uid: <c:out value="${testbed.id}"/> <br/>
                    urnPrefix: <c:out value="${testbed.urnPrefix}"/> <br/>
                    urnCapabilityPrefix: <c:out value="${testbed.urnCapabilityPrefix}"/> <br/>
                    <c:out value="${nodes[testbed.name]}"/> Nodes , <c:out value="${links[testbed.name]}"/> Links

                </div>
            </div>
            <%--</div>--%>
        </c:forEach>
    </div>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
