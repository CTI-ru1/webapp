<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/tag/custom.tld" prefix="util" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="lastNodeReadings" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="lastLinkReadings" scope="request" class="java.lang.String"/>
<jsp:useBean id="updated" scope="request" class="java.util.Set"/>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - Testbed <c:out value="${testbed.name}"/> status page</title>
    <%@include file="/head.jsp" %>

</head>
<body>
<%@include file="/header.jsp" %>
<div class="container">
    <h2><c:out value="${testbed.name}"/> Status </h2>
    <table class="table-hover table-condensed" style="vertical-align:middle;">
        <thead>
        <th> Node</th>
        <th> Capability</th>
        <th> Timestamp</th>
        <th> Reading</th>
        </thead>
        <c:set var="prevnode" value=""/>
        <c:forEach items="${lastNodeReadings}" var="lnr">
            <c:choose>
                <c:when test="${updated.contains(lnr.node.name)}">
                    <tr>

                        <td colspan="#" style="vertical-align:middle;">
                            <c:choose>
                                <c:when test="${prevnode != lnr.node.name}">
                                    <c:out value="${lnr.node.name}"/>
                                </c:when>
                            </c:choose>
                        </td>
                        <td>
                                ${lnr.capability.name}
                        </td>
                        <td>
                                ${lnr.lastNodeReading.timestamp}
                        </td>
                        <c:choose>
                            <c:when test="${lnr.lastNodeReading.stringReading == null}">
                                <td>
                                        ${lnr.lastNodeReading.reading}
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td>
                                        ${lnr.lastNodeReading.stringReading}
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:when>
            </c:choose>
            <c:set var="prevnode" value="${lnr.node.name}"/>
        </c:forEach>
        <c:forEach items="${lastNodeReadings}" var="lnr">
            <c:choose>
                <c:when test="${!updated.contains(lnr.node.name)}">
                    <tr class="error">

                        <td colspan="#" style="vertical-align:middle;">
                            <c:choose>
                                <c:when test="${prevnode != lnr.node.name}">
                                    <c:out value="${lnr.node.name}"/>
                                </c:when>
                            </c:choose>
                        </td>
                        <c:set var="prevnode" value="${lnr.node.name}"/>
                        <td>
                                ${lnr.capability.name}
                        </td>
                        <td>
                                ${lnr.lastNodeReading.timestamp}
                        </td>
                        <c:choose>
                            <c:when test="${lnr.lastNodeReading.stringReading == null}">
                                <td>
                                        ${lnr.lastNodeReading.reading}
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td>
                                        ${lnr.lastNodeReading.stringReading}
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:when>
            </c:choose>
        </c:forEach>

    </table>
    <%--<c:out value="${lastNodeReadings}" escapeXml="false"/>--%>
    <%--<c:out value="${lastLinkReadings}" escapeXml="false"/>--%>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
