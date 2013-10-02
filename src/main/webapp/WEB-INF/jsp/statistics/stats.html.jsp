<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="readingsPing" scope="request" class="java.lang.String"/>
<jsp:useBean id="readingsHome" scope="request" class="java.lang.String"/>

<html>
<head>
    <%@include file="/googleAnalytics.jsp" %>
    <%@include file="/head.jsp" %>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js"></script>
    <script type="text/javascript" src="<c:url value="/js/highcharts.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/charting.js"/>"></script>
    <script type="text/javascript">


        function displayCharts() {
            var chartHome = createHighChart(
                    'chartcontainerHome',
                    'Stats Load Time for /testbed/: ',
                    'datetime',
                    'Reading',
                    'time',
                    [<c:out value="${readingsHome}"/>]
            );
            var chartPing = createHighChart(
                    'chartcontainerPing',
                    'Stats Load Time for /ping/: ',
                    'datetime',
                    'Reading',
                    'time',
                    [<c:out value="${readingsPing}"/>]
            );
        }
    </script>

    <title>ÜberDust - Statistics </title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>"/>
</head>
<body onload="displayCharts()">

<%@include file="/header.jsp" %>
<div class="container" style="width:80%">
    <div id="chartcontainerHome" style="width: 100%; height: 400px"></div>
    <div id="chartcontainerPing" style="width: 100%; height: 400px"></div>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
