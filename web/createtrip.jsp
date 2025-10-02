<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create Trip</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        html, body {
            height: 100%;
            margin: 0;
            padding: 0;
            background-color: #f5f7fa;
        }
        body {
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .form-card {
            width: 90%;
            max-width: 900px;
            height: 90%;
            overflow-y: auto;
            background-color: #fff;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            padding: 40px 30px;
        }
        .form-card h2 {
            font-weight: 700;
            margin-bottom: 30px;
            color: #333;
            text-align: center;
        }
        .btn-primary {
            background-color: #4e73df;
            border-color: #4e73df;
        }
        .btn-primary:hover {
            background-color: #2e59d9;
            border-color: #2653c5;
        }
        .form-label { font-weight: 500; }
    </style>
</head>
<body>
<div class="form-card">
    <h2>Create New Trip</h2>

    <!-- Hiển thị thông báo -->
    <%
        String successMessage = (String) session.getAttribute("successMessage");
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (successMessage != null) {
    %>
        <div class="alert alert-success"><%= successMessage %></div>
    <%
            session.removeAttribute("successMessage");
        }
        if (errorMessage != null) {
    %>
        <div class="alert alert-danger"><%= errorMessage %></div>
    <%
        }
    %>

    <!-- Form tạo chuyến đi -->
    <form action="createtrip" method="post">
        <div class="row mb-3">
            <div class="col-md-12">
                <label for="name" class="form-label">Trip Name</label>
                <input type="text" class="form-control" id="name" name="name"
                       placeholder="Enter trip name" required>
            </div>
        </div>

        <div class="mb-3">
            <label for="location" class="form-label">Location</label>
            <input type="text" class="form-control" id="location" name="location"
                   placeholder="Enter location" required>
        </div>

        <div class="row mb-3">
            <div class="col-md-6">
                <label for="start_date" class="form-label">Start Date</label>
                <input type="date" class="form-control" id="start_date" name="start_date" required>
            </div>
            <div class="col-md-6">
                <label for="end_date" class="form-label">End Date</label>
                <input type="date" class="form-control" id="end_date" name="end_date" required>
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-6">
                <label for="budget" class="form-label">Budget</label>
                <div class="input-group">
                    <input type="number" class="form-control" id="budget" name="budget"
                           placeholder="Enter budget" step="1000000" min="0" required>
                    <span class="input-group-text">VND</span>
                </div>
            </div>
            <div class="col-md-6">
                <label for="status" class="form-label">Status</label>
                <select class="form-select" id="status" name="status" required>
                    <option value="Active">Active</option>
                    <option value="Blocked">Blocked</option>
                </select>
            </div>
        </div>

        <div class="text-center mt-4">
            <button type="submit" class="btn btn-primary btn-lg px-5">Create Trip</button>
        </div>
    </form>
</div>
</body>
</html>