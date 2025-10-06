<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>My Profile</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f4f4; }
        .profile-card {
            max-width: 600px; margin: 50px auto; background: #fff;
            border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            padding: 20px;
        }
        .form-group { margin-bottom: 15px; }
        label { font-weight: bold; display: block; margin-bottom: 5px; }
        input, select, textarea {
            width: 100%; padding: 8px; border-radius: 5px;
            border: 1px solid #ccc; box-sizing: border-box;
        }
        .avatar {
            width: 100px; height: 100px; border-radius: 50%;
            object-fit: cover; display: block; margin: 10px auto;
        }
        button {
            background-color: #007bff; color: white; border: none;
            padding: 10px 16px; border-radius: 5px; cursor: pointer;
        }
        button:hover { background-color: #0056b3; }
    </style>
</head>
<body>

<div class="profile-card">
    <h2 style="text-align:center;">My Profile</h2>

    <c:if test="${not empty message}">
        <div style="color:green; text-align:center;">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div style="color:red; text-align:center;">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/profile">
        <div style="text-align:center;">
            <img src="${user.avatar != null ? user.avatar : 'images/default-avatar.png'}" class="avatar" alt="Avatar">
        </div>

        <div class="form-group">
            <label>Họ và tên</label>
            <input type="text" name="name" value="${user.name}" required/>
        </div>

        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" value="${user.email}" readonly/>
        </div>

        <div class="form-group">
            <label>Số điện thoại</label>
            <input type="text" name="phone" value="${user.phone}"/>
        </div>

        <div class="form-group">
            <label>Ngày sinh</label>
            <input type="date" name="date_of_birth" value="${user.date_of_birth}"/>
        </div>

        <div class="form-group">
            <label>Giới tính</label>
            <select name="gender">
                <option value="Male" ${user.gender == 'Male' ? 'selected' : ''}>Nam</option>
                <option value="Female" ${user.gender == 'Female' ? 'selected' : ''}>Nữ</option>
                <option value="Other" ${user.gender == 'Other' ? 'selected' : ''}>Khác</option>
            </select>
        </div>

        <div class="form-group">
            <label>Địa chỉ</label>
            <textarea name="address" rows="3">${user.address}</textarea>
        </div>

        <div style="text-align:center;">
            <button type="submit">Cập nhật thông tin</button>
        </div>
    </form>
</div>
</body>
</html>
