<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 02.06.2021
  Time: 17:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Control</title>
    <script src="resources/js/sockjs-0.3.4.js"></script>
    <script src="resources/js/stomp.js"></script>
    <script ${script} defer></script>
    <link rel="stylesheet" href="resources/css/style.css">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Orbitron&display=swap" rel="stylesheet">
</head>
<body class="flex f-center f-around">
<div class="flex f-column f-center f-around">
    <div id="video-box" class="video flex f-center f-around" style="display: none">
        <video autoplay id="video" class="stream"></video>
    </div>
    <div class="flex f-center page-font f20 shadow control-wrap vmt3">
        <label id="head" data-id="${robotId}" class="control-head control-element flex f-center">${robotName}</label>
        <label id="message" class="control-element flex f-center f-around control-msg"></label>
        <label id="disconnect" class="control-tail control-element flex f-center">
            Disconnect
        </label>
    </div>
</div>
</body>
</html>
