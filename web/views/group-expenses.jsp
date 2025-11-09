<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="/views/partials/header.jsp"/>

<html>
    <head>
        <title>Khoản chi của chuyến đi</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                background-color: #f6f7f9;
            }

            .layout {
                display: flex;
            }

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

            .btn {
                padding: 6px 12px;
                border-radius: 6px;
                border: none;
                cursor: pointer;
                text-decoration: none;
                transition: 0.3s;
            }

            .btn:hover {
                opacity: 0.85;
            }

            .btn-add {
                background-color: #4CAF50;
                color: white;
                margin-bottom: 12px;
            }

            .btn-edit {
                background-color: #2980b9;
                color: white;
            }

            .btn-del {
                background-color: #e74c3c;
                color: white;
            }

            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                overflow: auto;
                background-color: rgba(0,0,0,0.5);
            }

            .modal-content {
                background-color: #fff;
                margin: 8% auto;
                padding: 20px;
                border-radius: 8px;
                width: 400px;
                position: relative;
            }

            .close {
                position: absolute;
                top: 10px;
                right: 15px;
                font-size: 24px;
                cursor: pointer;
            }

            input, select, textarea {
                padding: 6px;
                width: 100%;
                margin: 6px 0;
                box-sizing: border-box;
            }

            .error {
                color: red;
                margin-top: 10px;
            }
        </style>
    </head>
    <body>
        <div class="layout">
            <div class="sidebar">
                <h3>Group Menu</h3>
                <ul>
                    <li><a href="#">🕒 Time Line</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/manage?groupId=${groupId}">👥 Members</a></li>
                    <li><a href="#">🎯 Activities</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/manage/tasks?groupId=${groupId}">🧾 Tasks</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/expense?groupId=${groupId}" class="active">💰 Expense</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/memories?groupId=${groupId}">📸 Memories</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/notifications?groupId=${groupId}">🔔 Notification</a></li>
                </ul>
            </div>

            <div class="content">
                <h2>Khoản chi (Chuyến đi: ${trip.name})</h2>
                <div><strong>Ngân sách:</strong> ${tripBudget} | <strong>Tổng chi:</strong> ${totalExpense}</div>

                <c:if test="${not empty errorMessage}">
                    <div class="error">${errorMessage}</div>
                </c:if>

                <button class="btn btn-add" onclick="openModal('add')">➕ Thêm khoản chi</button>

                <table>
                    <thead>
                        <tr>
                            <th>Số tiền</th>
                            <th>Người trả</th>
                            <th>Loại</th>
                            <th>Mô tả</th>
                            <th>Trạng thái</th>
                            <th>Ngày tạo</th>
                            <th>Cập nhật</th>
                                <c:if test="${sessionScope.currentUser != null && groupRole eq 'Leader'}">
                                <th>Hành động</th>
                                </c:if>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="e" items="${expenses}">
                            <tr>
                                <td>${e.amount}</td>
                                <td>${e.payerName}</td>
                                <td>${e.expenseType}</td>
                                <td>${e.description}</td>
                                <td>${e.status}</td>
                                <td><fmt:formatDate value="${e.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td><fmt:formatDate value="${e.updatedAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <c:if test="${e.status == 'Pending' && sessionScope.currentUser != null && groupRole eq 'Leader'}">
                                    <td>
                                        <button class="btn btn-edit" onclick="openModal('edit', ${e.expenseId}, '${e.amount}', ${e.paidBy}, '${e.expenseType}', '${e.description}', '${e.status}')">Sửa</button>
                                        <form action="${pageContext.request.contextPath}/group/expense" method="post" style="display:inline;">
                                            <input type="hidden" name="groupId" value="${groupId}">
                                            <input type="hidden" name="tripId" value="${tripId}">
                                            <input type="hidden" name="expenseId" value="${e.expenseId}">
                                            <input type="hidden" name="action" value="delete">
                                            <button class="btn btn-del" onclick="return confirm('Xóa khoản chi này?')">Xóa</button>
                                        </form>
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty expenses}">
                            <tr><td colspan="7" style="text-align:center;">Chưa có khoản chi nào</td></tr>
                        </c:if>
                    </tbody>
                </table>

                <!-- Modal Thêm/Sửa -->
                <div id="expenseModal" class="modal">
                    <div class="modal-content">
                        <span class="close" onclick="closeModal()">&times;</span>
                        <h3 id="modalTitle">Thêm khoản chi</h3>
                        <form id="expenseForm" action="${pageContext.request.contextPath}/group/expense" method="post">
                            <input type="hidden" name="action" id="action" value="add">
                            <input type="hidden" name="tripId" value="${tripId}">
                            <input type="hidden" name="groupId" value="${groupId}">
                            <input type="hidden" name="expenseId" id="expenseId">

                            <label>Số tiền:</label>
                            <input type="number" name="amount" id="amount" step="0.01" min="0.01" required>
                            <c:choose>
                                <c:when test="${groupRole eq 'Leader'}">
                                    <label>Người chi trả:</label>
                                    <select name="paidBy" id="paidBy" required>
                                        <option value="">-- Chọn --</option>
                                        <c:forEach var="u" items="${userList}">
                                            <option value="${u.user_id}">${u.name}</option>
                                        </c:forEach>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="paidBy" value="${sessionScope.currentUser.user_id}">
                                    <p>Người chi trả: ${sessionScope.currentUser.name}</p>
                                </c:otherwise>
                            </c:choose>

                            <label>Loại chi phí:</label>
                            <select name="expenseType" id="expenseType">
                                <option value="Ăn uống">Ăn uống</option>
                                <option value="Di chuyển">Di chuyển</option>
                                <option value="Vé tham quan">Vé tham quan</option>
                                <option value="Lưu trú">Lưu trú</option>
                                <option value="Khác">Khác</option>
                            </select>

                            <label>Mô tả:</label>
                            <textarea name="description" id="description" rows="3"></textarea>
                            <c:if test="${sessionScope.currentUser != null && groupRole eq 'Leader'}">
                                <label>Trạng thái:</label>
                                <select name="status" id="status"></select>
                            </c:if>
                            <button type="submit" class="btn btn-add">Lưu</button>
                        </form>
                    </div>
                </div>

            </div>
        </div>

        <script>
            function openModal(mode, expenseId = '', amount = '', paidBy = '', expenseType = '', description = '', status = 'Pending') {
                document.getElementById('expenseModal').style.display = 'block';
                document.getElementById('expenseId').value = expenseId;
                document.getElementById('amount').value = amount;
                document.getElementById('paidBy').value = paidBy;
                document.getElementById('expenseType').value = expenseType;
                document.getElementById('description').value = description;

                const statusSelect = document.getElementById('status');
                statusSelect.innerHTML = '';

                if (mode === 'add') {
                    document.getElementById('modalTitle').innerText = 'Thêm khoản chi';
                    document.getElementById('action').value = 'add';
                    const option = document.createElement('option');
                    option.value = 'Pending';
                    option.text = 'Pending';
                    option.selected = true;
                    statusSelect.add(option);
                } else {
                    document.getElementById('modalTitle').innerText = 'Sửa khoản chi';
                    document.getElementById('action').value = 'update';
                    ['Pending', 'Approved'].forEach(s => {
                        const option = document.createElement('option');
                        option.value = s;
                        option.text = s;
                        if (s === status)
                            option.selected = true;
                        statusSelect.add(option);
                    });
            }
            }

            function closeModal() {
                document.getElementById('expenseModal').style.display = 'none';
            }

            window.onclick = function (event) {
                if (event.target === document.getElementById('expenseModal')) {
                    closeModal();
                }
            }
        </script>

        <jsp:include page="/views/partials/footer.jsp"/>
    </body>
</html>
