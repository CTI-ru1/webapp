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


<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <a class="brand" href="<c:url value="/"/>">Überdust</a>
        <ul class="nav">
            <li><a href="<c:url value="/rest/testbed"/>">testbeds</a></li>
            <c:if test="${testbed != null}">
                <li><a href="<c:url value="/rest/testbed/${testbed.id}"/>">testbed</a></li>
                <li><a href="<c:url value="/rest/testbed/${testbed.id}/node"/>">nodes</a></li>
                <li><a href="<c:url value="/rest/testbed/${testbed.id}/link"/>">links</a></li>
                <li><a href="<c:url value="/rest/testbed/${testbed.id}/capability"/>">capabilies</a></li>
                <li><a href="<c:url value="/rest/testbed/${testbed.id}/virtualnode"/>">virtual</a></li>
                <li><a href="<c:url value="/rest/testbed/${testbed.id}/status"/>">status</a></li>
            </c:if>
        </ul>
        <ul class="nav pull-right">
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
                        <form name='f' class="navbar-search pull-left"
                              action="<c:url value='/j_spring_security_check'/>"
                              method='POST'>
                            <input type='text' name='j_username' class="input-small" placeholder="username"
                                   value=''>
                            <input type='password' name='j_password' class="input-small" placeholder="password"/>
                            <input name="submit" type="submit" value="submit" class="btn"/>
                        </form>
                    </li>
                    <li>
                        <a href="<c:url value="/rest/register"/>">Register</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</div>






