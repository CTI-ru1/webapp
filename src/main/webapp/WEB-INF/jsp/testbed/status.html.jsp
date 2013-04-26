<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/tag/custom.tld" prefix="util" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="lastNodeReadings" scope="request" class="java.lang.String"/>
<jsp:useBean id="lastLinkReadings" scope="request" class="java.lang.String"/>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - Testbed <c:out value="${testbed.name}"/> status page</title>
    <%@include file="/head.jsp" %>

</head>
<body>
<%@include file="/header.jsp" %>
<div class="container">
    <h1>Testbed <c:out value="${testbed.name}"/> status page</h1>
    <c:out value="${lastNodeReadings}" escapeXml="false"/>
    <c:out value="${lastLinkReadings}" escapeXml="false"/>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
