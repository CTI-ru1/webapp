<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="link" scope="request" class="eu.wisebed.wisedb.model.Link"/>
<jsp:useBean id="text" scope="request" class="java.lang.String"/>
<jsp:useBean id="linkCapabilities" scope="request" class="java.lang.String"/>


<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - Show Link [<c:out value="${link.source.name}"/>,<c:out value="${link.target.name}"/>]</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>"/>
    <%@include file="/googleAnalytics.jsp"%>
</head>
<body>
<%@include file="/header.jsp" %>
    /<a href="<c:url value="/rest/testbed"/>">testbeds</a>/
    <a href="<c:url value="/rest/testbed/${testbed.id}"/>">testbed</a>/
    <a href="<c:url value="/rest/testbed/${testbed.id}/link/${link.source.name}/${link.target.name}"/>">link</a>

    <c:out value="${text}" escapeXml="false" />

    <c:out value="${linkCapabilities}" escapeXml="false" />


<%@include file="/footer.jsp" %>
</body>
</html>
