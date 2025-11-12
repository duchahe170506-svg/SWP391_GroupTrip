<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="/views/partials/header.jsp"/>

<html>
    <head>
        <title>Quản lý công việc</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
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

        <div class="layout" style="min-height: 729px;">
            <!-- ==== SIDEBAR TRÁI ==== -->
            <div class="sidebar">
                <h3>Group Menu</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/group/manage/timeline?groupId=${groupId}">🕒 Time Line</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/manage?groupId=${groupId}">👥 Members</a></li>
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
                    <!-- Bảng hiển thị dữ liệu -->
                    <div class="content" style="margin:20px; padding:20px; max-width:600px; background:#fff; border-radius:8px; box-shadow:0 2px 6px rgba(0,0,0,0.1);">
                        <h2>Chỉnh sửa công việc #${task.task_id}</h2>
                        <c:if test="${not empty errorMessage}">
                            <div style="padding:10px; margin-bottom:15px; background-color:#ffebee; color:#c62828; border-radius:4px; border-left:4px solid #c62828;">
                                ${errorMessage}
                            </div>
                        </c:if>
                        <c:choose>
                            <c:when test="${not isLeader}">
                                <div style="padding:15px; background-color:#fff3cd; color:#856404; border-radius:4px; border-left:4px solid #ffc107;">
                                    <strong>Thông báo:</strong> Bạn không có quyền chỉnh sửa task này. Chỉ người tạo chuyến đi mới có quyền này.
                                </div>
                                <br>
                                <a href="${pageContext.request.contextPath}/group/manage/tasks?groupId=${groupId}" style="color:#2980b9;">&larr; Quay lại danh sách</a>
                            </c:when>
                            <c:otherwise>
                                <form id="editTaskForm" action="${pageContext.request.contextPath}/group/manage/tasks-edit" method="post">
                                    <input type="hidden" name="task_id" value="${task.task_id}" />
                                    <input type="hidden" name="trip_id" value="${task.trip_id}" />

                                    <div style="margin-bottom:12px;">
                                        <label>Mô tả<span class="required-star">*</span>:</label>
                                        <textarea name="description" style="width:100%; padding:8px; border-radius:4px;" required>${task.description}</textarea>
                                    </div>

                                    <div style="margin-bottom:12px;">
                                        <label>Deadline<span class="required-star">*</span>:</label>
                                        <input type="date" id="deadline" name="deadline" value="${task.deadline}" style="padding:8px; border-radius:4px; width:100%;" required />
                                        <small style="color:#666;">
                                            <c:if test="${trip != null && trip.startDate != null && trip.endDate != null}">
                                                (Chuyến đi: <fmt:formatDate value="${trip.startDate}" pattern="dd/MM/yyyy"/> - <fmt:formatDate value="${trip.endDate}" pattern="dd/MM/yyyy"/>)
                                            </c:if>
                                        </small>
                                    </div>

                                    <div style="margin-bottom:12px;">
                                        <label>Chi phí dự kiến (VNĐ)<span class="required-star">*</span>:</label>
                                        <input type="number" name="estimated_cost" value="${task.estimated_cost}" min="0" style="padding:8px; border-radius:4px; width:100%;" required />
                                    </div>

                                    <div style="margin-bottom:12px;">
                                        <label>Trạng thái<span class="required-star">*</span>:</label>
                                        <select name="status" style="padding:8px; border-radius:4px; width:100%;" required>
                                            <option value="Pending" ${task.status == 'Pending' ? 'selected' : ''}>Pending</option>
                                            <option value="InProgress" ${task.status == 'InProgress' ? 'selected' : ''}>In Progress</option>
                                            <option value="Completed" ${task.status == 'Completed' ? 'selected' : ''}>Completed</option>
                                        </select>
                                    </div>
                                    <label for="assigned_to">Giao cho:</label><br>
                                    <select name="assigned_to" id="assigned_to" style="padding:5px; margin-bottom:10px;">
                                        <option value="">-- Chưa giao --</option>
                                        <c:forEach var="member" items="${listMember}">
                                            <option value="${member.user_id}" ${task.assigned_to == member.user_id ? 'selected' : ''}>
                                                ${member.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <div style="text-align:right;">
                                        <button type="submit" style="padding:8px 16px; background:#4CAF50; color:white; border:none; border-radius:6px;">Cập nhật</button>
                                    </div>
                                </form>
                                <br>
                                <a href="${pageContext.request.contextPath}/group/manage/tasks?groupId=${groupId}" style="color:#2980b9;">&larr; Quay lại danh sách</a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <script>
        <c:if test="${isLeader}">
            // Set minimum date to today (00:00:00)
            const today = new Date();
            today.setHours(0, 0, 0, 0); // Set về đầu ngày
            const dd = String(today.getDate()).padStart(2, '0');
            const mm = String(today.getMonth() + 1).padStart(2, '0');
            const yyyy = today.getFullYear();
            const todayStr = yyyy + '-' + mm + '-' + dd;
            
            const deadlineInput = document.getElementById('deadline');
            if (deadlineInput) {
                deadlineInput.setAttribute('min', todayStr);
                
                // Set max date to trip end date if available
                <c:if test="${trip != null && trip.endDate != null}">
                    const tripEndDate = new Date('${trip.endDate}');
                    const endDD = String(tripEndDate.getDate()).padStart(2, '0');
                    const endMM = String(tripEndDate.getMonth() + 1).padStart(2, '0');
                    const endYYYY = tripEndDate.getFullYear();
                    const endDateStr = endYYYY + '-' + endMM + '-' + endDD;
                    deadlineInput.setAttribute('max', endDateStr);
                </c:if>
                
                // Set min date to trip start date if available
                <c:if test="${trip != null && trip.startDate != null}">
                    const tripStartDate = new Date('${trip.startDate}');
                    const startDD = String(tripStartDate.getDate()).padStart(2, '0');
                    const startMM = String(tripStartDate.getMonth() + 1).padStart(2, '0');
                    const startYYYY = tripStartDate.getFullYear();
                    const startDateStr = startYYYY + '-' + startMM + '-' + startDD;
                    
                    // So sánh và chọn ngày lớn hơn giữa today và start date
                    if (tripStartDate > today) {
                        deadlineInput.setAttribute('min', startDateStr);
                    }
                </c:if>
                
                const editForm = document.getElementById('editTaskForm');
                if (editForm) {
                    editForm.addEventListener('submit', function (e) {
                        // Validate required fields
                        const fields = ['description', 'deadline', 'estimated_cost', 'status'];
                        for (let i = 0; i < fields.length; i++) {
                            const field = this.elements[fields[i]];
                            if (!field.value) {
                                e.preventDefault();
                                field.focus();
                                alert('Vui lòng điền đầy đủ thông tin cho ' + fields[i].replace('_', ' '));
                                return;
                            }
                        }
                        
                        // Validate deadline
                        const deadlineValue = deadlineInput.value;
                        if (deadlineValue) {
                            const selectedDate = new Date(deadlineValue);
                            selectedDate.setHours(0, 0, 0, 0); // Set về đầu ngày
                            
                            // Kiểm tra deadline không trong quá khứ (cho phép = hôm nay)
                            if (selectedDate < today) {
                                e.preventDefault();
                                alert('Deadline không được trong quá khứ (phải từ hôm nay trở đi)!');
                                deadlineInput.focus();
                                return;
                            }
                            
                            <c:if test="${trip != null}">
                                // Kiểm tra deadline trong khoảng start_date và end_date
                                <c:if test="${trip.startDate != null}">
                                    const tripStart = new Date('${trip.startDate}');
                                    if (selectedDate < tripStart) {
                                        e.preventDefault();
                                        alert('Deadline phải sau ngày bắt đầu chuyến đi!');
                                        deadlineInput.focus();
                                        return;
                                    }
                                </c:if>
                                
                                <c:if test="${trip.endDate != null}">
                                    const tripEnd = new Date('${trip.endDate}');
                                    if (selectedDate > tripEnd) {
                                        e.preventDefault();
                                        alert('Deadline phải trước ngày kết thúc chuyến đi!');
                                        deadlineInput.focus();
                                        return;
                                    }
                                </c:if>
                            </c:if>
                        }
                    });
                }
            }
        </c:if>
    </script>
    <jsp:include page="/views/partials/footer.jsp"/>
</html>
