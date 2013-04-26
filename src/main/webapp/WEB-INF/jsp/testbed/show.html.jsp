<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="text" scope="request" class="java.lang.String"/>
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

</head>
<body>
<%@include file="/header.jsp" %>

<div class="container">
    <h2>
            <c:out value="${testbed.name}"/> Description
    </h2>


    <div class="accordion-inner">

        <table class="table">
            <tbody>
            <tr>
                <td>Testbed ID</td>
                <td><c:out value="${testbed.id}"/></td>
            </tr>
            <tr>
                <td>Testbed Description</td>
                <td><c:out value="${testbed.description}"/></td>
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


    <table>
        <tr>
            <td style="vertical-align:top">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Nodes</th>
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
                                    <td class="table">
                                        <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/"/>"><c:out
                                                value="${node.name}"/></a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </td>
            <td style="vertical-align:top">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Links</th>
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
            </td>
            <td style="vertical-align:top">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Capabilities</th>
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
            </td>
            <td style="vertical-align:top">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Virtual</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${virtual == null || fn:length(virtual) == 0}">
                            <tr>
                                <td style="color : red">No virtual nodes found for testbed <c:out value="${testbed.name}"/></td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${virtual}" var="node">
                                <tr>
                                    <td class="table">
                                        <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/"/>"><c:out
                                                value="${node.name}"/></a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </td>
        </tr>
    </table>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
