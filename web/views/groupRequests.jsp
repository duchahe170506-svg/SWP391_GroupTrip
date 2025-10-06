<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Yêu cầu tham gia nhóm</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f5f7fb; margin:0 }
        .container { max-width:1000px; margin:24px auto; background:#fff; border:1px solid #e5e7ee; border-radius:12px; padding:16px }
        h2 { margin-top:0; }
        table { width:100%; border-collapse:collapse; margin-top:16px }
        th, td { border:1px solid #ddd; padding:10px; text-align:left }
        th { background:#f0f3f9 }
        .btn { padding:6px 12px; border-radius:6px; border:none; cursor:pointer; text-decoration:none; font-size:14px }
        .btn.approve { background:#16a34a; color:#fff }
        .btn.reject { background:#dc2626; color:#fff }
        .status { font-weight:bold; }
    </style>
</head>
<body>
    <div class="container">
        <h2>Danh sách yêu cầu tham gia nhóm</h2>

        <c:choose>
            <c:when test="${empty requests}">
                <p>Không có yêu cầu nào.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>Người gửi</th>
                            <th>Chuyến đi</th>
                            <th>Ngày gửi</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="r" items="${requests}">
                            <tr>
                                <td><c:out value="${r.userName}"/></td>
                                <td><c:out value="${r.tripName}"/></td>
                                <td><fmt:formatDate value="${r.requestedAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td class="status"><c:out value="${r.status}"/></td>
                                <td>
                                    <c:if test="${r.status == 'PENDING'}">
                                        <form action="group/requests" method="post" style="display:inline;">
                                            <input type="hidden" name="requestId" value="${r.requestId}"/>
                                            <input type="hidden" name="action" value="approve"/>
                                            <button class="btn approve" type="submit">Chấp nhận</button>
                                        </form>
                                        <form action="group/requests" method="post" style="display:inline;">
                                            <input type="hidden" name="requestId" value="${r.requestId}"/>
                                            <input type="hidden" name="action" value="reject"/>
                                            <button class="btn reject" type="submit">Từ chối</button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
