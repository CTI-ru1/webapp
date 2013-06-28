<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="nodes" scope="request" class="java.util.ArrayList"/>


<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - Show Testbed Virtual Nodes : <c:out value="${testbed.name}"/></title>
    <%@include file="/head.jsp" %>

</head>
<body>
<%@include file="/header.jsp" %>

<div class="container">
    <div class="span12">
        <h3>Virtual Nodes (view also as :
            <a href="<c:url value="/rest/testbed/${testbed.id}/virtualnode/raw"/>">raw</a>,
            <a href="<c:url value="/rest/testbed/${testbed.id}/virtualnode/json"/>">json</a>
            )
        </h3>
    </div>
    <table class="table-hover">
        <tbody>
        <c:forEach items="${nodes}" var="node">
            <tr>
                <td>
                    <a href="<c:url value="/rest/testbed/${testbed.id}/node/${node.name}/"/>">
                        <c:out value="${node.name}"/>
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
