<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="capabilities" scope="request" class="java.util.ArrayList"/>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - List Capabilities</title>
    <%@include file="/head.jsp" %>

</head>
<body>
<%@include file="/header.jsp" %>
<div class="container">
    <table>
        <thead>
        <tr>
            <th>Capabilities (view also as :
                <a href="<c:url value="/rest/testbed/${testbed.id}/capability/raw"/>">raw</a>,
                <a href="<c:url value="/rest/testbed/${testbed.id}/capability/json"/>">json</a>
                )
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${capabilities}" var="capability">
            <tr>
                <td>
                    <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${capability.name}"/>">
                        <c:out value="${capability.name}"/>
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
