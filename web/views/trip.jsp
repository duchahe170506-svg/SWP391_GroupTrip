<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN" />

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8"/>
        <title>List Trip</title>
        <style>
            body{
                font-family: Arial, sans-serif;
                background:#f5f7fb;
                margin:0
            }
            .topbar{
                background:#e8ebf2;
                padding:14px 20px;
                display:flex;
                gap:12px;
                align-items:center
            }
            .topbar .navbtn{
                background:white;
                border:1px solid #d0d3db;
                border-radius:10px;
                padding:8px 16px;
                text-decoration:none;
                color:#333
            }
            .container{
                display:grid;
                grid-template-columns:280px 1fr;
                gap:24px;
                max-width:1100px;
                margin:24px auto
            }
            .card{
                background:white;
                border:1px solid #e5e7ee;
                border-radius:12px;
                padding:16px;
                margin-bottom:16px;
                overflow:hidden
            }

            .sidebar .card form{
                display:flex;
                flex-direction:column;
                gap:10px;
            }
            .sidebar .card label{
                font-weight:600;
            }
            .sidebar .card input,
            .sidebar .card select,
            .sidebar .card button,
            .sidebar .card a.btn{
                width:100%;
                display:block;
                padding:10px;
                border:1px solid #d0d3db;
                border-radius:8px;
                text-align:left;
                text-decoration:none;
                color:#333;
                background:#fff;
            }
            .sidebar .card button{
                background:#3b82f6;
                color:#fff;
                border:none;
                text-align:center;
            }
            .sidebar .card a.btn{
                text-align:center;
                margin-top:6px;
                background:#16a34a;
                color:#fff;
                border:none;
            }
            .sidebar .card h3{
                text-align:center;
            }

            .trip{
                display:grid;
                grid-template-columns:160px 1fr;
                gap:16px
            }
            .thumb{
                width:160px;
                height:120px;
                background:#e9ecf4;
                border-radius:10px;
                object-fit:cover
            }
            .title{
                font-weight:700;
                font-size:18px;
                margin:0 0 8px
            }
            .title span.location{
                font-weight:normal;
                font-size:15px;
                color:#555;
                margin-left:6px;
            }
            .pill{
                display:inline-block;
                background:#f0f3f9;
                border:1px solid #dfe4ef;
                border-radius:8px;
                padding:6px 10px;
                margin:4px 8px 4px 0
            }
            .actions{
                display:flex;
                gap:8px;
                margin-top:10px
            }
            .btn{
                padding:10px 12px;
                border-radius:8px;
                border:1px solid #d0d3db;
                background:#fff;
                cursor:pointer;
                text-decoration:none;
                color:#333;
                display:inline-block
            }
            .btn.primary{
                background:#3b82f6;
                color:#fff;
                border:none
            }
            .empty{
                padding:24px;
                color:#666
            }
        </style>
    </head>
    <body>

        <!-- Thanh top -->
        <div class="topbar">
            <strong>List Trip</strong>
            <a class="navbtn" href="<c:url value='/'/>">Home</a>
            <a class="navbtn" href="<c:url value='/trips'/>">Trip</a>
            <a class="navbtn" href="#">About Us</a>
            <div style="margin-left:auto" class="navbtn">Welcome, User ▾</div>
        </div>

        <div class="container">
            <!-- Sidebar filter -->
            <div class="sidebar">
                <div class="card">
                    <h3>Find trip for you</h3>

                    <form method="get" action="<c:url value='/trips'/>">
                        <label>Budget</label>
                        <select name="budget">
                            <option value="" <c:if test="${empty budget}">selected</c:if>>-- Select budget --</option>
                            <option value="0-3000000" <c:if test="${budget=='0-3000000'}">selected</c:if>>Dưới 3.000.000</option>
                            <option value="3000000-7000000" <c:if test="${budget=='3000000-7000000'}">selected</c:if>>3–7 triệu</option>
                            <option value="7000000+" <c:if test="${budget=='7000000+'}">selected</c:if>>Trên 7 triệu</option>
                        </select>

                        <label>Loại chuyến đi</label>
                        <select name="tripType">
                            <option value="" <c:if test="${empty tripType}">selected</c:if>>-- Chọn loại --</option>
                            <option value="Du lịch nghỉ dưỡng" <c:if test="${tripType=='Du lịch nghỉ dưỡng'}">selected</c:if>>Du lịch nghỉ dưỡng</option>
                            <option value="Dã ngoại / Team Building" <c:if test="${tripType=='Dã ngoại / Team Building'}">selected</c:if>>Dã ngoại / Team Building</option>
                            <option value="Khám phá / Adventure" <c:if test="${tripType=='Khám phá / Adventure'}">selected</c:if>>Khám phá / Adventure</option>
                            <option value="Du lịch văn hoá – lịch sử" <c:if test="${tripType=='Du lịch văn hoá – lịch sử'}">selected</c:if>>Du lịch văn hoá – lịch sử</option>
                            <option value="Healing / Sức khỏe & Tâm linh" <c:if test="${tripType=='Healing / Sức khỏe & Tâm linh'}">selected</c:if>>Healing / Sức khỏe & Tâm linh</option>
                            <option value="Foodtour" <c:if test="${tripType=='Foodtour'}">selected</c:if>>Foodtour</option>
                        </select>

                        <label>Departure date</label>
                        <input type="date" name="departOn" value="${departOn}"/>

                        <label>Từ ngày</label>
                        <input type="date" name="from" value="${from}"/>

                        <label>Đến ngày</label>
                        <input type="date" name="to" value="${to}"/>

                        <button type="submit">Find tour</button>
                    </form>

                    <a class="btn" href="<c:url value='/trip/create'/>">+ Create Trip</a>
                </div>
            </div>

            <!-- Trip list -->
            <div>
                <c:choose>
                    <c:when test="${empty trips}">
                        <div class="card empty">Không tìm thấy chuyến đi phù hợp.</div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="t" items="${trips}">
                            <div class="card trip">
                                <c:choose>
                                    <c:when test="${not empty t.coverImage}">
                                        <img class="thumb" src="${t.coverImage}" alt="<c:out value='${t.name}'/>"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img class="thumb" src="https://via.placeholder.com/160x120?text=Trip" alt="no-image"/>
                                    </c:otherwise>
                                </c:choose>

                                <div>
                                    <div class="title">
                                        <c:out value="${t.name}"/>
                                        <span class="location">(<c:out value="${t.location}"/>)</span>
                                    </div>

                                    <div class="pill">📍 Điểm tập trung: 
                                        <c:out value="${t.meeting_point != null ? t.meeting_point : 'Chưa cập nhật'}"/>
                                    </div>

                                    <div class="pill">Ngày đi: <fmt:formatDate value="${t.startDate}" pattern="dd/MM/yyyy"/></div>
                                    <div class="pill">Ngày kết thúc: <fmt:formatDate value="${t.endDate}" pattern="dd/MM/yyyy"/></div>

                                    <div class="pill">
                                        Đã tham gia:
                                        <c:out value="${participantMap[t.tripId]}"/>
                                        <c:choose>
                                            <c:when test="${t.max_participants gt 0}">
                                                / <c:out value="${t.max_participants}"/>
                                            </c:when>
                                            <c:otherwise>
                                                / Không giới hạn
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <div class="pill">Chi phí dự kiến: 
                                        <fmt:formatNumber value="${t.budget}" type="currency" currencySymbol="₫"/> / 1 người
                                    </div>
                                    <div class="pill">Loại: <c:out value="${t.tripType}"/></div>

                                    <div class="actions">
                                        <c:url var="detailUrl" value="/trip/detail">
                                            <c:param name="id" value="${t.tripId}"/>
                                        </c:url>
                                        <c:url var="joinUrl" value="/trip/join">
                                            <c:param name="id" value="${t.tripId}"/>
                                        </c:url>
                                        <a class="btn" href="${detailUrl}">Xem chi tiết</a>
                                        <a class="btn primary" href="${joinUrl}">Tham gia chuyến đi</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </body>
</html>
