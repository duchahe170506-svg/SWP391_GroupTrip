<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Tạo lịch trình</title>
<style>
body{font-family:Arial;background:#f5f7fb;margin:0;padding:20px;}
.card{background:#fff;padding:20px;border-radius:12px;border:1px solid #ddd;max-width:900px;margin:auto;}
h1{margin-top:0}
label{display:block;margin-top:8px;font-weight:600}
input,textarea{width:100%;padding:8px;margin-top:4px;border:1px solid #ccc;border-radius:6px}
button{margin-top:16px;background:#3b82f6;color:#fff;padding:10px 20px;border:none;border-radius:8px;cursor:pointer}
.error{background:#fff3f3;border:1px solid #e58;color:#c00;padding:10px;margin-bottom:12px;border-radius:8px}
.day-block{border:1px solid #e0e0e0;padding:12px;border-radius:8px;margin-top:10px;background:#fafbff}
.activity-item{border:1px dashed #ccc;padding:10px;margin-top:10px;border-radius:6px;background:#fff;position:relative}
.addBtn{background:#16a34a;color:#fff;padding:6px 10px;border:none;border-radius:6px;margin-top:6px;cursor:pointer}
.removeBtn{background:#dc2626;color:#fff;border:none;padding:5px 10px;border-radius:6px;cursor:pointer;position:absolute;top:10px;right:10px}
</style>
</head>
<body>
<div class="card">
  <h1>Lịch trình cho chuyến đi: <c:out value="${trip.name}"/></h1>
  <p>
    <strong>Ngày đi:</strong> <fmt:formatDate value="${trip.startDate}" pattern="dd/MM/yyyy"/> &nbsp;&nbsp;
    <strong>Ngày kết thúc:</strong> <fmt:formatDate value="${trip.endDate}" pattern="dd/MM/yyyy"/> <br>
    <strong>Tổng số ngày:</strong> <c:out value="${dayCount}"/>
  </p>

  <c:if test="${not empty errors}">
    <div class="error">
      <ul>
        <c:forEach var="e" items="${errors}">
          <li>${e}</li>
        </c:forEach>
      </ul>
    </div>
  </c:if>

  <form method="post">
    <input type="hidden" name="tripId" value="${trip.tripId}"/>
    <input type="hidden" name="dayCount" value="${dayCount}"/>

    <c:forEach var="i" begin="1" end="${dayCount}">
      <div class="day-block" id="day-${i}">
        <h3>Ngày ${i}</h3>
        <div id="activities-${i}" class="activity-container" data-next-index="2">
          <div class="activity-item">
            <button type="button" class="removeBtn" onclick="removeActivity(this)">🗑</button>
            <label>Tiêu đề</label>
            <input type="text" name="title${i}_1">
            <label>Giờ bắt đầu</label>
            <input type="time" name="start${i}_1">
            <label>Giờ kết thúc</label>
            <input type="time" name="end${i}_1">
            <label>Mô tả</label>
            <textarea name="desc${i}_1"></textarea>
          </div>
        </div>
        <button type="button" class="addBtn" data-day="${i}">➕ Thêm hoạt động</button>
      </div>
    </c:forEach>

    <button type="submit">💾 Lưu toàn bộ lịch trình</button>
  </form>
</div>

<script>
document.querySelectorAll(".addBtn").forEach(btn => {
  btn.addEventListener("click", () => {
    const day = btn.dataset.day;
    const container = document.getElementById("activities-" + day);

    // 🔑 Lấy chỉ số kế tiếp (an toàn, không bị trùng)
    let idx = parseInt(container.dataset.nextIndex || "2", 10);

    container.insertAdjacentHTML("beforeend", `
      <div class="activity-item">
        <button type="button" class="removeBtn" onclick="removeActivity(this)">🗑</button>
        <label>Tiêu đề</label>
        <input type="text" name="title${day}_${idx}">
        <label>Giờ bắt đầu</label>
        <input type="time" name="start${day}_${idx}">
        <label>Giờ kết thúc</label>
        <input type="time" name="end${day}_${idx}">
        <label>Mô tả</label>
        <textarea name="desc${day}_${idx}"></textarea>
      </div>
    `);

    container.dataset.nextIndex = String(idx + 1);
  });
});

function removeActivity(btn){
  btn.closest(".activity-item").remove();
}
</script>
</body>
</html>
