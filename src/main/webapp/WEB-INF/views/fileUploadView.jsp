<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>JB Admin</title>
</head>
<body>

<div class="container">
    <div class="panel-body">

<form:form method="post"
           action="doUpload"
           enctype="multipart/form-data">
    <table class=" table table-striped table-bordered">
        <tr>
            <td>Name</td>
            <td><input type="text" name="name" /></td>
        </tr>
        <tr>
            <td>Select a file to upload</td>
            <td><input type="file" name="file" /></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit" /></td>
        </tr>
    </table>

</form:form>

    </div>
</div>

</body>
</html>

