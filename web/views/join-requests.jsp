<%-- 
    Document   : join-requests
    Created on : 5 Oct 2025, 07:07:09
    Author     : quang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="partials/header.jsp"/>

<html>
    <head>
        <title>Yêu cầu tham gia nhóm</title>
    </head>
    <body>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                background-color: #f6f7f9;
            }

            .layout {
                display: flex;
                min-height: 100vh;
            }

            /* ==== SIDEBAR TRÁI ==== */
            .sidebar {
                width: 220px;
                background-color: #77a096;
                color: black;
                padding: 20px;
            }

            .sidebar h3 {
                text-align: center;
                margin-bottom: 20px;
            }

            .sidebar ul {
                list-style: none;
                padding: 0;
            }

            .sidebar li {
                margin: 12px 0;
            }

            .sidebar a {
                color: #ecf0f1;
                text-decoration: none;
                display: block;
                padding: 8px 10px;
                border-radius: 6px;
                transition: 0.3s;
            }

            .sidebar a:hover,
            .sidebar a.active {
                background-color: black;
                color: #fff;
            }

            /* ==== NỘI DUNG PHẢI ==== */
            .content {
                flex: 1;
                background: #fff;
                padding: 20px 30px;
                margin: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 6px rgba(0,0,0,0.1);
            }

            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 12px;
            }

            th, td {
                border: 1px solid #ddd;
                padding: 8px;
                text-align: left;
            }

            th {
                background-color: #77a096;
                color: white;
            }

            /* === CÁC NÚT CHỨC NĂNG GIỮ NGUYÊN === */
            .btn {
                background-color: #77a096;
                color: white;
                padding: 6px 12px;
                border-radius: 6px;
                text-decoration: none;
                transition: 0.3s;
                border: none;
                cursor: pointer;
            }

            .btn:hover {
                background-color: #e67e22;
            }

            .btn-edit {
                background-color: #2980b9;
            }

            .btn-edit:hover {
                background-color: #1f6390;
            }

            .btn-del {
                background-color: #e74c3c;
            }

            .btn-del:hover {
                background-color: #c0392b;
            }

            input[type="text"] {
                padding: 5px;
                border: 1px solid #ccc;
                border-radius: 4px;
            }
        </style>

        <div class="layout">
            <!-- ==== SIDEBAR TRÁI ==== -->
            <div class="sidebar">
                <h3>Group Menu</h3>
                <ul>
                    <li><a href="#">🕒 Time Line</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/manage?groupId=4" class="active">👥 Members</a></li>
                    <li><a href="#">🎯 Activities</a></li>
                    <li><a href="#">🧾 Tasks</a></li>
                    <li><a href="#">💰 Expense</a></li>
                    <li><a href="${pageContext.request.contextPath}/group-memories">📸 Memories</a></li>
                    <li><a href="#">🔔 Notification</a></li>
                </ul>
            </div>

            <!-- ==== NỘI DUNG PHẢI ==== -->
            <div class="content">
                <div class="container">
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
                                    <c:forEach var="u" items="${userList}">
                                        <c:if test="${u.user_id == r.user_id}">
                                            ${u.name}
                                        </c:if>
                                    </c:forEach>
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
                    <br>
                    <h3>Xem thành viên nhóm</h3>
                    <c:url var="reqUrl" value="/group/manage">
                        <c:param name="groupId" value="${groupId}"/>
                    </c:url>
                    <a class="btn" href="${reqUrl}">Quay lại</a>
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="partials/footer.jsp"/>
</html>
