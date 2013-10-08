<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:useBean id="host" scope="request" class="java.lang.String"/>
<jsp:useBean id="testbedUrnPrefix" scope="request" class="java.lang.String"/>
<jsp:useBean id="capability" scope="request" class="java.lang.String"/>

<html>
<head>
    <META NAME="Description" CONTENT="Überdust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Überdust - Live Capability Readings</title>
    <%@include file="/head.jsp" %>

    <script type="text/javascript" src="<c:url value="/js/ws/uberdustws.js"/>"></script>

</head>

<body>
<%@include file="/header.jsp" %>
<div class="container">
    <table>
        <tr>
            <th> Live Readings</th>
        </tr>
        <tr>
            <td> Capability</td>
            <td><a href="/rest/testbed/<c:out value="${testbed}"/>/capability/<c:out value="${capability}"/>"><c:out
                    value="${capability}"/> </a>
            </td>
        </tr>
    </table>

    <span id="area"> </span>

    <script type="text/javascript">
        connect("<c:out value="${host}"/>", "<c:out value="${testbedUrnPrefix}"/>*", "<c:out
        value="${capability}"/>", function message(value2add) {
            var myspan = document.getElementById('area');
            var oldtext = myspan.innerHTML;
            myspan.innerHTML = value2add + "<br>" + oldtext;
        });
    </script>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
