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
        function uploadRule() {

        }
    </script>

</head>
<body onload="init()">
<%@include file="/header.jsp" %>

<div class="container">
    <div class="span12">
        <div class="span2" style="text-align: center;vertical-align: middle;">
            <button class="btn btn-large btn-primary" onclick="uploadRule();">Add Schdedule</button>
        </div>
        <div class="span8" style="text-align: left;">
            <div class="span6" style="text-align: left;">Schedule Description:</div>
            <div class="span6" style="text-align: center;">
                <pre id="generated"></pre>
            </div>
        </div>
    </div>
    <div id="blocklyDiv">
        <xml id="toolbox" style="display: none">
            <block type="schedule"></block>
            <block type="command"></block>
        </xml>
    </div>
</div>
<%@include file="/footer.jsp" %>
</body>
</html>
