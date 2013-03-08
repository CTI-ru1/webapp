<div id="footer" class="footer">
    <jsp:useBean id="time" scope="request" class="String"/>
    <c:if test="${time != ''}">
        page loaded in <c:out value="${time}"/> milliseconds
    </c:if>
</div>


<div id="realfooter" class="realfooter">
    <p style="text-align: center; margin: 0 0 10px; font-size:15px;">
        <a href="<c:url value="/"/>"> Home </a>
        |
        <a href="https://github.com/Uberdust/webapp/wiki"> Github Project</a>
        |
        <a href="http://ru1.cti.gr/index.php/software-a-systems/5-general-software/158-uberdust"> CTI & P - Ru1 </a>
        |
        <a href="<c:url value="/rest/help/"/>"> Help </a>
    </p>
</div>
