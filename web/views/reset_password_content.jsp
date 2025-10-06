<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="card" style="position:relative; overflow:hidden; max-width:520px;">
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
