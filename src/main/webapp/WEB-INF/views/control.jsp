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
</head>
<body>
    <h2 id="head" data-id="${robotId}">${robotName}</h2>
    <div>
        <video autoplay id="video" class="video" style="display: none"></video>
    </div>
    <button id="disconnect" style="display: none">Disconnect</button>
    <label id="message"></label>
</body>
</html>
