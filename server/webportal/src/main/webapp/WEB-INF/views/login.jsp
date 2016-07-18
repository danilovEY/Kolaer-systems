<%--
  Created by IntelliJ IDEA.
  User: danilovey
  Date: 13.07.2016
  Time: 13:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>${welcomeMassage}</h1>
    <br><br>
    <div class="container">
        <div class="row wrapper">
            <br><br>
            <form name='f' action="j_spring_security_check" method='POST'
                  onsubmit="return validate();">

                <label class="col-sm-4">111</label>
                <span class="col-sm-8"><input class="form-control" type='text' name='j_username' value=''></span>

                <br><br>
                <label class="col-sm-4">222</label>
                <span class="col-sm-8"><input class="form-control" type='password' name='j_password' /></span>

                <br><br>
                <input class="btn btn-primary" name="submit" type="submit" value="333" />

            </form>
        </div>
    </div>
</body>
</html>
