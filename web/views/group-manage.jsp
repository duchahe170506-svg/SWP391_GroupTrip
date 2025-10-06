<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Quản lý nhóm</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f5f5f5; }
        .container { max-width: 900px; margin: 30px auto; background: #fff; padding: 20px; border-radius: 8px; }
        h2 { margin-bottom: 20px; }
        table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background: #1a82e2; color: white; }
        a { color: #1a82e2; text-decoration: none; }
        .btn { padding: 6px 10px; border-radius: 4px; text-decoration: none; font-size: 14px; }
        .btn-edit { background: #10b981; color: white; }
        .btn-del { background: #ef4444; color: white; }
        .btn-req { background: #f59e0b; color: white; }
    </style>
</head>
<body>
<div class="container">
    <h2>Quản lý nhóm (ID: ${groupId})</h2>

    <!-- Danh sách thành viên -->
    <h3>Thành viên</h3>
    <table>
        <tr>
            <th>Tên</th>
            <th>Email</th>
            <th>Vai trò</th>
            <th>Chỉnh sửa vai trò</th>
            <th>Xóa</th>
        </tr>
        <c:forEach var="m" items="${members}">
            <tr>
                <td><a href="user/detail?id=${m.user_id}">${m.name}</a></td>
                <td>${m.email}</td>
                <td>${m.role}</td>

                <!-- Leader cố định (ID=14) có quyền đổi role và xóa -->
                <td>
                    <c:if test="${m.user_id ne group.created_by}">
                        <a class="btn btn-edit"
                           href="edit-role?groupId=${group.group_id}&userId=${m.user_id}">
                            Đổi role
                        </a>
                    </c:if>
                </td>
                <td>
                    <c:if test="${m.user_id ne group.created_by}">
                        <a class="btn btn-del"
                           href="remove-member?groupId=${group.group_id}&userId=${m.user_id}"
                           onclick="return confirm('Bạn có chắc muốn xóa thành viên này?');">
                           Xóa
                        </a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>

    <!-- Yêu cầu tham gia -->
    <h3>Yêu cầu tham gia nhóm</h3>
    <a class="btn btn-req" href="join-requests?groupId=${group.group_id}">
        Xem yêu cầu (${pendingCount} mới)
    </a>
</div>
</body>
</html>
