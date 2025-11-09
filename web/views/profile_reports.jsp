<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="partials/header.jsp" />

<html>
    <head>
        <title>Báo cáo của tôi</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
        <style>
            body {
                font-family:'Inter', sans-serif;
                background:#f4f6f9;
                margin:0;
            }
            .reports-page {
                display:flex;
                min-height:calc(100vh - 100px);
                background:#f8f9fa;
            }
            .sidebar {
                width:260px;
                background:#fff;
                border-right:1px solid #e0e0e0;
                padding:25px 0;
                box-shadow:2px 0 8px rgba(0,0,0,0.05);
            }
            .menu-list {
                list-style:none;
                padding:0;
                margin:0;
            }
            .menu-item {
                display:flex;
                align-items:center;
                color:#333;
                font-weight:500;
                padding:14px 25px;
                text-decoration:none;
                transition:0.25s;
            }
            .menu-item i {
                font-size:18px;
                margin-right:12px;
                width:22px;
                text-align:center;
            }
            .menu-item:hover {
                background:#e9f3ff;
                color:#007bff;
            }
            .menu-item.active {
                background:#e0efff;
                border-left:4px solid #007bff;
                color:#007bff;
            }

            .reports-content {
                flex:1;
                padding:40px 60px;
            }

            .top-bar {
                display:flex;
                justify-content:space-between;
                align-items:center;
                margin-bottom:25px;
            }

            .top-bar h2 {
                color:#2f4f4f;
                font-size:26px;
                font-weight:700;
                display:flex;
                align-items:center;
                gap:10px;
            }

            /* CARD */
            .report-card {
                background:#fff;
                border-radius:10px;
                padding:16px 18px;
                margin-bottom:12px;
                box-shadow:0 1px 6px rgba(0,0,0,0.06);
                transition:0.2s;
                border-left:4px solid transparent;
            }
            .report-card:hover {
                background:#f8fcff;
            }
            .report-card.pending {
                border-left-color:#ffc107;
            }
            .report-card.reviewed {
                border-left-color:#4da89b;
            }
            .report-card.rejected {
                border-left-color:#dc3545;
            }

            .report-title {
                font-weight:600;
                color:#333;
                font-size:16px;
                margin-bottom:6px;
            }
            .report-desc {
                color:#555;
                font-size:14px;
                line-height:1.5;
                margin-bottom:8px;
            }
            .report-meta {
                color:#666;
                font-size:13px;
                line-height:1.5;
            }
            .status-label {
                padding:2px 8px;
                border-radius:5px;
                font-size:12px;
                font-weight:600;
                color:#fff;
            }
            .status-pending {
                background:#ffc107;
            }
            .status-reviewed {
                background:#4da89b;
            }
            .status-rejected {
                background:#dc3545;
            }

            /* TOAST */
            .toast {
                position: fixed;
                top: 20px;
                right: 20px;
                background: #4da89b;
                color: white;
                padding: 8px 14px;
                border-radius: 6px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.15);
                opacity: 0;
                transform: translateY(-10px);
                transition: opacity 0.3s ease, transform 0.3s ease;
                z-index: 9999;
            }
            .toast.show {
                opacity: 1;
                transform: translateY(0);
            }

            .report-layout {
                display: flex;
                justify-content: space-between;
                gap: 20px;
            }

            .report-left {
                flex: 2;
            }

            .report-right {
                flex: 1;
                display: flex;
                flex-wrap: wrap;
                justify-content: flex-end;
                align-items: flex-start;
                gap: 8px;
            }

            .report-img {
                max-width: 120px;
                max-height: 80px;
                border-radius: 8px;
                margin: 5px;
                border: 1px solid #ddd;
            }

            .pdf-link {
                display: block;
                background: #eef3ff;
                padding: 5px 8px;
                border-radius: 6px;
                font-size: 13px;
                color: #007bff;
                text-decoration: none;
            }

            .pdf-link:hover {
                background: #d9e6ff;
            }

            .report-actions {
                margin-top: 10px;
            }

        </style>
    </head>

    <body>
        <div class="reports-page">
            <!-- SIDEBAR -->
            <div class="sidebar">
                <ul class="menu-list">
                    <li><a href="profile" class="menu-item"><i class="fa fa-user"></i> Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/notifications" class="menu-item"><i class="fa fa-bell"></i> Notifications</a></li>
                    <li><a href="${pageContext.request.contextPath}/report" class="menu-item active"><i class="fa fa-pen"></i> My Report</a></li>
                    <li><a href="#" class="menu-item"><i class="fa fa-briefcase"></i> Trip History</a></li>
                    <li><a href="${pageContext.request.contextPath}/profile-memories" class="menu-item"><i class="fa fa-images"></i> Nhật ký</a></li>
                    <li><a href="update_password" class="menu-item"><i class="fa fa-fingerprint"></i> Security</a></li>
                </ul>
            </div>

            <!-- CONTENT -->
            <div class="reports-content">
                <div class="top-bar">
                    <h2>📝 Báo cáo của tôi</h2>
                </div>

                <c:choose>
                    <c:when test="${not empty reports}">
                        <c:forEach var="r" items="${reports}">
                            <div class="report-card ${r.status eq 'PENDING' ? 'pending' : (r.status eq 'REVIEWED' ? 'reviewed' : 'rejected')}">
                                <div class="report-layout">
                                    <!-- BÊN TRÁI: NỘI DUNG -->
                                    <div class="report-left">
                                        <div class="report-title">
                                            Tiêu đề: ${r.title} - Mục đích:
                                            <c:choose>
                                                <c:when test="${r.type eq 'LEADER_MISCONDUCT'}">Khiếu nại Leader lạm quyền</c:when>
                                                <c:when test="${r.type eq 'KICK_DISPUTE'}">Khiếu nại việc bị kick</c:when>
                                                <c:when test="${r.type eq 'MEMBER_CONFLICT'}">Mâu thuẫn giữa thành viên</c:when>
                                                <c:when test="${r.type eq 'GROUP_ACTIVITY'}">Báo cáo hoạt động nhóm</c:when>
                                                <c:when test="${r.type eq 'SCAM'}">Tố cáo lừa đảo</c:when>
                                                <c:otherwise>Khác</c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div class="report-meta">
                                            👤 <b>Người bị tố cáo:</b> ${empty r.reported_user_name ? 'Không xác định' : r.reported_user_name} - 
                                            👥 <b>Nhóm liên quan:</b> ${empty r.group_name ? 'Không có' : r.group_name}<br>
                                        </div>

                                        <div class="report-desc">
                                            <b>Lý do:</b> ${r.description}
                                        </div>

                                        <div class="report-meta">
                                            <fmt:formatDate value="${r.created_at}" pattern="dd/MM/yyyy HH:mm"/> |
                                            🟢 Trạng thái:
                                            <span class="status-label
                                                  ${r.status eq 'PENDING' ? 'status-pending' : 
                                                    (r.status eq 'REVIEWED' ? 'status-reviewed' : 'status-rejected')}">
                                                      ${r.status}
                                                  </span>
                                            </div>

                                            <c:if test="${not empty r.admin_response}">
                                                <div style="margin-top:10px; font-size:14px; color:#444;">
                                                    💬 <b>Phản hồi từ quản trị:</b> ${r.admin_response}
                                                </div>
                                            </c:if>

                                            <!-- NÚT HÀNH ĐỘNG -->
                                            <div style="text-align:right; margin-top:8px;">
                                                <c:if test="${r.status eq 'PENDING' || r.status eq 'IN_PROGRESS'}">
                                                    <button class="btn-edit"
                                                            data-id="${r.report_id}" 
                                                            data-title="${r.title}" 
                                                            data-desc="${r.description}" 
                                                            data-type="${r.type}"
                                                            style="
                                                            background: #007bff !important;
                                                            color: white !important;
                                                            border: none !important;
                                                            border-radius: 5px !important;
                                                            padding: 6px 12px !important;
                                                            font-size: 13px !important;
                                                            cursor: pointer !important;
                                                            transition: 0.2s !important;
                                                            margin-right: 6px !important;
                                                            "
                                                            onmouseover="this.style.background = '#0056b3'"
                                                            onmouseout="this.style.background = '#007bff'">
                                                        ✏️ Sửa
                                                    </button>

                                                    <form action="report" method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="delete">
                                                        <input type="hidden" name="reportId" value="${r.report_id}">
                                                        <button type="submit"
                                                                onclick="return confirm('Xóa báo cáo này?')"
                                                                style="
                                                                background: #dc3545 !important;
                                                                color: white !important;
                                                                border: none !important;
                                                                border-radius: 5px !important;
                                                                padding: 6px 12px !important;
                                                                font-size: 13px !important;
                                                                cursor: pointer !important;
                                                                transition: 0.2s !important;
                                                                "
                                                                onmouseover="this.style.background = '#b02a37'"
                                                                onmouseout="this.style.background = '#dc3545'">
                                                            🗑️ Xóa
                                                        </button>
                                                    </form>
                                                </c:if>

                                            </div>

                                        </div>

                                        <!-- BÊN PHẢI: ẢNH MINH CHỨNG -->
                                        <div class="report-right">
                                            <c:forEach var="a" items="${r.attachments}">
                                                <c:choose>
                                                    <c:when test="${fn:endsWith(a, '.pdf')}">
                                                        <a href="${pageContext.request.contextPath}/${a}" target="_blank" class="pdf-link">
                                                            📄 Xem PDF
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="${pageContext.request.contextPath}/${a}" 
                                                             alt="Ảnh minh chứng" class="report-img">
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>

                        </c:when>
                        <c:otherwise>
                            <p>Hiện bạn chưa gửi báo cáo nào.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- MODAL CHỈNH SỬA BÁO CÁO -->
            <div id="editModal" class="modal" 
                 style="display:none; position:fixed; inset:0;
                 background:rgba(0,0,0,0.4); z-index:9999;
                 justify-content:center; align-items:center;">

                <div class="modal-content" 
                     style="background:#fff; padding:25px 30px;
                     border-radius:12px; width:450px; max-width:90%;
                     box-shadow:0 8px 25px rgba(0,0,0,0.2);
                     animation: fadeIn 0.25s ease-out;">

                    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:15px;">
                        <h3 style="margin:0; color:#007bff; font-size:20px; font-weight:700;">
                            ✏️ Sửa báo cáo
                        </h3>
                        <button type="button" onclick="closeModal()" 
                                style="background:none; border:none; font-size:20px; color:#888; cursor:pointer;">✖</button>
                    </div>

                    <form id="editForm" method="post" action="report" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="edit">
                        <input type="hidden" name="reportId" id="editReportId">

                        <div style="margin-bottom:12px;">
                            <label style="font-weight:600;">Tiêu đề:</label>
                            <input type="text" name="title" id="editTitle" 
                                   style="width:100%; padding:8px 10px; border:1px solid #ccc;
                                   border-radius:6px; font-size:14px; margin-top:4px;" required>
                        </div>

                        <div style="margin-bottom:12px;">
                            <label style="font-weight:600;">Mô tả:</label>
                            <textarea name="description" id="editDesc" rows="4"
                                      style="width:100%; padding:8px 10px; border:1px solid #ccc;
                                      border-radius:6px; font-size:14px; margin-top:4px;" required></textarea>
                        </div>

                        <div style="margin-bottom:12px;">
                            <label style="font-weight:600;">Loại báo cáo:</label>
                            <select name="type" id="editType"
                                    style="width:100%; padding:8px 10px; border:1px solid #ccc;
                                    border-radius:6px; font-size:14px; margin-top:4px;">
                                <option value="LEADER_MISCONDUCT">Khiếu nại Leader</option>
                                <option value="KICK_DISPUTE">Khiếu nại bị kick</option>
                                <option value="MEMBER_CONFLICT">Mâu thuẫn thành viên</option>
                                <option value="GROUP_ACTIVITY">Hoạt động nhóm</option>
                                <option value="SCAM">Lừa đảo</option>
                                <option value="OTHER">Khác</option>
                            </select>
                        </div>

                        <div class="delete-old" style="background:#f8f9fa; padding:10px; border-radius:8px; margin-bottom:12px;">
                            <label style="display:flex;align-items:center;gap:8px;cursor:pointer;font-size:14px;">
                                <input type="checkbox" name="deleteOldAttachments" value="true">
                                ❌ Xóa toàn bộ ảnh minh chứng cũ
                            </label>
                        </div>

                        <div class="new-attachments" style="margin-bottom:16px;">
                            <label style="font-weight:600;">📤 Thêm minh chứng mới:</label>
                            <input type="file" name="attachments" multiple 
                                   accept=".jpg,.jpeg,.png,.pdf" 
                                   style="display:block;margin-top:5px;font-size:13px;">
                            <p style="font-size:12px;color:#888;margin-top:4px;">Hỗ trợ JPG, PNG, PDF</p>
                        </div>

                        <div style="display:flex; justify-content:flex-end; gap:10px; margin-top:20px;">
                            <button type="button" onclick="closeModal()" 
                                    style="background:#ccc; color:#333; border:none;
                                    padding:8px 14px; border-radius:6px; cursor:pointer;
                                    font-weight:600;">Hủy</button>
                            <button type="submit" 
                                    style="background:#007bff; color:white; border:none;
                                    padding:8px 14px; border-radius:6px; cursor:pointer;
                                    font-weight:600;">💾 Lưu thay đổi</button>
                        </div>
                    </form>
                </div>
            </div>

            <style>
                @keyframes fadeIn {
                    from {
                        opacity: 0;
                        transform: translateY(-10px);
                    }
                    to {
                        opacity: 1;
                        transform: translateY(0);
                    }
                }
            </style>

            <script>
                document.querySelectorAll('.btn-edit').forEach(btn => {
                    btn.onclick = () => {
                        document.getElementById('editReportId').value = btn.dataset.id;
                        document.getElementById('editTitle').value = btn.dataset.title;
                        document.getElementById('editDesc').value = btn.dataset.desc;
                        document.getElementById('editType').value = btn.dataset.type;
                        document.getElementById('editModal').style.display = 'flex';
                    };
                });

                function closeModal() {
                    document.getElementById('editModal').style.display = 'none';
                }
            </script>






            <c:if test="${param.msg == 'report_submitted'}">
                <div id="toast" class="toast success">📢 Báo cáo đã được gửi thành công!</div>
                <script>
                    const toast = document.getElementById("toast");
                    toast.classList.add("show");
                    setTimeout(() => toast.classList.remove("show"), 3000);
                </script>
            </c:if>

            <jsp:include page="partials/footer.jsp" />
        </body>
    </html>
