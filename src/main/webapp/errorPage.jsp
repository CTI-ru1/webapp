<%@ page isErrorPage="true" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <title>ÜberDust - ${pageContext.errorData.statusCode}</title>
    <%@include file="/head.jsp" %>
</head>
<body>

<%@include file="/header.jsp" %>
<div class="container">
    <h3> ${pageContext.errorData.statusCode} Error Detected </h3>
    <p>URI Requested: <pre>${pageContext.errorData.requestURI}</pre></p>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>