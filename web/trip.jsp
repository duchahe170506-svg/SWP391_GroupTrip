<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>List Trip</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        header { background: #f5f5f5; padding: 10px; display: flex; align-items: center; justify-content: space-between; }
        header h2 { margin: 0; }
        nav a { margin: 0 10px; text-decoration: none; color: #333; font-weight: bold; }
        .container { display: flex; padding: 20px; }
        .sidebar { width: 250px; border-right: 1px solid #ddd; padding-right: 20px; }
        .content { flex: 1; padding-left: 20px; }
        .trip-card { border: 1px solid #ccc; border-radius: 5px; padding: 15px; margin-bottom: 15px; display: flex; }
        .trip-img { width: 120px; height: 120px; background: #eee; margin-right: 15px; display:flex;align-items:center;justify-content:center; }
        .trip-info { flex: 1; }
        .trip-info h3 { margin: 0 0 10px; }
        .trip-detail { margin: 5px 0; }
        .trip-actions button { margin-right: 10px; padding: 5px 10px; }
    </style>
</head>
<body>
<header>
    <h2>List Trip</h2>
    <nav>
        <a href="${pageContext.request.contextPath}/home">Home</a>
        <a href="${pageContext.request.contextPath}/trip">Trip</a>
        <a href="${pageContext.request.contextPath}/users">About Us</a>
    </nav>
    <div>Welcome, User ▼</div>
</header>

<div class="container">
    <!-- Sidebar filter -->
    <div class="sidebar">
        <h3>Find trip for you</h3>
        <form action="${pageContext.request.contextPath}/trip" method="get">
            <input type="hidden" name="action" value="filter"/>
            <div>
                <label>Budget</label><br/>
                <select name="budget">
                    <option value="">Select budget</option>
                    <option value="1000000">&lt; 1 triệu</option>
                    <option value="5000000">1 - 5 triệu</option>
                    <option value="10000000">5 - 10 triệu</option>
                    <option value="20000000">&gt; 10 triệu</option>
                </select>
            </div>
            <div>
                <label>Departure date</label><br/>
                From: <input type="date" name="from"/><br/>
                To: <input type="date" name="to"/>
            </div>
            <br/>
            <button type="submit">Find tour</button>
        </form>
    </div>

    <!-- Trip list -->
    <div class="content">
        <c:forEach var="t" items="${trips}">
            <div class="trip-card">
                <div class="trip-img">[Ảnh]</div>
                <div class="trip-info">
                    <h3>${t.name}</h3>
                    <div class="trip-detail">Ngày đi: ${t.startDate}</div>
                    <div class="trip-detail">Ngày kết thúc: ${t.endDate}</div>
                    <div class="trip-detail">Số người đã tham gia: (chưa có dữ liệu)</div>
                    <div class="trip-detail">Chi phí dự kiến: ${t.budget} VND/người</div>
                    <div class="trip-actions">
                        <button onclick="location.href='${pageContext.request.contextPath}/trip?action=detail&id=${t.tripId}'">Xem chi tiết</button>
                        <button onclick="location.href='${pageContext.request.contextPath}/trip?action=join&id=${t.tripId}'">Tham gia chuyến đi</button>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
