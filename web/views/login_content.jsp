<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="card" style="position:relative; overflow:hidden; max-width:520px;">
    <div class="accent-orb" style="right:-60px; top:-60px; width:140px; height:140px;"></div>
    <h1 style="text-transform:uppercase; text-align:center;">Sign in</h1>
    <c:if test="${not empty param.registered}">
        <div class="alert success">Registered successfully. Please sign in.</div>
    </c:if>
    <c:if test="${not empty param.reset}">
        <div class="alert success">Password changed successfully. Please sign in.</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/login" class="form">
        <div class="form-group">
            <label>Your Email</label>
            <input type="email" name="email" required placeholder="you@example.com"/>
        </div>
        <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" required placeholder="Enter your password" autocomplete="current-password"/>
        </div>
        <div class="form-meta" style="justify-content:flex-end;">
            <a href="${pageContext.request.contextPath}/forgot-password">Forgot password</a>
        </div>
        <div style="display:flex; justify-content:center; margin-top:10px;">
            <button type="submit" class="btn primary">Sign In</button>
        </div>
        <div style="text-align:center; color: var(--muted); margin-top:10px;">Or</div>
        <div class="form-meta" style="justify-content:center;">
            <span>Don't have an account?</span>
            <a href="${pageContext.request.contextPath}/signup">Sign Up</a>
        </div>
    </form>
</div>
