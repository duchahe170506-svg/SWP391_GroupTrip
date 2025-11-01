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
                font-family: Arial, sans-serif !important;
                background: #f5f7fb !important;
                margin: 0 !important;
                padding: 0 !important;
                min-height: 100vh !important;
                display: flex !important;
                flex-direction: column !important;
            }

            .wrap {
                flex: 1 !important; /* Giúp footer luôn nằm dưới cùng */
                max-width: 1000px !important;
                margin: 24px auto !important;
                padding: 0 16px !important;
            }

            .card {
                background: #fff !important;
                border: 1px solid #e5e7ee !important;
                border-radius: 12px !important;
                padding: 20px !important;
                margin-bottom: 16px !important;
                box-shadow: 0 2px 6px rgba(0,0,0,0.05) !important;
            }

            .trip-list {
                display: flex !important;
                flex-direction: column !important;
                gap: 14px !important;
            }

            .btn {
                padding: 8px 12px !important;
                border-radius: 8px !important;
                border: 1px solid #ccc !important;
                text-decoration: none !important;
                color: #333 !important;
                background: #eee !important;
                transition: background 0.2s ease !important;
            }

            .btn:hover {
                background: #ddd !important;
            }

            .btn.primary {
                background: #77a096 !important;
                color: #fff !important;
                border: none !important;
            }

            .btn.primary:hover {
                background: #77a096 !important;
            }

            .btn.danger {
                background: #dc2626 !important;
                color: #fff !important;
                border: none !important;
            }

            .btn.danger:hover {
                background: #b91c1c !important;
            }

            h1, h2 {
                color: #111827 !important;
            }

            h2 {
                margin-top: 32px !important;
                border-bottom: 2px solid #ddd !important;
                padding-bottom: 6px !important;
            }

            /* Đảm bảo footer luôn ở dưới */
            footer {
                margin-top: auto !important;
                background: #fff !important;
                border-top: 1px solid #e5e7eb !important;
                padding: 12px 0 !important;
                text-align: center !important;
                color: #555 !important;
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
                                <div class="actions">
                                    <a class="btn primary" href="${pageContext.request.contextPath}/group/manage?groupId=${t.groupId}">
                                        Xem
                                    </a>
                                    <a class="btn" href="<c:url value='/trip/edit?id=${t.tripId}'/>">Sửa</a>
                                    <a class="btn danger" href="<c:url value='/trip/delete?id=${t.tripId}'/>"
                                       onclick="return confirm('Bạn có chắc muốn xóa chuyến đi này?')">Xóa</a>
                                </div>
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
                                <a class="btn primary" href="<c:url value='/trip/detail?id=${t.tripId}'/>">Xem chi tiết</a>
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
