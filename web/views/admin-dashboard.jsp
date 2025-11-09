<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="/views/partials/header.jsp" />

<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css"/>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body { font-family: 'Inter', sans-serif; background:#f4f6f9; margin:0; padding:0; }
        h2 { color:#333; }
        #sidebar {
            min-width:360px;
            background:#fff;
            padding:20px;
            border-radius:10px;
            box-shadow:0 3px 6px rgba(0,0,0,0.05);
            height:100vh;
            position:sticky;
            top:0;
        }
        .menu-item { padding:12px 15px; margin-bottom:8px; border-radius:6px; cursor:pointer; font-weight:500; color:#333; text-decoration:none; display:block; transition:0.2s;}
        .menu-item:hover, .active-menu { background:#007bff; color:#fff; }
        .content { flex:1; padding:20px;}
        .card { border-radius:10px; box-shadow:0 2px 4px rgba(0,0,0,0.1); padding:15px; background:#fff; margin-bottom:20px; }
        .card h3 { margin:0; font-size:1.2rem; color:#007bff; }
    </style>
</head>
<body>
<div class="d-flex">
    <!-- Sidebar -->
    <div id="sidebar">
        <h4>Admin Menu</h4>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="menu-item active-menu">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/users" class="menu-item">Users</a>
        <a href="${pageContext.request.contextPath}/admin/trips" class="menu-item">Trips</a>
        <a href="${pageContext.request.contextPath}/admin/reports" class="menu-item">Reports</a>
    </div>

    <!-- Main Content -->
    <div class="content container-fluid">
        <h2>Dashboard</h2>

        <!-- Tổng quan -->
        <div class="row mb-4">
            <div class="col-md-3">
                <div class="card text-center">
                    <h3>Total Users</h3>
                    <p>${totalUsers}</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-center">
                    <h3>Total Trips This Year</h3>
                    <p>${totalTripsThisYear}</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-center">
                    <h3>Pending Reports</h3>
                    <p>${pendingReports}</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-center">
                    <h3>Resolved Reports</h3>
                    <p>${resolvedReports}</p>
                </div>
            </div>
        </div>

        <!-- Biểu đồ -->
        <div class="row mb-4">
            <div class="col-md-6">
                <div class="card">
                    <h3>Trips per Month</h3>
                    <canvas id="tripsChart"></canvas>
                </div>
            </div>
        </div>

        
    </div>
</div>

<script>
   
    const tripsLabels = [<c:forEach items="${tripsPerMonth.keySet()}" var="m" varStatus="loop">${m}<c:if test="${!loop.last}">,</c:if></c:forEach>];
    const tripsData = [<c:forEach items="${tripsPerMonth.values()}" var="v" varStatus="loop">${v}<c:if test="${!loop.last}">,</c:if></c:forEach>];
    const ctxTrips = document.getElementById('tripsChart').getContext('2d');
    new Chart(ctxTrips, {
        type: 'bar',
        data: {
            labels: tripsLabels,
            datasets: [{
                label: 'Trips per Month',
                data: tripsData,
                backgroundColor: 'rgba(75, 192, 192, 0.6)'
            }]
        }
    });

</script>

<jsp:include page="/views/partials/footer.jsp" />
</body>
</html>
