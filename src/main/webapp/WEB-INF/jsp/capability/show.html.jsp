<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="capability" scope="request" class="eu.wisebed.wisedb.model.Capability"/>
<jsp:useBean id="nodes" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="links" scope="request" class="java.util.ArrayList"/>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - Show Capability [<c:out value="${capability.name}"/>]</title>
    <%@include file="/head.jsp" %>
</head>
<body>
<%@include file="/header.jsp" %>
<div class="container">
    <div class="span12">
        <h3>
            <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${capability.name}"/>">${capability.name}</a>
        </h3>
    </div>

    <table class="table-hover">
        <thead>
        </thead>
        <tbody>
        <tr>
            <td>Unit of Measurement</td>
            <td>${capability.unit}</td>
        </tr>
        <tr>
            <td>Minimum Value</td>
            <td>${capability.minvalue}</td>
        </tr>
        <tr>
            <td>Maximum Value</td>
            <td>${capability.maxvalue}</td>
        </tr>
        <tr>
            <td>Capability Semantic Description</td>
            <td></td>
        </tr>
        <tr>
            <td>List Latest Readings</td>
            <td>
                <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${capability.name}/tabdelimited"/>">raw</a>
            </td>
        </tr>
        <tr>
            <td>Live Readings</td>
            <td>
                <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${capability.name}/live"/>">View</a>
            </td>
        </tr>
        </tbody>
    </table>
    <table>
        <thead>
        <tr>
            <c:choose>
                <c:when test="${fn:length(nodes) > 0}">
                    <th>Nodes</th>
                </c:when>
            </c:choose>
            <c:choose>
                <c:when test="${fn:length(links) > 0}">
                    <th>Links</th>
                </c:when>
            </c:choose>
        </tr>
        </thead>
        <tbody>
        <tr>
            <c:choose>
                <c:when test="${fn:length(nodes) > 0}">
                    <td>
                        <table class="table table-hover">
                            <tbody>
                            <c:forEach items="${nodes}" var="node">
                                <tr>
                                    <td>
                                        <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/"/>">
                                            <c:out value="${node.name}"/>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </td>
                </c:when>
            </c:choose>
            <c:choose>
                <c:when test="${fn:length(links) > 0}">
                    <td>
                        <table class="table table-hover">
                            <tbody>
                            <c:forEach items="${links}" var="link">
                                <tr>
                                    <td>
                                        <a href="<c:url value="/rest/testbed/${testbed.id}/link/${link.source.name}/${link.target.name}"/>">
                                            <c:out value="${link.source.name}"/>--<c:out value="${link.target.name}"/>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </td>
                </c:when>
            </c:choose>
        </tr>
        </tbody>
    </table>

</div>
<%@include file="/footer.jsp" %>
</body>
</html>
