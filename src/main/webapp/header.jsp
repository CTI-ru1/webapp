<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<spring:message code="uberdust.hudson.url" var="hudsonUrl" scope="application" text=""/>
<spring:message code="uberdust.hudson.build" var="hudsonBuild" scope="application" text=""/>
<spring:message code="uberdust.hudson.jobname" var="hudsonJobName" scope="application" text=""/>
<spring:message code="uberdust.webapp.version" var="uberdustWebappVersion" scope="application" text=""/>

<jsp:useBean id="username" scope="request" class="java.lang.String"/>


<c:set var="req" value="${pageContext.request}"/>
<c:set var="baseURL"
       value="${fn:replace(req.requestURL, fn:substring(req.requestURI,0, fn:length(req.requestURI)), req.contextPath)}"/>

<c:if test="${fn:length(hudsonUrl) !=0 && fn:length(hudsonBuild) !=0 && fn:length(hudsonJobName) !=0 && fn:length(uberdustWebappVersion) !=0}">
    <p style="font-size:small">
        Überdust [<a href="${hudsonUrl}job/${hudsonJobName}/${hudsonBuild}">v${uberdustWebappVersion}b${hudsonBuild}</a>]
    </p>
</c:if>


</div>


<header class="navbar navbar-fixed-top">
    <nav class="navbar navbar-default navbar-static-top" role="navigation">
        <div class="navbar-header">
            <a class="navbar-brand" href="<c:url value="/rest/testbed/"/>">Überdust</a>
        </div>
        <ul class="nav navbar-nav navbar-left">
            <li><a href="<c:url value="/rest/testbed"/>">testbeds</a></li>
            <c:if test="${testbed != null}">
                <li><a href="<c:url value="/rest/testbed/${testbed.id}"/>">testbed</a></li>
                <li><a href="<c:url value="/rest/testbed/${testbed.id}/node"/>">nodes</a></li>
                <li><a href="<c:url value="/rest/testbed/${testbed.id}/link"/>">links</a></li>
                <li><a href="<c:url value="/rest/testbed/${testbed.id}/capability"/>">capabilies</a></li>
                <li><a href="<c:url value="/rest/testbed/${testbed.id}/virtualnode"/>">virtual</a></li>
                <li><a href="<c:url value="/rest/testbed/${testbed.id}/schedule"/>">schedules</a></li>
                <li><a href="<c:url value="/rest/testbed/${testbed.id}/status"/>">status</a></li>
            </c:if>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <c:choose>
                <c:when test="${username!=''}">
                    <li>
                        <a href="<c:url value="/rest/user/${username}"/>">${username}</a>
                    </li>
                    <li>
                        <a href="<c:url value="/j_spring_security_logout"/>">Logout</a>
                    </li>

                </c:when>
                <c:otherwise>
                    <li>
                        <form class="navbar-form navbar-right"
                              action="<c:url value='/j_spring_security_check'/>"
                              method='POST'>
                            <div class="form-group">
                                <input type='text' name='j_username' class="form-control" placeholder="username"
                                       value='' style='width:100px'>
                                <input type='password' name='j_password' class="form-control" placeholder="password"
                                       style='width:100px'/>
                                <input name="submit" type="submit" value="submit" class="btn"/>
                            </div>
                        </form>
                    </li>
                    <li>
                        <a href="<c:url value="/rest/register"/>">Register</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
</header>






