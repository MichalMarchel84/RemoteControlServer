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
                After connecting, to find out how to control the robot, click the
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
        <h2>Establishing connection</h2>
        <p>
            Here is a quick overview of what's going on under the hood when you click a button with robot name:
        <ol>
            <li>
                By clicking the button, you send GET request to "/control" context with "id" parameter set to id of
                robot you want to connect with. Service verifies if it is ok to connect (robot is in "Ready" state and
                you are authorized to control it) and, if so, responds sending an HTML page equipped with browser
                control script which is assigned to this robot (here we assume that it is a default script, you can
                find more about assigning scripts in "Customization" section).
            </li><br/>
            <li>
                As the page is loaded, it displays "Initializing..." message. Script connects to "/endpoint" context
                with WebSocket (using STOMP over SockJS), and, when connection is established, sends SUBSCRIBE message
                to "/app/begin", appending robot id in "robotId" header. Once again service checks if connection can be
                established and responds with a message in format {"type":"message","data":"**message text**"} (message
                text depends on the result of verification). Script display this message on the page. If it is ok to
                connect, service registers connection and sends "start" message to robot in format
                {"type":"start","data":""}. Robot status in "Devices" panel changes to "In use".
            </li><br/>
            <li>
                When robot receives "start" message, it initiates establishing WebRTC connection. I don't want to get too
                much in details about WebRTC connection establishing. It is quite a long story, and is already well
                described on other pages (ie. <a href="https://www.baeldung.com/webrtc" target="_blank">here</a>).
                What is important in this scope, is that both robot and browser script exchanges signalling metadata
                through "/app/signalling" context, sending messages in format<br/><br/>
                {"type":"**type of message**","data":"**message payload**"}<br/><br/>
                with header {"caller": "robot"} or {"caller": "user"} respectively. Service simply relaying this
                messages to their destination.
            </li><br/>
            <li>
                Now - if everything went well - browser script and robot are connected directly peer-to-peer. Robot
                is streaming video from it's camera and browser script relays keyboard inputs and mouse events to
                the robot through WebRTC datachannel. At this point, robot notifies service sending
                {"type":"connected","data":"Connected"} to "/app/reports" and service relays it to browser script
                as {"type":"message","data":"Connected"}
                - message on page changes to "Connected"
            </li>
        </ol><br/><br/>
        When WebRTC connection gets closed, robot sends
        {"type":"disconnect","data":"**message to be displayed on page**"} to "/app/reports". Service removes
        connection from registry, robot status in "Devices" panel changes to "Ready"
        </p>
        <h2>Robot authentication</h2>
        <p>
            When robot program starts, it connects to "/endpoint" context via WebSocket and sends SUBSCRIBE message
            to "/app/authenticate", passing it's id and password from "identity.js" file in message headers "robotId"
            and "robotPass". If verification ends with success, robot is registered and accessible. Status in "Devices"
            panel changes to "Ready".<br/><br/>
            If robot's WebSocket gets closed, service removes robot from registry. Robot status in "Devices" panel
            changes to "Offline"
        </p>
    </div>
    <div id="art3" class="flex f-center article-head">
        <img src="/resources/icons/arrow-down-dark.svg" class="dev-panel-icon f30"/>
        <span class="page-font f30 ml2">Customization</span>
    </div>
    <div id="art3cont" class="article-body page-border flex f-column text-font f20" style="display: none">
        <p>
            As it was mentioned before, after connection between client and robot is established, server is no longer
            involved in communication. Therefore, everything what happens next, depends on how it was coded in client
            side script and robot side program. You can download the code for both ends from "Download" section for
            reference and refactor it as you like. Below you can find some further explanation on how it works.
        </p>
        <h2>Robot side program</h2>
        <p>
            Inside robot/src directory, there are two .js files: connection.js and core.js<br/><br>
            connection.js - here is the code responsible for establishing WebRTC connection, streaming media to client
            and forwarding control messages to core.js. By default, it streams only video, but if you're planning to
            install a microphone on your device, find function startVideo() and set "audio" constraint to
            "true".<br/>
            Debugging can be done by accessing chromium console available via VNC remote desktop :1 or by function
            logCore(value) which will write "value" to program console<br/><br/>
            core.js - this code starts connection.js in chromium browser (by launchNetwork() function) and controls
            robot hardware by interpreting control messages.<br/>
            Hardware control is made by instances of classes motor, servo and light. Respective GPIO pin numbers are
            specified in constants declared on the beginning of script.<br/>
            Interpretation of control messages is done by handleKeys(keys) and handleMouse(mouse) functions<br/><br/>
            Any changes to the above mentioned files will be applied after restarting program (you can reboot device or
            shut down program manually and run "bash start.sh" command from console).<br/><br/>
            log.txt - when program is started automatically from crontab, it generates log.txt file under robot/src
            directory. If program fails to start automatically - here you can find out what happened.
        </p>
        <h2>Client side script</h2>
        <p>
            If you don't specify any custom client side script, a default one will be loaded when opening connection to
            a device. It will display video stream from robot and pass keyboard and mouse actions to the robot.<br/><br/>
            If you like to have something else, you can create your own script and upload it by clicking "Scripts" tab
            in "Devices" panel. As the script is ready, it can be assigned to a device from device settings panel (
            under "Browser control script").
        </p>
        <h2>Configurations</h2>
        <p>
            Configurations are optional - you don't have to use it, but it can be useful in some situations.<br/><br/>
            Generally, configurations is an array of configuration objects which can be sent to "/app/config" as
            payload of STOMP message after robot was authenticated. Service will replace it's values with values stored
            in database (if they exists) and send configurations back to robot. New configurations will be stored in
            database and available anytime in device settings. They will be also available from browser control script.
            <br/><br/>
            For example, this configurations from "Tracked Robot" core.js
            <br/><br>
            <img src="/resources/img/config-file.png" class="img-wide"><br/><br/>
            <br/><br/>
            Converts to such view in Tracked Robot device settings (Video configuration is defined in connection.js):
            <br/><br>
            <img src="/resources/img/config-page.png" class="img-wide"><br/><br/>
            <br/><br/>
            Now, you don't need to modify these values in your code directly. Any changes done in device settings will
            be forwarded to device (although it's up to the device what it is going to do with it).
        </p>
    </div>
</div>

<%@include file="footer.jsp" %>
</body>
</html>
