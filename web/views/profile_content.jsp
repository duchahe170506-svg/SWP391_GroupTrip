<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>My Profile</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background-color: #f4f6f9;
            margin: 0;
        }

        /* === LAYOUT === */
        .profile-page {
            display: flex;
            width: 100%;
            min-height: calc(100vh - 100px);
            background-color: #f8f9fa;
        }

        /* === SIDEBAR === */
        .sidebar {
            width: 260px;
            background-color: #ffffff;
            border-right: 1px solid #e0e0e0;
            padding: 25px 0;
            display: flex;
            flex-direction: column;
            align-items: stretch;
            box-shadow: 2px 0 8px rgba(0,0,0,0.05);
        }

        .menu-list {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .menu-item {
            display: flex;
            align-items: center;
            color: #333;
            font-weight: 500;
            padding: 14px 25px;
            text-decoration: none;
            transition: all 0.25s ease;
        }

        .menu-item i {
            font-size: 18px;
            margin-right: 12px;
            width: 22px;
            text-align: center;
        }

        .menu-item:hover {
            background-color: #e9f3ff;
            color: #007bff;
        }

        .menu-item.active {
            background-color: #e0efff;
            border-left: 4px solid #007bff;
            color: #007bff;
        }

        /* === PROFILE CONTENT === */
        .profile-content {
            flex: 1;
            padding: 40px 60px;
        }

        .profile-card {
            background: #fff;
            border-radius: 10px;
            padding: 30px 40px;
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
            max-width: 800px;
            margin: 0 auto;
        }

        .profile-card h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #333;
        }

        .avatar {
            width: 110px;
            height: 110px;
            border-radius: 50%;
            object-fit: cover;
            display: block;
            margin: 0 auto 15px;
            border: 2px solid #007bff;
        }

        .form-group {
            margin-bottom: 18px;
        }

        label {
            display: block;
            font-weight: 600;
            margin-bottom: 6px;
            color: #444;
        }

        input, select, textarea {
            width: 100%;
            padding: 10px 12px;
            border-radius: 6px;
            border: 1px solid #ccc;
            box-sizing: border-box;
            transition: 0.2s;
        }

        input:focus, select:focus, textarea:focus {
            border-color: #007bff;
            outline: none;
            box-shadow: 0 0 4px rgba(0,123,255,0.2);
        }

        button {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 12px 20px;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 600;
            transition: 0.25s;
        }

        button:hover {
            background-color: #0056b3;
        }

        .message {
            text-align: center;
            margin-bottom: 10px;
        }

        .message.success { color: green; }
        .message.error { color: red; }

        /* === RESPONSIVE === */
        @media (max-width: 900px) {
            .profile-page {
                flex-direction: column;
            }
            .sidebar {
                width: 100%;
                flex-direction: row;
                justify-content: space-around;
                box-shadow: none;
                border-right: none;
                border-bottom: 1px solid #ddd;
            }
            .menu-item {
                justify-content: center;
                padding: 12px 10px;
            }
        }
    </style>
</head>
<body>

<div class="profile-page">
    <!-- LEFT SIDEBAR -->
    <div class="sidebar">
        <ul class="menu-list">
            <li><a href="profile" class="menu-item active"><i class="fa fa-user"></i> Profile</a></li>
            <li><a href="#" class="menu-item"><i class="fa fa-bell"></i> Notifications</a></li>
            <li><a href="#" class="menu-item"><i class="fa fa-briefcase"></i> Trip History</a></li>
            <li><a href="#" class="menu-item"><i class="fa fa-users"></i> Story</a></li>
            <li><a href="update_password" class="menu-item"><i class="fa fa-fingerprint"></i> Security</a></li>
        </ul>
    </div>

    <!-- PROFILE FORM -->
    <div class="profile-content">
        <div class="profile-card">
            <h2>My Profile</h2>

            <c:if test="${not empty message}">
                <div class="message success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="message error">${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/profile">
                <div style="text-align:center;">
                    <img src="${user.avatar != null ? user.avatar : 'images/default-avatar.png'}" class="avatar" alt="Avatar">
                </div>

                <div class="form-group">
                    <label>Full Name</label>
                    <input type="text" name="name" value="${user.name}" required/>
                </div>

                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email" value="${user.email}" readonly/>
                </div>

                <div class="form-group">
                    <label>Phone Number</label>
                    <input type="text" name="phone" value="${user.phone}"/>
                </div>

                <div class="form-group">
                    <label>Date of Birth</label>
                    <input type="date" name="date_of_birth" value="${user.date_of_birth}"/>
                </div>

                <div class="form-group">
                    <label>Gender</label>
                    <select name="gender">
                        <option value="Male" ${user.gender == 'Male' ? 'selected' : ''}>Male</option>
                        <option value="Female" ${user.gender == 'Female' ? 'selected' : ''}>Female</option>
                        <option value="Other" ${user.gender == 'Other' ? 'selected' : ''}>Other</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Address</label>
                    <textarea name="address" rows="3">${user.address}</textarea>
                </div>

                <div style="text-align:center;">
                    <button type="submit">Update Information</button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
