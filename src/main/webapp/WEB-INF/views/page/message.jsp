<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 13.06.2021
  Time: 15:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Contact form</title>
    <link rel="stylesheet" href="resources/css/style.css">
</head>
<body>
<%@include file="header.jsp" %>
<div class="flex f-column f-center vmt10">
    <p class="page-font text-center page-color f20 mb3">${pageMsg}</p>
    <div class="flex f-column f-center f-around page-border vp5">
        <label class="page-font page-color f30 mb3">Contact form</label>
        <form class="flex f-column f-center" method="post" action="/message" id="form">
            <input type="text" name="email" placeholder="Your email" class="mb3 text-input f20" style="width: max(300px, 50vw)"/>
            <textarea form="form" name="message" placeholder="Message" class="mb3 f15" style="width: max(300px, 50vw); height: 15em"></textarea>
            <input type="submit" value="Send" class="button-blue page-font f30 shadow">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </div>
</div>
<%@include file="footer.jsp" %>
</body>
</html>
