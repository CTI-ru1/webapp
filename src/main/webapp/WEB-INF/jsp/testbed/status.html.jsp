<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/tag/custom.tld" prefix="util" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="testbed" scope="request" class="eu.wisebed.wisedb.model.Testbed"/>
<jsp:useBean id="lastNodeReadings" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="lastLinkReadings" scope="request" class="java.util.ArrayList"/>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>ÜberDust - Testbed <c:out value="${testbed.name}"/> status page</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>"/>
    <%@include file="/googleAnalytics.jsp"%>
</head>
<body>
<%@include file="/header.jsp"%>
<h1>Testbed <c:out value="${testbed.name}"/> status page</h1>
<p>
    /<a href="<c:url value="/rest/testbed"/>">testbeds</a>/
    <a href="<c:url value="/rest/testbed/${testbed.id}"/>">testbed</a>/
    <a href="<c:url value="/rest/testbed/${testbed.id}/status"/>">status</a>
</p>

<c:choose>
    <c:when test="${lastNodeReadings != null}">
        <h2>Nodes</h2>
        <table>
            <thead>
            <th>Node</th>
            <th>Capability</th>
            <th>Timestamp</th>
            <th>Double Reading</th>
            <th>String Reading</th>
            </thead>
            <tbody>
            <c:forEach items="${lastNodeReadings}" var="lnr">
                <c:if test="${lnr != null}">
                    <c:if test="${ lnr.capability.name!='x' && lnr.capability.name!='y' && lnr.capability.name!='z' && lnr.capability.name!='phi' && lnr.capability.name!='theta' && lnr.capability.name!='room'&& lnr.capability.name!='description' }">
                        <tr>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/node/${lnr.node.name}"/>"><c:out value="${lnr.node.name}"/></a>
                            </td>
                            <td>
                                <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${lnr.capability.name}"/>"><c:out value="${lnr.capability.name}"/></a>
                            </td>
                            <c:if test="${lnr.lastNodeReading != null}">
                                <c:if test="${lnr.lastNodeReading.timestamp != null}">
                                    <c:choose>
                                        <c:when test="${util:checkIfDateIsToday(lnr.lastNodeReading.timestamp)}">
                                            <td>${lnr.lastNodeReading.timestamp}</td>
                                           <c:if test="${lnr.lastNodeReading.reading != null}">
                                               <td class="reading">${lnr.lastNodeReading.reading}</td>
                                           </c:if>
                                           <c:if test="${lnr.lastNodeReading.reading == null}">
                                               <td class="reading"></td>
                                           </c:if>
                                           <c:if test="${lnr.lastNodeReading.stringReading != null}">
                                                <td class="reading">${lnr.lastNodeReading.stringReading}</td>
                                            </c:if>
                                            <c:if test="${lnr.lastNodeReading.stringReading == null}">
                                                <td class="reading"></td>
                                           </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="color :red">${lnr.lastNodeReading.timestamp}</td>
                                            <c:if test="${lnr.lastNodeReading.reading != null}">
                                               <td class="reading" style="color :red">${lnr.lastNodeReading.reading}</td>
                                            </c:if>
                                            <c:if test="${lnr.lastNodeReading.reading == null}">
                                               <td class="reading" style="color :red"></td>
                                            </c:if>
                                            <c:if test="${lnr.lastNodeReading.stringReading != null}">
                                                <td class="reading" style="color :red">${lnr.lastNodeReading.stringReading}</td>
                                            </c:if>
                                            <c:if test="${lnr.lastNodeReading.stringReading == null}">
                                                <td class="reading" style="color :red"></td>
                                           </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:if>
                        </tr>
                    </c:if>
                </c:if>
            </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <p style="color :red"> No node status available</p>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${lastLinkReadings != null}">
        <h2>Links</h2>
        <table>
            <thead>
                <th>Link</th>
                <th>Capability</th>
                <th>Timestamp</th>
                <th>Double Reading</th>
                <th>String Reading</th>
            </thead>
            <tbody>
            <c:forEach items="${lastLinkReadings}" var="llr">
                <c:if test="${llr != null}">
                    <tr>
                        <td>
                            <a href="<c:url value="/rest/testbed/${testbed.id}/link/${llr.link.source.name}/${llr.link.target.name}"/>"><c:out value="[${llr.link.source.name},${llr.link.target.name}]"/></a>
                        </td>
                        <td>
                            <a href="<c:url value="/rest/testbed/${testbed.id}/capability/${llr.capability.name}"/>"><c:out value="${llr.capability.name}"/></a>
                        </td>
                        <c:if test="${llr.lastLinkReading != null}">
                            <c:if test="${llr.lastLinkReading.timestamp != null}">
                                <c:choose>
                                    <c:when test="${util:checkIfDateIsToday(llr.lastLinkReading.timestamp)}">
                                        <td>${llr.lastLinkReading.timestamp}</td>
                                       <c:if test="${llr.lastLinkReading.reading != null}">
                                           <td>${llr.lastLinkReading.reading}</td>
                                       </c:if>
                                       <c:if test="${llr.lastLinkReading.reading == null}">
                                           <td></td>
                                       </c:if>
                                       <c:if test="${llr.lastLinkReading.stringReading != null}">
                                            <td>${llr.lastLinkReading.stringReading}</td>
                                        </c:if>
                                        <c:if test="${llr.lastLinkReading.stringReading == null}">
                                            <td></td>
                                       </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <td style="color :red">${llr.lastLinkReading.timestamp}</td>
                                        <c:if test="${llr.lastLinkReading.reading != null}">
                                           <td style="color :red">${llr.lastLinkReading.reading}</td>
                                        </c:if>
                                        <c:if test="${llr.lastLinkReading.reading == null}">
                                           <td style="color :red"></td>
                                        </c:if>
                                        <c:if test="${llr.lastLinkReading.stringReading != null}">
                                            <td style="color :red">${llr.lastLinkReading.stringReading}</td>
                                        </c:if>
                                        <c:if test="${llr.lastLinkReading.stringReading == null}">
                                            <td style="color :red"></td>
                                       </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </c:if>
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <p style="color :red"> No link status available</p>
    </c:otherwise>
</c:choose>
<%@include file="/footer.jsp"%>
</body>
</html>
