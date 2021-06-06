<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 05.06.2021
  Time: 19:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Panel</title>
    <link rel="stylesheet" href="/resources/css/style.css">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Orbitron&display=swap" rel="stylesheet">
</head>
<body>
<div class="dev-panel flex f-column page-font f30">
    <div class="flex f-center underline-bright pb2 mb3">
        <img src="/resources/icons/user.svg" class="user-img">
        <label><c:out value="${userName}"/></label>
    </div>
    <label onclick="window.location.href='/user'" class="dev-panel-item">
        <div class="flex f-between f-center">
            Dashboard
            <img src="/resources/icons/arrow-bright.svg" class="dev-panel-icon">
        </div>
    </label>
    <label onclick="window.location.href='/user/new'" class="dev-panel-item">
        <div class="flex f-between f-center">
            New device
            <img src="/resources/icons/arrow-bright.svg" class="dev-panel-icon">
        </div>
    </label>
    <label onclick="window.location.href='/user/settings'" class="dev-panel-item">
        <div class="flex f-between f-center">
            Settings
            <img src="/resources/icons/arrow-bright.svg" class="dev-panel-icon">
        </div>
    </label>
    <form action="<c:url value="/logout"/>" method="post" class="dev-panel-item flex">
        <button type="submit" class="flex f-center f-between dev-panel-logout page-font f30">
            Log out
            <img src="/resources/icons/power.svg" class="dev-panel-icon">
        </button>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>
</body>
</html>
