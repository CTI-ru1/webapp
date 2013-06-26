<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="link" scope="request" class="eu.wisebed.wisedb.model.Link"/>
<jsp:useBean id="capabilities" scope="request" class="java.util.ArrayList"/>


<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - Show Link [<c:out value="${link.source.name}"/>,<c:out value="${link.target.name}"/>]</title>
    <%@include file="/head.jsp" %>

</head>
<body>
<%@include file="/header.jsp" %>

<div class="container">
    <h3>
        <a href="<c:url value="/rest/testbed/${testbed.id}/link/${link.source.name}/${link.target.name}"/>">
            <c:out value="${link.source.name}"/>--<c:out value="${link.target.name}"/>
        </a>
    </h3>
    <table class="table-hover">
        <tbody>
        <tr>
            <td>Source</td>
            <td>
                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${link.source.name}/"/>">
                    <c:out value="${link.source.name}"/>
                </a>
            </td>
        </tr>
        <tr>
            <td>Target</td>
            <td>
                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${link.target.name}/"/>">
                    <c:out value="${link.target.name}"/>
                </a>
            </td>
        </tr>
        </tbody>
    </table>
    <table class="table table-hover">
        <thead>
        <tr>
            <th>Capabilities</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${capabilities}" var="capability">
            <tr>
                <td>
                    <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${capability.capability.name}"/>">
                            ${capability.capability.name}
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%@include file="/footer.jsp" %>
</body>
</html>
