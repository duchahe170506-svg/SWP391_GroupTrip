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
        <th>ID</th>
        <th>Name</th>
        <th>Email</th>
        <th>Password</th>
        <th>Role</th>
        <th>Status</th>
      
    </tr>
    <c:forEach var="u" items="${users}">
        <tr>
            <td>${u.getUser_id()}</td>
            <td>${u.getName()}</td>
            <td>${u.getEmail()}</td>
            <td>${u.getPassword()}</td>
            <td>${u.getRole()}</td>
            <td>${u.getStatus()}</td>
            
        </tr>
    </c:forEach>
</table>
</body>
</html>
