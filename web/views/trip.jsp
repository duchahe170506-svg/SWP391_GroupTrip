<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN" />
<jsp:include page="/views/partials/header.jsp"/>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8"/>
        <title>List Trip</title>

        <style>
            * {
                box-sizing: border-box;
            }
            body {
                font-family: "Inter", Arial, sans-serif;
                background: #f4f6fa;
                margin: 0;
                color: #333;
            }
            a {
                text-decoration: none;
                color: inherit;
            }

            /* SIDEBAR */
            .sidebar {
                max-width: 280px;
                margin: 24px;
                float: left;
            }
            .sidebar .card {
                background: #fff;
                border: 1px solid #e0e3eb;
                border-radius: 12px;
                padding: 20px;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
                transition: transform 0.2s ease, box-shadow 0.2s ease;
            }
            .sidebar .card:hover {
                transform: translateY(-2px);
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.07);
            }
            .sidebar .card h3 {
                text-align: center;
                font-size: 18px;
                font-weight: 700;
                margin-bottom: 16px;
                color: #1e3a8a;
            }
            .sidebar .card form {
                display: flex;
                flex-direction: column;
                gap: 12px;
            }
            .sidebar .card label {
                font-weight: 600;
                color: #444;
                font-size: 14px;
            }
            .sidebar .card input,
            .sidebar .card select {
                width: 100%;
                padding: 10px 12px;
                border: 1px solid #cfd4e0;
                border-radius: 8px;
                background: #fff;
                font-size: 14px;
                transition: border-color 0.2s ease;
            }
            .sidebar .card input:focus,
            .sidebar .card select:focus {
                outline: none;
                border-color: #3b82f6;
            }

            /* Nút chính */
            .sidebar .card button {
                margin-top: 10px;
                background: #3b82f6 !important;
                color: #fff !important;
                border: none !important;
                border-radius: 8px !important;
                padding: 10px !important;
                font-weight: 600 !important;
                cursor: pointer !important;
                transition: background 0.2s ease !important;
            }
            .sidebar .card button:hover {
                background: #2563eb !important;
            }

            /* Nút Create Trip */
            .sidebar .card a.btn {
                margin-top: 6px;
                display: block;
                text-align: center;
                background: #16a34a !important;
                color: #fff !important;
                padding: 10px !important;
                border-radius: 8px !important;
                font-weight: 600 !important;
                transition: background 0.2s ease !important;
            }
            .sidebar .card a.btn:hover {
                background: #15803d !important;
            }

            /* TRIP LIST */
            .trip-list {
                margin-left: 340px;
                padding: 24px;
            }
            .card.trip {
                background: #fff;
                border: 1px solid #e2e5ec;
                border-radius: 12px;
                padding: 18px;
                display: grid;
                grid-template-columns: 180px 1fr;
                gap: 20px;
                align-items: start;
                margin-bottom: 20px;
                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
                transition: transform 0.2s ease, box-shadow 0.2s ease;
            }
            .card.trip:hover {
                transform: translateY(-4px);
                box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
            }
            .thumb {
                width: 180px;
                height: 130px;
                border-radius: 10px;
                object-fit: cover;
                border: 1px solid #e0e3eb;
                background-color: #eef1f8;
            }
            .title {
                font-weight: 700;
                font-size: 18px;
                color: #1e293b;
                margin-bottom: 6px;
            }
            .title .location {
                font-weight: 500;
                font-size: 14px;
                color: #64748b;
                margin-left: 6px;
            }
            .pill {
                display: inline-block;
                background: #f1f5f9;
                border-radius: 6px;
                padding: 6px 10px;
                margin: 4px 6px 4px 0;
                font-size: 13px;
                color: #334155;
            }

            /* Nút hành động */
            .actions {
                display: flex;
                flex-wrap: wrap;
                gap: 8px;
                margin-top: 12px;
            }
            .btn {
                display: inline-block;
                padding: 8px 14px !important;
                border-radius: 8px !important;
                border: 1px solid #d1d5db !important;
                background: #fff !important;
                cursor: pointer !important;
                text-align: center !important;
                font-weight: 600 !important;
                font-size: 14px !important;
                color: #374151 !important;
                transition: background 0.2s ease, border-color 0.2s ease !important;
            }
            .btn:hover {
                background: #f3f4f6 !important;
                border-color: #9ca3af !important;
            }
            .btn.primary {
                background: #77a096 !important;
                border: none !important;
                color: #fff !important;
            }
            .btn.primary:hover {
                background: #5f857d !important;
            }

            .empty {
                padding: 30px;
                background: #fff;
                border-radius: 12px;
                text-align: center;
                color: #6b7280;
                font-size: 15px;
                border: 1px dashed #d1d5db;
            }

            /* RESPONSIVE */
            @media (max-width: 900px) {
                .sidebar {
                    float: none;
                    max-width: 100%;
                    margin: 16px;
                }
                .trip-list {
                    margin-left: 0;
                    padding: 16px;
                }
                .card.trip {
                    grid-template-columns: 1fr;
                }
                .thumb {
                    width: 100%;
                    height: 180px;
                }
            }
        </style>
    </head>

    <body>
        <main class="clearfix">
            <c:if test="${not empty message}">
                <div style="
                     padding: 12px 16px;
                     margin: 0 24px 16px;
                     border-radius: 8px;
                     background-color: #fef3c7;
                     color: #92400e;
                     font-weight: 600;
                     border: 1px solid #fcd34d;
                     ">
                    ${message}
                </div>
            </c:if>

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

            <div class="trip-list">
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
                                        Đã tham gia: <c:out value="${participantMap[t.tripId]}"/>
                                        <c:choose>
                                            <c:when test="${t.max_participants gt 0}">
                                                / <c:out value="${t.max_participants}"/>
                                            </c:when>
                                            <c:otherwise>/ Không giới hạn</c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="pill">
                                        Chi phí dự kiến:
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
                                        <c:url var="cancelUrl" value="/trip/join">
                                            <c:param name="id" value="${t.tripId}"/>
                                            <c:param name="action" value="cancel"/>
                                        </c:url>
                                        <a class="btn" href="${detailUrl}">Xem chi tiết</a>
                                        <a class="btn primary" href="${joinUrl}">Tham gia chuyến đi</a>
                                        <c:if test="${userRequestMap[t.tripId]}">
                                            <a class="btn " href="${cancelUrl}">Hủy yêu cầu</a>
                                        </c:if>

                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>
    </body>
    <jsp:include page="/views/partials/footer.jsp"/>
</html>
