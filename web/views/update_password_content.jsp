<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Update Password</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background-color: #f4f6f9;
            margin: 0;
        }

        .profile-page {
            display: flex;
            width: 100%;
            min-height: calc(100vh - 100px);
            background-color: #f8f9fa;
        }

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

        .menu-list { list-style: none; padding: 0; margin: 0; }
        .menu-item {
            display: flex; align-items: center;
            color: #333; font-weight: 500;
            padding: 14px 25px;
            text-decoration: none; transition: all 0.25s ease;
        }
        .menu-item i { font-size: 18px; margin-right: 12px; width: 22px; text-align: center; }
        .menu-item:hover { background-color: #e9f3ff; color: #007bff; }
        .menu-item.active { background-color: #e0efff; border-left: 4px solid #007bff; color: #007bff; }

        .profile-content { flex: 1; padding: 40px 60px; }

        .profile-card {
            background: #fff;
            border-radius: 10px;
            padding: 30px 40px;
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
            max-width: 800px;
            margin: 0 auto;
        }

        .profile-card h2 { text-align: center; margin-bottom: 25px; color: #333; }

        .form-group { margin-bottom: 18px; position: relative; }
        label { display: block; font-weight: 600; margin-bottom: 6px; color: #444; }
        input { 
            width: 100%; padding: 10px 40px 10px 12px; 
            border-radius: 6px; border: 1px solid #ccc; 
            box-sizing: border-box; transition: 0.2s; 
        }
        input:focus { border-color: #007bff; outline: none; box-shadow: 0 0 4px rgba(0,123,255,0.2); }

        .toggle-password {
            position: absolute;
            right: 12px;
            top: 36px;
            cursor: pointer;
            color: #777;
            font-size: 16px;
        }
        .toggle-password:hover { color: #333; }

        button {
            background-color: #007bff; color: white; border: none;
            padding: 12px 20px; border-radius: 6px; cursor: pointer;
            font-weight: 600; transition: 0.25s;
        }
        button:hover:enabled { background-color: #0056b3; }
        button:disabled { background-color: #aaa; cursor: not-allowed; }

        .message { text-align: center; margin-bottom: 10px; }
        .message.success { color: green; }
        .message.error { color: red; }

        .checklist {
            margin: 15px 0;
            padding: 0;
            list-style: none;
            font-size: 14px;
            color: #555;
        }

        .checklist li {
            margin-bottom: 6px;
            display: flex;
            align-items: center;
        }

        .checklist i {
            margin-right: 8px;
        }

        .checklist i.fa-circle-xmark { color: red; }
        .checklist i.fa-circle-check { color: green; }

        @media (max-width: 900px) {
            .profile-page { flex-direction: column; }
            .sidebar {
                width: 100%; flex-direction: row; justify-content: space-around;
                box-shadow: none; border-right: none; border-bottom: 1px solid #ddd;
            }
            .menu-item { justify-content: center; padding: 12px 10px; }
        }
    </style>
</head>
<body>

<div class="profile-page">
    <div class="sidebar">
        <ul class="menu-list">
            <li><a href="profile" class="menu-item"><i class="fa fa-user"></i> Profile</a></li>
            <li><a href="#" class="menu-item"><i class="fa fa-bell"></i> Notifications</a></li>
            <li><a href="#" class="menu-item"><i class="fa fa-briefcase"></i> Trip History</a></li>
            <li><a href="#" class="menu-item"><i class="fa fa-users"></i> Story</a></li>
            <li><a href="update_password" class="menu-item active"><i class="fa fa-fingerprint"></i> Security</a></li>
        </ul>
    </div>

    <div class="profile-content">
        <div class="profile-card">
            <h2>Update Password</h2>

            <c:if test="${not empty message}">
                <div class="message success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="message error">${error}</div>
            </c:if>

            <form id="passwordForm" method="post" action="${pageContext.request.contextPath}/update_password">
                <div class="form-group">
                    <label>Current Password</label>
                    <input type="password" name="current_password" id="currentPassword" required/>
                    <i class="fa fa-eye toggle-password" data-target="currentPassword"></i>
                </div>

                <div class="form-group">
                    <label>New Password</label>
                    <input type="password" id="newPassword" name="new_password" required/>
                    <i class="fa fa-eye toggle-password" data-target="newPassword"></i>
                </div>

                <div class="form-group">
                    <label>Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirm_password" required/>
                    <i class="fa fa-eye toggle-password" data-target="confirmPassword"></i>
                    <div id="matchMsg" class="message error" style="display:none;">Passwords do not match!</div>
                </div>

                <ul class="checklist">
                    <li><i id="length" class="fa fa-circle-xmark"></i> At least 8 characters</li>
                    <li><i id="uppercase" class="fa fa-circle-xmark"></i> One uppercase letter</li>
                    <li><i id="lowercase" class="fa fa-circle-xmark"></i> One lowercase letter</li>
                    <li><i id="number" class="fa fa-circle-xmark"></i> One number</li>
                    <li><i id="special" class="fa fa-circle-xmark"></i> One special character (!@#$%^&*())</li>
                </ul>

                <div style="text-align:center; margin-top:20px;">
                    <button type="button" onclick="window.location.href='profile'">Cancel</button>
                    <button type="submit" id="updateBtn" disabled>Update Password</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    const newPassword = document.getElementById('newPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    const matchMsg = document.getElementById('matchMsg');
    const updateBtn = document.getElementById('updateBtn');

    const checks = {
        length: document.getElementById('length'),
        uppercase: document.getElementById('uppercase'),
        lowercase: document.getElementById('lowercase'),
        number: document.getElementById('number'),
        special: document.getElementById('special')
    };

    function validatePassword() {
        const pwd = newPassword.value;
        const conditions = {
            length: pwd.length >= 8,
            uppercase: /[A-Z]/.test(pwd),
            lowercase: /[a-z]/.test(pwd),
            number: /[0-9]/.test(pwd),
            special: /[!@#$%^&*()]/.test(pwd)
        };

        for (let key in conditions) {
            if (conditions[key]) {
                checks[key].classList.replace('fa-circle-xmark', 'fa-circle-check');
            } else {
                checks[key].classList.replace('fa-circle-check', 'fa-circle-xmark');
            }
        }

        const match = confirmPassword.value === pwd && pwd !== "";
        matchMsg.style.display = match ? 'none' : (confirmPassword.value ? 'block' : 'none');

        const allValid = Object.values(conditions).every(v => v === true);
        updateBtn.disabled = !(allValid && match);
    }

    newPassword.addEventListener('input', validatePassword);
    confirmPassword.addEventListener('input', validatePassword);

    // 👁 Toggle password visibility
    document.querySelectorAll('.toggle-password').forEach(icon => {
        icon.addEventListener('click', function() {
            const target = document.getElementById(this.dataset.target);
            const isPassword = target.type === "password";
            target.type = isPassword ? "text" : "password";
            this.classList.toggle('fa-eye');
            this.classList.toggle('fa-eye-slash');
        });
    });
</script>

</body>
</html>
