<%--
  Created by IntelliJ IDEA.
  User: danilovey
  Date: 03.11.2016
  Time: 12:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tools</title>
</head>
<body>

<div th:if="${message}">
    <h2 th:text="${message}"/>
</div>

<div>
    <form method="POST" enctype="multipart/form-data" action="${pageContext.request.contextPath}/rest/upload/employees">
        <table>
            <tr><td>File to upload:</td><td><input type="file" name="file" /></td></tr>
            <tr><td></td><td><input type="submit" value="Upload" /></td></tr>
        </table>
    </form>
</div>

</body>
</html>
