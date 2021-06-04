<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 29.05.2021
  Time: 14:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Devices</title>
    <link rel="stylesheet" href="resources/css/style.css">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Orbitron&display=swap" rel="stylesheet">
</head>
<body style="margin: 0" class="flex">
<div class="dev-panel flex f-column page-font f30">
    <div class="flex f-center underline-bright pb2 mb3">
        <img src="resources/icons/user.svg" class="user-img">
        <label><c:out value="${userName}"/></label>
    </div>
    <label class="dev-panel-item">
        <div class="flex f-between f-center">
            New device
            <img src="resources/icons/arrow-bright.svg" class="dev-panel-icon">
        </div>
    </label>
    <label class="dev-panel-item">
        <div class="flex f-between f-center">
            Settings
            <img src="resources/icons/arrow-bright.svg" class="dev-panel-icon">
        </div>
    </label>
    <form action="<c:url value="/logout"/>" method="post" class="dev-panel-item flex">
        <button type="submit" class="flex f-center f-between dev-panel-logout page-font f30">
            Log out
            <img src="resources/icons/power.svg" class="dev-panel-icon">
        </button>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>
<div class="dev-content flex f-column">
    <div class="flex f-column f-center page-border vm5">
        <h2 class="page-font f30 page-color">Registered devices</h2>
        <ul id="robots" class="list-none flex f-column f20 dev-list page-font f20">
            <c:forEach items="${robots}" var="robot">
                <li id="id${robot.id}" class="flex f-center mb3 f20">
                    <button onclick="window.open('/control?id=${robot.id}', '_blank')" class="button-robot f20 ml2">
                        <c:out value="${robot.name}"/>
                        <canvas class="lamp-red"></canvas>
                    </button>
                    <label class="underline-dark dev-list-msg flex f-center"></label>
                    <button class="dev-btn f20 mr1 blue"><img src="resources/icons/info.svg" class="dev-icon"></button>
                    <button class="dev-btn f20 mr1 orange"><img src="resources/icons/settings.svg" class="dev-icon"></button>
                    <button class="dev-btn f20 mr2 red"><img src="resources/icons/delete.svg" class="dev-icon"></button>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
</body>
</html>
