<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="/views/partials/header.jsp"/>

<html>
    <head>
        <title>Chỉnh sửa lịch trình</title>
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
                max-width: 700px;
            }

            .content h2 {
                margin-top: 0;
            }

            label {
                display: block;
                margin-bottom: 6px;
                font-weight: 600;
            }

            input[type="number"],
            input[type="text"],
            input[type="time"],
            textarea {
                width: 100%;
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 6px;
                margin-bottom: 14px;
                font-size: 14px;
            }

            textarea {
                resize: vertical;
                min-height: 90px;
            }

            .btn {
                background-color: #77a096;
                color: white;
                padding: 8px 16px;
                border-radius: 6px;
                text-decoration: none;
                transition: 0.3s;
                border: none;
                cursor: pointer;
            }

            .btn:hover {
                background-color: #e67e22;
            }

            .btn-secondary {
                background-color: #ccc;
                color: #000;
            }

            .alert-error {
                padding: 10px;
                margin-bottom: 15px;
                background-color: #ffebee;
                color: #c62828;
                border-radius: 6px;
                border-left: 4px solid #c62828;
            }
        </style>

        <div class="layout">
            <div class="sidebar">
                <h3>Group Menu</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/group/manage/timeline?groupId=${groupId}" class="active">🕒 Time Line</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/manage?groupId=${groupId}" class="active">👥 Members</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/manage/tasks?groupId=${groupId}">🧾 Tasks</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/expense?groupId=${groupId}">💰 Expense</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/memories?groupId=${groupId}">📸 Memories</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/notifications?groupId=${groupId}">🔔 Notification</a></li>
                </ul>
            </div>

            <div class="content">
                <h2>Chỉnh sửa lịch trình #${itinerary.itineraryId}</h2>
                <p>Chuyến đi: <strong>${trip.name}</strong></p>

                <c:if test="${not empty errorMessage}">
                    <div class="alert-error">${errorMessage}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/group/manage/itinerary-edit" method="post">
                    <input type="hidden" name="itinerary_id" value="${itinerary.itineraryId}" />
                    <input type="hidden" name="groupId" value="${groupId}"/>

                    <label for="day_number">Ngày thứ <span style="color:#c62828">*</span></label>
                    <input type="number" id="day_number" name="day_number" min="1" value="${itinerary.dayNumber}" required />

                    <label for="title">Tiêu đề</label>
                    <input type="text" id="title" name="title" value="${itinerary.title}" maxlength="150" />

                    <label for="description">Mô tả</label>
                    <textarea id="description" name="description">${itinerary.description}</textarea>

                    <label for="start_time">Thời gian bắt đầu <span style="color:#c62828">*</span></label>
                    <input type="time" id="start_time" name="start_time"
                           value="<c:if test='${itinerary.startTime != null}'><fmt:formatDate value='${itinerary.startTime}' pattern='HH:mm'/></c:if>" required />

                    <label for="end_time">Thời gian kết thúc <span style="color:#c62828">*</span></label>
                    <input type="time" id="end_time" name="end_time"
                           value="<c:if test='${itinerary.endTime != null}'><fmt:formatDate value='${itinerary.endTime}' pattern='HH:mm'/></c:if>" required />

                    <div style="display:flex; gap:10px; margin-top:18px;">
                        <button type="submit" class="btn"><i class="fa-solid fa-floppy-disk"></i> Lưu</button>
                        <a href="${pageContext.request.contextPath}/group/manage/timeline?groupId=${groupId}" class="btn btn-secondary">
                            &larr; Quay lại
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </body>
    <jsp:include page="/views/partials/footer.jsp"/>
</html>

