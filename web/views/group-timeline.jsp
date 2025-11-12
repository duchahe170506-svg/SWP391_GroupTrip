<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="/views/partials/header.jsp"/>

<html>
    <head>
        <title>Timeline chuyến đi</title>
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

            .table-container {
                max-height: 600px;
                overflow-y: auto;
                border: 1px solid #ddd;
                border-radius: 6px;
            }

            .table-container table {
                width: 100%;
                border-collapse: collapse;
            }

            .table-container th,
            .table-container td {
                border: 1px solid #ddd;
                padding: 8px;
                text-align: left;
            }

            .table-container th {
                position: sticky;
                top: 0;
                background-color: #77a096;
                color: white;
                z-index: 2;
            }

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

            .btn-icon {
                color: #555;
                margin: 0 6px;
                font-size: 18px;
                transition: 0.3s;
                text-decoration: none;
                background: none;
                border: none;
                cursor: pointer;
                padding: 0;
            }

            .btn-icon:hover {
                color: #2980b9;
            }

            .btn-icon.delete-btn:hover {
                color: #e74c3c;
            }

            .inline-form {
                display: inline;
            }

        </style>

        <div class="layout">
            <div class="sidebar">
                <h3>Group Menu</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/group/manage/timeline?groupId=${groupId}" class="active">🕒 Time Line</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/manage?groupId=${groupId}">👥 Members</a></li>
                    <li><a href="#">🎯 Activities</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/manage/tasks?groupId=${groupId}">🧾 Tasks</a></li>
                    <li><a href="#">💰 Expense</a></li>
                    <li><a href="${pageContext.request.contextPath}/group-memories?groupId=${groupId}">📸 Memories</a></li>
                    <li><a href="#">🔔 Notification</a></li>
                </ul>
            </div>

            <div class="content">
                <div style="display:flex; justify-content:space-between; align-items:center;">
                    <h2>Lịch trình chuyến đi: ${trip.name}</h2>
                    <c:if test="${isLeader}">
                        <a href="${pageContext.request.contextPath}/itinerary/create?tripId=${tripId}" class="btn">+ Add Timeline</a>
                    </c:if>
                </div>

                <div class="table-container" style="margin-top:18px;">
                    <c:if test="${param.updateSuccess == '1'}">
                        <div style="padding:10px; margin-bottom:15px; background:#e8f5e9; color:#2e7d32; border-radius:6px;">
                            Cập nhật lịch trình thành công.
                        </div>
                    </c:if>
                    <c:if test="${param.deleteSuccess == '1'}">
                        <div style="padding:10px; margin-bottom:15px; background:#fdecea; color:#c62828; border-radius:6px;">
                            Đã xóa lịch trình thành công.
                        </div>
                    </c:if>

                    <table>
                        <thead>
                            <tr>
                                <th style="width: 60px;">No</th>
                                <th>Ngày</th>
                                <th>Thời gian</th>
                                <th>Tiêu đề</th>
                                <th>Mô tả</th>
                                <c:if test="${isLeader}">
                                    <th style="width:120px; text-align:center;">Hành động</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty itineraries}">
                                    <c:forEach var="itinerary" items="${itineraries}" varStatus="loop">
                                        <tr>
                                            <td style="text-align:center;">${loop.index + 1}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${itinerary.dayNumber > 0}">
                                                        Ngày ${itinerary.dayNumber}
                                                    </c:when>
                                                    <c:otherwise>
                                                        -
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${itinerary.startTime != null && itinerary.endTime != null}">
                                                        <fmt:formatDate value="${itinerary.startTime}" pattern="HH:mm"/> - <fmt:formatDate value="${itinerary.endTime}" pattern="HH:mm"/>
                                                    </c:when>
                                                    <c:when test="${itinerary.startTime != null}">
                                                        <fmt:formatDate value="${itinerary.startTime}" pattern="HH:mm"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        -
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${itinerary.title != null ? itinerary.title : '-'}</td>
                                            <td>${fn:length(itinerary.description) > 0 ? itinerary.description : '-'}</td>
                                            <c:if test="${isLeader}">
                                                <td style="text-align:center;">
                                                    <a href="${pageContext.request.contextPath}/group/manage/itinerary-edit?itinerary_id=${itinerary.itineraryId}"
                                                       class="btn-icon" title="Chỉnh sửa">
                                                        <i class="fa-solid fa-pen-to-square"></i>
                                                    </a>
                                                    <form action="${pageContext.request.contextPath}/group/manage/itinerary-delete"
                                                          method="post" class="inline-form">
                                                        <input type="hidden" name="itinerary_id" value="${itinerary.itineraryId}"/>
                                                        <button type="submit" class="btn-icon delete-btn" title="Xóa"
                                                                onclick="return confirm('Bạn chắc chắn muốn xóa lịch trình này?');">
                                                            <i class="fa-solid fa-trash"></i>
                                                        </button>
                                                    </form>
                                                </td>
                                            </c:if>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="${isLeader ? '6' : '5'}" style="text-align:center; color:#777;">Chưa có hoạt động nào.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
    <jsp:include page="/views/partials/footer.jsp"/>
</html>

