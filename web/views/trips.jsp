<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Trips" %>
<%
    List<Trips> trips = (List<Trips>) request.getAttribute("trips");
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");
%>
<!DOCTYPE html>
<html>
<head>
    <title>List Trip</title>
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">Group Trip</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="home">Home</a></li>
                <li class="nav-item"><a class="nav-link active" href="trips">Trip</a></li>
                <li class="nav-item"><a class="nav-link" href="#">About Us</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Welcome, User ▼</a></li>
            </ul>
        </div>
    </div>
</nav>

<!-- Nội dung -->
<div class="container mt-4">
    <!-- Thông báo -->
    <% if (successMessage != null) { %>
        <div class="alert alert-success"><%= successMessage %></div>
    <% } %>
    <% if (errorMessage != null) { %>
        <div class="alert alert-danger"><%= errorMessage %></div>
    <% } %>

    <div class="row">
        <!-- Form tìm kiếm -->
        <div class="col-md-3">
            <div class="card p-3">
                <h5>Find trip for you</h5>
                <form action="trips" method="get">
                    <div class="mb-3">
                        <label class="form-label">Select budget</label>
                        <input type="number" class="form-control" name="budget"
                               value="<%= request.getParameter("budget") != null ? request.getParameter("budget") : "" %>">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Departure date (from)</label>
                        <input type="date" class="form-control" name="start_date"
                               value="<%= request.getParameter("start_date") != null ? request.getParameter("start_date") : "" %>">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Departure date (to)</label>
                        <input type="date" class="form-control" name="end_date"
                               value="<%= request.getParameter("end_date") != null ? request.getParameter("end_date") : "" %>">
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Find tour</button>
                </form>
                <hr>
                <!-- Nút tạo mới -->
                <a href="createtrip" class="btn btn-success w-100">+ Create Trip</a>
            </div>
        </div>

        <!-- Danh sách trip -->
        <div class="col-md-9">
            <h3 class="mb-3">List Trip</h3>
            <%
                if (trips != null && !trips.isEmpty()) {
                    for (Trips t : trips) {
            %>
            <div class="card mb-3">
                <div class="row g-0">
                    <!-- Ảnh đại diện -->
                    <div class="col-md-3 d-flex align-items-center justify-content-center">
                        <img src="https://via.placeholder.com/150" class="img-fluid rounded-start" alt="Trip Image">
                    </div>
                    <!-- Thông tin chuyến đi -->
                    <div class="col-md-9">
                        <div class="card-body">
                            <h5 class="card-title"><%= t.getName() %> - <%= t.getLocation() %></h5>
                            <p class="card-text">
                                Ngày đi: <%= t.getStart_date() %> | Ngày kết thúc: <%= t.getEnd_date() %><br>
                                Ngân sách: <%= t.getBudget() %> VND / người<br>
                                Trạng thái: <%= t.getStatus() %>
                            </p>
                            <a href="trips?action=detail&id=<%= t.getTrip_id() %>" class="btn btn-outline-primary btn-sm">Xem chi tiết</a>
                            <a href="trips?action=join&id=<%= t.getTrip_id() %>" class="btn btn-success btn-sm">Tham gia chuyến đi</a>
                        </div>
                    </div>
                </div>
            </div>
            <% } } else { %>
            <div class="alert alert-warning">Không có chuyến đi nào phù hợp!</div>
            <% } %>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>