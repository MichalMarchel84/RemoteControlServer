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
    <title>Remote Control</title>
    <script src="resources/js/sockjs-0.3.4.js"></script>
    <script src="resources/js/stomp.js"></script>
    <script src="resources/js/script.js" defer></script>
</head>
<body>
<h1>Home Page</h1>
<sec:authorize access="!isAuthenticated()">
        <button onclick="window.location.href='/login'">Login</button>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
    <form action="<c:url value="/logout"/>" method="post">
        <input type="submit" value="Logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</sec:authorize>
<ul id="robots">
    <c:forEach items="${robots}" var="robot">
        <li id="id${robot.id}">
            <button>${robot.name}</button>
            <label>Status: ${robot.connectedWith}</label>
        </li>
    </c:forEach>
</ul>
</body>
</html>
