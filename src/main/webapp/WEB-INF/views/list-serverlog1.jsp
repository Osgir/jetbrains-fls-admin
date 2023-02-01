<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>

<!DOCTYPE html>
<html>


<body>
<%@include file="/WEB-INF/views/template/header.jsp" %>
<div class="container">
    <div class="col-md-offset-1 col-md-10" >
        <h2>JB Blacklist Manager</h2>

        <br/>
        <div class="panel panel-info">
            <div class="panel-heading">
                <div class="panel-title">Search</div>
            </div>
            <div class="panel-body">
                <table id="myTable" class="table table-striped table-bordered">
                    <tr>
                        <th>Add date</th>
                        <th>Host name</th>
                        <th>User login</th>
                        <th>Product</th>

                    </tr>

                    <c:forEach var="bList" items="${serverlog}">

                        <tr>

                            <td><fmt:formatDate value="${bList.logdate}" pattern="dd.MM.yyyy HH:mm:ss" /></td>
                            <td>${bList.hostname}</td>
                            <td>${bList.username}</td>
                            <td>${bList.productFamily.name}</td>


                        </tr>

                    </c:forEach>

                </table>

            </div>
        </div>
    </div>

</div>
</body>
</html>