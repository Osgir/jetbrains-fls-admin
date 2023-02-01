<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<html>
<head>
    <title>JB Admin</title>
</head>


<body>
<%@include file="/WEB-INF/views/template/header.jsp" %>

Логин: <security:authentication property="principal.username"/>
</body>
</html>