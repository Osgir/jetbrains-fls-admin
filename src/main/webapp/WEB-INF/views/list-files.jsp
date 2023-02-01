<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>

<body>
<%@include file="/WEB-INF/views/template/header.jsp" %>
<div class="container">
    <div class="col-md-offset-1 col-md-10">
        <h2>User Relationship Manager</h2>

        <input type="button" value="Add File"
               onclick="window.location.href='upload'; return false;"
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

                    <c:forEach var="tempUser" items="${files}">



                        <c:url var="getLink" value="/databasefile/get">
                            <c:param name="id" value="${tempUser.id}"/>
                        </c:url>


                        <tr>
                            <td>${tempUser.name}</td>

                            <td>
                                 <a href="${getLink}"

                                    class="btn btn-primary active" aria-pressed="true"
                                 >View</a>
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