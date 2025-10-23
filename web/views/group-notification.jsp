<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="/views/partials/header.jsp"/>

<html>
<head>
    <title>Group Notifications</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background: #f4f5f7; margin:0; }
        .layout { display: flex; min-height: 100vh; }
        .sidebar { width: 220px; background:#77a096; color:white; padding:20px; }
        .sidebar h3 { text-align:center; margin-bottom:20px; }
        .sidebar ul { list-style:none; padding:0; }
        .sidebar li { margin:12px 0; }
        .sidebar a { color:#ecf0f1; text-decoration:none; display:block; padding:8px 10px; border-radius:6px; }
        .sidebar a.active, .sidebar a:hover { background-color:#333; }

        .content { flex:1; padding:20px 30px; margin:20px; background:white; border-radius:10px; box-shadow:0 4px 12px rgba(0,0,0,0.1); }
        .filter-bar { display:flex; gap:10px; flex-wrap:wrap; margin-bottom:20px; }
        .filter-btn { border:none; padding:6px 12px; border-radius:6px; cursor:pointer; transition:0.3s; background:#ddd; }
        .filter-btn.active { background:#77a096; color:white; }
        .filter-btn:hover { background: #77a096; color: white; }

        .notification-card { background: white; padding:15px 20px; border-radius:10px; box-shadow:0 2px 8px rgba(0,0,0,0.05); display:flex; align-items:center; gap:15px; }
        .notification-icon.role-change { background:#0d6efd; }
        .notification-icon.join-invite { background:#ffc107; }
        .notification-icon.removal { background:#dc3545; }
        .notification-icon { width:40px; height:40px; border-radius:50%; display:flex; align-items:center; justify-content:center; color:white; font-size:18px; flex-shrink:0; }

        .notification-body { flex:1; }
        .notification-title { font-weight:600; }
        .notification-time { font-size:0.85rem; color:#6c757d; margin-top:4px; }
        .user-name { font-weight:500; color:#0d6efd; }
        .reason { font-style:italic; color:#b02a37; }
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
            <li><a href="${pageContext.request.contextPath}/group-memories?groupId=${groupId}">📸 Memories</a></li>
            <li><a href="${pageContext.request.contextPath}/group/notifications?groupId=${groupId}" class="active">🔔 Notification</a></li>
        </ul>
    </div>

    <div class="content">
        <h2>Notifications (Chuyến đi: ${trip.name})</h2>

        <div class="filter-bar">
            <button class="filter-btn active" data-type="all">Tất cả</button>
            <button class="filter-btn" data-type="role-change">Thay đổi vai trò</button>
            <button class="filter-btn" data-type="join-invite">Tham gia / Lời mời</button>
            <button class="filter-btn" data-type="removal">Xóa / Kick</button>
        </div>

        <div class="notifications">
            <c:forEach var="entry" items="${allLogs}">
                <div class="notification-card ${entry.type}" data-type="${entry.type}">
                    <div class="notification-icon ${entry.type}">
                        <c:choose>
                            <c:when test="${entry.type eq 'role-change'}">👤</c:when>
                            <c:when test="${entry.type eq 'join-invite'}">✉️</c:when>
                            <c:when test="${entry.type eq 'removal'}">❌</c:when>
                        </c:choose>
                    </div>
                    <div class="notification-body">
                        <c:choose>
                            <c:when test="${entry.type eq 'role-change'}">
                                <span class="user-name">${userMap[entry.log.user_id]}</span> đã được thay đổi role bởi 
                                <span class="user-name">${userMap[entry.log.changed_by]}</span> thành <b>${entry.log.new_role}</b>
                            </c:when>
                            <c:when test="${entry.type eq 'join-invite'}">
                                <c:choose>
                                    <c:when test="${entry.log.status eq 'INVITED'}">
                                        <span class="user-name">${userMap[entry.log.invited_by]}</span> đã mời 
                                        <span class="user-name">${userMap[entry.log.user_id]}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="user-name">${userMap[entry.log.user_id]}</span> yêu cầu tham gia nhóm
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:when test="${entry.type eq 'removal'}">
                                <span class="user-name">${userMap[entry.log.removed_user_id]}</span> đã bị xóa khỏi nhóm bởi 
                                <span class="user-name">${userMap[entry.log.removed_by]}</span>
                                <c:if test="${not empty entry.log.reason}">
                                    <div class="reason">Lý do: ${entry.log.reason}</div>
                                </c:if>
                            </c:when>
                        </c:choose>
                        <div class="notification-time">
                            <fmt:formatDate value="${entry.time}" pattern="dd/MM/yyyy HH:mm"/>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<script>
    const filterBtns = document.querySelectorAll('.filter-btn');
    const notifications = document.querySelectorAll('.notification-card');

    filterBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            filterBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            const type = btn.dataset.type;
            notifications.forEach(n => {
                if (type === 'all' || n.classList.contains(type)) {
                    n.style.display = 'flex';
                } else {
                    n.style.display = 'none';
                }
            });
        });
    });
</script>

<jsp:include page="/views/partials/footer.jsp"/>
</body>
</html>
