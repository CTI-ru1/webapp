<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="node" scope="request" class="eu.wisebed.wisedb.model.Node"/>
<jsp:useBean id="capability" scope="request" class="eu.wisebed.wisedb.model.Capability"/>
<jsp:useBean id="readings" scope="request" class="java.lang.String"/>

<html>
<head>
    <%@include file="/googleAnalytics.jsp" %>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.0/jquery.min.js"></script>
    <script type="text/javascript" src="<c:url value="/js/highcharts.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/themes/gray.js"/>"></script>
    <script type="text/javascript">


        function displayChart() {
            var chart;

            chart = new Highcharts.Chart({
                chart:{
                    renderTo:'chartcontainer',
                    defaultSeriesType:'spline',
                    zoomType:'x',
                    spacingRight:20
                },
                title:{
                    text:'Readings Chart Testbed : '
                            .concat('<c:out value="${testbed.name}"/>')
                            .concat(' Node : ')
                            .concat('<c:out value="${node.name}"/>')
                            .concat(' Capability : ')
                            .concat('<c:out value="${capability.name}"/>')
                },
                subtitle:{
                    text:document.ontouchstart === undefined ?
                            'Click and drag in the plot area to zoom in' :
                            'Drag your cursor over the plot to zoom in'
                },
                xAxis:{
                    type:'datetime',
                    tickPixelInterval:150,
                    maxZoom:1000
                },
                yAxis:{
                    title:{
                        text:'Reading'
                    },
                    min:0.6,
                    startOnTick:false,
                    showFirstLabel:false
                },
                tooltip:{
                    shared:true
                },
                legend:{
                    enabled:false
                },
                series:[
                    {
                        name:'Reading value (<c:out value="${capability.unit}"/>,<c:out value="${capability.datatype}"/>)',
                        data:[<c:out value="${readings}"/>]
                    }
                ]
            });
        }
    </script>

    <title>ÜberDust - Readings Chart Testbed: <c:out value="${testbed.name}"/> <c:out value="${node.name}"/> ,
        Capability
        : <c:out value="${capability.name}"/></title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>"/>
</head>
<body onload="displayChart()">

<%@include file="/header.jsp" %>
<div class="container">
    <div id="chartcontainer" style="width: 100%; height: 400px"></div>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
