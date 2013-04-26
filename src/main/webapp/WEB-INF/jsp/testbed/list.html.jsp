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

<jsp:useBean id="text" scope="request" class="java.lang.String"/>
<jsp:useBean id="testbeds" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="nodes" scope="request" class="java.util.HashMap"/>
<jsp:useBean id="links" scope="request" class="java.util.HashMap"/>
<body>
<%@include file="/header.jsp" %>

<div class="container">
    <h1>Welcome to Überdust</h1>

    <%--<c:out value="${text}" escapeXml="false" />--%>
    Available Testbeds : (<c:out value="${fn:length(testbeds)}"/>)

    <%--<div class="accordion" id="accordion2">--%>
    <c:forEach items="${testbeds}" var="testbed">
        <%--<div class="accordion-group">--%>
        <div class="accordion-heading">
            <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2"
               href="#collapse<c:out value="${testbed.id}"/>">
                <c:out value="${testbed.name}"/>
            </a>
        </div>
        <div id="collapse<c:out value="${testbed.id}"/>" class="accordion-body collapse in">
            <div class="accordion-inner">
                <a href="<c:url value="/rest/testbed/${testbed.id}"/>"><img src="<c:url value="/img/wsn.jpg"/>"></a>
                Id: <c:out value="${testbed.id}"/> |
                Nodes (<c:out value="${nodes[testbed.name]}"/>) |
                Links (<c:out value="${links[testbed.name]}"/>)


            </div>
        </div>
        <%--</div>--%>
    </c:forEach>
    <%--</div>--%>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
