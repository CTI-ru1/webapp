<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileReader" %>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <title>ÜberDust Man Page</title>
    <%@include file="/head.jsp" %>
</head>

<jsp:useBean id="text" scope="request" class="java.lang.String"/>

<body>
<%@include file="/header.jsp" %>

<div class="container">
    <h1>ÜberDust Applications and Utils</h1>

    <h3>Android Application:</h3>
    <h3>Google Chrome Extension:</h3>
    <h3>Google Chrome Application:</h3>
    <h3>Firefox Os Application:</h3>
    <h3>Windows 8 Application:</h3>


</div>

<%@include file="/footer.jsp" %>
</body>
</html>
