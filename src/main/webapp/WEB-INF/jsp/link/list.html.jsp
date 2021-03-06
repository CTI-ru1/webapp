<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="links" scope="request" class="java.util.ArrayList"/>


<html>
<head>
    <META NAME="Description" CONTENT="Überdust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Überdust - Show Testbed Links : <c:out value="${testbed.name}"/></title>
    <%@include file="/head.jsp" %>

</head>
<body>
<%@include file="/header.jsp" %>
<div class="container">
    <div class="col-md-12">
        <h3>Links (view also as :
            <a href="<c:url value="/rest/testbed/${testbed.id}/link/raw"/>">raw</a>,
            <a href="<c:url value="/rest/testbed/${testbed.id}/link/json"/>">json</a>
            )
        </h3>
    </div>

    <table class="table-hover">
        <tbody>
        <c:forEach items="${links}" var="link">
            <tr>
                <td>
                    <a href="<c:url value="/rest/testbed/${testbed.id}/link/${link.source.name}/${link.target.name}"/>">
                        <c:out value="${link.source.name}"/>--<c:out value="${link.target.name}"/>
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


