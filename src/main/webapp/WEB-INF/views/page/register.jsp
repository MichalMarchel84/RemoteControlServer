<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 05.06.2021
  Time: 10:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="resources/css/style.css">
</head>
<body class="page-margin">
<%@include file="header.jsp" %>
<div class="flex f-column f-center vmt10">
    <div class="flex f-column f-center f-around page-border vp5">
        <label class="page-font page-color f30 mb3">Create new account</label>
        <form:form method="post" modelAttribute="user" cssClass="flex f-column f-center">
            <span class="error-msg page-font">${nameError}</span>
            <form:errors path="username" cssClass="error-msg page-font"/>
            <form:input path="username" placeholder="User name" cssClass="mb3 text-input f20"/>
            <form:errors path="password" cssClass="error-msg page-font"/>
            <form:password path="password" placeholder="Password" cssClass="mb3 text-input f20"/>
            <span class="error-msg page-font">${repeatError}</span>
            <input type="password" name="repeat" placeholder="Repeat password" class="mb3 text-input f20">
            <input type="submit" class="button-blue page-font f20 shadow">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
</body>
</html>
