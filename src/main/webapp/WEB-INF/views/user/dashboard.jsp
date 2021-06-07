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
</head>
<body class="flex">
<%@include file="panel.jsp"%>
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
                    <button id="info" class="dev-btn f20 mr1 blue"><img src="resources/icons/info.svg" class="dev-icon"></button>
                    <button id="settings" onclick="window.open('/user/settings?robotId=${robot.id}')" class="dev-btn f20 mr1 orange"><img src="resources/icons/settings.svg" class="dev-icon"></button>
                    <button id="delete" class="dev-btn f20 mr2 red"><img src="resources/icons/delete.svg" class="dev-icon"></button>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
</body>
</html>
