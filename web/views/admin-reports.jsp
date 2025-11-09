<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/views/partials/header.jsp" />

<html>
<head>
    <title>Admin Reports</title>
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background: #f4f6f9;
            margin:0;
            padding:0;
        }
        h2, h4 {
            color:#333;
        }

        

        /* Sidebar */
        #sidebar {
            min-width:360px;
            background:#fff;
            padding:20px;
            border-radius:10px;
            box-shadow:0 3px 6px rgba(0,0,0,0.05);
            height:fit-content;
            position:sticky;
            top:20px;
            align-self:flex-start;
        }

        .menu-item {
            padding:12px 15px;
            margin-bottom:8px;
            border-radius:6px;
            cursor:pointer;
            font-weight:500;
            color:#333;
            text-decoration:none;
            display:block;
            transition:0.2s;
        }
        .menu-item:hover, .active-menu {
            background:#007bff;
            color:#fff;
        }

        /* Content */
        .content {
            flex:1;
        }

        /* Report card */
        .report-card {
            background:#fff;
            padding:15px 20px;
            border-radius:10px;
            box-shadow:0 3px 6px rgba(0,0,0,0.08);
            margin-bottom:15px;
            transition:0.2s;
        }
        .report-card:hover {
            box-shadow:0 6px 12px rgba(0,0,0,0.12);
        }
        .report-header {
            display:flex;
            justify-content:space-between;
            align-items:center;
            margin-bottom:10px;
        }
        .report-header h4 {
            margin:0;
            font-size:18px;
            color:#007bff;
        }
        .report-status {
            padding:4px 8px;
            border-radius:5px;
            color:#fff;
            font-size:13px;
            font-weight:600;
            text-transform:uppercase;
        }
        .report-status.PENDING { background:#f0ad4e; }
        .report-status.IN_PROGRESS { background:#17a2b8; }
        .report-status.RESOLVED { background:#5cb85c; }
        .report-status.REJECTED { background:#d9534f; }

        .report-info p {
            margin:4px 0;
            font-size:14px;
        }

        .inline-form {
            display:flex;
            flex-direction:column;
            gap:6px;
        }
        .inline-form textarea {
            width:100%;
            padding:8px;
            border-radius:6px;
            border:1px solid #ccc;
            resize:none;
            font-size:14px;
        }
        .inline-form select, .inline-form button {
            padding:8px 12px;
            border-radius:6px;
            font-size:14px;
            border:1px solid #ccc;
        }
        .inline-form button {
            background:#007bff;
            color:#fff;
            cursor:pointer;
            border:none;
            transition:0.2s;
        }
        .inline-form button:hover {
            background:#0056b3;
        }

        .report-thumb {
            width:100px;
            height:auto;
            border-radius:6px;
            margin:2px;
            border:1px solid #ddd;
            cursor:pointer;
        }

        #imgModal {
            display:none;
            position:fixed;
            z-index:9999;
            left:0;
            top:0;
            width:100%;
            height:100%;
            background: rgba(0,0,0,0.9);
            justify-content:center;
            align-items:center;
        }
        #modalImg {
            width:auto;
            height:auto;
            min-width:65%;
            min-height:65%;
            max-width:100%;
            max-height:100%;
            border-radius:10px;
            box-shadow:0 4px 12px rgba(0,0,0,0.4);
        }
        #imgModal span {
            position:absolute;
            top:20px;
            right:35px;
            color:#fff;
            font-size:40px;
            cursor:pointer;
        }

        /* Filter */
        .filter-group {
            margin-bottom:15px;
        }
        .filter-group label {
            font-weight:500;
            margin-right:10px;
            color:#333;
        }
        .filter-group select, .filter-group input {
            padding:6px 10px;
            border-radius:6px;
            border:1px solid #ccc;
            font-size:14px;
        }

        .view-log-btn {
            background: #007bff;
            color: #fff;
            padding: 3px 8px;
            border-radius: 5px;
            font-size: 13px;
            text-decoration: none;
            margin-left: 8px;
            transition: 0.2s;
        }
        .view-log-btn:hover {
            background: #0056b3;
        }

        @media (max-width:1024px) {
            .d-flex { flex-direction:column; }
            #sidebar { width:100%; margin-bottom:20px; }
        }
    </style>
</head>
<body>



<div class="d-flex">
    <!-- Sidebar / Navbar bên trái -->
    <div id="sidebar">
        <h4>Admin Menu</h4>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/users" class="menu-item">Users</a>
        <a href="${pageContext.request.contextPath}/admin/trips" class="menu-item">Trips</a>
        <a href="${pageContext.request.contextPath}/admin/reports" class="menu-item active-menu">Reports</a>
    </div>

    <!-- Content chính -->
    <div class="content">
        <div id="reportsSection">
            <h4>Reports List</h4>

            <!-- Filter -->
            <div class="filter-group">
                <label>Status:</label>
                <select id="reportStatusFilter">
                    <option value="ALL">All</option>
                    <option value="PENDING">Pending</option>
                    <option value="IN_PROGRESS">In-Progress</option>
                    <option value="RESOLVED">Resolved</option>
                    <option value="REJECTED">Rejected</option>
                </select>

                <label>Type:</label>
                <select id="reportTypeFilter">
                    <option value="ALL">All</option>
                    <option value="LEADER_MISCONDUCT">Leader Misconduct</option>
                    <option value="KICK_DISPUTE">Kick Dispute</option>
                    <option value="MEMBER_CONFLICT">Member Conflict</option>
                    <option value="GROUP_ACTIVITY">Group Activity</option>
                    <option value="SCAM">Scam</option>
                </select>

                <label>Date:</label>
                <input type="date" id="reportDateFilter">
            </div>

            <!-- Reports Cards -->
            <div id="reportsContainer">
                <c:forEach var="r" items="${reports}">
                    <div class="report-card"
                         data-status="${r.status}"
                         data-type="${r.type}"
                         data-date="${fn:substring(r.created_at.toString(), 0, 10)}">

                        <div class="report-header">
                            <h4>${r.title}</h4>
                            <span class="report-status ${r.status}">${r.status}</span>
                        </div>

                        <div class="report-info">
                            <p><b>Reporter:</b> ${empty r.reporter_name ? '-' : r.reporter_name} </p>
                            <c:if test="${r.hasNewEvidence}">
                                <p style="color:#007bff; font-weight:600;">📎 Người dùng đã bổ sung bằng chứng mới</p>
                            </c:if>

                            <p><b>Reported User:</b> ${empty r.reported_user_name ? '-' : r.reported_user_name} ( UserID: ${r.reported_user_id} )</p>
                            <p><b>Group:</b> ${empty r.group_name ? '-' : r.group_name} ( TripID: ${r.group_id} )
                                <c:if test="${not empty r.group_id}">
                                    <a href="${pageContext.request.contextPath}/group/notifications?groupId=${r.group_id}" 
                                       class="view-log-btn" 
                                       target="_blank">🔍 View Log</a>
                                </c:if>
                            </p>
                            <p><b>Type:</b>
                                <c:choose>
                                    <c:when test="${r.type == 'LEADER_MISCONDUCT'}">Leader Misconduct</c:when>
                                    <c:when test="${r.type == 'KICK_DISPUTE'}">Kick Dispute</c:when>
                                    <c:when test="${r.type == 'MEMBER_CONFLICT'}">Member Conflict</c:when>
                                    <c:when test="${r.type == 'GROUP_ACTIVITY'}">Group Activity</c:when>
                                    <c:when test="${r.type == 'SCAM'}">Scam</c:when>
                                    <c:otherwise>Other</c:otherwise>
                                </c:choose>
                            </p>
                            <p><b>Attachments:</b>
                                <c:forEach var="a" items="${r.attachments}">
                                    <c:choose>
                                        <c:when test="${fn:endsWith(a, '.pdf')}">
                                            <a href="${pageContext.request.contextPath}/${a}" target="_blank">📄 PDF</a>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/${a}" 
                                                 alt="attachment" 
                                                 class="report-thumb" 
                                                 onclick="openModal(this)">
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </p>
                            <p><b>Created:</b> <fmt:formatDate value="${r.created_at}" pattern="dd/MM/yyyy HH:mm"/></p>
                        </div>

                        <form class="inline-form" method="post" action="dashboard">
                            <input type="hidden" name="action" value="updateReport">
                            <input type="hidden" name="report_id" value="${r.report_id}">
                            <textarea name="admin_response" rows="2" placeholder="Enter response...">${fn:escapeXml(r.admin_response)}</textarea>
                            <select name="status" required>
                                <option value="PENDING" ${r.status=='PENDING'?'selected':''}>Pending</option>
                                <option value="IN_PROGRESS" ${r.status=='IN_PROGRESS'?'selected':''}>In-Progress</option>
                                <option value="RESOLVED" ${r.status=='RESOLVED'?'selected':''}>Resolved</option>
                                <option value="REJECTED" ${r.status=='REJECTED'?'selected':''}>Rejected</option>
                            </select>
                            <button type="submit">Update</button>
                        </form>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- Modal -->
        <div id="imgModal">
            <span onclick="closeModal()">&times;</span>
            <img id="modalImg">
        </div>
    </div>
</div>

<script>
    // Filter reports
    const statusFilter = document.getElementById('reportStatusFilter');
    const typeFilter = document.getElementById('reportTypeFilter');
    const dateFilter = document.getElementById('reportDateFilter');
    const reportCards = document.querySelectorAll('.report-card');

    function filterReports() {
        const status = statusFilter.value;
        const type = typeFilter.value;
        const date = dateFilter.value;

        reportCards.forEach(card => {
            const cardStatus = card.dataset.status;
            const cardType = card.dataset.type;
            const cardDate = card.dataset.date;
            let show = true;

            if (status !== 'ALL' && cardStatus !== status)
                show = false;
            if (type !== 'ALL' && cardType !== type)
                show = false;
            if (date && cardDate !== date)
                show = false;

            card.style.display = show ? 'block' : 'none';
        });
    }

    statusFilter.addEventListener('change', filterReports);
    typeFilter.addEventListener('change', filterReports);
    dateFilter.addEventListener('change', filterReports);

    // Modal functions
    function openModal(img) {
        const modal = document.getElementById('imgModal');
        const modalImg = document.getElementById('modalImg');
        modal.style.display = 'flex';
        modalImg.src = img.src;
    }
    function closeModal() {
        document.getElementById('imgModal').style.display = 'none';
    }
</script>

<jsp:include page="/views/partials/footer.jsp" />
</body>
</html>
