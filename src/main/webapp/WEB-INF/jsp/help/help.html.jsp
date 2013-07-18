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
    <h1>ÜberDust Man Page</h1>
    <h3>Data Encodings:</h3>
    <table class="table table-condensed">
        <tr><th>Type</th><th>Url keyword</th></tr>
        <tr><td>HTML</td><td>html </td></tr>
        <tr><td>text/plain</td><td>raw or tabdelimited</td></tr>
        <tr><td>application/json</td><td> json</td></tr>
        <tr><td>application/rdf+xml</td><td> rdf/rdf+xml</td></tr>
        <tr><td>GeoRSS</td><td> georss</td></tr>
        <tr><td>GeoJSON</td><td> geojson</td></tr>
        <tr><td>KML</td><td> kml</td></tr>
        <tr><td>WiseML</td><td> wiseml</td></tr>
    </table>
    <h3> More</h3>
    <ul>
        <li>
            <a href="<c:url value="/rest/help/websockets"/>">Websocket Usage</a>
        </li>
    </ul>
    <h3>Available REST Urls:</h3>
  <pre>
    <%
        String file = application.getRealPath("/") + "mappings.list";

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        StringBuilder sb = new StringBuilder();


        while ((line = reader.readLine()) != null) {
            if (!line.equals("")){
                sb.append("\n"+line);
            }
        }

        reader.close();
        out.println(sb.toString());

    %>
</pre>



</div>

<%@include file="/footer.jsp" %>
</body>
</html>
