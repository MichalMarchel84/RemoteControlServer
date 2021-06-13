<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 05.06.2021
  Time: 19:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>User settings</title>
</head>
<body class="flex">
<%@include file="panel.jsp" %>
<div class="dev-content flex f-column">
    <div class="flex f-column f-center page-border vm5">
        <h2 class="page-font f30 page-color">Device settings</h2>

        <form:form modelAttribute="robot" method="post" cssClass="flex f-column stg-table">
            <div class="flex f-column">
                <label class="f20 page-font flex f-baseline vmv2 page-color">
                    Robot name:
                    <form:input path="name" cssClass="f20 ml2 text-input"/>
                </label>
                <table>
                    <tr>
                        <td class="f20 page-font page-color">Robot id:</td>
                        <td class="text-font f20" style="color: black">${robot.id}</td>
                    </tr>
                    <tr>
                        <td class="f20 page-font page-color">Password:</td>
                        <td class="text-font f15" style="color: black">${robot.password}</td>
                    </tr>
                </table>
                <form:hidden path="id"/>
            </div>
            <ul id="cfg" class="list-none flex f-column f20 dev-list page-font f20">
                <c:forEach items="${robot.configurations}" var="config" varStatus="i">
                    <li class="flex f-column">
                        <label class="mb3 underline-dark page-color">${config.name}</label>
                        <input type="hidden" name="configurations[${i.index}].name" value="${config.name}">
                        <div class="flex f-wrap f-around mb3">
                            <c:forEach items="${config.params}" var="cfgParam" varStatus="j">
                                <label class="f15 mr1 pb2">
                                        ${cfgParam.name}
                                    <input name="configurations[${i.index}].params[${j.index}].value"
                                           value="${cfgParam.value}" class="stg-input f15 mr1">
                                </label>
                                <input type="hidden" name="configurations[${i.index}].params[${j.index}].name"
                                       value="${cfgParam.name}">
                            </c:forEach>
                        </div>
                    </li>
                </c:forEach>
            </ul>
            <div class="flex f-between">
<%--                <label class="flex f-center f-column page-font f20 page-color">--%>
<%--                    Custom script--%>
<%--                    <form:textarea path="script" cssClass="stg-script"/>--%>
<%--                </label>--%>
                <div class="flex f-column f-around">
                    <button type="submit" class="button-blue f30 shadow page-font">Save</button>
                </div>
            </div>
        </form:form>

    </div>
</div>
</body>
</html>
