<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>

<body>
<%@include file="/WEB-INF/views/template/header.jsp" %>
<div class="container">
    <div class="col-md-offset-1 col-md-10">
        <h2>User Relationship Manager</h2>

        <input type="button" value="Add User"
               onclick="window.location.href='new'; return false;"
               class="btn btn-primary" />
        <br/><br/>
        <div class="panel panel-info">
            <div class="panel-heading">
                <div class="panel-title">User List</div>
            </div>
            <div class="panel-body">
                <table class="table table-striped table-bordered table">
                    <tr>
                        <th>User name</th>

                        <th>Action</th>
                    </tr>

                    <c:forEach var="tempUser" items="${users}">

                        <c:url var="updateLink" value="/user/edit">
                            <c:param name="userId" value="${tempUser.id}" />
                        </c:url>

                        <c:url var="getLink" value="/user/delete">
                            <c:param name="userId" value="${tempUser.id}" />
                        </c:url>

                        <tr>
                            <td>${tempUser.username}</td>

                            <td>
                               <a href="${updateLink}" class="btn btn-primary active">Update</a>
                                 <a href="${getLink}"
                                     onclick="if (!(confirm('Are you sure you want to delete this user?'))) return false"
                                    class="btn btn-danger active" aria-pressed="true"
                                 >Delete</a>
                            </td>

                        </tr>

                    </c:forEach>




                </table>

            </div>
        </div>
    </div>

</div>
</body>
</html>