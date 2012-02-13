<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="node" scope="request" class="eu.wisebed.wisedb.model.Node"/>
<jsp:useBean id="capabilities" scope="request" class="java.util.ArrayList"/>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - Show Node : <c:out value="${node.id}"/></title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>"/>
</head>
<body>
<%@include file="/header.jsp" %>
<p>
    /<a href="<c:url value="/rest/testbed"/>">testbeds</a>/
    <a href="<c:url value="/rest/testbed/${testbed.id}"/>">testbed</a>/
    <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.id}"/>">node</a>
</p>

<table>
    <tbody>
    <tr>
        <td>Node ID</td>
        <td>
            <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.id}"/>"><c:out value="${node.id}"/></a>
        </td>
    </tr>
    <tr>
        <td><a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.id}/capabilities" />">Capabilities</a>(<c:out value="${fn:length(capabilities)}"/>)</td>
        <td>
            <table class="readings">
                <tbody>
                <c:forEach items="${capabilities}" var="capability">
                    <tr>
                        <td>
                            <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${capability.capability.name}"/>"><c:out
                                    value="${capability.capability.name}"/></a>
                        </td>
                        <td>
                            <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.id}/capability/${capability.capability.name}/html/limit/10"/>">HTML</a>
                        </td>
                        <td>
                            <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.id}/capability/${capability.capability.name}/tabdelimited/limit/10"/>">Tab
                                Delimited</a>
                        </td>
                        <td>
                            <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.id}/capability/${capability.capability.name}/json/limit/10"/>">JSON</a>
                        </td>
                        <td>
                            <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.id}/capability/${capability.capability.name}/wiseml"/>">WiseML</a>
                            <span style="color : red">Not implemented yet</span>
                        </td>
                        <td>
                            <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.id}/capability/${capability.capability.name}/latestreading"/>">Latest
                                Reading</a>(<a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.id}/capability/${capability.capability.name}/latestreading/json"/>">JSON</a>)
                        </td>
                        <td>
                            <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.id}/capability/${capability.capability.name}/chart/limit/10"/>">Chart</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </td>
    </tr>
    <tr>
        <td>GeoRSS Feed</td>
        <td>
            <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.id}/georss"/>">GeoRSS Feed</a>
            (<a href="http://maps.google.com/maps?q=<c:url value="${baseURL}/rest/testbed/${testbed.id}/node/${node.id}/georss"/>">View
            On Google Maps</a>)
        </td>
    </tr>
    <tr>
        <td>KML Feed</td>
        <td>
            <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.id}/kml"/>">KML feed</a>
            (<a href="http://maps.google.com/maps?q=<c:url value="${baseURL}/rest/testbed/${testbed.id}/node/${node.id}/kml"/>">View
            On Google Maps</a>)
            <span style="color : red">Not implemented yet</span>
        </td>
    </tr>
     <tr>
            <td>Rdf description</td>
            <td>
                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.id}/rdf"/>">
                    <img src="http://www.mkbergman.com/wp-content/themes/ai3/images/2009Posts/090326_rdf_200.png" width="20px">
                    Rdf description
                </a>


            </td>
        </tr>
    </tbody>
</table>
<%@include file="/footer.jsp" %>
</body>
</html>