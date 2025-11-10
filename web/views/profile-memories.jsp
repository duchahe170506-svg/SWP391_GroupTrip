<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="partials/header.jsp" />
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Nhật ký của bạn</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
        <style>
            body {
                font-family:'Inter', sans-serif;
                background:#f4f6f9;
                margin:0;
            }
            .profile-page {
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
            .profile-content {
                flex:1;
                padding:40px 60px;
            }
            .profile-card {
                background:#fff;
                border-radius:10px;
                padding:30px 40px;
                box-shadow:0 3px 10px rgba(0,0,0,0.1);
                max-width:800px;
                margin:0 auto;
            }
            .memory-card {
                border:1px solid #eee;
                border-radius:10px;
                padding:18px;
                margin-bottom:20px;
                background:#fafafa;
                transition:0.2s;
                position:relative;
            }
            .memory-card:hover {
                box-shadow:0 2px 10px rgba(0,0,0,0.05);
            }
            .memory-card b {
                font-size:18px;
                color:#2c3e50;
            }
            .memory-img {
                max-width:100%;
                max-height:300px;
                border-radius:8px;
                margin:10px 0;
                display:block;
            }
            .memory-actions {
                margin:8px 0;
            }
            .memory-actions button {
                padding:4px 10px;
                border-radius:6px;
                border:none;
                background:#3498db;
                color:white;
                cursor:pointer;
                margin-right:5px;
            }
            .reactions {
                margin:12px 0;
                display:flex;
                flex-wrap:wrap;
                gap:10px;
            }
            .reaction {
                cursor:pointer;
                font-size:14px;
                padding:4px 8px;
                border-radius:20px;
                background:#eee;
                transition:0.2s;
            }
            .reaction.active {
                background:#ffe066;
                font-weight:bold;
            }
            .comments {
                margin-top:12px;
            }
            .comment {
                padding:6px 0;
                border-bottom:1px dashed #ddd;
                font-size:14px;
                display:flex;
                justify-content:space-between;
                align-items:center;
            }
            .comment b {
                color:#2980b9;
            }
            .comment-actions button {
                background:none;
                border:none;
                color:#888;
                cursor:pointer;
                margin-left:5px;
            }
            .comment-actions button:hover {
                color:#000;
            }
            .comment-form {
                display:flex;
                gap:8px;
                margin-top:12px;
            }
            .comment-form input {
                flex:1;
                padding:8px;
                border:1px solid #ddd;
                border-radius:6px;
            }
            .comment-form button {
                background:#77a096;
                color:white;
                border:none;
                padding:8px 14px;
                border-radius:6px;
                cursor:pointer;
            }
            .modal {
                display:none;
                position:fixed;
                top:0;
                left:0;
                width:100%;
                height:100%;
                background:rgba(0,0,0,0.5);
                z-index:9999;
                justify-content:center;
                align-items:center;
            }
            .modal .modal-content {
                background:#fff;
                border-radius:10px;
                padding:20px;
                max-width:500px;
                width:90%;
                position:relative;
            }
            #successMsg {
                display:none;
                position:fixed;
                top:20px;
                right:20px;
                background:#4caf50;
                color:white;
                padding:10px 15px;
                border-radius:6px;
                z-index:10000;
                opacity:0;
                transition:opacity 0.3s;
            }
            #successMsg.show {
                display:block;
                opacity:1;
            }
        </style>
    </head>
    <body>
        <div class="profile-page">
            <div class="sidebar">
                <ul class="menu-list">
                    <li><a href="profile" class="menu-item"><i class="fa fa-user"></i> Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/notifications" class="menu-item"><i class="fa fa-bell"></i> Notifications</a></li>
                    <li><a href="${pageContext.request.contextPath}/report" class="menu-item"><i class="fa fa-pen"></i> My Report</a></li>
                    <li><a href="#" class="menu-item"><i class="fa fa-briefcase"></i> Trip History</a></li>
                    <li><a href="${pageContext.request.contextPath}/profile-memories" class="menu-item active"><i class="fa fa-images"></i> Nhật ký</a></li>
                    <li><a href="update_password" class="menu-item"><i class="fa fa-fingerprint"></i> Security</a></li>
                </ul>
            </div>

            <div class="profile-content">
                <div class="profile-card">
                    <h2>Nhật ký của bạn</h2>
                    <div style="text-align:right; margin-bottom:12px;">
                        <button id="btn-new-memory">Đăng kỷ niệm mới</button>
                    </div>

                    <c:forEach var="s" items="${shares}">
                        <div class="memory-card" data-share-id="${s.shareId}">
                            <c:if test="${not empty s.tripName}">
                                <p style="font-style: italic; color: #555;">
                                    🏞️ Được chia sẻ từ chuyến đi: <b>${s.tripName}</b>
                                </p>
                            </c:if>
                            <b>${s.title}</b>
                            <p>${s.content}</p>
                            <c:if test="${not empty s.imageUrl}">
                                <img src="${pageContext.request.contextPath}/${s.imageUrl}" style="width: 200px; height: auto; border-radius: 8px; object-fit: cover;"/>
                            </c:if>

                            <div class="memory-actions">
                                <c:if test="${s.sharedBy == sessionScope.currentUser.user_id}">
                                    <c:if test="${empty s.tripName}">
                                    <button class="btn-edit-post">Chỉnh sửa</button>
                                    </c:if>
                                    <button class="btn-delete-post" style="background:#e74c3c;">Xóa</button>
                                </c:if>
                            </div>

                            <div class="reactions">
                                <c:forEach var="type" items="${['Like','Love','Haha','Wow','Sad','Angry']}">
                                    <span class="reaction ${s.userReaction==type?'active':''}" data-type="${type}">
                                        ${type} (${s.reactionsCountMap[type] != null ? s.reactionsCountMap[type] : 0})
                                    </span>
                                </c:forEach>
                                <span style="margin-left:8px; color:#7f8c8d; font-size:13px;">Tổng: ${s.reactionsCount}</span>
                            </div>

                            <div class="comments">
                                <c:forEach var="cmt" items="${s.comments}">
                                    <div class="comment" data-comment-id="${cmt.commentId}">
                                        <div><b>${cmt.userName}:</b> ${cmt.content}</div>
                                        <c:if test="${cmt.userId == sessionScope.currentUser.user_id}">
                                            <div class="comment-actions">
                                                <button class="btn-edit-comment"><i class="fa fa-pen"></i></button>
                                                <button class="btn-delete-comment"><i class="fa fa-trash"></i></button>
                                            </div>
                                        </c:if>
                                    </div>
                                </c:forEach>
                            </div>

                            <div class="comment-form">
                                <input type="text" placeholder="Viết bình luận..."/>
                                <button class="btn-comment">Gửi</button>
                            </div>
                            <p style="font-size: 13px; color: gray; margin-top: 0;">
                                🕒 Đăng vào: 
                            <fmt:formatDate value="${s.sharedAt}" pattern="dd/MM/yyyy HH:mm"/>
                            </p>
                        </div>
                    </c:forEach>

                    <c:if test="${empty shares}">
                        <p style="text-align:center; color:#777; font-style:italic;">Bạn chưa chia sẻ kỷ niệm nào.</p>
                    </c:if>
                </div>
            </div>
        </div>

        <!-- Modal Đăng & Sửa bài -->
        <div id="memoryModal" class="modal">
            <div class="modal-content">
                <h3 id="modalTitle">Đăng kỷ niệm</h3>
                <input type="hidden" id="editShareId">

                <input type="text" id="memoryTitle" placeholder="Tiêu đề" style="width:100%; padding:8px; margin:8px 0;">
                <textarea id="memoryContent" placeholder="Nội dung" style="width:100%; padding:8px; margin:8px 0;" rows="2"></textarea>

                <!-- 👇 Chỗ này đổi từ text -> file -->
                <input type="file" id="memoryImage" accept="image/*" style="width:100%; padding:8px; margin:8px 0;">

                <!-- Xem trước ảnh -->
                <img id="previewImage" style="max-width:100%; border-radius:8px; margin:8px 0; display:none;"/>
                <input type="hidden" id="oldImageUrl" name="oldImageUrl">

                <div class="privacy-choice">
                    <label>
                        <input type="radio" name="privacy" value="Public" checked>
                        <span>Public</span>
                    </label>
                    <label>
                        <input type="radio" name="privacy" value="Private">
                        <span>Private</span>
                    </label>
                </div>
                <div style="margin-top:12px; text-align:right;">
                    <button id="cancelMemory">Hủy</button>
                    <button id="submitMemory">Lưu</button>
                </div>
            </div>
        </div>

        <!-- Modal sửa bình luận -->
        <div id="editCommentModal" class="modal">
            <div class="modal-content">
                <h3>Sửa bình luận</h3>
                <input type="hidden" id="editCommentId">
                <textarea id="editCommentContent" style="width:100%; padding:8px; margin:8px 0;" rows="4"></textarea>
                <div style="text-align:right;">
                    <button id="cancelEditComment">Hủy</button>
                    <button id="saveEditComment">Lưu</button>
                </div>
            </div>
        </div>

        <div id="successMsg">Thành công!</div>

        <script>
            const ctx = '${pageContext.request.contextPath}';
            const modal = document.getElementById('memoryModal');
            const editCommentModal = document.getElementById('editCommentModal');

// Hiện modal đăng mới
            document.getElementById('btn-new-memory').onclick = () => {
                document.getElementById('modalTitle').innerText = 'Đăng kỷ niệm mới';
                document.getElementById('editShareId').value = '';
                modal.style.display = 'flex';
            };
            document.getElementById('cancelMemory').onclick = () => modal.style.display = 'none';

// --- FLASH MESSAGE ---
            function showSuccessAndReload(msg) {
                localStorage.setItem('successMsg', msg);
                location.reload();
            }
            window.addEventListener('load', () => {
                const msg = localStorage.getItem('successMsg');
                if (msg) {
                    const el = document.getElementById('successMsg');
                    el.innerText = msg;
                    el.classList.add('show');
                    localStorage.removeItem('successMsg');
                    setTimeout(() => el.classList.remove('show'), 2000);
                }
            });

// Xem trước ảnh khi người dùng chọn file
            document.getElementById('memoryImage').addEventListener('change', function () {
                const file = this.files[0];
                const preview = document.getElementById('previewImage');
                if (file) {
                    const reader = new FileReader();
                    reader.onload = e => {
                        preview.src = e.target.result;
                        preview.style.display = 'block';
                    };
                    reader.readAsDataURL(file);
                } else {
                    preview.src = '';
                    preview.style.display = 'none';
                }
            });

            document.getElementById('submitMemory').onclick = () => {
                const formData = new FormData();
                const fileInput = document.getElementById('memoryImage');
                const title = document.getElementById('memoryTitle').value.trim();
                const content = document.getElementById('memoryContent').value.trim();
                const privacy = document.querySelector('input[name="privacy"]:checked')?.value || 'Public';
                const editShareId = document.getElementById('editShareId').value;
                const oldImageUrl = document.getElementById('oldImageUrl')?.value || ''; // thêm dòng này nếu bạn muốn giữ ảnh cũ

                // Nếu có ID thì là sửa, không thì là tạo mới
                formData.append('action', editShareId ? 'editShare' : 'submit');
                formData.append('title', title);
                formData.append('content', content);
                formData.append('privacy', privacy);

                if (editShareId)
                    formData.append('shareId', editShareId);

                // Nếu có ảnh mới thì gửi ảnh mới, nếu không thì gửi ảnh cũ
                if (fileInput.files[0]) {
                    formData.append('imageFile', fileInput.files[0]);
                } else if (oldImageUrl) {
                    formData.append('oldImageUrl', oldImageUrl);
                }

                fetch(ctx + '/profile-memories', {
                    method: 'POST',
                    body: formData
                })
                        .then(res => res.json())
                        .then(data => {
                            if (data.success)
                                showSuccessAndReload(editShareId ? "Đã cập nhật bài viết!" : "Đăng kỷ niệm thành công!");
                            else
                                alert(data.error || "Không thể thực hiện hành động");
                        })
                        .catch(err => alert("Lỗi kết nối máy chủ"));
            };

// --- Sửa bài ---
            document.querySelectorAll('.btn-edit-post').forEach(b => b.onclick = function () {
                    const card = this.closest('.memory-card');
                    const id = card.dataset.shareId;
                    const title = card.querySelector('b').innerText;
                    const content = card.querySelector('p').innerText;
                    const img = card.querySelector('img')?.getAttribute('src') || ''; // lấy ảnh cũ nếu có

                    document.getElementById('modalTitle').innerText = 'Chỉnh sửa bài viết';
                    document.getElementById('editShareId').value = id;
                    document.getElementById('memoryTitle').value = title;
                    document.getElementById('memoryContent').value = content;

                    // Gắn lại ảnh cũ vào preview (nếu có)
                    const preview = document.getElementById('previewImage');
                    if (img) {
                        preview.src = img;
                        preview.style.display = 'block';
                        document.getElementById('oldImageUrl').value = img.replace(ctx + '/', ''); // chỉ lưu phần đường dẫn tương đối
                    } else {
                        preview.style.display = 'none';
                        document.getElementById('oldImageUrl').value = '';
                    }

                    modal.style.display = 'flex';
                });


// --- Xóa bài ---
            document.querySelectorAll('.btn-delete-post').forEach(b => b.onclick = function () {
                    if (!confirm('Bạn có chắc muốn xóa bài viết này không?'))
                        return;
                    const shareId = this.closest('.memory-card').dataset.shareId;
                    fetch(ctx + '/profile-memories', {method: 'POST', headers: {'Content-Type': 'application/x-www-form-urlencoded'}, body: new URLSearchParams({action: 'deleteShare', shareId})})
                            .then(r => r.json()).then(d => {
                        if (d.success)
                            showSuccessAndReload('Đã xóa bài viết!');
                        else
                            alert(d.error || 'Không thể xóa');
                    });
                });

// --- Bình luận ---
            document.querySelectorAll('.btn-comment').forEach(btn => {
                btn.onclick = function () {
                    const card = this.closest('.memory-card');
                    const input = card.querySelector('input');
                    const content = input.value.trim();
                    if (!content)
                        return alert('Nhập bình luận');
                    const shareId = card.dataset.shareId;
                    fetch(ctx + '/profile-memories', {method: 'POST', headers: {'Content-Type': 'application/x-www-form-urlencoded'}, body: new URLSearchParams({action: 'comment', shareId, content})})
                            .then(r => r.json()).then(d => {
                        if (d.success)
                            showSuccessAndReload('Bình luận thành công!');
                    });
                };
            });

// --- Sửa bình luận ---
            document.querySelectorAll('.btn-edit-comment').forEach(btn => {
                btn.onclick = function () {
                    const cmt = this.closest('.comment');
                    const id = cmt.dataset.commentId;
                    const text = cmt.querySelector('div').innerText.replace(/^.*?:/, '').trim();
                    document.getElementById('editCommentId').value = id;
                    document.getElementById('editCommentContent').value = text;
                    editCommentModal.style.display = 'flex';
                };
            });
            document.getElementById('cancelEditComment').onclick = () => editCommentModal.style.display = 'none';
            document.getElementById('saveEditComment').onclick = () => {
                const commentId = document.getElementById('editCommentId').value;
                const content = document.getElementById('editCommentContent').value.trim();
                if (!content)
                    return alert('Nhập nội dung');
                fetch(ctx + '/profile-memories', {method: 'POST', headers: {'Content-Type': 'application/x-www-form-urlencoded'}, body: new URLSearchParams({action: 'editComment', commentId, content})})
                        .then(r => r.json()).then(d => {
                    if (d.success)
                        showSuccessAndReload('Đã sửa bình luận!');
                });
            };

// --- Xóa bình luận ---
            document.querySelectorAll('.btn-delete-comment').forEach(btn => {
                btn.onclick = function () {
                    if (!confirm('Xóa bình luận này?'))
                        return;
                    const commentId = this.closest('.comment').dataset.commentId;
                    fetch(ctx + '/profile-memories', {method: 'POST', headers: {'Content-Type': 'application/x-www-form-urlencoded'}, body: new URLSearchParams({action: 'deleteComment', commentId})})
                            .then(r => r.json()).then(d => {
                        if (d.success)
                            showSuccessAndReload('Đã xóa bình luận!');
                    });
                };
            });

// --- Cảm xúc ---
            document.querySelectorAll('.reaction').forEach(r => {
                r.onclick = function () {
                    const card = this.closest('.memory-card');
                    const shareId = card.dataset.shareId;
                    const type = this.dataset.type;
                    fetch(ctx + '/profile-memories', {method: 'POST', headers: {'Content-Type': 'application/x-www-form-urlencoded'}, body: new URLSearchParams({action: 'react', shareId, type})})
                            .then(res => res.json()).then(data => {
                        if (data.success)
                            showSuccessAndReload('Thả cảm xúc thành công!');
                    });
                };
            });
        </script>

        <jsp:include page="partials/footer.jsp" />
    </body>
</html>
