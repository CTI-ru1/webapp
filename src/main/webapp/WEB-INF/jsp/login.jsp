<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/tag/custom.tld" prefix="util" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
<head>
    <META NAME="Description" CONTENT="Überdust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Überdust - Login page</title>
    <%@include file="/head.jsp" %>

</head>
<body onload='document.f.j_username.focus();'>
<%@include file="/header.jsp" %>
<div class="container">
    <h3>Login with Username and Password (Authentication with Database)</h3>

    <c:choose>
        <c:when test="${not empty error}">
            <div class="errorblock">
                Your login attempt was not successful, try again.
                <br/>
                Caused :
                    ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
            </div>
        </c:when>
        <c:otherwise>
            <jsp:forward page="/rest/testbed"/>
        </c:otherwise>
    </c:choose>


</div>
<%@include file="/footer.jsp" %>
</body>
</html>

