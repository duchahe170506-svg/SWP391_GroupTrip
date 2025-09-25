<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="card" style="position:relative; overflow:hidden; max-width:520px;">
    <div class="accent-orb" style="right:-60px; top:-60px; width:140px; height:140px;"></div>
    <h1>Verify Email</h1>
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
