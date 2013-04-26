<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:useBean id="readings" scope="request" class="java.util.ArrayList"/>

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

    <table class="table">
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

                <td><c:out value="${reading.stringReading}"/><c:out value="${reading.reading}"/></td>


            </tr>
        </c:forEach>

        </tbody>
    </table>
</div>

<%@include file="/footer.jsp" %>
</body>
</html>
