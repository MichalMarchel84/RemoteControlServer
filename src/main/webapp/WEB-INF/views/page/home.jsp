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
    <title>Home</title>
    <script src="resources/js/sockjs-0.3.4.js"></script>
    <script src="resources/js/stomp.js"></script>
    <script src="resources/js/home.js" defer></script>
    <link rel="stylesheet" href="resources/css/style.css">
</head>
<body class="page-margin">

<%@include file="header.jsp" %>

<div class="flex f-column">
    <h1 class="page-font f30 page-color mt5">Remote Control Project</h1>
    <p class="f20">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla quam velit, vulputate eu pharetra nec, mattis ac
        neque. Duis vulputate commodo lectus, ac blandit elit tincidunt id. Sed rhoncus, tortor sed eleifend tristique,
        tortor mauris molestie elit, et lacinia ipsum quam nec dui. Quisque nec mauris sit amet elit iaculis pretium sit
        amet quis magna. Aenean velit odio, elementum in tempus ut, vehicula eu diam. Pellentesque rhoncus aliquam
        mattis. Ut vulputate eros sed felis sodales nec vulputate justo hendrerit. Vivamus varius pretium ligula, a
        aliquam odio euismod sit amet. Quisque laoreet sem sit amet orci ullamcorper at ultricies metus viverra.
        Pellentesque arcu mauris, malesuada quis ornare accumsan, blandit sed diam.</p>
    <div class="flex f-column f-center page-border vm5">
        <h2 class="page-font f30 page-color">Try it yourself</h2>
        <div class="flex f-between mb3">
            <div class="table-element flex f-column f-center vertical-line f20">
                <h3 class="page-font page-color">Robots available</h3>
                <ul id="robots" class="list-none flex f-column">
                    <c:forEach items="${robots}" var="robot">
                        <li id="id${robot.id}" class="flex f-center mb3 f20">
                            <button onclick="window.open('/control?id=${robot.id}', '_blank')" class="button-robot f20">
                                    <c:out value="${robot.name}"/><canvas class="lamp-red"></canvas>
                            </button>
                            <label class="page-font ml2">${robot.connectedWith}</label>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <p class="table-element f20">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla quam velit,
                vulputate eu pharetra nec,
                mattis ac neque. Duis vulputate commodo lectus, ac blandit elit tincidunt id. Sed rhoncus, tortor sed
                eleifend tristique, tortor mauris molestie elit, et lacinia ipsum quam nec dui. Quisque nec mauris sit
                amet elit iaculis pretium sit amet quis magna. Aenean velit odio, elementum in tempus ut, vehicula eu
                diam. Pellentesque rhoncus aliquam mattis. Ut vulputate eros sed felis sodales nec vulputate justo
                hendrerit. Vivamus varius pretium ligula, a aliquam odio euismod sit amet.
            </p>
        </div>
    </div>
</div>
</body>
</html>
