<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/favicon.ico">

<%@ page isELIgnored="false" %>

<title>JBAdmin</title>
<link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/jquery-3.6.0.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>

<%--<div class="col-md-offset-50 col-md-30">--%>
<security:authorize access="hasAnyAuthority('ADMIN', 'USER')" var="isUSer"/>
<%--        <button type="button" class="btn btn-secondary float-start">Example Button floated left</button>--%>
<%--        <button type="button" class="btn btn-secondary float-end">Example Button floated right</button>--%>
<sec:authorize access="isAuthenticated()">
    <div class="  m-lg-3   w-90 ">
        <div class="row">
            <div class="col">
                <div class="btn-group btn-group-toggle" data-toggle="buttons">

                    <sec:authorize access="hasAuthority('ROLE_JBSERVER')">
                        <input type="button" value="JBLogins"
                               onclick="window.location.href='\\jbemployee\\list';"
                               class="btn btn-outline-info btn-sm"/>

                        <input type="button" value="UploadFiles"
                               onclick="window.location.href='\\file\\index';"
                               class="btn btn-outline-info btn-sm"/>

                        <input type="button" value="Charts"
                               onclick="window.location.href='\\charts\\index';"
                               class="btn btn-outline-info btn-sm"/>


                    </sec:authorize>


                    <sec:authorize access="hasAuthority('ROLE_ADMIN')">
                        <input type="button" value="Users"
                               onclick="window.location.href='\\user\\list';"
                               class="btn btn-outline-info btn-sm"/>
                    </sec:authorize>

                        <%--                        <sec:authorize access="!isAuthenticated()">--%>
                        <%--                        <input type="button" value="Login"--%>
                        <%--                               onclick="window.location.href='\\login';"--%>
                        <%--                               class="btn btn-info"/>--%>
                        <%--                        </sec:authorize>--%>
                </div>
            </div>

            <div class="row">

                <div class="col-md-auto text-right" style=" color: green">

                    <c:if test="${isUSer}"> Логин: <security:authentication property="principal.username"/> </c:if>
                </div>
                <div class="col">
                    <form action="/logout" method=post >
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="submit" class="btn btn-outline-info btn-sm" value="Logout">
                    </form>

                </div>
            </div>
        </div>
    </div>
</sec:authorize>
<hr/>