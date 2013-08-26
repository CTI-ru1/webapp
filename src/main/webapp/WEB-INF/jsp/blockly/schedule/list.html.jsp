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
</head>

<%--<jsp:useBean id="text" scope="request" class="java.lang.String"/>--%>
<jsp:useBean id="schedules" scope="request" class="java.util.ArrayList"/>
<%--<jsp:useBean id="nodes" scope="request" class="java.util.HashMap"/>--%>
<%--<jsp:useBean id="links" scope="request" class="java.util.HashMap"/>--%>
<%--<jsp:useBean id="origins" scope="request" class="java.util.HashMap"/>--%>
<%--<jsp:useBean id="nodePositions" scope="request" class="java.util.ArrayList"/>--%>

<body>
<%@include file="/header.jsp" %>

<div class="container">

    <h4>Schedules:</h4>
    <table class="table table-condensed table-hover">
        <thead>
        <tr>
            <th>user</th>
            <th>m</th>
            <th>h</th>
            <th>d</th>
            <th>M</th>
            <th>D</th>
            <th>C</th>
            <th>Last Execution</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${schedules}" var="schedule">
            <tr>
                <td>monty</td>
                <td>${schedule.minute}</td>
                <td>${schedule.hour}</td>
                <td>${schedule.dom}</td>
                <td>${schedule.month}</td>
                <td>${schedule.dow}</td>
                <td>${schedule.node} - ${schedule.capability} - '${schedule.payload}'</td>
                <td>Last Execution</td>
                <td>
                    <button class="btn">Run Now</button>
                    <button class="btn btn-danger">Delete</button>
                    <button class="btn btn-info">Edit</button>
                    <button class="btn btn-inverse">Disable</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
