<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Create Trip</title>
    <style>
        body{font-family:Arial,sans-serif;background:#f5f7fb;margin:0}
        .wrap{max-width:900px;margin:24px auto;padding:0 16px}
        .card{background:#fff;border:1px solid #e5e7ee;border-radius:12px;padding:20px}
        h1{margin:0 0 16px}
        form{display:grid;grid-template-columns:1fr 1fr;gap:14px}
        .full{grid-column:1 / -1}
        label{font-weight:600;margin-bottom:6px;display:block}
        input[type="text"], input[type="number"], input[type="date"], textarea, select{
            width:100%; padding:10px; border:1px solid #d0d3db; border-radius:8px; box-sizing:border-box; background:#fff;
        }
        textarea{min-height:110px; resize:vertical}
        .actions{display:flex;gap:10px;margin-top:10px}
        .btn{padding:10px 14px;border-radius:8px;border:1px solid #d0d3db;background:#fff;text-decoration:none;color:#333;display:inline-block;text-align:center}
        .btn.primary{background:#3b82f6;color:#fff;border:none}
        .errors{background:#fff4f4;border:1px solid #f5c2c2;color:#b42323;padding:12px;border-radius:8px;margin-bottom:14px}
        .hint{color:#6b7280;font-size:12px;margin-top:4px}
    </style>
</head>
<body>

<div class="wrap">
    <div class="card">
        <h1>Tạo chuyến đi</h1>

        <!-- Hiển thị lỗi -->
        <c:if test="${not empty errors}">
            <div class="errors">
                <ul style="margin:0 0 0 18px;">
                    <c:forEach var="e" items="${errors}">
                        <li><c:out value="${e}"/></li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <form method="post" action="<c:url value='/trip/create'/>">
            <!-- Tên chuyến đi -->
            <div>
                <label for="name">Tên chuyến đi</label>
                <input type="text" id="name" name="name" required
                       value="<c:out value='${form_name}'/>" placeholder="Đà Nẵng - Hội An - Huế"/>
            </div>

            <!-- Địa điểm -->
            <div>
                <label for="location">Địa điểm</label>
                <input type="text" id="location" name="location" required
                       value="<c:out value='${form_location}'/>" placeholder="Đà Nẵng, Quảng Nam, Thừa Thiên Huế"/>
            </div>

            <!-- 🟩 Địa điểm tập trung -->
            <div>
                <label for="meeting_point">📍 Địa điểm tập trung</label>
                <input type="text" id="meeting_point" name="meeting_point"
                       value="<c:out value='${form_meeting_point}'/>" placeholder="VD: Cổng trường Đại học FPT Cần Thơ"/>
            </div>

            <!-- 🟩 Số người tối thiểu -->
            <div>
                <label for="min_participants">👥 Số người tối thiểu</label>
                <input type="number" id="min_participants" name="min_participants" min="1"
                       value="<c:out value='${form_minParticipants}'/>" placeholder="VD: 5"/>
            </div>

            <!-- Ngày đi -->
            <div>
                <label for="startDate">Ngày đi <span style="color:red;">*</span></label>
                <input type="date" id="startDate" name="startDate" required
                       value="<c:out value='${form_startDate}'/>"/>
                <div class="hint">Ngày đi phải từ hôm nay trở đi</div>
            </div>

            <!-- Ngày kết thúc -->
            <div>
                <label for="endDate">Ngày kết thúc <span style="color:red;">*</span></label>
                <input type="date" id="endDate" name="endDate" required
                       value="<c:out value='${form_endDate}'/>"/>
                <div class="hint">Ngày kết thúc phải sau hoặc bằng ngày đi</div>
            </div>

            <!-- Ngân sách -->
            <div>
                <label for="budget">Ngân sách (₫/người)</label>
                <input type="number" id="budget" name="budget" min="0" step="1000"
                       value="<c:out value='${form_budget}'/>" placeholder="VD: 3500000"/>
            </div>

            <!-- Ảnh cover -->
            <div>
                <label for="coverImage">Ảnh bìa (URL)</label>
                <input type="text" id="coverImage" name="coverImage"
                       value="<c:out value='${form_coverImage}'/>" placeholder="https://.../cover.jpg"/>
            </div>

            <!-- Số người tối đa -->
            <div>
                <label for="maxParticipants">Số người tham gia tối đa</label>
                <input type="number" id="maxParticipants" name="maxParticipants" min="1"
                       value="<c:out value='${form_maxParticipants}'/>" placeholder="VD: 10"/>
            </div>

            <!-- Ảnh phụ -->
            <div class="full">
                <label>Ảnh chi tiết (tối đa 3 URL)</label>
                <input type="text" name="image1" placeholder="https://.../img1.jpg" value="${form_image1}"/>
                <input type="text" name="image2" placeholder="https://.../img2.jpg" value="${form_image2}"/>
                <input type="text" name="image3" placeholder="https://.../img3.jpg" value="${form_image3}"/>
            </div>

            <!-- Loại chuyến đi -->
            <div>
                <label for="tripType">Loại chuyến đi</label>
                <select id="tripType" name="tripType" required>
                    <c:set var="sel" value="${form_tripType}"/>
                    <option value="" ${empty sel ? 'selected' : ''}>-- Chọn loại --</option>
                    <option value="Du lịch nghỉ dưỡng" ${sel=='Du lịch nghỉ dưỡng' ? 'selected' : ''}>Du lịch nghỉ dưỡng</option>
                    <option value="Dã ngoại / Team Building" ${sel=='Dã ngoại / Team Building' ? 'selected' : ''}>Dã ngoại / Team Building</option>
                    <option value="Khám phá / Adventure" ${sel=='Khám phá / Adventure' ? 'selected' : ''}>Khám phá / Adventure</option>
                    <option value="Du lịch văn hoá – lịch sử" ${sel=='Du lịch văn hoá – lịch sử' ? 'selected' : ''}>Du lịch văn hoá – lịch sử</option>
                    <option value="Healing / Sức khỏe & Tâm linh" ${sel=='Healing / Sức khỏe & Tâm linh' ? 'selected' : ''}>Healing / Sức khỏe & Tâm linh</option>
                    <option value="Foodtour" ${sel=='Foodtour' ? 'selected' : ''}>Foodtour</option>
                </select>
            </div>

            <!-- Trạng thái -->
            <div>
                <label for="status">Trạng thái</label>
                <select id="status" name="status">
                    <c:set var="s" value="${empty form_status ? 'Active' : form_status}"/>
                    <option value="Active"  ${s=='Active' ? 'selected' : ''}>Active</option>
                    <option value="Private" ${s=='Private' ? 'selected' : ''}>Private</option>
                </select>
            </div>

            <!-- Mô tả -->
            <div class="full">
                <label for="description">Mô tả</label>
                <textarea id="description" name="description" placeholder="Mô tả lịch trình, điểm đến, lưu ý..."><c:out value="${form_description}"/></textarea>
            </div>

            <!-- Actions -->
            <div class="full actions">
                <button class="btn primary" type="submit">Tạo chuyến đi</button>
                <a class="btn" href="<c:url value='/trips'/>">Hủy / Quay lại</a>
            </div>
        </form>
    </div>
</div>

<script>
    // Validation phía client
    const start = document.getElementById('startDate');
    const end = document.getElementById('endDate');
    
    // Set minimum date = today cho startDate
    const today = new Date();
    const yyyy = today.getFullYear();
    const mm = String(today.getMonth() + 1).padStart(2, '0');
    const dd = String(today.getDate()).padStart(2, '0');
    const todayStr = yyyy + '-' + mm + '-' + dd;
    
    start.setAttribute('min', todayStr);
    
    // Khi startDate thay đổi, cập nhật min của endDate
    function syncMin() { 
        if (start.value) {
            end.min = start.value;
        } else {
            end.min = todayStr;
        }
    }
    
    start.addEventListener('change', syncMin);
    syncMin(); // Gọi ngay khi load trang
    
    // Validate trước khi submit
    document.querySelector('form').addEventListener('submit', function(e) {
        const startVal = start.value;
        const endVal = end.value;
        
        if (!startVal) {
            e.preventDefault();
            alert('Vui lòng chọn ngày đi!');
            start.focus();
            return false;
        }
        
        if (!endVal) {
            e.preventDefault();
            alert('Vui lòng chọn ngày kết thúc!');
            end.focus();
            return false;
        }
        
        // Kiểm tra startDate >= today
        const startDate = new Date(startVal);
        const todayDate = new Date(todayStr);
        if (startDate < todayDate) {
            e.preventDefault();
            alert('Ngày đi phải từ hôm nay trở đi!');
            start.focus();
            return false;
        }
        
        // Kiểm tra endDate >= startDate
        const endDate = new Date(endVal);
        if (endDate < startDate) {
            e.preventDefault();
            alert('Ngày kết thúc phải sau hoặc bằng ngày đi!');
            end.focus();
            return false;
        }
        
        return true;
    });
</script>

</body>
</html>
