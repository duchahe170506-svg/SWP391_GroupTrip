<%-- 
    Document   : join-requests
    Created on : 5 Oct 2025, 07:07:09
    Author     : quang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Yêu cầu tham gia nhóm</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f9f9f9; }
        h2 { text-align: center; margin-top: 20px; }
        table { width: 90%; margin: 20px auto; border-collapse: collapse; background: #fff; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }
        th { background: #1a82e2; color: #fff; }
        a { color: #1a82e2; text-decoration: none; }
        button { padding: 6px 12px; border: none; border-radius: 4px; cursor: pointer; }
        .approve { background: #28a745; color: white; }
        .reject { background: #dc3545; color: white; }
    </style>
</head>
<body>
    <h2>Danh sách yêu cầu tham gia nhóm</h2>
    <table>
        <tr>
            <th>Người gửi</th>
            <th>Thời gian yêu cầu</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
        </tr>
        <c:forEach var="r" items="${requests}">
            <tr>
                <td>
                    <a href="user/detail?id=${r.user_id}">
                        ${userDAO.getUserById(r.user_id).getName()}
                    </a>
                </td>
                <td>${r.requested_at}</td>
                <td>${r.status}</td>
                <td>
                    <c:if test="${r.status eq 'PENDING'}">
                        <form action="join-requests" method="post" style="display:inline">
                            <input type="hidden" name="requestId" value="${r.request_id}" />
                            <input type="hidden" name="groupId" value="${r.group_id}" />
                            <button type="submit" class="approve" name="action" value="approve">Chấp nhận</button>
                            <button type="submit" class="reject" name="action" value="reject">Từ chối</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
