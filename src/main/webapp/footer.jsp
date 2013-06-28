<div class="navbar navbar-fixed-bottom">
    <div class="navbar-inner">
        <ul class="nav">
            <li><a href="https://github.com/Uberdust/webapp/wiki"> Github Project</a></li>
            <li><a href="http://ru1.cti.gr/index.php/software-a-systems/5-general-software/158-uberdust"> CTI & P -
                Ru1 </a>
            </li>
            <li><a href="<c:url value="/rest/help/"/>"> Help </a></li>
            <jsp:useBean id="time" scope="request" class="String"/>
            <c:if test="${time != ''}">
                <li><a href="<c:url value="/rest/statistics"/>"> page loaded in <c:out value="${time}"/>
                    milliseconds</a></li>
            </c:if>

        </ul>
    </div>
</div>
<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<c:url value="/js/jquery.js"/>"></script>
<script src="<c:url value="/js/bootstrap-transition.js"/>"></script>
<script src="<c:url value="/js/bootstrap-alert.js"/>"></script>
<script src="<c:url value="/js/bootstrap-modal.js"/>"></script>
<script src="<c:url value="/js/bootstrap-dropdown.js"/>"></script>
<script src="<c:url value="/js/bootstrap-scrollspy.js"/>"></script>
<script src="<c:url value="/js/bootstrap-tab.js"/>"></script>
<script src="<c:url value="/js/bootstrap-tooltip.js"/>"></script>
<script src="<c:url value="/js/bootstrap-popover.js"/>"></script>
<script src="<c:url value="/js/bootstrap-button.js"/>"></script>
<script src="<c:url value="/js/bootstrap-collapse.js"/>"></script>
<script src="<c:url value="/js/bootstrap-carousel.js"/>"></script>
<script src="<c:url value="/js/bootstrap-typeahead.js"/>"></script>
