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
            body{
                font-family:Arial,sans-serif;
                background:#f5f7fb;
                margin:0
            }
            .wrap{
                max-width:1000px;
                margin:24px auto;
                padding:0 16px
            }
            .card{
                background:#fff;
                border:1px solid #e5e7ee;
                border-radius:12px;
                padding:20px;
                margin-bottom:16px
            }
            .trip-list{
                display:flex;
                flex-direction:column;
                gap:14px
            }
            .btn{
                padding:8px 12px;
                border-radius:8px;
                border:1px solid #ccc;
                text-decoration:none;
                color:#333;
                background:#eee
            }
            .btn.primary{
                background:#3b82f6;
                color:#fff;
                border:none
            }
            .btn.danger{
                background:#dc2626;
                color:#fff;
                border:none
            }
            h2{
                margin-top:32px;
                border-bottom:2px solid #ddd;
                padding-bottom:6px
            }
        </style>
    </head>
    <body>
        <div class="wrap">
            <c:if test="${param.deleted == '1'}">
                <div style="background:#d1fae5;color:#065f46;padding:10px;border-radius:8px;margin-bottom:16px;">
                    ✅ Xóa chuyến đi thành công.
                </div>
            </c:if>

            <c:if test="${param.cantdelete == '1'}">
                <div style="background:#fff3cd;color:#856404;padding:10px;border-radius:8px;margin-bottom:16px;">
                    ⚠️ Không thể xóa chuyến đi vì đã có người tham gia. 
                    Hãy đổi trạng thái chuyến đi sang <strong>Blocked</strong> nếu muốn ngừng hoạt động.
                </div>
            </c:if>

            <c:if test="${param.error == '1'}">
                <div style="background:#fee2e2;color:#991b1b;padding:10px;border-radius:8px;margin-bottom:16px;">
                    ❌ Xảy ra lỗi khi xóa chuyến đi.
                </div>
            </c:if>

            <h1>Chuyến đi của tôi</h1>

            <!-- Leader section -->
            <c:if test="${not empty leaderTrips}">
                <h2>Chuyến đi tôi tạo</h2>
                <div class="trip-list">
                    <c:forEach var="t" items="${leaderTrips}">
                        <div class="card">
                            <strong><c:out value="${t.name}"/></strong> — <c:out value="${t.location}"/>
                            <div>Thời gian: <fmt:formatDate value="${t.startDate}" pattern="dd/MM/yyyy"/> → <fmt:formatDate value="${t.endDate}" pattern="dd/MM/yyyy"/></div>
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
                            <div>Thời gian: <fmt:formatDate value="${t.startDate}" pattern="dd/MM/yyyy"/> → <fmt:formatDate value="${t.endDate}" pattern="dd/MM/yyyy"/></div>
                            <a class="btn primary" href="<c:url value='/trip/detail?id=${t.tripId}'/>">Xem chi tiết</a>
                        </div>
                    </c:forEach>
                </div>
            </c:if>

            <c:if test="${empty joinedTrips and empty leaderTrips}">
                <p>Hiện tại bạn chưa tham gia hoặc tạo chuyến đi nào.</p>
            </c:if>
        </div>
    </body>
    <jsp:include page="partials/footer.jsp"/>
</html>
