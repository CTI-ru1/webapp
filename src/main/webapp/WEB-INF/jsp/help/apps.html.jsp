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
    <META NAME="Description" CONTENT="Überdust"/>
    <META http-equiv="Content-Language" content="en"/>
    <title>Überdust Man Page</title>
    <%@include file="/head.jsp" %>
</head>

<jsp:useBean id="text" scope="request" class="java.lang.String"/>

<body>
<%@include file="/header.jsp" %>

<div class="container">
    <h1>Überdust Applications and Utils</h1>

    <ul class="media-list">
        <li class="media">
            <a class="pull-left" href="<c:url value="/files/uclient-debug.apk"/>">
                <img class="media-object" data-src="holder.js/64x64" src="<c:url value="/img/logos/android.png"/>"
                     style="width: 64px"/>
            </a>

            <div class="media-body">
                <h4 class="media-heading">Android Application</h4>
                Android Application to identify and interact with Überdust connected devices and virtual nodes.
                Available <a href="<c:url value="/files/uclient-debug.apk"/>">here</a>.
            </div>
        </li>
        <li class="media">
            <a class="pull-left" href="https://chrome.google.com/webstore/detail/uberdust-controller/mpmpapplnfgkgbljalnpokocikkpmlli">
                <img class="media-object" data-src="holder.js/64x64" src="<c:url value="/img/logos/chrome.png"/>"
                     style="width: 64px"/>
            </a>

            <div class="media-body">
                <h4 class="media-heading">Google Chrome Extension</h4>
                Used to interact with a specific NodeCapability via a simple switch in the Google Chrome Toolbar.
                Available <a href="https://chrome.google.com/webstore/detail/uberdust-controller/mpmpapplnfgkgbljalnpokocikkpmlli">here</a>.
            </div>
        </li>
        <li class="media">
            <a class="pull-left" href="https://chrome.google.com/webstore/detail/uberdust/oeabnighppapiggpgpnoiamhiilgjeck">
                <img class="media-object" data-src="holder.js/64x64" src="<c:url value="/img/logos/chrome.png"/>"
                     style="width: 64px"/>
            </a>

            <div class="media-body">
                <h4 class="media-heading">Chrome Store Application</h4>
                Offers more advanced features in comparisson to the Google Chrome Extension and the ability to control
                multiple NodeCapabilities from different Devices and Testbeds at the same time.
                Available <a href="https://chrome.google.com/webstore/detail/uberdust/oeabnighppapiggpgpnoiamhiilgjeck">here</a>.
            </div>
        </li>
        <li class="media">
            <a class="pull-left" href="#">
                <img class="media-object" data-src="holder.js/64x64" src="<c:url value="/img/logos/firefoxos.png"/>"
                     style="width: 64px"/>
            </a>

            <div class="media-body">
                <h4 class="media-heading">Firefox Marketplace Application</h4>
                Offers the same functionality as the Chrome Store Application using either the Mozilla Firefox Browser
                Applications or the upcoming FirefoxOS Smartphone Operating System.
                Available <a href="https://marketplace.firefox.com/app/uberdust-controller-1">here</a>.
            </div>
        </li>
        <li class="media">
            <a class="pull-left" href="#">
                <img class="media-object" data-src="holder.js/64x64" src="<c:url value="/img/logos/metro.png"/>"
                     style="width: 64px"/>
            </a>

            <div class="media-body">
                <h4 class="media-heading">Windows 8 Application</h4>
                WIP
            </div>
        </li>
    </ul>
</div>

<%@include file="/footer.jsp" %>
</body>
</html>
