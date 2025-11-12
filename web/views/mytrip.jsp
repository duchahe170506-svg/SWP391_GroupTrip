<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="partials/header.jsp"/>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8"/>
        <title>My Trips</title>
        <style>
            body {
                font-family: 'Inter', Arial, sans-serif !important;
                background: #f0f3f6 !important;
                margin: 0 !important;
                padding: 0 !important;
                min-height: 100vh !important;
                display: flex !important;
                flex-direction: column !important;
            }

            /* Container chính */
            .wrap {
                flex: 1 !important;
                max-width: 980px !important;
                margin: 32px auto !important;
                padding: 0 18px !important;
            }

            /* Thẻ hiển thị chuyến đi */
            .card {
                background: #fff !important;
                border-radius: 14px !important;
                padding: 20px !important;
                border: 1px solid #dde2eb !important;
                box-shadow: 0 3px 10px rgba(0,0,0,0.06) !important;
                transition: 0.25s !important;
            }

            .card:hover {
                transform: translateY(-2px) !important;
                box-shadow: 0 6px 16px rgba(0,0,0,0.08) !important;
            }

            /* Danh sách chuyến đi */
            .trip-list {
                display: flex !important;
                flex-direction: column !important;
                gap: 16px !important;
            }

            /* Nút */
            .btn {
                padding: 8px 12px !important;
                border-radius: 8px !important;
                border: none !important;
                cursor: pointer !important;
                text-decoration: none !important;
                font-size: 14px !important;
                display: inline-block !important;
                transition: 0.2s ease !important;
            }

            .btn.primary {
                background: #77a096 !important;
                color: #fff !important;
            }

            .btn.primary:hover {
                background: #5d857c !important;
            }

            .btn.danger {
                background: #e74c3c !important;
                color: white !important;
            }

            .btn.danger:hover {
                background: #c0392b !important;
            }

            .btn:hover {
                opacity: 0.9 !important;
            }

            /* Thông báo */
            .message {
                padding: 10px 14px !important;
                border-radius: 6px !important;
                margin-bottom: 18px !important;
                font-size: 14px !important;
            }

            .message.success {
                background: #d1fae5 !important;
                color: #065f46 !important;
            }
            .message.warning {
                background: #fff3cd !important;
                color: #7a5c00 !important;
            }
            .message.error {
                background: #fde2e1 !important;
                color: #7f1d1d !important;
            }

            /* Section Title */
            h1, h2 {
                color: #1b1f23 !important;
                margin-bottom: 14px !important;
            }

            h2 {
                font-size: 18px !important;
                border-left: 4px solid #77a096 !important;
                padding-left: 10px !important;
                margin-top: 34px !important;
            }

            /* Footer */
            footer {
                margin-top: auto !important;
                background: #fff !important;
                border-top: 1px solid #e5e7eb !important;
                padding: 14px 0 !important;
                text-align: center !important;
                color: #444 !important;
                font-size: 14px !important;
            }

            /* Action buttons row */
            .actions {
                display: flex !important;
                flex-wrap: wrap !important;
                gap: 8px !important;
                margin-top: 12px !important;
                align-items: center !important;
            }

        </style>

    </head>
    <body>
        <main>
            <div class="wrap">
                <c:if test="${param.deleted == '1'}">
                    <div class="message success">✅ Xóa chuyến đi thành công.</div>
                </c:if>

                <c:if test="${param.cantdelete == '1'}">
                    <div class="message warning">
                        ⚠️ Không thể xóa chuyến đi vì đã có người tham gia.
                        Hãy đổi trạng thái chuyến đi sang <strong>Blocked</strong> nếu muốn ngừng hoạt động.
                    </div>
                </c:if>

                <c:if test="${param.error == '1'}">
                    <div class="message error">❌ Xảy ra lỗi khi xóa chuyến đi.</div>
                </c:if>

                <h1>Chuyến đi của tôi</h1>

                <!-- Leader section -->
                <c:if test="${not empty leaderTrips}">
                    <h2>Chuyến đi tôi tạo</h2>
                    <div class="trip-list">
                        <c:forEach var="t" items="${leaderTrips}">
                            <div class="card">
                                <strong><c:out value="${t.name}"/></strong> — <c:out value="${t.location}"/>
                                <div>
                                    Thời gian:
                                    <fmt:formatDate value="${t.startDate}" pattern="dd/MM/yyyy"/> →
                                    <fmt:formatDate value="${t.endDate}" pattern="dd/MM/yyyy"/>
                                </div>
                                <div class="pill">Trạng thái: <c:out value="${t.status}"/></div>

                                <c:choose>
                                    <c:when test="${t.status == 'Blocked'}">
                                        <div class="message warning">
                                            ⚠️ Chuyến đi vi phạm đã bị Admin khóa.
                                        </div>
                                    </c:when>
                                    <c:otherwise>

                                        <div class="actions">
                                            <a class="btn primary" href="${pageContext.request.contextPath}/group/manage?groupId=${t.groupId}">
                                                Xem
                                            </a>
                                            <a class="btn" href="<c:url value='/trip/edit?id=${t.tripId}'/>">Sửa</a>
                                            <a class="btn danger" href="<c:url value='/trip/delete?id=${t.tripId}'/>"
                                               onclick="return confirm('Bạn có chắc muốn xóa chuyến đi này?')">Xóa</a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>       

                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <!-- Joined section -->
                <c:if test="${not empty joinedTrips}">
                    <h2>Chuyến đi tôi đã tham gia</h2>
                    <div class="trip-list">
                        <c:forEach var="t" items="${joinedTrips}">
                            <div class="card">
                                <strong><c:out value="${t.name}"/></strong> — <c:out value="${t.location}"/>
                                <div>
                                    Thời gian:
                                    <fmt:formatDate value="${t.startDate}" pattern="dd/MM/yyyy"/> →
                                    <fmt:formatDate value="${t.endDate}" pattern="dd/MM/yyyy"/>
                                </div>

                                <c:choose>
                                    <c:when test="${t.status == 'Blocked'}">
                                        <div class="message warning">
                                            ⚠️ Chuyến đi vi phạm đã bị admin khóa.
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="actions">
                                            <a class="btn primary" href="${pageContext.request.contextPath}/group/manage?groupId=${t.groupId}">Xem</a>
                                            <a class="btn primary" href="<c:url value='/trip/detail?id=${t.tripId}'/>">Xem chi tiết</a>
                                        </div>
                                    </c:otherwise>
                                </c:choose> 

                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <c:if test="${empty joinedTrips and empty leaderTrips}">
                    <p>Hiện tại bạn chưa tham gia hoặc tạo chuyến đi nào.</p>
                </c:if>
            </div>
        </main>

        <jsp:include page="partials/footer.jsp"/>
    </body>
</html>
