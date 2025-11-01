<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="partials/header.jsp" />
<html>
    <head>
        <title>Thông báo của bạn</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
        <style>
            body {
                font-family:'Inter', sans-serif;
                background:#f4f6f9;
                margin:0;
            }
            .notifications-page {
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
            .notifications-content {
                flex: 1;
                padding: 40px 60px;
            }

            .top-bar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 25px;
            }

            .top-bar h2 {
                color: #2f4f4f;
                font-size: 26px;
                font-weight: 700;
                display: flex;
                align-items: center;
                gap: 10px;
            }

            /* ---------------- NÚT ---------------- */
            .btn {
                padding: 8px 14px !important;
                border: none !important;
                border-radius: 6px !important;
                cursor: pointer !important;
                font-size: 14px !important;
                transition: 0.25s !important;
                font-weight: 500 !important;
                display: inline-block !important;
                text-decoration: none !important;
            }

            .btn-mark {
                background: #4da89b !important;
                color: #fff !important;
            }

            .btn-mark:hover {
                background: #3f9589 !important;
            }

            .btn-delete {
                background: #dc3545 !important;
                color: #fff !important;
            }

            .btn-delete:hover {
                background: #c82333 !important;
            }

            .btn-action {
                background: #009688 !important;
                color: #fff !important;
            }

            .btn-action:hover {
                background: #00796b !important;
            }

            .btn-neutral {
                background: #6c757d !important;
                color: #fff !important;
            }

            .btn-neutral:hover {
                background: #5c636a !important;
            }

            /* ---------------- THẺ THÔNG BÁO ---------------- */
            .noti-card {
                background: #fff;
                border-radius: 10px;
                padding: 10px 14px;
                margin-bottom: 10px;
                box-shadow: 0 1px 6px rgba(0,0,0,0.06);
                transition: 0.2s;
                display: flex;
                justify-content: space-between;
                align-items: flex-start;
            }

            .noti-card:hover {
                background: #f8fcff;
            }

            .noti-card.unread {
                border-left: 4px solid #4da89b;
                background: #f0fdfa;
            }

            .noti-content {
                flex: 1;
                overflow: hidden;
            }

            .noti-content p {
                margin: 2px 0;
                color: #444;
                font-size: 14px;
                line-height: 1.4;
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
                overflow: hidden;
                text-overflow: ellipsis;
            }

            .noti-content b {
                color: #2f4f4f;
                font-size: 14px;
            }

            .noti-meta {
                font-size: 12px;
                color: #999;
            }

            .actions {
                display: flex;
                flex-direction: row;
                gap: 6px;
                margin-left: 10px;
                margin-top: 6px;
            }

            .actions .btn {
                font-size: 12px !important;
                padding: 4px 8px !important;
                border-radius: 5px !important;
            }

            .text-muted {
                color: #888;
                font-size: 13px;
                margin-top: 5px;
            }

            /* POPUP */
            .popup-overlay {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.45);
                justify-content: center;
                align-items: center;
                z-index: 1000;
            }

            .popup {
                background: #fff;
                padding: 25px;
                border-radius: 12px;
                width: 460px;
                box-shadow: 0 8px 24px rgba(0,0,0,0.2);
                animation: fadeIn 0.3s ease;
            }

            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: scale(0.95);
                }
                to {
                    opacity: 1;
                    transform: scale(1);
                }
            }

            .popup h3 {
                margin-top: 0;
                color: #2f4f4f;
            }

            .close-btn {
                background: #aaa !important;
                color: white !important;
                border: none !important;
                padding: 6px 10px !important;
                cursor: pointer !important;
                float: right !important;
                border-radius: 6px !important;
            }

            textarea, input[type="text"], input[type="file"] {
                width: 100% !important;
                margin-top: 5px !important;
                margin-bottom: 12px !important;
                padding: 8px !important;
                border-radius: 6px !important;
                border: 1px solid #ccc !important;
                font-size: 14px !important;
                font-family: inherit !important;
            }

            label {
                font-weight: 500;
                color: #333;
            }

            /* TOAST */
            .toast {
                position: fixed !important;
                top: 20px !important;
                right: 20px !important;
                background: #4da89b !important;
                color: white !important;
                padding: 8px 14px !important;
                border-radius: 6px !important;
                box-shadow: 0 2px 8px rgba(0,0,0,0.15) !important;
                opacity: 0 !important;
                transform: translateY(-10px) !important;
                transition: opacity 0.3s ease, transform 0.3s ease !important;
                z-index: 9999 !important;
                width: fit-content !important;
            }

            .toast.show {
                opacity: 1 !important;
                transform: translateY(0) !important;
            }
        </style>
    </head>
    <body>
        <div class="notifications-page">
            <div class="sidebar">
                <ul class="menu-list">
                    <li><a href="profile" class="menu-item"><i class="fa fa-user"></i> Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/notifications" class="menu-item active"><i class="fa fa-bell"></i> Notifications</a></li>
                    <li><a href="${pageContext.request.contextPath}/report" class="menu-item"><i class="fa fa-pen"></i> My Report</a></li>
                    <li><a href="#" class="menu-item"><i class="fa fa-briefcase"></i> Trip History</a></li>
                    <li><a href="${pageContext.request.contextPath}/profile-memories" class="menu-item"><i class="fa fa-images"></i> Nhật ký</a></li>
                    <li><a href="update_password" class="menu-item"><i class="fa fa-fingerprint"></i> Security</a></li>
                </ul>
            </div>

            <div class="notifications-content">
                <div class="top-bar">
                    <h2>🔔 Thông báo của bạn</h2>

                    <a href="notifications?action=markAll" class="btn btn-mark">Đánh dấu tất cả là đã đọc</a>
                </div>

                <c:choose>
                    <c:when test="${not empty notifications}">
                        <c:forEach var="n" items="${notifications}">
                            <div class="noti-card ${n.status eq 'UNREAD' ? 'unread' : ''}">
                                <div>
                                    <b>${n.typeLabel}</b> - ${n.message}

                                    <c:if test="${n.type eq 'INVITE'}">
                                        <div>Người gửi lời mời: ${n.senderName}</div>
                                    </c:if>
                                    <c:if test="${n.type eq 'KICKED'}">
                                        <div>Người xóa bạn khỏi nhóm: ${n.senderName}</div>
                                    </c:if>

                                    <c:if test="${n.type eq 'GROUP_ANNOUNCEMENT'}">
                                        <div>Người gửi: ${n.senderName} - ${n.tripName}</div>
                                    </c:if>


                                    <div class="actions">
                                        <c:if test="${n.status eq 'UNREAD'}">
                                            <a href="notifications?action=markRead&id=${n.notificationId}" class="btn btn-mark">✓ Đã đọc</a>
                                        </c:if>
                                        <c:if test="${n.type eq 'KICKED'}">
                                            <button class="btn btn-action open-complaint"
                                                    data-removal="${n.relatedId}"           
                                                    data-id="${n.notificationId}" 
                                                    data-reported="${n.senderId}">           
                                                Gửi khiếu nại
                                            </button>
                                        </c:if>


                                        <c:if test="${n.type eq 'INVITE'}">
                                            <div class="invite-actions">
                                                <a href="${pageContext.request.contextPath}/trip/detail?id=${n.relatedId}" 
                                                   class="btn btn-info">Xem thông tin</a>

                                                <c:choose>
                                                    <c:when test="${n.requestStatus eq 'PENDING' || n.requestStatus eq 'INVITED'}">
                                                        <a href="notifications?action=acceptInvite&id=${n.relatedRequestId}" class="btn btn-mark">✅ Chấp nhận</a>
                                                        <a href="notifications?action=rejectInvite&id=${n.relatedRequestId}" class="btn btn-delete">❌ Từ chối</a>
                                                    </c:when>
                                                    <c:when test="${n.requestStatus eq 'ACCEPTED'}">
                                                        <span class="btn">Đã chấp nhận</span>
                                                    </c:when>
                                                    <c:when test="${n.requestStatus eq 'REJECTED'}">
                                                        <span class="btn">Đã từ chối</span>
                                                    </c:when>
                                                </c:choose>


                                            </div>
                                        </c:if>
                                        <c:if test="${n.status eq 'READ'}">
                                            <a href="notifications?action=delete&id=${n.notificationId}"
                                               class="btn btn-delete" 
                                               onclick="return confirm('Bạn có chắc muốn xóa thông báo này không? Hành động này không thể khôi phục.');">
                                                🗑 Xóa
                                            </a>
                                        </c:if>

                                    </div>
                                </div>
                                <div class="text-muted small">
                                    <fmt:formatDate value="${n.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p>Không có thông báo nào.</p>
                    </c:otherwise>
                </c:choose>

                <!-- POPUP GỬI KHIẾU NẠI -->
                <div id="complaintPopup" class="popup-overlay">
                    <div class="popup">
                        <button class="close-btn" onclick="closePopup('complaintPopup')">✖</button>
                        <h3>📩 Gửi khiếu nại</h3>
                        <form method="post" action="report" enctype="multipart/form-data">
                            <input type="hidden" name="action" value="create">
                            <input type="hidden" name="type" value="KICK_DISPUTE">

                            <!-- Ẩn các ID cần thiết -->
                            <input type="hidden" name="notificationId" id="notiIdInput">
                            <input type="hidden" name="removalId" id="removalIdInput">
                            <input type="hidden" name="groupId" id="groupIdInputHidden">
                            <input type="hidden" name="reportedUserId" id="reportedUserInput">

                            <label>Tiêu đề:</label>
                            <input type="text" name="title" placeholder="Nhập tiêu đề khiếu nại" required>

                            <label>Nội dung chi tiết:</label>
                            <textarea name="description" rows="5" placeholder="Mô tả chi tiết sự việc, lý do khiếu nại..." required></textarea>

                            <label>Minh chứng (ảnh hoặc tài liệu):</label>
                            <input type="file" name="attachments" multiple accept=".jpg,.jpeg,.png,.pdf" required>

                            <div style="margin-top:12px; text-align:right;">
                                <button type="submit" class="btn btn-action">📨 Gửi khiếu nại</button>
                                <button type="button" class="btn btn-neutral" onclick="closePopup('complaintPopup')">Hủy</button>
                            </div>
                        </form>
                    </div>
                </div>


            </div>
        </div>

        <div id="confirmDeletePopup" class="popup-overlay">
            <div class="popup">
                <h3>⚠️ Xác nhận xóa</h3>
                <p>Bạn có chắc muốn xóa thông báo này không?<br><b>Hành động này không thể khôi phục.</b></p>
                <div style="text-align:right; margin-top:15px;">
                    <button class="btn btn-neutral" onclick="closePopup('confirmDeletePopup')">Hủy</button>
                    <button class="btn btn-delete" id="confirmDeleteBtn">Xóa</button>
                </div>
            </div>
        </div>

        <c:if test="${param.success == 'deleted'}">
            <div id="toast" class="toast">Xóa thành công!</div>
            <script>
                const toast = document.getElementById("toast");
                toast.classList.add("show");
                setTimeout(() => {
                toast.classList.remove("show");
                }, 2000);
            </script>
        </c:if>            
        <c:choose>
            <c:when test="${fn:contains(toastMessage, '❌')}">
                <div id="toast" class="toast" style="background:#dc3545;">${toastMessage}</div>
            </c:when>
            <c:otherwise>
                <div id="toast" class="toast" style="background:#4da89b;">${toastMessage}</div>
            </c:otherwise>
        </c:choose>

        <c:if test="${not empty toastMessage}">
            <div id="toast" class="toast">${toastMessage}</div>
            <script>
                const toast = document.getElementById("toast");
                toast.classList.add("show");
                setTimeout(() => {
                toast.classList.remove("show");
                }, 2000);
            </script>
        </c:if>    

        <c:if test="${param.msg == 'report_submitted'}">
            <div id="toast" class="toast success">📢 Báo cáo đã được gửi thành công!</div>
            <script>
                const toast = document.getElementById("toast");
                toast.classList.add("show");
                setTimeout(() => toast.classList.remove("show"), 3000);
            </script>
            <style>
                .toast {
                    position: fixed;
                    top: 20px;
                    right: 20px;
                    background: #4CAF50;
                    color: white;
                    padding: 12px 20px;
                    border-radius: 8px;
                    box-shadow: 0 2px 8px rgba(0,0,0,0.2);
                    opacity: 0;
                    transition: opacity 0.3s ease;
                    z-index: 9999;
                }
                .toast.show {
                    opacity: 1;
                }
            </style>
        </c:if>


        <script>
            document.addEventListener("DOMContentLoaded", () => {

            document.querySelectorAll(".open-complaint").forEach(btn => {
            btn.addEventListener("click", () => {
            document.getElementById("removalIdInput").value = btn.dataset.removal || '';
            document.getElementById("notiIdInput").value = btn.dataset.id || '';
            document.getElementById("reportedUserInput").value = btn.dataset.reported || '';
            document.getElementById("complaintPopup").style.display = "flex";
            });
            });
            let deleteUrl = "";
            document.querySelectorAll(".open-delete").forEach(btn => {
            btn.addEventListener("click", e => {
            e.preventDefault();
            deleteUrl = btn.dataset.deleteUrl;
            document.getElementById("confirmDeletePopup").style.display = "flex";
            });
            });
            const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");
            if (confirmDeleteBtn) {
            confirmDeleteBtn.addEventListener("click", () => {
            if (deleteUrl)
                    window.location.href = deleteUrl;
            });
            }


            const toast = document.getElementById("toast");
            if (toast) {
            toast.classList.add("show");
            setTimeout(() => toast.classList.remove("show"), 2000);
            }
            });
            function closePopup(id) {
            const popup = document.getElementById(id);
            if (popup)
                    popup.style.display = "none";
            }
        </script>
        <jsp:include page="partials/footer.jsp" />
    </body>
</html>
