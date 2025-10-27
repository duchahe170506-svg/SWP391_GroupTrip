<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="/views/partials/header.jsp"/>

<html>
    <head>
        <title>Quản lý nhóm</title>

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
                    <li><a href="${pageContext.request.contextPath}/group/manage?groupId=${groupId}" class="active">👥 Members</a></li>
                    <li><a href="#">🎯 Activities</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/manage/tasks?groupId=${groupId}">🧾 Tasks</a></li>
                    <li><a href="#">💰 Expense</a></li>
                    <li><a href="${pageContext.request.contextPath}/group-memories">📸 Memories</a></li>
                    <li><a href="#">🔔 Notification</a></li>
                </ul>
            </div>

            <!-- ==== NỘI DUNG PHẢI ==== -->
            <div class="content">
                <div class="container">
                    <h2>Quản lý nhóm (ID: ${groupId})</h2>

                    <c:if test="${not empty param.error}">
                        <div style="background:#fee2e2;color:#991b1b;padding:10px;border-radius:6px;margin-bottom:12px;">
                            ${fn:escapeXml(param.error)}
                        </div>
                    </c:if>
                    <c:if test="${not empty param.success}">
                        <div style="background:#ecfdf5;color:#065f46;padding:10px;border-radius:6px;margin-bottom:12px;">
                            ${fn:escapeXml(param.success)}
                        </div>
                    </c:if>

                    <h3>Thành viên</h3>
                    <table>
                        <tr>
                            <th>Tên</th>
                            <th>Email</th>
                            <th>Vai trò</th>
                            <th>Ngày tham gia</th>
                            <th>Trạng thái</th>
                            <th>Ngày rời nhóm</th>
                            <th>Chỉnh sửa vai trò</th>
                            <th>Xóa</th>
                        </tr>
                        <c:forEach var="m" items="${members}">
                            <tr>
                                <td>
                                    <c:url var="userDetailUrl" value="/user/detail">
                                        <c:param name="id" value="${m.user_id}" />
                                    </c:url>
                                    <a href="${userDetailUrl}">${m.name}</a>

                                </td>
                                <td>${m.email}</td>
                                <td>${m.role}</td>
                                <td><fmt:formatDate value="${m.joined_at}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${m.status eq 'Active'}">
                                            <span style="color:green;">Đang hoạt động</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color:red;">Đã rời nhóm</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:if test="${not empty m.removed_at}">
                                        <fmt:formatDate value="${m.removed_at}" pattern="dd/MM/yyyy HH:mm"/>
                                    </c:if>
                                </td>


                                <td>
                                    <c:if test="${m.user_id ne 1 and m.status eq 'Active'}">
                                        <c:url var="editUrl" value="/group/edit-role">
                                            <c:param name="groupId" value="${groupId}"/>
                                            <c:param name="userId" value="${m.user_id}"/>
                                        </c:url>
                                        <a class="btn btn-edit" href="${editUrl}">Đổi role</a>
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${m.user_id ne 1 and m.status eq 'Active'}">
                                        <form action="${pageContext.request.contextPath}/group/remove-member" method="post" style="display:inline;"
                                              onsubmit="return confirm('Bạn có chắc muốn xóa thành viên này?');">
                                            <input type="hidden" name="groupId" value="${groupId}">
                                            <input type="hidden" name="userId" value="${m.user_id}">
                                            <input type="hidden" name="removedBy" value="${currentUser.user_id}">
                                            <input type="text" name="reason" placeholder="Lý do xóa" required style="width:120px;">
                                            <button type="submit" class="btn btn-del">Xóa</button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>

                    <br>
                    <h3>Yêu cầu tham gia nhóm</h3>
                    <c:url var="reqUrl" value="/group/join-requests">
                        <c:param name="groupId" value="${groupId}"/>
                    </c:url>
                    <a class="btn" href="${reqUrl}">Xem yêu cầu (${pendingCount} mới)</a>
                </div>
            </div>
        </div>

    </body>
    <jsp:include page="/views/partials/footer.jsp"/>
</html>
