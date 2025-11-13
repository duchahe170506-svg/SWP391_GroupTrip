<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
    .card {
        position: relative;
        overflow: hidden;
        background: #fff;
        max-width: 520px;
        width: 100%;
        padding: 40px 30px;
        border-radius: 12px;
        box-shadow: 0 6px 16px rgba(0,0,0,0.1);
        z-index: 1;
    }

    .accent-stripe {
        position: absolute;
        background: linear-gradient(135deg, #007bff, #00d4ff);
        opacity: 0.3;
        border-radius: 50%;
        transform: rotate(25deg);
        z-index: 0;
    }

    h1 {
        text-align: center;
        font-size: 1.8rem;
        color: #333;
        margin-bottom: 25px;
    }

    .form-group {
        margin-bottom: 18px;
    }

    label {
        display: block;
        font-weight: 600;
        margin-bottom: 6px;
        color: #555;
    }

    input[type="email"],
    input[type="password"],
    input[type="text"] {
        width: 100%;
        padding: 10px 12px;
        border: 1px solid #ccc;
        border-radius: 6px;
        outline: none;
        font-size: 1rem;
        transition: border-color 0.2s ease;
    }

    input:focus {
        border-color: #007bff;
    }

    .btn {
        display: inline-block;
        width: 100%;
        padding: 10px 0;
        border: none;
        border-radius: 6px;
        font-size: 1rem;
        cursor: pointer;
        transition: background 0.2s ease;
    }

    .btn.primary {
        background: #007bff;
        color: #fff;
        font-weight: bold;
    }

    .btn.primary:hover {
        background: #0056d2;
    }

    .alert {
        padding: 12px 16px;
        border-radius: 6px;
        margin-bottom: 15px;
        font-size: 0.95rem;
        animation: fadeIn 0.3s ease-in-out;
    }

    .alert.success {
        background: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
    }

    .alert.error {
        background: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
    }

    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(-5px); }
        to { opacity: 1; transform: translateY(0); }
    }
</style>
<div class="card" style="left: 36%; max-width:520px;">
    <div class="accent-stripe" style="left:-40px; bottom:-40px; width:180px; height:180px;"></div>
    <h1>Verify Email</h1>
    <c:if test="${param.sent == '1'}">
        <div class="alert success">Verification code sent to your email</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/reset-password" class="form" autocomplete="off">
        <input type="hidden" name="stage" value="${empty stage ? 'otp' : stage}"/>
        <c:choose>
            <c:when test="${stage == 'password'}">
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email" value="${email}" required readonly/>
                </div>
                <input type="hidden" name="otp" value="${otp}"/>
                <div class="form-group">
                    <label>New password</label>
                    <input type="password" name="password" required placeholder="••••••••" autocomplete="new-password" value=""/>
                </div>
                <div class="form-group">
                    <label>Confirm password</label>
                    <input type="password" name="confirm" required placeholder="••••••••" autocomplete="new-password" value=""/>
                </div>
                <button type="submit" class="btn primary">Update password</button>
            </c:when>
            <c:otherwise>
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email" value="${email}" required placeholder="you@example.com"/>
                </div>
                <div class="form-group">
                    <label>Code OTP</label>
                    <input type="text" name="otp" placeholder="Enter 6-digit code"/>
                </div>
                <button type="submit" class="btn primary">Verify OTP</button>
            </c:otherwise>
        </c:choose>
    </form>
</div>