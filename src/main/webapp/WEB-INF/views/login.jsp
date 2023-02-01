<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<link href="/resources/css/bootstrap.min.css" rel="stylesheet">
<link href="/resources/css/fontawesome.min.css" rel="stylesheet">
<script src="/resources/js/jquery-3.3.1.slim.min.js"></script>
<script src="/resources/js/bootstrap.min.js"></script>

<link href="/resources/css/main.css" rel="stylesheet" type="text/css">
<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/favicon.ico">
<div class="container">

    <div class="row justify-content-center mt-5">
        <div class="col-md-6 ">
            <form method="POST" action="/login" class="form-horizontal">
                <sec:csrfInput />
                <span class="heading">Авторизация</span>
                <div class="form-group">
                    <input name="username" type="text" class="form-control"  placeholder="username">
                    <i class="fa fa-user"></i>
                </div>
                <div class="form-group help">
                    <input name="password" type="password" class="form-control"  placeholder="password">
                    <i class="fa fa-lock"></i>
                    <a href="/user/list/" class="fa fa-question-circle"></a>
                </div>
                <div class="form-group mb-5">

                    <button type="submit" class="btn btn-default">ВХОД</button>
                </div>
            </form>
        </div>

    </div>
</div><
