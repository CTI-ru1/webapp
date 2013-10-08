<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<html>
<head>
    <META NAME="Description" CONTENT="Überdust"/>
    <META http-equiv="Content-Language" content="en"/>
    <title>Überdust - NodeCapability Not Found</title>
    <%@include file="/head.jsp" %>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
</head>

<body>
<%@include file="/header.jsp" %>
<div class="container">
    <h1>404</h1>

    Seems like the NodeCapability you are looking for does not exist! Check your link or contact us to investigate the issue.

</div>
<%@include file="/footer.jsp" %>
</body>
</html>
