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
    <p class="f20 text-font ml5">
        Project is intended to simplify connectivity over the internet to a remote device.
        It focus mostly on devices such as remote controlled robot with camera, but can be of use with any device able
        to run WebSocket technology.<br/><br/>
        One of project's main objectives is to maximize flexibility of usage: you can use installer available on the
        page to set up your device within minutes,
        modify existing code or write something completely custom - depending on your actual needs.<br/><br/>
        Later on this page you can find more information about usage, or give it a try taking control of one of
        available robots. Hope you will enjoy!<br/><br/>
    </p>
    <p class="f20 text-font" style="color: red">
        Note: Current status of this project is "work in progress" so don't be surprised if some things won't work as it
        should... Sorry for inconvenience.
    </p>
    <div class="flex f-column f-center page-border vm5">
        <h2 class="page-font f30 page-color">Try it yourself</h2>
        <div class="flex f-between mb3">
            <div class="table-element flex f-column f-center vertical-line f20">
                <h3 class="page-font page-color">Robots available</h3>
                <ul id="robots" class="list-none flex f-column">
                    <c:forEach items="${robots}" var="robot">
                        <li id="id${robot.id}" class="flex f-center mb3 f20">
                            <a href="/robot-articles?name=${robot.name.replaceAll(" ", "-").toLowerCase()}"
                               class="dev-btn f20 mr1 blue">
                                <img src="resources/icons/info.svg" class="dev-icon">
                            </a>
                            <button onclick="window.open('/control?id=${robot.id}', '_blank')" class="button-robot f20">
                                <c:out value="${robot.name}"/>
                                <canvas class="lamp-red"></canvas>
                            </button>
                            <label class="page-font ml2">${robot.connectedWith}</label>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <p class="table-element f20 text-font" style="text-align: center">
                If you wonder how it works in practice, just click a robot you want to try.<br/><br/>
                You will receive live video stream from camera and have full control over robot's functions.
                After connecting, to find out how to control the robot, click on the
                <img src="/resources/icons/info.svg" style="width: 1em; height: auto"/>
                button (top-right corner).<br/><br/>Note that robot need to be in "Ready" state to accept connection.
                Maximum duration of test run is 5 minutes. After this time, you will be disconnected.
            </p>
        </div>
    </div>
    <div id="art1" class="flex f-center article-head">
        <img src="/resources/icons/arrow-down-dark.svg" class="dev-panel-icon f30"/>
        <span class="page-font f30 ml2">How to start?</span>
    </div>
    <div id="art1cont" class="article-body page-border" style="display: none">
        <p class="text-font f20">
            First of all, you need a robot. Obviously... If you don't have one already, or you want to build one you've
            been playing with in "Try it yourself" section, click the
            <img src="/resources/icons/info.svg" style="width: 1em; height: auto"/> button near the robot name.
            You will get the information on what parts you need and how they should be put together.<br/><br/>
            Next, download desired package with robot program and unpack it somewhere on your Raspberry Pi.
            If you are not familiar with Raspberry Pi microcomputer, check out
            <a href="https://www.raspberrypi.org/documentation/installation/installing-images/" target="_blank">this
                article</a>
            for instructions on how to set it up. Robot program requires chromium browser to work - normally it is
            installed together with Raspberry Pi OS,
            but if you didn't use default installation, make sure you have it.
            From terminal, cd to "Robot" directory and run "bash install.sh" - script will install node js with
            necessary libraries
            and set automatic start of program at boot (entry in crontab).<br/><br/>
            After successful installation, you need to log in to your account (click "Register" if you don't have any)
            and click "New device".
            You will see something simillar to this:<br/><br/>
            <img src="/resources/img/settings.png" class="img-wide"><br/><br/>
            In robot program directory, open identity.js in text editor and paste values from page:<br/><br/>
            <img src="/resources/img/identity.png" class="img-wide"><br/><br/>
            Save changes and restart your device (or run bash start.sh under src dir). If everything went well, you
            should see in your "Devices"
            panel a green dot next to your device name - now you should be able to connect to it.<br/><br/>
            Connecting from outside local network:<br/><br/>
            To establish video connection, program uses WebRTC technology which - making the story short - connects p2p
            via UDP.
            It means that your NAT/Firewall need to be able to accept such. From my experience, the most reliable way to
            accomplish that is to set
            port forwarding on router to which device is connected. Unfortunately, WebRTC uses random ports by default.
            To force it to use specific ports,
            put file "robot_policy_ports.json" into "/etc/chromium-browser/policies/managed" folder. Now you can set
            port forwarding for ports
            30000 and 30001 (or modify "robot_policy_ports.json" if you prefer different ports).
        </p>
    </div>
    <div id="art2" class="flex f-center article-head">
        <img src="/resources/icons/arrow-down-dark.svg" class="dev-panel-icon f30"/>
        <span class="page-font f30 ml2">How this service works?</span>
    </div>
    <div id="art2cont" class="article-body page-border flex f-column text-font f20" style="display: none">
        <h2>Overview</h2>
        <p>
            Here is a quick overview of what's going on under the hood when you click a button with robot name:
        <ol>
            <li>
                By clicking the button, you send GET request to "/control" context with "id" parameter set to id of
                robot you want to connect with. Service verifies if it is ok to connect (robot is in "ready" state and
                you are authorized to control it) and, if so, responds sending an HTML page equipped with browser
                control script which is assigned to this robot (here we assume that it is a default script, you can
                find more about assigning scripts in "Customization" section).
            </li><br/>
            <li>
                As the page is loaded, it displays "Initializing..." message. Script connects to "/endpoint" context
                with WebSocket (using STOMP over SockJS), and, when connection is established, sends SUBSCRIBE message
                to "/app/begin", appending robot id in "robotId" header. Once again service checks if connection can be
                established and responds with a message in format {"type":"message","data":**message text**} (message
                text depends on the result of verification). Script display this message on the page. If it is ok to
                connect, service registers connection and sends "start" message to robot in format
                {"type":"start","data":""}.
            </li><br/>
            <li>
                When robot receives "start" message, it starts establishing WebRTC connection. I don't want to get too
                much in details about WebRTC connection establishing. It is quite a long story, and is already well
                described on other pages (ie. <a href="https://www.baeldung.com/webrtc" target="_blank">here</a>).
                What is important in this scope, is that both robot and browser script exchanges signalling metadata
                through "/app/signalling" context, sending messages in format<br/>
                {"type":**type of message**,"data":**message payload**}<br/>
                with header {"caller": "robot"} or {"caller": "user"} respectively.
            </li>
        </ol>
        </p>
    </div>
</div>

<%@include file="footer.jsp" %>
</body>
</html>
