<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="/views/partials/header.jsp"/>

<html>
    <head>
        <title>Group Memories</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                background: #f6f7f9;
            }
            .layout {
                display: flex;
                min-height: 100vh;
            }
            .sidebar {
                width: 220px;
                background-color: #77a096;
                color: #fff;
                padding: 20px;
            }
            .sidebar h3 {
                text-align: center;
                margin-bottom: 20px;
            }
            .sidebar ul {
                list-style: none;
                padding: 0;
            }
            .sidebar li {
                margin: 12px 0;
            }
            .sidebar a {
                color: #ecf0f1;
                text-decoration: none;
                display: block;
                padding: 8px 10px;
                border-radius: 6px;
                transition: 0.3s;
            }
            .sidebar a:hover, .sidebar a.active {
                background-color: #000;
                color: #fff;
            }

            .content {
                flex: 1;
                background: #fff;
                padding: 20px 30px;
                margin: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 6px rgba(0,0,0,0.1);
            }

            .memory-card {
                background: #fff;
                padding: 15px;
                margin-bottom: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 6px rgba(0,0,0,0.1);
                transition: 0.3s;
            }
            .memory-card:hover {
                transform: scale(1.01);
            }
            .memory-title {
                font-weight: bold;
                font-size: 18px;
                margin-bottom: 5px;
            }
            .memory-content {
                margin-bottom: 10px;
            }
            .memory-image {
                width: 36%;
                border-radius: 6px;
                margin-bottom: 10px;
            }
            .memory-tags {
                font-size: 13px;
                color: #555;
                margin-bottom: 10px;
            }

            .reaction-btn {
                background: #e4e6eb;
                border: none;
                border-radius: 18px;
                padding: 5px 12px;
                font-size: 14px;
                cursor: pointer;
                transition: 0.2s;
                margin-right: 5px;
            }
            .reaction-btn.active {
                background-color: #ffe066;
                transform: scale(1.1);
            }

            .reaction-btn:hover {
                background: #d8dadf;
            }

            .comment-form {
                margin-top: 10px;
                display: flex;
                gap: 5px;
            }
            .comment-form input[type="text"] {
                flex: 1;
                padding: 6px;
                border: 1px solid #ccc;
                border-radius: 4px;
            }
            .comment-form button {
                background-color: #77a096;
                border: none;
                color: white;
                border-radius: 4px;
                padding: 6px 10px;
                cursor: pointer;
            }
            .comment-form button:hover {
                background-color: #4c7c6c;
            }

            .memory-controls {
                display: flex;
                gap: 8px;
                margin-top: 10px;
                flex-wrap: wrap;
            }
            .btn {
                padding: 5px 10px !important;
                border: none !important;
                border-radius: 4px !important;
                color: #fff !important;
                background-color: #77a096 !important;
                cursor: pointer !important;
                transition: 0.3s !important;
            }

            .btn-add {
                background-color: #77a096;
            }
            .btn-edit {
                background-color: #2980b9;
            }
            .btn-cls {
                background-color: #e74c3c;
            }
            .btn-share {
                background-color: #8e44ad;
            }
            .btn:hover {
                opacity: 0.85;
            }

            .comments {
                margin-top: 10px;
                font-size: 14px;
            }
            .comment {
                padding: 4px 0;
                border-bottom: 1px solid #eee;
            }
            .comment-user {
                font-weight: bold;
                margin-right: 4px;
            }
            .reaction-count {
                font-size: 13px;
                color: #555;
                margin-left: 10px;
            }
            .posted-info {
                font-size: 12px;
                color: #888;
                margin-top: 8px;
            }

            /* Modal */
            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.4);
            }
            .modal-content {
                background: white;
                margin: 8% auto !important;
                padding: 20px !important;
                width: 800px !important;
                border-radius: 8px !important;
            }
            .modal input, .modal textarea {
                width: 100%;
                margin-bottom: 10px;
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 4px;
            }
            #imagePreview {
                max-width: 200px;   /* chiều rộng tối đa */
                max-height: 150px;  /* chiều cao tối đa */
                object-fit: contain; /* giữ tỉ lệ ảnh */
                display: none;      /* ẩn mặc định */
                border-radius: 8px; /* tùy chọn, bo góc */
                margin-top: 10px;
            }
        </style>
    </head>
    <body>
        <div class="layout">
            <div class="sidebar">
                <h3>Group Menu</h3>
                <ul>
                    <li><a href="#">🕒 Time Line</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/manage?groupId=${groupId}">👥 Members</a></li>
                    <li><a href="#">🎯 Activities</a></li>
                    <li><a href="#">🧾 Tasks</a></li>
                    <li><a href="#">💰 Expense</a></li>
                    <li><a href="${pageContext.request.contextPath}/group-memories?groupId=${groupId}" class="active">📸 Memories</a></li>
                    <li><a href="${pageContext.request.contextPath}/group/notifications?groupId=${groupId}">🔔 Notification</a></li>
                </ul>
            </div>

            <div class="content">
                <h2>📸 Kỉ niệm (Chuyến đi: ${trip.name})</h2>


                <c:if test="${not empty sessionScope.successMessage}">
                    <div id="alertBox" class="alert alert-success" 
                         style="position: fixed; top: 20px; right: 20px; padding: 12px 20px; border-radius: 8px;
                         background-color: #d4edda; color: #155724; box-shadow: 0 2px 8px rgba(0,0,0,0.2);
                         z-index: 9999; transition: opacity 0.5s ease;">
                        ${sessionScope.successMessage}
                    </div>
                    <c:remove var="successMessage" scope="session"/>
                </c:if>

                <c:if test="${not empty sessionScope.errorMessage}">
                    <div id="alertBox" class="alert alert-danger" 
                         style="position: fixed; top: 20px; right: 20px; padding: 12px 20px; border-radius: 8px;
                         background-color: #f8d7da; color: #721c24; box-shadow: 0 2px 8px rgba(0,0,0,0.2);
                         z-index: 9999; transition: opacity 0.5s ease;">
                        ${sessionScope.errorMessage}
                    </div>
                    <c:remove var="errorMessage" scope="session"/>
                </c:if>

                <script>
                    // 👇 Làm thông báo biến mất sau 2 giây
                    setTimeout(() => {
                        const alertBox = document.getElementById("alertBox");
                        if (alertBox) {
                            alertBox.style.opacity = "0";
                            setTimeout(() => alertBox.remove(), 500); // Xóa sau khi mờ đi
                        }
                    }, 2000);
                </script>


                <button class="btn btn-add" id="btnAddMemory">➕ Thêm bài</button>

                <c:forEach var="m" items="${memories}">
                    <div class="memory-card" data-memory-id="${m.memoryId}">
                        <div class="memory-title">${m.title}</div>
                        <div class="memory-content">${m.content}</div>
                        <c:if test="${not empty m.imageUrl}">
                            <img src="${pageContext.request.contextPath}/${m.imageUrl}" alt="Memory Image" class="memory-image"/>
                        </c:if>

                        <div class="memory-tags">
                            <c:if test="${not empty m.taggedUsers}">
                                Tagged:
                                <c:forEach var="u" items="${m.taggedUsers}" varStatus="loop">
                                    ${u.name}<c:if test="${!loop.last}">, </c:if>
                                </c:forEach>
                            </c:if>
                        </div>

                        <form action="${pageContext.request.contextPath}/group-memories" method="post">
                            <input type="hidden" name="action" value="react">
                            <input type="hidden" name="memoryId" value="${m.memoryId}">
                            <input type="hidden" name="groupId" value="${groupId}">

                            <button class="reaction-btn ${m.userReaction eq 'Like' ? 'active' : ''}" name="type" value="Like">👍</button>
                            <button class="reaction-btn ${m.userReaction eq 'Love' ? 'active' : ''}" name="type" value="Love">❤️</button>
                            <button class="reaction-btn ${m.userReaction eq 'Haha' ? 'active' : ''}" name="type" value="Haha">😂</button>
                            <button class="reaction-btn ${m.userReaction eq 'Wow' ? 'active' : ''}" name="type" value="Wow">😮</button>
                            <button class="reaction-btn ${m.userReaction eq 'Sad' ? 'active' : ''}" name="type" value="Sad">😢</button>
                            <button class="reaction-btn ${m.userReaction eq 'Angry' ? 'active' : ''}" name="type" value="Angry">😡</button>

                            <span class="reaction-count">Reactions: ${m.reactionsCount}</span>
                        </form>




                        <div class="comments">
                            <c:forEach var="cmt" items="${m.comments}">
                                <div class="comment" data-comment-id="${cmt.commentId}">
                                    <span class="comment-user">${cmt.userName}:</span> 
                                    <span class="comment-content">${cmt.content}</span>

                                    <!-- Chỉ hiển thị nếu là user hiện tại -->
                                    <c:if test="${cmt.userId == currentUser.user_id}">
                                        <button type="button" class="btn btn-edit-comment" 
                                                data-comment-id="${cmt.commentId}" 
                                                data-content="${cmt.content}">✏️ Sửa</button>

                                        <form method="post" action="${pageContext.request.contextPath}/group-memories" style="display:inline;" class="delete-comment-form">
                                            <input type="hidden" name="action" value="deleteComment">
                                            <input type="hidden" name="commentId" value="${cmt.commentId}">
                                            <input type="hidden" name="groupId" value="${groupId}">
                                            <input type="hidden" name="memoryId" value="${m.memoryId}">
                                            <button type="submit" class="btn btn-cls" onclick="return confirm('Xóa bình luận này?')">🗑️ Xóa</button>
                                        </form>

                                        <!-- Form ẩn để sửa comment -->
                                        <form method="post" action="${pageContext.request.contextPath}/group-memories" class="edit-comment-form" style="display:none; margin-top:5px;">
                                            <input type="hidden" name="action" value="editComment">
                                            <input type="hidden" name="commentId" value="${cmt.commentId}">
                                            <input type="hidden" name="memoryId" value="${m.memoryId}">
                                            <input type="hidden" name="groupId" value="${groupId}">
                                            <input type="text" name="content" value="${cmt.content}" style="width:70%;">
                                            <button type="submit" class="btn btn-add">💾 Lưu</button>
                                            <button type="button" class="btn btn-cls btn-cancel-edit">❌ Hủy</button>
                                        </form>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </div>



                        <form class="comment-form" action="${pageContext.request.contextPath}/group-memories" method="post">
                            <input type="hidden" name="action" value="comment">
                            <input type="hidden" name="memoryId" value="${m.memoryId}">
                            <input type="hidden" name="groupId" value="${groupId}">
                            <input type="text" name="content" placeholder="Viết bình luận...">
                            <button type="submit">💬 Gửi</button>
                        </form>

                        <div class="memory-controls">
                            <c:if test="${m.userId == currentUser.user_id}">
                                <button class="btn btn-edit" 
                                        data-id="${m.memoryId}" 
                                        data-title="${m.title}" 
                                        data-content="${m.content}" 
                                        data-img="${m.imageUrl}">✏️ Sửa</button>
                                <form action="${pageContext.request.contextPath}/group-memories" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="memoryId" value="${m.memoryId}">
                                    <input type="hidden" name="groupId" value="${groupId}">
                                    <button type="submit" class="btn btn-cls" onclick="return confirm('Xóa bài này?')">🗑️ Xóa</button>
                                </form>
                            </c:if>

                            <button type="button" class="btn btn-share"
                                    data-memory-id="${m.memoryId}"
                                    data-group-id="${groupId}"
                                    data-title="${m.title}"
                                    data-content="${m.content}"
                                    data-img="${m.imageUrl}">📤 Chia sẻ</button>


                        </div>

                        <div class="posted-info">
                            Đăng bởi: ${m.userName} lúc <fmt:formatDate value="${m.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- Modal thêm/sửa -->
        <div id="modalMemory" class="modal">
            <div class="modal-content">
                <h3 id="modalTitle">Thêm bài</h3>
                <form id="memoryForm" method="post" action="${pageContext.request.contextPath}/group-memories"
                      enctype="multipart/form-data">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="groupId" value="${groupId}">
                    <input type="hidden" name="memoryId" id="memoryId">

                    <input type="text" name="title" id="memoryTitle" placeholder="Tiêu đề" required>
                    <textarea name="content" id="memoryContent" placeholder="Nội dung" rows="4" required></textarea>

                    <label for="memoryImageFile">Ảnh kỷ niệm:</label>
                    <input type="file" id="memoryImageFile" name="imageFile" accept="image/*">
                    <input type="hidden" name="oldImageUrl" id="oldImageUrl">
                    <img id="imagePreview" src="" alt="" style="display:none;">

                    <div style="text-align:right;">
                        <button type="submit" class="btn btn-add">Lưu</button>
                        <button type="button" id="closeModal" class="btn btn-cls">Đóng</button>
                    </div>
                </form>
            </div>
        </div>

        <div id="modalShare" class="modal">
            <div class="modal-content">
                <h3>Chia sẻ kỷ niệm</h3>

                <!-- Thông tin kỷ niệm -->
                <div id="shareMemoryInfo" style="margin-bottom:10px;">
                    <p><strong>Tiêu đề:</strong> <span id="shareTitleDisplay"></span></p>
                    <p><strong>Nội dung:</strong> <span id="shareContentDisplay"></span></p>
                    <img id="shareImage" src="" alt="Ảnh kỷ niệm" style="max-width:200px; max-height:150px; display:none; border-radius:6px;">
                </div>

                <!-- Form chọn privacy -->
                <form id="shareForm" method="post" action="${pageContext.request.contextPath}/group-memories">
                    <input type="hidden" name="action" value="share">
                    <input type="hidden" name="memoryId" id="shareMemoryId">
                    <input type="hidden" name="groupId" id="shareGroupId">
                    <input type="hidden" name="title" id="shareTitle">
                    <input type="hidden" name="content" id="shareContent">
                    <input type="hidden" name="imageUrl" id="shareImageInput">
                    <label>Chọn quyền riêng tư:</label>
                    <select name="privacy" id="privacySelect">
                        <option value="Public">Công khai</option>
                        <option value="Private">Riêng tư</option>
                    </select>
                    <div style="text-align:right; margin-top:10px;">
                        <button type="submit" class="btn btn-add">Chia sẻ</button>
                        <button type="button" class="btn btn-cls" id="closeShareModal">Đóng</button>
                    </div>
                </form>
            </div>
        </div>

        <div id="modalComment" class="modal">
            <div class="modal-content">
                <h3>Sửa bình luận</h3>
                <form id="commentForm" method="post" action="${pageContext.request.contextPath}/group-memories">
                    <input type="hidden" name="action" value="editComment">
                    <input type="hidden" name="commentId" id="commentId">
                    <input type="hidden" name="memoryId" id="commentMemoryId">
                    <input type="hidden" name="groupId" id="commentGroupId">
                    <textarea name="content" id="commentContent" rows="3" required></textarea>
                    <div style="text-align:right; margin-top:10px;">
                        <button type="submit" class="btn btn-add">Lưu</button>
                        <button type="button" class="btn btn-cls" id="closeCommentModal">Đóng</button>
                    </div>
                </form>
            </div>
        </div>


        <script>
            document.getElementById('btnAddMemory').onclick = () => {
                document.getElementById('modalTitle').textContent = 'Thêm bài';
                document.querySelector('#memoryForm input[name=action]').value = 'add';
                document.getElementById('memoryId').value = '';
                document.getElementById('memoryTitle').value = '';
                document.getElementById('memoryContent').value = '';
                document.getElementById('oldImageUrl').value = '';
                document.getElementById('imagePreview').style.display = 'none';
                document.getElementById('modalMemory').style.display = 'block';
                document.getElementById('memoryImageFile').required = true;
            };

            document.querySelectorAll('.btn-edit').forEach(btn => {
                btn.onclick = function () {
                    document.getElementById('modalTitle').textContent = 'Sửa bài';
                    document.querySelector('#memoryForm input[name=action]').value = 'edit';
                    document.getElementById('memoryId').value = this.dataset.id;
                    document.getElementById('memoryTitle').value = this.dataset.title;
                    document.getElementById('memoryContent').value = this.dataset.content;
                    document.getElementById('oldImageUrl').value = this.dataset.img || '';
                    if (this.dataset.img) {
                        const preview = document.getElementById('imagePreview');
                        preview.src = '${pageContext.request.contextPath}/' + this.dataset.img;
                        preview.style.display = 'block';
                    } else {
                        document.getElementById('imagePreview').style.display = 'none';
                    }
                    document.getElementById('modalMemory').style.display = 'block';
                    document.getElementById('memoryImageFile').required = false;
                };
            });

            document.getElementById('closeModal').onclick = () => {
                document.getElementById('modalMemory').style.display = 'none';
            };

            document.querySelectorAll('.btn-share').forEach(btn => {
                btn.onclick = function () {

                    const memoryId = this.dataset.memoryId;
                    const groupId = this.dataset.groupId;
                    const title = this.dataset.title || '';
                    const content = this.dataset.content || '';
                    const img = this.dataset.img || '';

                    document.getElementById('shareMemoryId').value = memoryId;
                    document.getElementById('shareGroupId').value = groupId;
                    document.getElementById('shareTitle').value = title;
                    document.getElementById('shareContent').value = content;
                    document.getElementById('shareImageInput').value = img;

                    document.getElementById('shareTitleDisplay').textContent = title;
                    document.getElementById('shareContentDisplay').textContent = content;

                    const shareImage = document.getElementById('shareImage');
                    if (img) {
                        shareImage.src = '${pageContext.request.contextPath}/' + img;
                        shareImage.style.display = 'block';
                    } else {
                        shareImage.style.display = 'none';
                    }

                    document.getElementById('modalShare').style.display = 'block';
                }
            });

            document.getElementById('closeShareModal').onclick = function () {
                document.getElementById('modalShare').style.display = 'none';
            }


            document.querySelectorAll('.btn-edit-comment').forEach(btn => {
                btn.onclick = function () {
                    const commentDiv = this.closest('.comment');
                    commentDiv.querySelector('.comment-content').style.display = 'none';
                    commentDiv.querySelector('.edit-comment-form').style.display = 'inline-block';
                    this.style.display = 'none';
                }
            });

            document.querySelectorAll('.btn-cancel-edit').forEach(btn => {
                btn.onclick = function () {
                    const form = this.closest('.edit-comment-form');
                    const commentDiv = form.closest('.comment');
                    commentDiv.querySelector('.comment-content').style.display = 'inline';
                    commentDiv.querySelector('.btn-edit-comment').style.display = 'inline-block';
                    form.style.display = 'none';
                }
            });

        </script>

        <jsp:include page="/views/partials/footer.jsp"/>
    </body>
</html>