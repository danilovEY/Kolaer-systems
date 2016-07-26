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
    <c:forEach items="${map}" var="entry">
        <a href="${entry.key}">${entry.key}</a> <label> - ${entry.value}</label> </br>
    </c:forEach>
</body>
</html>
