<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>

    .card {
        background: #fff;
        border-radius: 16px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        padding: 2rem 2.5rem;
        text-align: center;
        overflow: hidden;
        width: 100%;
    }

    .accent-orb {
        position: absolute;
        background: linear-gradient(135deg, #4a90e2, #5ddcff);
        border-radius: 50%;
        opacity: 0.2;
        z-index: 0;
    }

    .form {
        position: relative;
        z-index: 1;
    }

    .form-group {
        text-align: left;
        margin-bottom: 1.2rem;
    }

    label {
        display: block;
        font-weight: 600;
        margin-bottom: 0.4rem;
        color: #444;
    }

    input[type="email"] {
        width: 100%;
        padding: 0.75rem;
        border: 1px solid #ccc;
        border-radius: 8px;
        font-size: 1rem;
        transition: all 0.2s ease;
    }

    input[type="email"]:focus {
        outline: none;
        border-color: #4a90e2;
        box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.2);
    }

    .btn.primary {
        background: #4a90e2;
        border: none;
        padding: 0.8rem 1.5rem;
        border-radius: 8px;
        color: #fff;
        font-size: 1rem;
        font-weight: 600;
        cursor: pointer;
        transition: background 0.2s ease;
    }

    .btn.primary:hover {
        background: #3c7cd7;
    }

    .alert.error {
        background: #ffe6e6;
        color: #d63031;
        border-left: 4px solid #d63031;
        padding: 0.75rem 1rem;
        margin-bottom: 1rem;
        border-radius: 6px;
        text-align: left;
    }
</style>
<div class="card" style="left: 33%; max-width:520px;">
    <div class="accent-orb" style="right:-60px; top:-60px; width:140px; height:140px;"></div>
    <div style="font-size: 20px">Verify Email</div>
    <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/forgot-password" class="form">
        <div class="form-group">
            <label>Your Email Address:</label>
            <input type="email" name="email" required placeholder="Enter your email"/>
        </div>
        <button type="submit" class="btn primary">Send OTP</button>
    </form>
</div>
