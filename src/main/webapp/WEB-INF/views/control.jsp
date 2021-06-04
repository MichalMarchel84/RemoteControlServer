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
<body style="margin: 0">
<div class="flex f-column f-center">
    <div class="video flex f-center f-around">
        <video autoplay id="video" class="stream"></video>
    </div>
    <div class="flex f-baseline page-font f30 vmt3">
        <label id="head" data-id="${robotId}">${robotName}</label>
        <label id="message">Test</label>
        <button id="disconnect" style="display: none">Disconnect</button>
    </div>
</div>
</body>
</html>
