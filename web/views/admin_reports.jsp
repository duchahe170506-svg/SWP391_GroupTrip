<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="partials/header.jsp" />

<html>
    <head>
        <title>Quản lý Báo cáo</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
        <style>
            body {
                font-family:'Inter',sans-serif;
                background:#f4f6f9;
                margin:0;
                padding:20px;
            }
            table {
                width:100%;
                border-collapse:collapse;
                background:#fff;
                border-radius:10px;
                overflow:hidden;
            }
            th, td {
                padding:10px 12px;
                border-bottom:1px solid #ddd;
                text-align:left;
                font-size:14px;
                vertical-align: top;
            }
            th {
                background:#007bff;
                color:#fff;
            }
            tr:hover {
                background:#f1f9ff;
            }
            form.inline-form {
                display:flex;
                flex-direction:column;
                gap:6px;
            }
            textarea {
                width:100%;
                padding:8px;
                resize:none;
            }
            select, button {
                padding:6px 10px;
            }
            button {
                cursor:pointer;
                background:#007bff;
                color:#fff;
                border:none;
                border-radius:5px;
            }
            button:hover {
                background:#0056b3;
            }
            .small {
                font-size:13px;
                color:#666;
            }
        </style>
    </head>
    <body>
        <h2>📋 Quản lý Báo cáo Người Dùng</h2>

    <c:choose>
        <c:when test="${not empty reports}">
            <table>
                <tr>
                    <th>#</th>
                    <th>Tiêu đề</th>
                    <th>Người gửi</th>
                    <th>Người bị tố cáo</th>
                    <th>Nhóm</th>
                    <th>Loại</th>
                    <th>Bằng chứng</th>
                    <th>Ngày gửi</th>
                    <th>Trạng thái</th>
                    <th>Phản hồi</th>
                </tr>

                <c:forEach var="r" items="${reports}">
                    <tr>
                        <td>${r.report_id}</td>
                        <td><b>${r.title}</b></td>
                        <td class="small">${empty r.reporter_name ? '-' : r.reporter_name}</td>
                        <td class="small">${empty r.reported_user_name ? '-' : r.reported_user_name}</td>
                        <td class="small">${empty r.group_name ? '-' : r.group_name}</td>
                        <td class="small">
                    <c:choose>
                        <c:when test="${r.type == 'LEADER_MISCONDUCT'}">Khiếu nại Leader</c:when>
                        <c:when test="${r.type == 'KICK_DISPUTE'}">Khiếu nại bị kick</c:when>
                        <c:when test="${r.type == 'MEMBER_CONFLICT'}">Mâu thuẫn thành viên</c:when>
                        <c:when test="${r.type == 'GROUP_ACTIVITY'}">Báo cáo hoạt động</c:when>
                        <c:when test="${r.type == 'SCAM'}">Tố cáo lừa đảo</c:when>
                        <c:otherwise>Khác</c:otherwise>
                    </c:choose>
                    </td>
                    <td>
                    <c:forEach var="a" items="${r.attachments}">
                        <c:choose>
                            <c:when test="${fn:endsWith(a, '.pdf')}">
                                <a href="${pageContext.request.contextPath}/${a}" target="_blank">📄 Xem tài liệu PDF</a>
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/${a}" alt="Ảnh minh chứng" style="max-width:150px; border-radius:8px; margin:5px;">
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    </td>

                    <td class="small"><fmt:formatDate value="${r.created_at}" pattern="dd/MM/yyyy HH:mm"/></td>
                    <td class="small">${r.status}</td>
                    <td style="width:320px;">
                        <!-- sửa action đúng đường dẫn servlet admin -->
                        <form class="inline-form" method="post" action="${pageContext.request.contextPath}/admin/reports">
                            <input type="hidden" name="report_id" value="${r.report_id}">
                            <textarea name="admin_response" rows="2" placeholder="Nhập phản hồi...">${fn:escapeXml(r.admin_response)}</textarea>

                            <select name="status" required>
                                <option value="PENDING" ${r.status == 'PENDING' ? 'selected' : ''}>Chờ xử lý</option>
                                <option value="RESOLVED" ${r.status == 'RESOLVED' ? 'selected' : ''}>Duyệt</option>
                                <option value="REJECTED" ${r.status == 'REJECTED' ? 'selected' : ''}>Từ chối</option>
                            </select>

                            <button type="submit">Cập nhật</button>
                        </form>
                    </td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>

        <c:otherwise>
            <div style="background:#fff;padding:20px;border-radius:8px;box-shadow:0 1px 6px rgba(0,0,0,0.06);">
                Hiện chưa có báo cáo nào.
            </div>
        </c:otherwise>
    </c:choose>

    <c:if test="${param.msg == 'updated_success'}">
        <script>
            alert('✅ Cập nhật phản hồi thành công!');
            // optional: redirect để refresh danh sách
            // location.href = '${pageContext.request.contextPath}/admin/reports';
        </script>
    </c:if>
    <c:if test="${param.msg == 'updated_failed'}">
        <script>alert('❌ Lỗi khi cập nhật phản hồi.');</script>
    </c:if>

    <jsp:include page="partials/footer.jsp" />
</body>
</html>
