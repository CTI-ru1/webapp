<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="capability" scope="request" class="eu.wisebed.wisedb.model.Capability"/>
<jsp:useBean id="capabilityText" scope="request" class="java.lang.String"/>
<jsp:useBean id="nodes" scope="request" class="java.lang.String"/>
<jsp:useBean id="links" scope="request" class="java.lang.String"/>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - Show Capability [<c:out value="${capability.name}"/>]</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>"/>
    <%@include file="/googleAnalytics.jsp"%>
</head>
<body>
<%@include file="/header.jsp" %>
<p>
    /<a href="<c:url value="/rest/testbed"/>">testbeds</a>/
    <a href="<c:url value="/rest/testbed/${testbed.id}"/>">testbed</a>/
    <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${capability.name}"/>">capability</a>
</p>

<c:out value="${capabilityText}" escapeXml="false" />
<c:out value="${nodes}" escapeXml="false" />
<c:out value="${links}" escapeXml="false" />

<%@include file="/footer.jsp" %>
</body>
</html>
