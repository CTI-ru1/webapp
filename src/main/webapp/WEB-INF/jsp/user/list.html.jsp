<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="roles" scope="request" class="java.util.HashMap"/>
<jsp:useBean id="users" scope="request" class="java.util.ArrayList"/>

<html>
<head>
    <META NAME="Description" CONTENT="Überdust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>Überdust - Users</title>
    <%@include file="/head.jsp" %>

</head>
<body>
<%@include file="/header.jsp" %>

<div class="container">
    <h2>User Manager </h2>

    <div class="col-md-12">
        <ul class="media-list">
            <c:forEach items="${roles}" var="entry">
                <li class="media">
                    <a class="pull-left" href="#">
                        <img class="media-object" data-src="holder.js/64x64" src="<c:url value="/img/avatar.png"/>"
                             style="width: 64px"/>
                    </a>

                    <div class="media-body">
                        <h4 class="media-heading"><a
                                href="<c:url value="/rest/user/${entry.key.username}"/>">${entry.key.username}</a></h4>

                        <div class="media">
                            <table class="table table-condensed">
                                <thead>
                                <th width="300px">
                                    <form class="form-inline">
                                        Details
                                        <c:choose>
                                            <c:when test="${entry.key.enabled}">
                                                <button class="btn btn-danger"> Disable</button>
                                            </c:when>
                                            <c:otherwise>
                                                <button class="btn btn-success"> Enable</button>
                                            </c:otherwise>
                                        </c:choose>
                                    </form>
                                </th>
                                <th>
                                    <form class="form-inline" role="form">Roles:
                                        <div class="form-group"><input type="text" class="form-control"
                                                                       placeholder="ROLE_NEW" style="height:100%"></div>
                                        <button type="submit" class="btn">Add</button>
                                    </form>
                                </th>
                                </thead>
                                <tbody>
                                <td>
                                    <table class="table table-condensed">
                                        <tr>
                                            <th>Id</th>
                                            <td>
                                                    ${entry.key.id}
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Email</th>
                                            <td>
                                                    ${entry.key.email}
                                            </td>
                                        </tr>
                                    </table>

                                </td>
                                <td>
                                    <table class="table table-condensed">
                                        <c:forEach items="${entry.value}" var="role">
                                            <tr>
                                                <td>
                                                    <c:out value="${role.authority}"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </td>
                                </tbody>
                            </table>
                        </div>
                </li>
            </c:forEach>
        </ul>
        <h2></h2>
    </div>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
