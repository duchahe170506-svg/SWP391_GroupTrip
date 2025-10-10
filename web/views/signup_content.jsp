<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Sign up</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <style>
        .card {
            position: relative;
            overflow: hidden;
            max-width: 520px;
            width: 100%;
            background: #fff;
            border-radius: 10px;
            padding: 30px 40px;
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        }

        h1 {
            text-transform: uppercase;
            text-align: center;
            margin-bottom: 20px;
        }

        .form-group {
            margin-bottom: 18px;
            position: relative;
        }

        label {
            display: block;
            font-weight: 600;
            margin-bottom: 6px;
            color: #333;
        }

        input {
            width: 100%;
            padding: 10px 40px 10px 12px;
            border-radius: 6px;
            border: 1px solid #ccc;
            box-sizing: border-box;
            transition: 0.2s;
        }

        input:focus {
            border-color: #007bff;
            outline: none;
            box-shadow: 0 0 4px rgba(0,123,255,0.2);
        }

        .toggle-password {
            position: absolute;
            right: 12px;
            top: 36px;
            cursor: pointer;
            color: #777;
            font-size: 16px;
        }

        .toggle-password:hover {
            color: #333;
        }

        .btn.primary {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 12px 20px;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 600;
            transition: 0.25s;
        }

        .btn.primary:hover:enabled {
            background-color: #0056b3;
        }

        .btn.primary:disabled {
            background-color: #aaa;
            cursor: not-allowed;
        }

        .alert.error {
            color: red;
            text-align: center;
            margin-bottom: 10px;
        }

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

        .form-meta {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 5px;
        }

        .form-meta a {
            color: #007bff;
            text-decoration: none;
        }

        .form-meta a:hover {
            text-decoration: underline;
        }

        #matchMsg {
            color: red;
            font-size: 14px;
            margin-top: 4px;
            display: none;
        }
    </style>
</head>
<body>

<div class="card">
    <h1>Sign up</h1>

    <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/signup" class="form">
        <div class="form-group">
            <label>Name</label>
            <input type="text" name="name" required placeholder="John Doe"/>
        </div>

        <div class="form-group">
            <label>Your Email</label>
            <input type="email" name="email" required placeholder="you@example.com"/>
        </div>

        <div class="form-group">
            <label>Password</label>
            <input type="password" id="password" name="password" required placeholder="Enter your password"/>
            <i class="fa fa-eye toggle-password" data-target="password"></i>
        </div>

        <div class="form-group">
            <label>Repeat Password</label>
            <input type="password" id="confirm" name="confirm" required placeholder="Re-enter password"/>
            <i class="fa fa-eye toggle-password" data-target="confirm"></i>
            <div id="matchMsg">Passwords do not match!</div>
        </div>

        <ul class="checklist">
            <li><i id="length" class="fa fa-circle-xmark"></i> At least 8 characters</li>
            <li><i id="uppercase" class="fa fa-circle-xmark"></i> One uppercase letter</li>
            <li><i id="lowercase" class="fa fa-circle-xmark"></i> One lowercase letter</li>
            <li><i id="number" class="fa fa-circle-xmark"></i> One number</li>
            <li><i id="special" class="fa fa-circle-xmark"></i> One special character (!@#$%^&*())</li>
        </ul>

        <div style="display:flex; justify-content:center; margin-top:10px;">
            <button type="submit" class="btn primary" id="registerBtn" disabled>Register</button>
        </div>

        <div style="text-align:center; color: var(--muted); margin-top:10px;">Or</div>

        <div class="form-meta">
            <span>Already have an account?</span>
            <a href="${pageContext.request.contextPath}/login">Sign in</a>
        </div>
    </form>
</div>

<script>
    const password = document.getElementById('password');
    const confirm = document.getElementById('confirm');
    const registerBtn = document.getElementById('registerBtn');
    const matchMsg = document.getElementById('matchMsg');

    const checks = {
        length: document.getElementById('length'),
        uppercase: document.getElementById('uppercase'),
        lowercase: document.getElementById('lowercase'),
        number: document.getElementById('number'),
        special: document.getElementById('special')
    };

    function validatePassword() {
        const pwd = password.value;
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

        const match = confirm.value === pwd && pwd !== "";
        matchMsg.style.display = match ? 'none' : (confirm.value ? 'block' : 'none');

        const allValid = Object.values(conditions).every(v => v === true);
        registerBtn.disabled = !(allValid && match);
    }

    password.addEventListener('input', validatePassword);
    confirm.addEventListener('input', validatePassword);

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
