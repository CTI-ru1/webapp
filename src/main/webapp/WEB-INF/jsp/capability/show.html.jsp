<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="capability" scope="request" class="eu.wisebed.wisedb.model.Capability"/>
<jsp:useBean id="nodes" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="links" scope="request" class="java.util.ArrayList"/>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - Show Capability [<c:out value="${capability.name}"/>]</title>
    <%@include file="/head.jsp" %>
</head>
<body>
<%@include file="/header.jsp" %>
<div class="container">
    <table class="table">
        <thead>
        <tr>
            <th>Capability</th>
            <th>
                <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${capability.name}"/>">${capability.name}</a>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <th>Unit of Measurement</th>
            <th>${capability.unit}</th>
        </tr>
        <tr>
            <th>Capability Semantic Description</th>
            <th></th>
        </tr>
        <tr>
            <th>List Latest Readings</th>
            <th>
                <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${capability.name}/tabdelimited"/>">raw</a>
            </th>
        </tr>
        <tr>
            <td>
                <table>
                    <thead>
                    <tr>
                        <th>Nodes</th>
                    </tr>
                    </thead>
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
            </td>
            <td>
                <table>
                    <thead>
                    <tr>
                        <th>Links</th>
                    </tr>
                    </thead>
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
            </td>
        </tr>
        </tbody>


    </table>

</div>
<%@include file="/footer.jsp" %>
</body>
</html>
