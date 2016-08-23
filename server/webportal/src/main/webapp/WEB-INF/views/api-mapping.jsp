<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: danilovey
  Date: 26.07.2016
  Time: 11:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<br>
<div align="center">
    <table border="1">
        <caption>Таблица URL</caption>
        <tr>
            <th>URL</th>
            <th>Метод запроса</th>
            <th>Описание</th>
            <th>Доступ всем</th>
            <th>Доступ SUPER ADMIN</th>
            <th>Доступ ADMIN</th>
            <th>Доступ USER</th>
            <th>Доступ ANONYMOUS</th>
        </tr>

        <c:forEach items="${listApi}" var="entry">
            <tr>
                <td>${entry.url}</td>
                <td>${entry.requestMethod}</td>
                <td>${entry.description}</td>
                <td>${entry.accessAll}</td>
                <td>${entry.accessSuperAdmin}</td>
                <td>${entry.accessPsrAdmin}</td>
                <td>${entry.accessUser}</td>
                <td>${entry.accessAnonymous}</td>
            </tr>
        </c:forEach>
    </table>
    </div>
</body>
</html>
