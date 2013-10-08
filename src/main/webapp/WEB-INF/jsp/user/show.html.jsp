<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="user" scope="request" class="eu.wisebed.wisedb.model.User"/>
<jsp:useBean id="roles" scope="request" class="java.util.ArrayList"/>

<html>
<head>
    <META NAME="Description" CONTENT="Überdust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>Überdust - User: <c:out value="${user.username}"/></title>
    <%@include file="/head.jsp" %>

</head>
<body>
<%@include file="/header.jsp" %>

<div class="container">
    <div class="col-md-12">
        <h2><a href="<c:url value="/rest/user/${user.username}"/>">${user.username}</a></h2>
    </div>
    <div class="col-md-12">
        <table class="table table-hover">
            <tr>
                <td>Email</td>
                <td>${user.email}</td>
            </tr>
        </table>
    </div>

</div>
<%@include file="/footer.jsp" %>
</body>
</html>
