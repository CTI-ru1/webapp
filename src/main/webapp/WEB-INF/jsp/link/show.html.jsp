<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="linkCapabilityMap" scope="request" class="java.util.HashMap"/>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - Show Link <c:forEach items="${links}" var="link">[<c:out value="${link.source.name}"/>,<c:out
            value="${link.target.name}"/>]</c:forEach></title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>"/>
    <%@include file="/googleAnalytics.jsp"%>
</head>
<body>
<%@include file="/header.jsp" %>
<table>
    <tbody>

    <c:forEach items="${linkCapabilityMap}" var="link">
        <tr>
            <td>
                /<a href="<c:url value="/rest/testbed"/>">testbeds</a>/
                <a href="<c:url value="/rest/testbed/${testbed.id}"/>">testbed</a>/
                <a href="<c:url value="/rest/testbed/${testbed.id}/link/${link.key.source.name}/${link.key.target.name}"/>">link</a>
            </td>
        </tr>
        <tr>
            <td>
                <a href="<c:url value="/rest/testbed/${testbed.id}/link/${link.key.source.name}/${link.key.target.name}"/>"><c:out
                        value="${link.key.source.name},${link.key.target.name}"/></a>
            </td>
        </tr>
        <tr>
            <td>Source ID</td>
            <td>
                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${link.key.source.name}"/>"><c:out
                        value="${link.key.source.name}"/></a>
            </td>
        </tr>
        <tr>
            <td>Target ID</td>
            <td>
                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${link.key.target.name}"/>"><c:out
                        value="${link.key.target.name}"/></a>
            </td>
        </tr>
        <tr>
            <td>Capabilities(<c:out value="${fn:length(link.value)}"/>)</td>
            <td>
                <table>
                    <tbody>
                    <c:forEach items="${link.value}" var="capability">
                        <tr>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${capability.capability.name}"/>"><c:out
                                        value="${capability.capability.name}"/></a>
                            </td>
                            <td>
                                <!-- TODO add stuff from next page -->
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%@include file="/footer.jsp" %>
</body>
</html>
