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
    <c:set var="prevnode" value=""/>

    <c:forEach items="${lastNodeReadings}" var="lnr">
        <%--<c:choose>--%>
        <%--<c:when test="${updated.contains(lnr.node.name)}">--%>
    <c:choose>
    <c:when test="${prevnode != lnr.node.name}">
    <c:choose>
    <c:when test="${prevnode != ''}">
    </table>
</div>
</div>
</div>
</c:when>
</c:choose>
<div class="accordion" id="accordion<c:out value="${lnr.node.id}"/>">
    <div class="accordion-group">
        <div class="accordion-heading">
            <c:choose>
            <c:when test="${!updated.contains(lnr.node.name)}">
            <div class="alert">
                </c:when>
                </c:choose>

                <a class="accordion-toggle" data-toggle="collapse"
                   data-parent="#accordion"
                   href="#collapseOne<c:out value="${lnr.node.id}"/>">
                    <c:out value="${lnr.node.name}"/>
                </a>
                <c:choose>
                <c:when test="${!updated.contains(lnr.node.name)}">
            </div>
            </c:when>
            </c:choose>
        </div>
        <c:choose>
        <c:when test="${!updated.contains(lnr.node.name)}">
        <div id="collapseOne<c:out value="${lnr.node.id}"/>" class="accordion-body collapse ">
            </c:when>
            <c:otherwise>
            <div id="collapseOne<c:out value="${lnr.node.id}"/>" class="accordion-body collapse in">
                </c:otherwise>
                </c:choose>

                <div class="accordion-inner">
                    <a href="<c:url value="/rest/testbed/${testbed.id}/node/${lnr.node.name}/"/>"> View Node</a>
                    <table class="table-hover table table-condensed table-striped">
                        <c:choose>
                        <c:when test="${fn:startsWith(lnr.capability.name , 'urn')}">
                        <tr>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${lnr.capability.name}"/>"><c:out
                                        value="${lnr.capability.name}"/></a>
                            <td>
                                    ${lnr.lastNodeReading.timestamp}
                            <td>
                                <c:choose>
                                <c:when test="${lnr.lastNodeReading.stringReading == null}">

                                    ${lnr.lastNodeReading.reading}

                                </c:when>
                                <c:otherwise>
                                    ${fn:substring(lnr.lastNodeReading.stringReading, 0, 20)}
                                </c:otherwise>
                                </c:choose>

                                </c:when>
                                </c:choose>
                                </c:when>
                                <c:otherwise>
                                <c:choose>
                                <c:when test="${fn:startsWith(lnr.capability.name , 'urn')}">
                        <tr>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${lnr.capability.name}"/>"><c:out
                                        value="${lnr.capability.name}"/></a>
                            <td>
                                    ${lnr.lastNodeReading.timestamp}
                            <td>
                                <c:choose>
                                <c:when test="${lnr.lastNodeReading.stringReading == null}">

                                    ${lnr.lastNodeReading.reading}

                                </c:when>
                                <c:otherwise>
                                    ${fn:substring(lnr.lastNodeReading.stringReading, 0, 20)}
                                </c:otherwise>
                                </c:choose>
                                </c:when>
                                </c:choose>

                                </c:otherwise>
                                </c:choose>
                                    <%--</c:when>--%>
                                    <%--</c:choose>--%>
                                    <c:set var="prevnode" value="${lnr.node.name}"/>
                                </c:forEach>

                                <%--<c:out value="${lastNodeReadings}" escapeXml="false"/>--%>
                                <%--<c:out value="${lastLinkReadings}" escapeXml="false"/>--%>
                </div>
                <%@include file="/footer.jsp" %>
</body>
</html>
