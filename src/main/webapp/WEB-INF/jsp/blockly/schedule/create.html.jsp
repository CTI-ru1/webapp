<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="nodes" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="capabilities" scope="request" class="java.util.ArrayList"/>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>ÜberDust - Create Schedule</title>
    <%@include file="/head.jsp" %>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="<c:url value="/js/blockly/blockly_compressed.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/blockly/en_compressed.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/blockly/javascript_compressed.js"/>"></script>
    <script type="text/javascript">
        window.nodes_arr = [];
        <c:forEach items="${nodes}" var="node">
        window.nodes_arr.push(["<c:out value="${node.name}"/>", "<c:out value="${node.name}"/>"]);
        </c:forEach>
        window.capabilities_arr = [];
        <c:forEach items="${capabilities}" var="capability">
        window.capabilities_arr.push(["<c:out value="${capability.name}"/>", "<c:out value="${capability.name}"/>"]);
        </c:forEach>

    </script>
    <script type="text/javascript" src="<c:url value="/js/blockly/uberdust_blocks.js"/>"></script>
    <style>
        html, body {
            background-color: #fff;
            margin: 0;
            padding: 0;
            overflow: hidden;
            height: 100%;
        }

        .blocklySvg {
            height: 100%;
            width: 100%;
        }
    </style>
    <script>
        function generateCode() {
            fcode = Blockly.Generator.workspaceToCode('JavaScript');
            $("#generated").text(fcode);
        }
        function init() {
            setInterval(generateCode, 2000);
            Blockly.inject(document.getElementById('blocklyDiv'),
                    {path: '../../', toolbox: document.getElementById('toolbox')});
            // Let the top-level application know that Blockly is ready.
            window.parent.blocklyLoaded(Blockly);
        }
    </script>

</head>
<body onload="init()">
<%@include file="/header.jsp" %>

<script>
    function uploadRule() {
        $("#output").text("Generating Schedule...");
        try {
            jcode = JSON.parse(Blockly.Generator.workspaceToCode('JavaScript'));
            jcode.username = "${username}";
            $("#output").text("Planning...");
            $.post("add", jcode,function (data) {
                $("#output").text("Planned! Please Wait...");
                window.location = "<c:url value="/rest/testbed/${testbed.id}/schedule"/>";
            }).error(function (jqXHR, status, error) {
                        $("#output").text("An error occured: " + jqXHR.responseText);
                    });
        } catch (err) {
            $("#output").text("Rule is not complete.");
        }

    }
</script>

<div class="container">
    <div class="col-md-12">
        <div class="col-md-2" style="text-align: center;vertical-align: middle;">
            <button class="btn btn-large btn-primary" onclick="uploadRule();">Add Schdedule</button>
        </div>
        <div class="col-md-8" style="text-align: left;">
            <div class="col-md-6" style="text-align: left;visibility: hidden">Schedule Description:</div>
            <div class="col-md-6" style="text-align: center;visibility: hidden">
                <pre id="generated"></pre>
            </div>
            <span id="output" class="col-md-6"></span>
        </div>
    </div>
    <div id="blocklyDiv">
        <xml id="toolbox" style="display: none">
            <block type="schedule"></block>
            <block type="cronschedule"></block>
            <block type="oneoffschedule"></block>
        </xml>
    </div>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
