<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <script>
        const robotId = ${robotId};
        const configs = ${configs};
    </script>
    <script ${script} defer></script>
    <link rel="stylesheet" href="resources/css/style.css">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Orbitron&display=swap" rel="stylesheet">
</head>
<body class="flex f-center f-around">
<div class="flex f-column f-center f-around">
    <button id="info" class="dev-btn f20 ml2 blue control-info-btn"><img src="resources/icons/info.svg" class="dev-icon"></button>
    <div id="video-box" class="video flex f-center f-around" style="display: none">
        <video autoplay id="video" class="stream"></video>
    </div>
    <div class="flex f-center page-font f20 shadow control-wrap vmt3">
        <label id="head" class="control-head control-element flex f-center">
            <c:out value="${robotName}"/>
        </label>
        <label id="message" class="control-element flex f-center f-around control-msg">Initializing...</label>
        <label id="disconnect" class="control-tail control-element flex f-center">
            Disconnect
        </label>
    </div>
    <div id="info-cont" class="control-info-panel page-border flex f-column f-between f-center" style="display: none">
        <div class="flex f-column f-center">
            <p class="f30 page-color page-font text-center">Hold down keys listed below to move:</p>
            <table cellspacing="20" id="info-list" class="text-center text-font f20">
                <tr>
                    <th class="page-color">key</th><th class="page-color">command</th>
                </tr>
            </table>
        </div>
        <button id="close" class="button-blue f20 shadow page-font mb3">Close</button>
    </div>
</div>
</body>
</html>
