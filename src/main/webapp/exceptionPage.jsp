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
    <title>ÜberDust - Exception Occured</title>
    <%@include file="/head.jsp" %>
</head>
<body>
<%@include file="/header.jsp" %>
<div class="container">
    <h3>Exception Detected</h3>
    <p>${pageContext.exception.message} </p>
    <pre class="pre-scrollable">
      <c:forEach var="stacktraceElement" items="${pageContext.exception.stackTrace}">
          <c:out value="${stacktraceElement}" escapeXml="false"/>
      </c:forEach>                                                       
    </pre>

    </table>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>