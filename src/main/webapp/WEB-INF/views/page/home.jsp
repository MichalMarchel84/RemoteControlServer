<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 29.05.2021
  Time: 14:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Home</title>
    <script src="resources/js/sockjs-0.3.4.js"></script>
    <script src="resources/js/stomp.js"></script>
    <script src="resources/js/home.js" defer></script>
    <link rel="stylesheet" href="resources/css/style.css">
</head>
<body class="page-margin">

<%@include file="header.jsp" %>

<div class="flex f-column">
    <h1 class="page-font f30 page-color mt5">Remote Control Project</h1>
    <p class="f20 text-font">
        Project is intended to simplify connectivity over the internet to a remote device.
        It focus mostly on devices such as remote controlled robot with camera, but can be of use with any device able to run WebSocket technology.<br/><br/>
        One of project's main objectives is to maximize flexibility of usage: you can use installer available on the page to set up your device within minutes,
        modify existing code or write something completely custom - depending on your actual needs.<br/><br/>
        Later on this page you can find more information about usage, or give it a try taking control of one of available robots. Hope you will enjoy!<br/><br/>
    </p>
    <p class="f20 text-font" style="color: red">
        Note: Current status of this project is "work in progress" so don't be surprised if some things won't work as it should... Sorry for inconvenience.
    </p>
    <div class="flex f-column f-center page-border vm5">
        <h2 class="page-font f30 page-color">Try it yourself</h2>
        <div class="flex f-between mb3">
            <div class="table-element flex f-column f-center vertical-line f20">
                <h3 class="page-font page-color">Robots available</h3>
                <ul id="robots" class="list-none flex f-column">
                    <c:forEach items="${robots}" var="robot">
                        <li id="id${robot.id}" class="flex f-center mb3 f20">
                            <button onclick="window.open('/control?id=${robot.id}', '_blank')" class="button-robot f20">
                                    <c:out value="${robot.name}"/><canvas class="lamp-red"></canvas>
                            </button>
                            <label class="page-font ml2">${robot.connectedWith}</label>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <p class="table-element f20 text-font" style="text-align: center">
                If you wonder how it works in practice, just click a robot you want to try.<br/><br/>
                You will receive live video stream from camera and have full control over robot's functions.
                After connecting, to find out how to control the robot, click on the <img src="/resources/icons/info.svg" style="width: 1em; height: auto"/>
                button (top-right corner).<br/><br/>Note that robot need to be in "Ready" state to accept connection.
                Maximum duration of test run is 5 minutes. After this time, you will be disconnected.
            </p>
        </div>
    </div>
</div>
</body>
</html>
