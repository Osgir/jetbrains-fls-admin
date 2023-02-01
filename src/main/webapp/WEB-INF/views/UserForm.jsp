<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>

<body>
<%@include file="/WEB-INF/views/template/header.jsp" %>
<div class="container">
    <div class="col-md-offset-2 col-md-7">
        <h2 class="text-center">Users</h2>
        <div class="panel panel-info">
            <div class="panel-heading">
                <div class="panel-title">Add new User</div>
                <div class="panel-body">
                    <form:form action="saveUser" cssClass="form-horizontal"
                               method="post" modelAttribute="user">

                        <form:hidden path="id" id="userId"/>

                        <div class="form-group">
                            <label for="username" class="col-md-4 control-label">Login</label>
                            <div class="col-md-9">
                                <form:input path="username" cssClass="form-control"/>
                            </div>

                            <label for="password" class="col-md-4 control-label">Password</label>
                            <div class="col-md-9">
                                <form:input path="password" cssClass="form-control"/>
                            </div>

                            <hr/>

                            <c:forEach var="roleList" items="${roles}">
                                <div class="form-check form-check-inline">
                                    <form:checkbox path="roles" value="${roleList.id}" label="${roleList.name}"/>
                                </div>
                            </c:forEach>

                        </div>

                        <div class="form-group">

                            <div class="col-md-offset-1 col-md-10">
                                <input type="submit" value="Submit"
                                       class="btn btn-primary"/>
                                <input type="button" value="Back"
                                       onclick="window.location.href='list'; return false;"
                                       class="btn btn-primary"/>
                            </div>
                        </div>

                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>