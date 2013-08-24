<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<jsp:useBean id="nodes" scope="request" class="java.util.ArrayList"/>

<html>
<head>
    <META NAME="Description" CONTENT="ÜberDust"/>
    <META http-equiv="Content-Language" content="en"/>
    <META http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>ÜberDust - Create Virtual Node</title>
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
        function init() {
            Blockly.inject(document.getElementById('blocklyDiv'),
                    {path: '../../', toolbox: document.getElementById('toolbox')});
            // Let the top-level application know that Blockly is ready.
            window.parent.blocklyLoaded(Blockly);
        }
    </script>

</head>
<body>
<%@include file="/header.jsp" %>
<body onload="init()">
<%@include file="/header.jsp" %>

<div class="container">
    <div class="span6">
        <button class="btn btn-large btn-primary">Create Node!</button>
    </div>
    <div id="blocklyDiv">
        <xml id="toolbox" style="display: none">
            <block type="virtual_node"></block>
            <block type="node"></block>
            <block type="text"></block>
            <!--<block type="controls_repeat_ext"></block>
            <block type="math_number"></block>
            <block type="math_arithmetic"></block>
            <block type="text_print"></block>-->
        </xml>
    </div>
    <%@include file="/footer.jsp" %>
</body>
</html>
