<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 05.06.2021
  Time: 11:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<html>
<head>
    <title>Log in</title>
    <link rel="stylesheet" href="resources/css/style.css">
</head>
<body class="page-margin">
<%@include file="header.jsp" %>
<div class="flex f-column f-center vmt10">
    <label class="page-font page-color f30">${newAccount}</label>
    <div class="flex f-column f-center f-around page-border vp5">
        <label class="page-font page-color f30 mb3">Log in</label>
        <form method="post" action="/login" class="flex f-column">
            <input type="text" name="username" value="${fn:escapeXml(username)}" placeholder="User name" class="mb3 text-input f20">
            <input type="password" name="password" placeholder="Password" class="mb3 text-input f20">
            <input type="submit" class="button-blue page-font f20 shadow">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <label class="error-msg page-font f15">
            ${errMsg}
        </label>
    </div>
</div>
</body>
</html>
