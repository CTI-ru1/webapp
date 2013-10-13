<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<html>
<head>
    <META NAME="Description" CONTENT="Überdust"/>
    <META http-equiv="Content-Language" content="en"/>
    <title>Überdust - List Schedules</title>
    <%@include file="/head.jsp" %>
</head>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="schedules" scope="request" class="java.util.ArrayList"/>
<%--<jsp:useBean id="nodes" scope="request" class="java.util.HashMap"/>--%>
<%--<jsp:useBean id="links" scope="request" class="java.util.HashMap"/>--%>
<%--<jsp:useBean id="origins" scope="request" class="java.util.HashMap"/>--%>
<%--<jsp:useBean id="nodePositions" scope="request" class="java.util.ArrayList"/>--%>

<body>
<%@include file="/header.jsp" %>

<div class="container" style="width: 90%">

    <h4>Schedules:</h4>
    <script type="text/javascript">
        function deleteRule(id, username) {
            $.ajax({
                url: 'schedule/' + id + "/" + username + "/",
                type: 'DELETE',
                success: function (result) {
                    location.reload(true);
                }
            });
        }
        function runRule(id, username) {
            $.ajax({
                url: 'schedule/' + id + "/" + username + "/",
                type: 'GET',
                success: function (result) {
                    location.reload(true);
                }
            });
        }
    </script>

    <table class="table table-condensed table-hover">
        <thead>
        <tr>
            <th>user</th>
            <th>s</th>
            <th>m</th>
            <th>h</th>
            <th>d</th>
            <th>M</th>
            <th>D</th>
            <th>C</th>
            <th>Last Execution</th>
            <th><a href="<c:url value="/rest/testbed/${testbed.id}/schedule/create"/>">Add new</a></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${schedules}" var="schedule">
            <tr>
                <td><a href="<c:url value="/rest/user/${schedule.username}"/>">${schedule.username}</a></td>
                <td>${schedule.second}</td>
                <td>${schedule.minute}</td>
                <td>${schedule.hour}</td>
                <td>${schedule.dom}</td>
                <td>${schedule.month}</td>
                <td>${schedule.dow}</td>
                <td>${schedule.node} - ${schedule.capability} - '${schedule.payload}'</td>
                <td>${schedule.last}</td>
                <td>
                    <button onclick="runRule(${schedule.id},'${username}')" class="btn">Run Now</button>
                    <button onclick="deleteRule(${schedule.id},'${username}')" class="btn btn-danger">Delete</button>
                    <button class="btn btn-info disabled" disabled="disabled">Edit</button>
                    <button class="btn btn-inverse disabled" disabled="disabled">Disable</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
