<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết chuyến đi</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f7fb;
            margin: 0;
        }
        .wrap {
            max-width: 900px;
            margin: 24px auto;
            padding: 0 16px;
        }
        .card {
            background: #fff;
            border: 1px solid #e5e7ee;
            border-radius: 12px;
            padding: 20px;
        }
        h1 { margin-top: 0; color: #1d4ed8; }
        .images { display: flex; gap: 10px; margin: 16px 0; flex-wrap: wrap; }
        .images img {
            width: 250px;
            height: 170px;
            object-fit: cover;
            border-radius: 10px;
            border: 1px solid #ddd;
        }
        .pill {
            display: inline-block;
            background: #f0f3f9;
            border: 1px solid #dfe4ef;
            border-radius: 8px;
            padding: 6px 10px;
            margin: 4px 8px 4px 0;
        }
        .description {
            white-space: pre-wrap;
            line-height: 1.6;
            margin-top: 8px;
        }
        .back {
            display: inline-block;
            margin-top: 20px;
            text-decoration: none;
            padding: 10px 14px;
            border-radius: 8px;
            border: 1px solid #ccc;
            background: #eee;
            color: #333;
        }
    </style>
</head>
<body>
<div class="wrap">
    <div class="card">
        <h1><c:out value="${trip.name}"/></h1>

        <!-- Ảnh bìa -->
        <c:if test="${not empty trip.coverImage}">
            <img src="${trip.coverImage}" alt="Ảnh bìa"
                 style="width:100%;max-height:400px;object-fit:cover;
                 border-radius:12px;border:1px solid #ddd;margin-bottom:20px"/>
        </c:if>

        <!-- Ảnh phụ -->
        <c:if test="${not empty images}">
            <h3>Ảnh khác</h3>
            <div class="images">
                <c:forEach var="img" items="${images}">
                    <img src="${img.imageUrl}" alt="Trip image"/>
                </c:forEach>
            </div>
        </c:if>

        <!-- Thông tin cơ bản -->
        <div class="pill">Địa điểm: <c:out value="${trip.location}"/></div>

        <!-- 🟩 Thêm địa điểm tập trung -->
        <div class="pill">📍 Điểm tập trung: 
            <c:out value="${trip.meeting_point != null ? trip.meeting_point : 'Chưa cập nhật'}"/>
        </div>

        <div class="pill">Ngày đi: <fmt:formatDate value="${trip.startDate}" pattern="dd/MM/yyyy"/></div>
        <div class="pill">Ngày kết thúc: <fmt:formatDate value="${trip.endDate}" pattern="dd/MM/yyyy"/></div>
        <div class="pill">Ngân sách: <fmt:formatNumber value="${trip.budget}" type="currency" currencySymbol="₫"/></div>
        <div class="pill">Loại: <c:out value="${trip.tripType}"/></div>
        <div class="pill">Trạng thái: <c:out value="${trip.status}"/></div>

        <div class="pill">
            Số người tham gia: 
            <c:out value="${participantCount}"/> /
            <c:choose>
                <c:when test="${trip.max_participants gt 0}">
                    <c:out value="${trip.max_participants}"/>
                </c:when>
                <c:otherwise>Không giới hạn</c:otherwise>
            </c:choose>
        </div>

        <!-- 🟩 Thêm số người tối thiểu -->
        <div class="pill">👥 Số người tối thiểu: 
            <c:out value="${trip.min_participants}"/>
        </div>

        <!-- Mô tả -->
        <h3>Mô tả</h3>
        <div class="description">
            <c:out value="${trip.description}"/>
        </div>
<!-- 🟢 Lịch trình -->
<c:if test="${not empty itineraries}">
    <h3 style="margin-top:24px;">🗓️ Lịch trình chi tiết</h3>
    <div>
        <c:forEach var="it" items="${itineraries}">
            <div class="pill">
                <strong>Ngày ${it.dayNumber}:</strong>
                <c:out value="${it.title}"/>
                <br/>
                <span class="muted">
                    <fmt:formatDate value="${it.startTime}" pattern="HH:mm"/> -
                    <fmt:formatDate value="${it.endTime}" pattern="HH:mm"/>
                </span>
                <div style="margin-top:4px;">
                    <c:out value="${it.description}"/>
                </div>
            </div>
        </c:forEach>
    </div>
</c:if>
<c:if test="${empty itineraries}">
    <p><em>Chưa có lịch trình nào được thêm.</em></p>
</c:if>

        <!-- Nút quay lại -->
        <c:choose>
            <c:when test="${from eq 'mytrip'}">
                <a class="back" href="<c:url value='/mytrips'/>">← Quay lại My Trip</a>
            </c:when>
            <c:otherwise>
                <a class="back" href="<c:url value='/trips'/>">← Quay lại danh sách</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
