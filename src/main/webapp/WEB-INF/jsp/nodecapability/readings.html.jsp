<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:useBean id="readings" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="node" scope="request" class="eu.wisebed.wisedb.model.Node"/>
<jsp:useBean id="capability" scope="request" class="eu.wisebed.wisedb.model.Capability"/>
<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - Show Readings</title>
    <%@include file="/head.jsp" %>

</head>

<body>
<%@include file="/header.jsp" %>

<div class="container">
    <c:out value="${text}" escapeXml="false"/>
    <div class="span4">
        <h3>
            <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/"/>">
                <c:out value="${node.name}"/>
            </a>
        </h3>
    </div>
    <div class="span4">
        <h3>
            <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${capability.name}"/>">${capability.name}</a>
        </h3>
    </div>

    <table class="table table-hover">
        <thead>
        <tr>
            <th>Timestamp</th>
            <th>Reading</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${readings}" var="reading">
            <tr>
                <td><c:out value="${reading.timestamp}"/></td>

                <td>
                    <c:choose>
                        <c:when test="${reading.stringReading == null || reading.stringReading == '' }">
                            ${reading.reading}
                        </c:when>
                        <c:otherwise>
                            ${fn:substring(reading.stringReading, 0, 20)}
                        </c:otherwise>
                    </c:choose>
                </td>

            </tr>
        </c:forEach>

        </tbody>
    </table>
</div>

<%@include file="/footer.jsp" %>
</body>
</html>
