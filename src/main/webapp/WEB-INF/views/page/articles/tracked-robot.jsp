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
    <title>Tracked robot</title>
    <link rel="stylesheet" href="resources/css/style.css">
</head>
<body>
<%@include file="../header.jsp" %>
<div class="flex f-column f-center page-border vm5">
    <h2 class="page-font f30 page-color">Tracked robot</h2>
    <div class="flex f-between mb3">
        <div class="table-element flex f-column f-center vertical-line f20">
            <img src="/resources/img/tracked_robot.jpg" class="expandable"/>
        </div>
        <p class="table-element f20 text-font" style="text-align: center">
            Robot with track suspension and turret equipped with camera and flashlight. Suitable for surveillance.
            Build on basis of DFRobot Devastator platform and Raspberry Pi 3A+ microcomputer.<br/><br/>
            To keep things as simple as it can be, turret is operated by two small servos and all consumers are powered
            by 20 Ah power bank (Blow PB15).<br/><br/>
            Power bank is a convenient power source as it has all the power management features already built-in (like
            voltage stabilization, overload protection, charging), it is also cost-effective solution allowing standby
            operation for approx. 24h. Disadvantage of power bank is, that such devices does not offer great power
            output, so you need to be extremely cautious about total power consumption as well as power peaks (i.e.
            on startup of electric motor).
        </p>
    </div>
</div>
<div class="flex f-column f-center">
    <h2 class="page-font f30 page-color">Wiring diagram</h2>
    <img src="/resources/img/TR_schema.png" style="width: 80%">
</div>
<%@include file="../footer.jsp" %>
</body>
</html>
