<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 03.06.2021
  Time: 09:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Orbitron&display=swap" rel="stylesheet">
</head>
<body>
<div class="flex f-between f-center underline-dark header-color">
    <div class="flex f-center">
        <img src="resources/icons/logo.svg" class="logo-icon">
        <label class="page-font f40">RC</label>
    </div>
    <div class="page-font f20">
        <a href="/home" class="link header-color">Home</a>
        <span>|</span>
        <a href="/download" class="link header-color">Download</a>
        <span>|</span>
        <a href="/register" class="link header-color">Register</a>
        <sec:authorize access="isAuthenticated()">
            <span>|</span>
            <a href="/user" class="link header-color">Devices</a>
        </sec:authorize>

        <sec:authorize access="!isAuthenticated()">
            <button onclick="window.location.href='/login'" class="button-blue shadow page-font f20 ml2">Log in</button>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <form action="<c:url value="/logout"/>" method="post" style="display: inline">
                <input type="submit" value="Log out" class="button-blue shadow page-font f20 ml2">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </sec:authorize>
    </div>
</div>
</body>
</html>
