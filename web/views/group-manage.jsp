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
                    <li><a href="${pageContext.request.contextPath}/group/expense?groupId=${groupId}">💰 Expense</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/memories?groupId=${groupId}">📸 Memories</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/notifications?groupId=${groupId}">🔔 Notification</a></li>
                </ul>
            </div>

            <!-- ==== NỘI DUNG PHẢI ==== -->
            <div class="content">
                <div class="container">
                    <h2>Thành viên (Chuyến đi: ${trip.name})</h2>
                    <br>

                    <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; margin-bottom: 20px;">

                        <form action="${pageContext.request.contextPath}/group/invite" method="post"
                              style="display:flex; gap:8px; align-items:center; flex-wrap:wrap; max-width:450px;">
                            <input type="hidden" name="groupId" value="${groupId}" />
                            <input type="email" name="email" placeholder="Nhập email người được mời" required
                                   style="flex:1; min-width:200px; padding:6px 10px; border:1px solid #ccc; border-radius:4px;">
                            <button type="submit" class="btn" style="padding:6px 14px; flex:0 0 auto;">Mời thành viên</button>
                        </form>

                        <form action="${pageContext.request.contextPath}/group/leave" method="post"
                              onsubmit="return confirmLeave();" style="display:flex; align-items:center; gap:8px;">
                            <input type="hidden" name="groupId" value="${groupId}" />
                            <input type="hidden" name="userId" value="${sessionScope.currentUser.user_id}" />
                            <input type="text" name="reason" placeholder="Lý do rời nhóm" required
                                   style="width:250px; padding:6px; border:1px solid #ccc; border-radius:4px;">
                            <button type="submit" class="btn btn-del">Rời nhóm</button>
                        </form>

                    </div>


                    <script>
                        function confirmLeave() {
                            let reason = document.querySelector('input[name="reason"]').value;
                            if (reason.trim() === "") {
                                alert("Vui lòng nhập lý do rời nhóm!");
                                return false;
                            }
                            return confirm("Bạn có chắc chắn muốn rời nhóm không?");
                        }
                    </script>        

                    <c:if test="${not empty sessionScope.successMessage}">
                        <div style="background:#ecfdf5;color:#065f46;padding:10px;border-radius:6px;margin-bottom:10px;">
                            ${sessionScope.successMessage}
                        </div>
                        <c:remove var="successMessage" scope="session"/>
                    </c:if>

                    <c:if test="${not empty sessionScope.errorMessage}">
                        <div style="background:#fee2e2;color:#991b1b;padding:10px;border-radius:6px;margin-bottom:10px;">
                            ${sessionScope.errorMessage}
                        </div>
                        <c:remove var="errorMessage" scope="session"/>
                    </c:if>


                    <!-- Thông báo -->
                    <c:if test="${not empty param.error}">
                        <div class="msg error">${fn:escapeXml(param.error)}</div>
                    </c:if>
                    <c:if test="${not empty param.success}">
                        <div class="msg success">${fn:escapeXml(param.success)}</div>
                    </c:if>
                    <c:if test="${not empty param.inviteError}">
                        <div style="background:#fee2e2;color:#991b1b;padding:10px;border-radius:6px;margin-bottom:10px;">
                            ${fn:escapeXml(param.inviteError)}
                        </div>
                    </c:if>

                    <c:if test="${not empty param.inviteSuccess}">
                        <div style="background:#ecfdf5;color:#065f46;padding:10px;border-radius:6px;margin-bottom:10px;">
                            ${fn:escapeXml(param.inviteSuccess)}
                        </div>
                    </c:if>    


                    <table>
                        <tr>
                            <th>Tên</th>
                            <th>Email</th>
                            <th>Vai trò</th>
                            <th>Ngày tham gia</th>
                                <c:if test="${sessionScope.currentUser != null && groupRole eq 'Leader'}">
                                <th>Chỉnh sửa vai trò</th>
                                <th>Xóa</th>
                                </c:if>
                        </tr>
                        <c:forEach var="m" items="${members}">
                            <tr>
                                <td>${m.name}</td>
                                <td>${m.email}</td>
                                <td>${m.role}</td>
                                <td><fmt:formatDate value="${m.joined_at}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <c:if test="${sessionScope.currentUser != null && groupRole eq 'Leader'}">
                                    <td>
                                        <c:url var="toggleUrl" value="/group/edit-role">
                                            <c:param name="groupId" value="${groupId}"/>
                                            <c:param name="userId" value="${m.user_id}"/>
                                            <c:param name="action" value="toggle"/>
                                        </c:url>
                                        <a class="btn btn-edit" href="${toggleUrl}" onclick="return confirm('Bạn có chắc muốn đổi role người này không?');">Đổi role</a>

                                        <c:url var="promoteUrl" value="/group/edit-role">
                                            <c:param name="groupId" value="${groupId}"/>
                                            <c:param name="userId" value="${m.user_id}"/>
                                            <c:param name="action" value="promoteLeader"/>
                                        </c:url>
                                        <a class="btn btn-edit" href="${promoteUrl}" 
                                           onclick="return confirm('Bạn có chắc muốn phong người này làm Leader không?');">Phong Leader</a>
                                    </td>
                                    <td>

                                        <form action="${pageContext.request.contextPath}/group/remove-member" method="post" style="display:inline;"
                                              onsubmit="return confirm('Bạn có chắc muốn xóa thành viên này?');">
                                            <input type="hidden" name="groupId" value="${groupId}">
                                            <input type="hidden" name="userId" value="${m.user_id}">
                                            <input type="hidden" name="removedBy" value="${leaderId}">
                                            <input type="text" name="reason" placeholder="Lý do xóa" required style="width:120px;">
                                            <button type="submit" class="btn btn-del">Xóa</button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>

                    <br>

                    <!-- Yêu cầu tham gia từ người dùng -->
                    <c:if test="${not empty userRequests}">
                        <h3>Yêu cầu tham gia từ người dùng</h3>
                        <table>
                            <tr>
                                <th>Người gửi</th>
                                <th>Thời gian</th>
                                <th>Trạng thái</th>
                                <th>Người duyệt</th>
                                <th>Hành động</th>
                            </tr>
                            <c:forEach var="r" items="${userRequests}">
                                <tr>
                                    <td><a href="#" 
                                           class="user-info-link" 
                                           data-name="${userMap[r.user_id]}" 
                                           data-email="${emailMap[r.user_id]}"
                                           data-id="${r.user_id}">
                                            ${userMap[r.user_id]}
                                        </a></td>
                                    <td><fmt:formatDate value="${r.requested_at}" pattern="dd/MM/yyyy HH:mm"/></td>
                                    <td>${r.status}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${r.reviewed_by != null}">
                                                ${userMap[r.reviewed_by]}
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color:gray;">Chưa duyệt</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${r.status eq 'PENDING'
                                                      && sessionScope.currentUser != null
                                                      && (groupRole eq 'Leader' or groupRole eq 'CoLeader')}">
                                              <form action="${pageContext.request.contextPath}/group/manage" method="post" style="display:inline">
                                                  <input type="hidden" name="requestId" value="${r.request_id}" />
                                                  <input type="hidden" name="groupId" value="${r.group_id}" />
                                                  <button type="submit" class="btn-edit" name="action" value="approve">Chấp nhận</button>
                                              </form>
                                        </c:if>


                                        <c:if test="${r.status eq 'PENDING'
                                                      && sessionScope.currentUser != null
                                                      && groupRole eq 'Leader'}">
                                              <form action="${pageContext.request.contextPath}/group/manage" method="post" style="display:inline">
                                                  <input type="hidden" name="requestId" value="${r.request_id}" />
                                                  <input type="hidden" name="groupId" value="${r.group_id}" />
                                                  <button type="submit" class="btn-del" name="action" value="reject">Từ chối</button>
                                              </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:if>

                    <br>    
                    <!-- Lời mời từ Leader -->
                    <c:if test="${not empty leaderInvites}">
                        <h3>Lời mời từ thành viên nhóm</h3>
                        <table>
                            <tr>
                                <th>Thành viên mời</th>
                                <th>Người được mời</th>
                                <th>Thời gian</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th>
                            </tr>
                            <c:forEach var="r" items="${leaderInvites}">
                                <tr>
                                    <td>${userMap[r.invited_by]}</td>
                                    <td>${userMap[r.user_id]}</td>
                                    <td><fmt:formatDate value="${r.requested_at}" pattern="dd/MM/yyyy HH:mm"/></td>
                                    <td>${r.status}</td>

                                    <td>
                                        <c:if test="${r.status eq 'INVITED' 
                                                      && sessionScope.currentUser != null 
                                                      && r.invited_by eq sessionScope.currentUser.user_id
                                                      && r.reviewed_by == null}">
                                              <form action="${pageContext.request.contextPath}/group/manage" method="post" style="display:inline">
                                                  <input type="hidden" name="requestId" value="${r.request_id}" />
                                                  <input type="hidden" name="groupId" value="${r.group_id}" />
                                                  <button type="submit" class="btn-del" name="action" value="cancel">Thu hồi</button>
                                              </form>
                                        </c:if>

                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:if>

                </div>
            </div>
        </div>
        <!-- Popup hiển thị thông tin người dùng -->
        <div id="userPopup" 
             style="display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%);
             background:#fff; padding:20px; border-radius:8px; box-shadow:0 2px 10px rgba(0,0,0,0.3);
             z-index:999;">
            <h3>Thông tin người yêu cầu</h3>
            <p><strong>Tên:</strong> <span id="popupName"></span></p>
            <p><strong>Email:</strong> <span id="popupEmail"></span></p>
            <button onclick="closePopup()" class="btn">Đóng</button>
        </div>

        <!-- Overlay nền mờ -->
        <div id="overlay" 
             style="display:none; position:fixed; top:0; left:0; width:100%; height:100%;
             background:rgba(0,0,0,0.3); z-index:998;" 
             onclick="closePopup()"></div>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                document.querySelectorAll('.user-info-link').forEach(link => {
                    link.addEventListener('click', function (e) {
                        e.preventDefault();
                        const name = this.dataset.name;
                        const email = this.dataset.email;
                        document.getElementById('popupName').textContent = name || "Không có dữ liệu";
                        document.getElementById('popupEmail').textContent = email || "Không có dữ liệu";
                        document.getElementById('userPopup').style.display = 'block';
                        document.getElementById('overlay').style.display = 'block';
                    });
                });
            });

            function closePopup() {
                document.getElementById('userPopup').style.display = 'none';
                document.getElementById('overlay').style.display = 'none';
            }
        </script>


    </body>
    <jsp:include page="/views/partials/footer.jsp"/>
</html>
