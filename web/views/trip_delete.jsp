<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Xóa chuyến đi</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background: #f5f7fb;
      margin: 0;
      padding: 0;
    }
    .wrap {
      max-width: 700px;
      margin: 60px auto;
      background: #fff;
      border: 1px solid #e5e7ee;
      border-radius: 12px;
      padding: 24px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.08);
    }
    h1 {
      color: #dc2626;
      text-align: center;
    }
    .info {
      margin: 16px 0;
      background: #f9fafb;
      padding: 12px 16px;
      border-radius: 8px;
      border: 1px solid #e5e7ee;
      line-height: 1.6;
    }
    .btns {
      display: flex;
      justify-content: center;
      gap: 16px;
      margin-top: 24px;
    }
    .btn {
      padding: 10px 16px;
      border-radius: 8px;
      border: 1px solid #ccc;
      text-decoration: none;
      font-weight: bold;
      cursor: pointer;
    }
    .btn.cancel {
      background: #e5e7eb;
      color: #111827;
    }
    .btn.delete {
      background: #dc2626;
      color: white;
      border: none;
    }
    .notice {
      background: #fef2f2;
      color: #b91c1c;
      padding: 10px 14px;
      border: 1px solid #fecaca;
      border-radius: 8px;
      margin-top: 10px;
      text-align: center;
    }
  </style>
</head>
<body>
<div class="wrap">
  <h1>⚠️ Xóa chuyến đi</h1>

  <div class="info">
    <p><strong>Tên chuyến đi:</strong> <c:out value="${trip.name}"/></p>
    <p><strong>Địa điểm:</strong> <c:out value="${trip.location}"/></p>
    <p><strong>Ngày đi:</strong> <fmt:formatDate value="${trip.startDate}" pattern="dd/MM/yyyy"/></p>
    <p><strong>Ngày kết thúc:</strong> <fmt:formatDate value="${trip.endDate}" pattern="dd/MM/yyyy"/></p>
  </div>

  <div class="notice">
    Bạn có chắc chắn muốn xóa chuyến đi này không?<br>
    Hành động này <strong>không thể hoàn tác</strong>.
  </div>

  <form method="post" action="<c:url value='/trip/delete'/>">
    <input type="hidden" name="id" value="${trip.tripId}"/>
    <div class="btns">
      <a href="<c:url value='/mytrips'/>" class="btn cancel">← Quay lại</a>
      <button type="submit" class="btn delete">🗑️ Xác nhận xóa</button>
    </div>
  </form>
</div>
</body>
</html>
