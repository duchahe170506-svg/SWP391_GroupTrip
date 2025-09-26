<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>User List</title>
</head>
<body>
<h2>Danh sách Users</h2>
<table border="1" cellpadding="5" cellspacing="0">
    <tr>
        <th>ID</th><th>Name</th><th>Email</th><th>Role</th><th>Status</th>
    </tr>
    <c:forEach var="u" items="${users}">
        <tr>
            <td>${u.user_id}</td>
            <td>${u.name}</td>
            <td>${u.email}</td>
            <td>${u.role}</td>
            <td>${u.status}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
