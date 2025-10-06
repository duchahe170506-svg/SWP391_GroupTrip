<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="card" style="position:relative; overflow:hidden; max-width:520px;">
    <div class="accent-stripe" style="left:-50px; bottom:-50px; width:160px; height:160px;"></div>
    <h1 style="text-transform:uppercase; text-align:center;">Sign up</h1>
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
            <input type="password" name="password" required placeholder="Enter your password"/>
        </div>
        <div class="form-group">
            <label>Repeat Password</label>
            <input type="password" name="confirm" required placeholder="Re-enter password"/>
        </div>
        <div style="display:flex; justify-content:center; margin-top:10px;">
            <button type="submit" class="btn primary">Register</button>
        </div>
        <div style="text-align:center; color: var(--muted); margin-top:10px;">Or</div>
        <div class="form-meta" style="justify-content:center;">
            <span>Already have an account ?</span>
            <a href="${pageContext.request.contextPath}/login">Sign in</a>
        </div>
    </form>
</div>
