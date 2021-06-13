<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 13.06.2021
  Time: 16:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Scripts</title>
    <script src="/resources/js/scripts.js" defer></script>
</head>
<body class="flex">
<%@include file="panel.jsp" %>
<div class="dev-content flex f-column" style="margin-top: auto; margin-bottom: auto">
    <div class="flex f-column f-center page-border vm5">
        <h2 class="page-font f30 page-color">Stored scripts</h2>
        <ul id="scripts" class="list-none flex f-column f20 dev-list page-font f20">
            <c:forEach items="${scripts}" var="script" varStatus="status">
                <li class="flex f-column mb3 f20" id="id${script.id}" data-id="${script.id}">
                    <div class="flex f-center">
                        <div class="flex f-column f-column-reverse" style="align-self: stretch">
                            <label class="page-font page-color f20 ml5">${script.name}</label>
                        </div>
                        <label class="underline-dark dev-list-msg flex f-center"></label>
                        <button id="edit"
                                class="dev-btn f20 mr1 orange">
                            <img src="/resources/icons/edit.png" class="dev-icon">
                        </button>
                        <button id="delete" class="dev-btn f20 mr5 red">
                            <img src="/resources/icons/delete.svg" class="dev-icon">
                        </button>
                    </div>
                    <form method="post" id="form${script.id}" class="flex f-between ml5 mr5 vmt3" style="display: none">
                        <input type="hidden" name="id" value="${script.id}">
                        <label class="f20 page-font page-color">
                            Script name
                            <input type="text" name="name" value="${script.name}" class="f20 text-font script-name ml2">
                        </label>
                        <input type="submit" value="Save" class="f20 button-blue shadow">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>
                    <textarea name="content" form="form${script.id}" rows="20" class="mr5 ml5"
                              style="display: none">${script.script}</textarea>
                </li>
            </c:forEach>
        </ul>
    </div>
    <div class="flex f-column f-center page-border vm5">
        <h2 class="page-color page-font f30">Create new script</h2>
        <div class="flex f-column vm5" style="width: 100%">
            <form method="post" id="newScript" class="flex f-between ml5 mr5 vmt3">
                <input type="hidden" name="id" value="0">
                <label class="f20 page-font page-color">
                    Script name
                    <input type="text" name="name" class="f20 text-font script-name ml2">
                </label>
                <input type="submit" value="Create" class="f20 button-blue shadow">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
            <textarea name="content" form="newScript" rows="10" class="mr5 ml5" placeholder="Paste your script here"></textarea>
        </div>
    </div>
</div>
</body>
</html>
