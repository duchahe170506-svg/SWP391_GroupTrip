<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<jsp:include page="partials/header.jsp"/>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Sửa chuyến đi</title>
        <style>
            body {
                font-family: Arial, sans-serif !important;
                background: #f5f7fb !important;
                margin: 0 !important;
                padding: 0 !important;
                min-height: 100vh !important;
                display: flex !important;
                flex-direction: column !important;
            }

            .wrap {
                flex: 1 !important;
                max-width: 900px !important;
                margin: 24px auto !important;
                padding: 0 16px !important;
            }

            .card {
                background: #fff !important;
                border: 1px solid #e5e7ee !important;
                border-radius: 12px !important;
                padding: 20px !important;
                box-shadow: 0 2px 6px rgba(0,0,0,0.05) !important;
            }

            h1 {
                margin-top: 0 !important;
                color: #111827 !important;
            }

            label {
                font-weight: 600 !important;
                display: block !important;
                margin-bottom: 6px !important;
                color: #111827 !important;
            }

            input[type="text"],
            input[type="number"],
            input[type="date"],
            textarea,
            select {
                width: 100% !important;
                padding: 10px !important;
                border: 1px solid #d0d3db !important;
                border-radius: 8px !important;
                box-sizing: border-box !important;
                margin-bottom: 12px !important;
                font-size: 14px !important;
                background: #fff !important;
                color: #111827 !important;
            }

            input[disabled], textarea[disabled], select[disabled] {
                background: #f3f4f6 !important;
                color: #6b7280 !important;
                cursor: not-allowed !important;
            }

            textarea {
                min-height: 100px !important;
                resize: vertical !important;
            }

            .btn {
                padding: 10px 14px !important;
                border-radius: 8px !important;
                border: 1px solid #ccc !important;
                text-decoration: none !important;
                background: #eee !important;
                color: #333 !important;
                cursor: pointer !important;
                display: inline-block !important;
                font-size: 14px !important;
                transition: background 0.2s ease !important;
            }

            .btn:hover {
                background: #ddd !important;
            }

            .btn.primary {
                background: #77a096 !important;
                color: #fff !important;
                border: none !important;
            }

            .btn.primary:hover {
                background: #77a096 !important;
            }

            .btn.back {
                background: #e5e7eb !important;
                color: #333 !important;
                border: none !important;
            }

            .btn[disabled] {
                opacity: 0.6 !important;
                cursor: not-allowed !important;
            }

            .errors {
                background: #fff4f4 !important;
                border: 1px solid #f5c2c2 !important;
                color: #b42323 !important;
                padding: 10px !important;
                border-radius: 8px !important;
                margin-bottom: 14px !important;
            }

            .notice {
                background: #fefce8 !important;
                border: 1px solid #fde68a !important;
                color: #854d0e !important;
                padding: 10px !important;
                border-radius: 8px !important;
                margin-bottom: 14px !important;
            }

            .muted {
                color: #6b7280 !important;
                font-size: 12px !important;
                margin-top: -8px !important;
                margin-bottom: 12px !important;
                display: block !important;
            }

            .row {
                display: grid !important;
                grid-template-columns: 1fr 1fr !important;
                gap: 12px !important;
            }

            @media (max-width: 640px) {
                .row {
                    grid-template-columns: 1fr !important;
                }
            }

            footer {
                margin-top: auto !important;
                background: #fff !important;
                border-top: 1px solid #e5e7eb !important;
                padding: 12px 0 !important;
                text-align: center !important;
                color: #555 !important;
            }
        </style>

    </head>
    <body>
        <div class="wrap">
            <div class="card">
                <h1>✏️ Chỉnh sửa chuyến đi</h1>

                <!-- Lỗi từ server -->
                <c:if test="${not empty error}">
                    <div class="errors">
                        <strong>Lỗi:</strong> <c:out value="${error}"/>
                    </div>
                </c:if>

                <!-- Nếu thiếu attr (trường hợp hiếm), gán mặc định -->
                <c:if test="${empty participantCount}"><c:set var="participantCount" value="1"/></c:if>
                <c:if test="${empty diffDays}"><c:set var="diffDays" value="999"/></c:if>
                <c:if test="${empty allowNameDescImageStatus}"><c:set var="allowNameDescImageStatus" value="true"/></c:if>
                <c:if test="${empty allowEditDate}"><c:set var="allowEditDate" value="true"/></c:if>
                <c:if test="${empty allowEditLocation}"><c:set var="allowEditLocation" value="true"/></c:if>
                <c:if test="${empty allowEditBudget}"><c:set var="allowEditBudget" value="true"/></c:if>
                <c:if test="${empty budgetLimit10}"><c:set var="budgetLimit10" value="false"/></c:if>
                <c:if test="${empty allowSubmit}"><c:set var="allowSubmit" value="true"/></c:if>

                    <!-- Banner -->
                <c:choose>
                    <c:when test="${diffDays < 0}">
                        <div class="notice">⛔ Chuyến đi đã qua ngày khởi hành — <strong>không sửa được gì</strong>.</div>
                    </c:when>
                    <c:when test="${diffDays <= 3 && participantCount >= 2}">
                        <div class="notice">🚫 Còn ≤ 3 ngày — <strong>không được sửa ngày, ngân sách, và địa điểm</strong>. Bạn vẫn có thể cập nhật tên, mô tả, ảnh, trạng thái.</div>
                    </c:when>
                    <c:when test="${participantCount >= 2 && diffDays >= 15}">
                        <div class="notice">⚠️ Đã có người tham gia và còn ≥ 15 ngày — cho phép đổi <strong>tên, mô tả, ảnh</strong> và <strong>ngân sách nhẹ (&lt;10%)</strong>. <strong>Không</strong> được đổi ngày hoặc địa điểm.</div>
                    </c:when>
                    <c:when test="${participantCount >= 2 && diffDays >= 4 && diffDays <= 14}">
                        <div class="notice">⚠️ Đã có người tham gia (còn 4–14 ngày) — chỉ được đổi <strong>tên, mô tả, ảnh, trạng thái</strong>. <strong>Không</strong> được đổi ngày, địa điểm, ngân sách.</div>
                    </c:when>
                    <c:otherwise>
                        <div class="notice">✅ Chỉ có Leader — bạn có thể chỉnh <strong>mọi trường</strong>.</div>
                    </c:otherwise>
                </c:choose>

                <form method="post" action="<c:url value='/trip/edit'/>">
                    <input type="hidden" name="tripId" value="${trip.tripId}"/>

                    <!-- Tên -->
                    <label>Tên chuyến đi</label>
                    <c:choose>
                        <c:when test="${not allowNameDescImageStatus}">
                            <input type="text" value="${trip.name}" disabled/>
                            <input type="hidden" name="name" value="${trip.name}"/>
                        </c:when>
                        <c:otherwise>
                            <input type="text" name="name" value="${trip.name}" required/>
                        </c:otherwise>
                    </c:choose>

                    <!-- Địa điểm -->
                    <label>Địa điểm</label>
                    <c:choose>
                        <c:when test="${not allowEditLocation}">
                            <input type="text" value="${trip.location}" disabled/>
                            <input type="hidden" name="location" value="${trip.location}"/>
                        </c:when>
                        <c:otherwise>
                            <input type="text" name="location" value="${trip.location}" required/>
                        </c:otherwise>
                    </c:choose>

                    <!-- 🟩 Địa điểm tập trung -->
                    <label>📍 Địa điểm tập trung</label>
                    <input type="text" name="meeting_point" value="${trip.meeting_point}" 
                           placeholder="VD: Cổng trường Đại học FPT Cần Thơ" />

                    <div class="row">
                        <!-- Ngày đi -->
                        <div>
                            <label>Ngày đi</label>
                            <c:choose>
                                <c:when test="${not allowEditDate}">
                                    <input type="date" value="<fmt:formatDate value='${trip.startDate}' pattern='yyyy-MM-dd'/>" disabled/>
                                    <input type="hidden" name="startDate" value="<fmt:formatDate value='${trip.startDate}' pattern='yyyy-MM-dd'/>"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="date" name="startDate" value="<fmt:formatDate value='${trip.startDate}' pattern='yyyy-MM-dd'/>"/>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- Ngày kết thúc -->
                        <div>
                            <label>Ngày kết thúc</label>
                            <c:choose>
                                <c:when test="${not allowEditDate}">
                                    <input type="date" value="<fmt:formatDate value='${trip.endDate}' pattern='yyyy-MM-dd'/>" disabled/>
                                    <input type="hidden" name="endDate" value="<fmt:formatDate value='${trip.endDate}' pattern='yyyy-MM-dd'/>"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="date" name="endDate" value="<fmt:formatDate value='${trip.endDate}' pattern='yyyy-MM-dd'/>"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Ngân sách -->
                    <label>Ngân sách (₫)</label>
                    <c:choose>
                        <c:when test="${not allowEditBudget}">
                            <input type="number" step="1000" value="<c:out value='${trip.budget}'/>" disabled/>
                            <input type="hidden" name="budget" value="<c:out value='${trip.budget}'/>"/>
                        </c:when>
                        <c:otherwise>
                            <input type="number" name="budget" step="1000" value="<c:out value='${trip.budget}'/>"/>
                            <c:if test="${budgetLimit10}">
                                <span class="muted">Chỉ điều chỉnh <strong>&lt;10%</strong> so với hiện tại để được chấp nhận.</span>
                            </c:if>
                        </c:otherwise>
                    </c:choose>

                    <!-- Ảnh bìa -->
                    <label>Ảnh bìa (URL)</label>
                    <c:choose>
                        <c:when test="${not allowNameDescImageStatus}">
                            <input type="text" value="${trip.coverImage}" disabled/>
                            <input type="hidden" name="coverImage" value="${trip.coverImage}"/>
                        </c:when>
                        <c:otherwise>
                            <input type="text" name="coverImage" value="${trip.coverImage}"/>
                        </c:otherwise>
                    </c:choose>

                    <!-- 🟩 Số người tối thiểu -->
                    <label>👥 Số người tối thiểu</label>
                    <input type="number" name="min_participants" min="1" value="${trip.min_participants}" required/>

                    <!-- Trạng thái -->
                    <label>Trạng thái</label>
                    <c:choose>
                        <c:when test="${not allowNameDescImageStatus}">
                            <select disabled>
                                <option ${trip.status == 'Active' ? 'selected' : ''}>Active</option>
                                <option ${trip.status == 'Private' ? 'selected' : ''}>Private</option>
                            </select>
                            <input type="hidden" name="status" value="${trip.status}"/>
                        </c:when>
                        <c:otherwise>
                            <select name="status">
                                <option value="Active" ${trip.status == 'Active' ? 'selected' : ''}>Active</option>
                                <option value="Private" ${trip.status == 'Private' ? 'selected' : ''}>Private</option>
                            </select>
                        </c:otherwise>
                    </c:choose>

                    <!-- Mô tả -->
                    <label>Mô tả</label>
                    <c:choose>
                        <c:when test="${not allowNameDescImageStatus}">
                            <textarea disabled>${trip.description}</textarea>
                            <input type="hidden" name="description" value="${trip.description}"/>
                        </c:when>
                        <c:otherwise>
                            <textarea name="description">${trip.description}</textarea>
                        </c:otherwise>
                    </c:choose>

                    <div style="margin-top:16px;">
                        <button type="submit" class="btn primary" <c:if test="${not allowSubmit}">disabled</c:if>>💾 Lưu thay đổi</button>
                        <a class="btn back" href="<c:url value='/mytrips'/>">← Quay lại chuyến đi của tôi</a>
                    </div>
                </form>
            </div>
        </div>
    </body>
     <jsp:include page="partials/footer.jsp"/>
</html>
