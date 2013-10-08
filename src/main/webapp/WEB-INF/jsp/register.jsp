<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:useBean id="host" scope="request" class="java.lang.String"/>
<jsp:useBean id="node" scope="request" class="java.lang.String"/>
<jsp:useBean id="capability" scope="request" class="java.lang.String"/>
<jsp:useBean id="uuid" scope="request" class="java.lang.Integer"/>

<html>
<head>
    <META NAME="Description" CONTENT="Überdust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Überdust - Show Readings</title>
    <%@include file="/head.jsp" %>
    <script src="<c:url value="/js/md5.js"/>"></script>


    <script type="text/javascript">
        // Validates email address of course.
        function validEmail(e) {
            var filter = /^\s*[\w\-\+_]+(\.[\w\-\+_]+)*\@[\w\-\+_]+\.[\w\-\+_]+(\.[\w\-\+_]+)*\s*$/;
            return String(e).search(filter) != -1;
        }

        function checkValid() {
            $("#register").button('toggle');
            var email = $("#email").val();
            var username = $("#username").val();
            var uuid = $("#uuid").val();

            var password = $("#password1").val();
            if (username == '') {
                $("#error").text("Username cannot be empty.");
                return false;
            }

            if (!validEmail(email)) {
                $("#error").text("Provided email is incorrect.");
                return false;
            }
            if (password == '') {
                $("#error").text("Password cannot be empty.");
                return false;
            }
            if (password != $("#password2").val() || password == '') {
                $("#error").text("Passwords do not match.");
                return false;
            }
            $("#error").text("");
            $.post('user/' + username + '/' + email + '/' + CryptoJS.MD5(password) + '/' + uuid + '/', function (data) {
                $("#register").button('toggle');
                window.location = "<c:url value="/rest/user/"/>" + username;
            });


            return false;
        }
    </script>

</head>

<body>
<%@include file="/header.jsp" %>
<div class="container">
    <c:choose>
        <c:when test="${username==''}">

            <div style="text-align: center; padding-top: 40px;">
                <form class="form-horizontal" role="form" action="">
                    <input type="hidden" id="uuid" value="${uuid}"/>

                    <div class="form-group">
                        <label for="username" class="col-lg-2 control-label">Username</label>

                        <div class="col-lg-10">
                            <input type="text" id="username" placeholder="username" class="form-control"
                                   style="height:50px"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="email" class="col-lg-2 control-label">Email</label>

                        <div class="col-lg-10">
                            <input type="text" id="email" name="email" placeholder="email" class="form-control"
                                   style="height:50px"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="password1" class="col-lg-2 control-label">Password</label>

                        <div class="col-lg-10">
                            <input type="password" id="password1" placeholder="password" class="form-control"
                                   style="height:50px"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="password2" class="col-lg-2 control-label">Confirm Password</label>

                        <div class="col-lg-10">
                            <input type="password" id="password2" placeholder="retypepassword" class="form-control"
                                   style="height:50px"/>
                        </div>
                    </div>


                </form>
                <div id="error" style="color: red;"></div>

                <button id="register" onclick="checkValid();" class="btn btn-large">Register</button>
            </div>
        </c:when>
        <c:otherwise>
            <script type="text/javascript">
                window.location = "<c:url value="/rest/user/${username}"/>";
            </script>
        </c:otherwise>
    </c:choose>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
