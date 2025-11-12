<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="/views/partials/header.jsp"/>

<html>
    <head>
        <title>Quản lý công việc</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

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
            .table-container {
                max-height: 600px; /* hoặc 400px tùy bạn muốn bảng cao bao nhiêu */
                overflow-y: auto;  /* bật cuộn dọc */
                border: 1px solid #ddd; /* để khung rõ ràng */
                border-radius: 6px;
            }

            /* Đảm bảo phần header luôn cố định */
            .table-container table {
                width: 100%;
                border-collapse: collapse;
            }

            .table-container th, .table-container td {
                border: 1px solid #ddd;
                padding: 8px;
                text-align: left;
            }

            .table-container th {
                position: sticky;
                top: 0;
                background-color: #77a096;
                color: white;
                z-index: 2; /* giữ header nằm trên */
            }
            .loading-icon {
                border: 3px solid #f3f3f3; /* màu nền vòng ngoài */
                border-top: 3px solid #77a096; /* màu chính */
                border-radius: 50%;
                width: 16px;
                height: 16px;
                animation: spin 1s linear infinite;
                display: inline-block;
                vertical-align: middle;
            }

            @keyframes spin {
                0% {
                    transform: rotate(0deg);
                }
                100% {
                    transform: rotate(360deg);
                }
            }
            .modal {
                display: none;
                position: fixed;
                z-index: 9999;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0,0,0,0.4);
            }

            .modal-content {
                background-color: #fff;
                margin: 10% auto;
                padding: 20px;
                border-radius: 10px;
                width: 45%;
                box-shadow: 0 4px 10px rgba(0,0,0,0.2);
                animation: fadeIn 0.3s;
            }

            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(-20px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            .close-btn {
                float: right;
                font-size: 24px;
                cursor: pointer;
            }
            .toast {
                position: fixed;
                bottom: 20px;
                right: 20px;
                background-color: #4CAF50;
                color: white;
                padding: 10px 18px;
                border-radius: 8px;
                font-size: 14px;
                box-shadow: 0 2px 6px rgba(0,0,0,0.2);
                opacity: 0;
                transform: translateY(20px);
                transition: all 0.3s ease;
                z-index: 10000;
            }
            .toast.show {
                opacity: 1;
                transform: translateY(0);
            }
            .toast-error {
                background-color: #e74c3c;
            }
            .btn-icon {
                color: #555;
                margin: 0 5px;
                font-size: 18px;
                transition: 0.3s;
            }
            .btn-icon:hover {
                color: #2980b9;
            }
            .btn-icon.delete-btn:hover {
                color: #e74c3c;
            }
        </style>

        <div class="layout">
            <!-- ==== SIDEBAR TRÁI ==== -->
            <div class="sidebar">
                <h3>Group Menu</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/group/manage/timeline?groupId=${groupId}">🕒 Time Line</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/manage?groupId=${tripId}">👥 Members</a></li>
                    <li><a href="#">🎯 Activities</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/manage/tasks?groupId=${groupId}"  class="active">🧾 Tasks</a></li>
                    <li><a href="#">💰 Expense</a></li>
                    <li><a href="${pageContext.request.contextPath}/group-memories">📸 Memories</a></li>
                    <li><a href="#">🔔 Notification</a></li>
                </ul>
            </div>

            <!-- ==== NỘI DUNG PHẢI ==== -->
            <div class="content">
                <div class="container">

                    <!-- Thanh tìm kiếm và filter -->
                    <form method="get" action="${pageContext.request.contextPath}/group/manage/tasks"
                          style="display:flex; justify-content:space-between; align-items:center; margin-bottom:10px;">
                        <div>
                            <input type="hidden" name="groupId" value="${groupId}"/>
                            <input type="text" id="keywordInput" name="keyword" placeholder="🔍 Search..." value="${keyword != null ? keyword : ''}" />
                            <button type="submit" class="btn">Search</button>
                            <select id="statusSelect" name="status" class="btn" onchange="this.form.submit()" style="padding: 9px">
                                <option value="">Filter Tasks</option>
                                <option value="Pending" ${status == 'Pending' ? 'selected' : ''}>Pending</option>
                                <option value="InProgress" ${status == 'InProgress' ? 'selected' : ''}>In Progress</option>
                                <option value="Completed" ${status == 'Completed' ? 'selected' : ''}>Completed</option>
                            </select>
                            <button type="button" class="btn btn-del" onclick="clearFilters()">Clear</button>
                        </div>

                        <c:if test="${isLeader}">
                            <div>
                                <a href="${pageContext.request.contextPath}/group/manage/tasks-add?group_id=${groupId}" class="btn">+ Add Tasks</a>
                            </div>
                        </c:if>
                    </form>

                    <!-- Bảng hiển thị dữ liệu -->
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th style="width: 50px;">No</th>
                                    <th>Tiêu đề</th>
                                    <th>Thời hạn</th>
                                    <th>Được giao cho</th>
                                    <th>Ngân sách</th>
                                    <th>Trạng thái</th>
                                    <c:if test="${isLeader}">
                                        <th>Hành động</th>
                                    </c:if>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty tasks}">
                                        <c:forEach var="t" items="${tasks}">
                                            <tr class="task-row" 
                                                style="
                                                <c:choose>
                                                    <c:when test='${t.status eq "Completed"}'>background-color:#e6ffee;</c:when>
                                                    <c:when test='${t.status eq "InProgress"}'>background-color:#fff9e6;</c:when>
                                                    <c:otherwise>background-color:#ffffff;</c:otherwise>
                                                </c:choose>">
                                                <td style="text-align:center;">${t.task_id}</td>
                                                <td>
                                                    <a href="#" class="task-link" data-task-id="${t.task_id}">
                                                        ${t.description}
                                                    </a>
                                                </td>
                                                <td><fmt:formatDate value="${t.deadline}" pattern="dd-MM-yyyy"/></td>
                                                <td>
                                                    ${t.assignedUserName}
                                                </td>
                                                <td><fmt:formatNumber value="${t.estimated_cost}" type="number"/></td>
                                                <td style="text-align:center;">
                                                    <c:choose>
                                                        <c:when test="${t.status eq 'Completed'}">
                                                            <input type="checkbox" checked disabled />
                                                        </c:when>

                                                        <c:when test="${t.status eq 'InProgress'}">
                                                            <div class="loading-icon"></div>
                                                        </c:when>

                                                        <c:otherwise>
                                                            <input type="checkbox" disabled />
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <c:if test="${isLeader}">
                                                    <td style="text-align:center;">
                                                        <a href="${pageContext.request.contextPath}/group/manage/tasks-edit?task_id=${t.task_id}" class="btn-icon" title="Edit">
                                                            <i class="fa-solid fa-pen-to-square"></i>
                                                        </a>
                                                        <c:if test="${t.status ne 'Completed'}">
                                                            <a href="#" class="btn-icon delete-btn" title="Delete" data-task-id="${t.task_id}">
                                                                <i class="fa-solid fa-trash"></i>
                                                            </a>
                                                        </c:if>
                                                        <c:if test="${t.status eq 'Completed'}">
                                                            <span class="btn-icon" style="color:#ccc; cursor:not-allowed;" title="Không thể xóa task đã hoàn thành">
                                                                <i class="fa-solid fa-trash"></i>
                                                            </span>
                                                        </c:if>
                                                    </td>
                                                </c:if>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr><td colspan="${isLeader ? '7' : '6'}" style="text-align:center; color:#777;">Không có công việc nào.</td></tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div id="deleteConfirmModal" class="modal">
            <div class="modal-content" style="width: 350px; text-align:center;">
                <p style="font-size:16px;">Bạn có chắc muốn xóa task này không?</p>
                <div style="margin-top:20px;">
                    <button id="confirmDeleteBtn" class="btn btn-del" style="margin-right:10px;">Xóa</button>
                    <button id="cancelDeleteBtn" class="btn" style="background:#ccc; color:#000;">Hủy</button>
                </div>
            </div>
        </div>
        <div id="taskModal" class="modal">
            <div class="modal-content">
                <span class="close-btn">&times;</span>
                <div id="taskModalBody">
                    <!-- AJAX sẽ load nội dung ở đây -->
                </div>
            </div>
        </div>
        <div id="toast" class="toast">Cập nhật thành công!</div>
        <script>
            let taskIdToDelete = null; // Lưu lại id task cần xóa

            document.addEventListener("click", function (e) {
                const editBtn = e.target.closest(".edit-btn");
                const deleteBtn = e.target.closest(".delete-btn");

                // === Xử lý EDIT ===
                if (editBtn) {
                    e.preventDefault();
                    const taskId = editBtn.getAttribute("data-task-id");
                    const modal = document.getElementById("taskModal");
                    const modalBody = document.getElementById("taskModalBody");

                    fetch(`${window.location.origin}/group-trip/group/manage/tasks-edit?task_id=` + taskId)
                            .then(response => response.text())
                            .then(html => {
                                modalBody.innerHTML = html;
                                modal.style.display = "block";
                            })
                            .catch(err => {
                                modalBody.innerHTML = "<p style='color:red;'>Lỗi tải dữ liệu!</p>";
                                modal.style.display = "block";
                            });
                }

                // === Xử lý DELETE ===
                if (deleteBtn) {
                    e.preventDefault();
                    taskIdToDelete = deleteBtn.getAttribute("data-task-id");
                    // Hiện modal confirm
                    document.getElementById("deleteConfirmModal").style.display = "block";
                }
            });

            // === Xử lý nút xác nhận xóa ===
document.getElementById("confirmDeleteBtn").addEventListener("click", function () {
    if (!taskIdToDelete) return;

    const params = new URLSearchParams();
    params.append("task_id", taskIdToDelete);

    fetch(`${window.location.origin}/group-trip/group/manage/tasks-delete`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params.toString()
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                closeDeleteModal();
                showToast("Xóa thành công!");
                setTimeout(() => window.location.reload(), 1000);
            } else {
                closeDeleteModal();
                showToast("Xóa thất bại: " + data.message, true);
            }
        })
        .catch(err => {
            closeDeleteModal();
            showToast("Lỗi khi xóa: " + err, true);
        });
});              // === Xử lý nút hủy xóa ===
                    document.getElementById("cancelDeleteBtn").addEventListener("click", closeDeleteModal);

                    function closeDeleteModal() {
                        document.getElementById("deleteConfirmModal").style.display = "none";
                        taskIdToDelete = null;
                    }

                    // === Toast thông báo ===
                    function showToast(message, isError = false) {
                        const toast = document.getElementById("toast");
                        toast.textContent = message;
                        toast.classList.remove("toast-error");
                        if (isError)
                            toast.classList.add("toast-error");
                        toast.classList.add("show");
                        setTimeout(() => toast.classList.remove("show"), 2500);
                    }

                    // === Hàm xóa bộ lọc ===
                    function clearFilters() {
                        document.getElementById("keywordInput").value = "";
                        document.getElementById("statusSelect").selectedIndex = 0;
                        const form = document.querySelector('form[action*="group/manage/tasks"]');
                        const groupId = form.querySelector('input[name="groupId"]').value;
                        window.location.href = `${window.location.origin}/group-trip/group/manage/tasks?groupId=${groupId}`;
                            }

                            // === Đóng modal chi tiết task ===
                            const modal = document.getElementById("taskModal");
                            const closeBtn = document.querySelector(".close-btn");

                            if (closeBtn) {
                                closeBtn.addEventListener("click", closeModal);
                            }

                            window.addEventListener("click", function (event) {
                                if (event.target === modal) {
                                    closeModal();
                                }
                            });

                            function closeModal() {
                                const modal = document.getElementById("taskModal");
                                const modalBody = document.getElementById("taskModalBody");
                                modal.style.display = "none";
                                modalBody.innerHTML = ""; // Xóa nội dung để tránh rác DOM
                            }
        </script>


    </body>
    <jsp:include page="/views/partials/footer.jsp"/>
</html>
