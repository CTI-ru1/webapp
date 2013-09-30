<spring:message code="uberdust.hudson.url" var="hudsonUrl" scope="application" text=""/>
<spring:message code="uberdust.hudson.build" var="hudsonBuild" scope="application" text=""/>
<spring:message code="uberdust.hudson.jobname" var="hudsonJobName" scope="application" text=""/>
<spring:message code="uberdust.webapp.version" var="uberdustWebappVersion" scope="application" text=""/>


<header class="navbar navbar-fixed-bottom" style="margin-bottom: -25px">
    <nav class="navbar navbar-default navbar-static-bottom" role="navigation">
        <div class="navbar-header">
        </div>
        <ul class="nav navbar-nav navbar-left">

            <li><a href="https://github.com/Uberdust/webapp/wiki"> Github Project</a></li>
            <li><a href="http://ru1.cti.gr/index.php/software-a-systems/5-general-software/158-uberdust"> CTI -
                Ru1 </a>
            </li>
            <li><a href="<c:url value="/rest/help/"/>"> Help </a></li>
            <li><a href="<c:url value="/rest/apps/"/>"> Apps </a></li>
            <li><a> v${uberdustWebappVersion}b${hudsonBuild} </a></li>
        </ul>

        <ul class="nav navbar-nav navbar-right">
            <jsp:useBean id="time" scope="request" class="String"/>
            <c:if test="${time != ''}">
                <li><a href="<c:url value="/rest/statistics"/>"> page loaded in <c:out value="${time}"/>
                    milliseconds</a></li>
            </c:if>

        </ul>
    </nav>
</header>
<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<c:url value="/js/jquery.js"/>"></script>
<script src="<c:url value="/js/bootstrap.js"/>"></script>
