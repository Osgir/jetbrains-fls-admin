<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<body>
<%@include file="/WEB-INF/views/template/header.jsp" %>
<div class="container">
    <div class="panel-body">
        <table class=" table table-bordered">
            <tbody>
            <tr>
                <td>
                    <form action="/file/upload" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <INPUT TYPE="radio" name="type" value="0" checked="true"/>Logins<br>
                        <INPUT TYPE="radio" name="type" VALUE="1"/>Computers<br>
                        <input type="file" accept=".json" name="file" size="40"/>
                        <input type="submit"/>
                    </form>
                </td>
            </tr>
            <tr>
                <td>
                    <div>
                        <p>File Example:</p>
                        Computers:<br>
                        [{"Computers": ["computer1", "computer2"]}]<br><br>
                        Logins:<br>
                        [{"Logins": ["login1", "login2"]}]<br>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>