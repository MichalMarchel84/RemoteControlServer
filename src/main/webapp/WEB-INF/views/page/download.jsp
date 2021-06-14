<%--
  Created by IntelliJ IDEA.
  User: falcon
  Date: 13.06.2021
  Time: 13:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Download</title>
    <link rel="stylesheet" href="resources/css/style.css">
</head>
<body class="page-margin">
<%@include file="header.jsp" %>
<table class="text-font f30 vmv2" cellspacing="20">
    <tr>
        <td>
            Default browser control script
        </td>
        <td>
            <a href="/resources/js/control.js" download><img src="/resources/icons/download.png" class="dev-panel-icon"></a>
        </td>
    </tr>
    <tr>
        <td>
            Program for tracked robot
        </td>
        <td>
            <a href="/tracked_robot.zip" download><img src="/resources/icons/download.png" class="dev-panel-icon"></a>
        </td>
    </tr>
</table>
<%@include file="footer.jsp" %>
</body>
</html>
