<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/views/partials/header.jsp" />

<html>
<head>
    <title>Admin - Trips</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css"/>
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background: #f4f6f9;
            margin:0;
            padding:0;
        }
        h2 { color:#333; }

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
    </style>
    <script>
        function confirmStatusChange(form, tripName, currentStatus) {
            const actionText = currentStatus === 'Active' ? 'khóa' : 'mở khóa';
            if (confirm("Bạn chắc chắn muốn " + actionText + " trip " + tripName + " không?")) {
                form.submit();
            }
        }
    </script>
</head>
<body>

<div class="d-flex">
    <!-- Sidebar / Navbar bên trái -->
    <div id="sidebar">
        <h4>Admin Menu</h4>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/users" class="menu-item">Users</a>
        <a href="${pageContext.request.contextPath}/admin/trips" class="menu-item active-menu">Trips</a>
        <a href="${pageContext.request.contextPath}/admin/reports" class="menu-item">Reports</a>
    </div>

    <!-- Content chính -->
    <div class="content container mt-4">
        <h2>Danh sách Trips</h2>

        <c:if test="${not empty message}">
            <div class="alert alert-info">${message}</div>
        </c:if>

        <!-- Search form -->
        <form method="get" action="">
            <div class="row mb-3">
                <div class="col-md-4">
                    <input type="text" name="keyword" class="form-control"
                           placeholder="Tìm theo tên trip hoặc leader" 
                           value="${keyword}"/>
                </div>
                <div class="col-md-2">
                    <button class="btn btn-primary">Tìm kiếm</button>
                </div>
            </div>
        </form>

        <table class="table table-bordered table-striped">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Tên Trip</th>
                    <th>Leader</th>
                    <th>Ngày bắt đầu</th>
                    <th>Ngày kết thúc</th>
                    <th>Trạng thái</th>
                    <th>Xem chi tiết</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="t" items="${trips}">
                    <tr>
                        <td>${t.tripId}</td>
                        <td>${t.name}</td>
                        <td>${t.group_name}</td>
                        <td><fmt:formatDate value="${t.startDate}" pattern="dd/MM/yyyy"/></td>
                        <td><fmt:formatDate value="${t.endDate}" pattern="dd/MM/yyyy"/></td>
                        <td>${t.status}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/group/manage?groupId=${t.tripId}" 
                               class="btn btn-sm btn-info">Chi tiết</a>
                        </td>
                        <td>
                            <form method="post" style="display:inline;">
                                <input type="hidden" name="action" value="changeStatus"/>
                                <input type="hidden" name="trip_id" value="${t.tripId}"/>
                                <input type="hidden" name="currentStatus" value="${t.status}"/>
                                <button type="button" class="btn btn-sm ${t.status=='Active'?'btn-danger':'btn-success'}"
                                        onclick="confirmStatusChange(this.form,'${t.name}','${t.status}')">
                                    ${t.status=='Active'?'Block':'Unlock'}
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/views/partials/footer.jsp" />

</body>
</html>
