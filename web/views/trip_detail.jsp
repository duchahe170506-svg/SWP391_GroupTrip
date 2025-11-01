<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/views/partials/header.jsp"/>

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
                padding: 0;
            }
            .wrap {
                max-width: 1000px;
                margin: 30px auto;
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.08);
                padding: 20px 30px;
            }
            h1 {
                color: #1d4ed8;
                margin-top: 0;
            }

            .top-section {
                display: flex;
                gap: 20px;
                margin-bottom: 24px;
                align-items: flex-start;
            }
            .cover {
                flex: 2;
            }
            .cover img {
                width: 100%;
                height: 350px;
                object-fit: cover;
                border-radius: 12px;
                border: 1px solid #ddd;
            }
            .side-images {
                flex: 1;
                display: flex;
                flex-direction: column;
                gap: 10px;
            }
            .side-images img {
                width: 100%;
                height: 110px;
                object-fit: cover;
                border-radius: 10px;
                border: 1px solid #ddd;
            }

            .info-section {
                display: flex;
                flex-wrap: wrap;
                gap: 10px;
                margin-top: 10px;
            }
            .pill {
                flex: 1 1 calc(33% - 10px);
                background: #f0f3f9;
                border: 1px solid #dfe4ef;
                border-radius: 8px;
                padding: 8px 12px;
                font-size: 14px;
            }

            .description {
                margin-top: 16px;
                line-height: 1.6;
                white-space: pre-wrap;
            }
            .itinerary {
                margin-top: 24px;
            }
            .itinerary-item {
                background: #f8fafc;
                border: 1px solid #e5e7eb;
                border-radius: 10px;
                padding: 10px 14px;
                margin-bottom: 10px;
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
            <h1><c:out value="${trip.name}"/></h1>

            <div class="top-section">
                <!-- Ảnh bìa -->
                <div class="cover">
                    <c:if test="${not empty trip.coverImage}">
                        <img src="${trip.coverImage}" alt="Ảnh bìa"/>
                    </c:if>
                </div>

                <!-- Ảnh phụ -->
                <div class="side-images">
                    <c:if test="${not empty images}">
                        <c:forEach var="img" items="${images}" varStatus="loop">
                            <c:if test="${loop.index < 3}">
                                <img src="${img.imageUrl}" alt="Trip image"/>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </div>
            </div>

            <!-- Thông tin -->
            <div class="info-section">
                <div class="pill">📍 Địa điểm: <c:out value="${trip.location}"/></div>
                <div class="pill">Điểm tập trung: 
                    <c:out value="${trip.meeting_point != null ? trip.meeting_point : 'Chưa cập nhật'}"/>
                </div>
                <div class="pill">Ngày đi: <fmt:formatDate value="${trip.startDate}" pattern="dd/MM/yyyy"/></div>
                <div class="pill">Ngày kết thúc: <fmt:formatDate value="${trip.endDate}" pattern="dd/MM/yyyy"/></div>
                <div class="pill">Ngân sách: <fmt:formatNumber value="${trip.budget}" type="currency" currencySymbol="₫"/></div>
                <div class="pill">Loại: <c:out value="${trip.tripType}"/></div>
                <div class="pill">Trạng thái: <c:out value="${trip.status}"/></div>
                <div class="pill">👥 Tối thiểu: <c:out value="${trip.min_participants}"/></div>
                <div class="pill">Số người: 
                    <c:out value="${participantCount}"/> /
                    <c:choose>
                        <c:when test="${trip.max_participants gt 0}">
                            <c:out value="${trip.max_participants}"/>
                        </c:when>
                        <c:otherwise>Không giới hạn</c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Mô tả -->
            <h3>Mô tả</h3>
            <div class="description"><c:out value="${trip.description}"/></div>

            <!-- Lịch trình -->
            <div class="itinerary">
                <h3>🗓️ Lịch trình chi tiết</h3>
                <c:if test="${not empty itineraries}">
                    <c:forEach var="it" items="${itineraries}">
                        <div class="itinerary-item">
                            <strong>Ngày ${it.dayNumber}:</strong> <c:out value="${it.title}"/><br/>
                            <small><fmt:formatDate value="${it.startTime}" pattern="HH:mm"/> - 
                                <fmt:formatDate value="${it.endTime}" pattern="HH:mm"/></small>
                            <div><c:out value="${it.description}"/></div>
                        </div>
                    </c:forEach>
                </c:if>
                <c:if test="${empty itineraries}">
                    <p><em>Chưa có lịch trình nào được thêm.</em></p>
                </c:if>
            </div>

            <!-- Nút quay lại -->
            <c:choose>
                <c:when test="${from eq 'mytrip'}">
                    <a class="back" href="<c:url value='/mytrips'/>">← Quay lại My Trip</a>
                </c:when>
                <c:otherwise>
                    <a class="back" href="<c:url value='/trips'/>">← Quay lại danh sách</a>
                </c:otherwise>
            </c:choose>
            <a class="back" href="<c:url value='/notifications'/>">← Quay lại Thông báo</a>
        </div>

        <jsp:include page="/views/partials/footer.jsp"/>
    </body>
</html>
