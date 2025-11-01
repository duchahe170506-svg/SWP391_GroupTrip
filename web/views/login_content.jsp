<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="card" 
     style="position:relative !important; 
            overflow:hidden !important; 
            max-width:520px !important; 
            margin:60px auto !important; 
            padding:30px !important; 
            background:white !important; 
            border-radius:10px !important; 
            box-shadow:0 4px 12px rgba(0,0,0,0.1) !important;">
    
    <div class="accent-orb" 
         style="right:-60px !important; 
                top:-60px !important; 
                width:140px !important; 
                height:140px !important; 
                background:var(--accent,#007bff) !important; 
                position:absolute !important; 
                border-radius:50% !important; 
                opacity:0.15 !important;">
    </div>

    <h1 style="text-transform:uppercase !important; 
               text-align:center !important; 
               margin-bottom:20px !important;">Sign in</h1>

    <c:if test="${not empty param.registered}">
        <div class="alert success" 
             style="margin-bottom:10px !important;">Registered successfully. Please sign in.</div>
    </c:if>
    <c:if test="${not empty param.reset}">
        <div class="alert success" 
             style="margin-bottom:10px !important;">Password changed successfully. Please sign in.</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert error" 
             style="margin-bottom:10px !important;">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login" class="form" 
          style="display:flex !important; flex-direction:column !important; gap:12px !important;">
        
        <div class="form-group" style="display:flex !important; flex-direction:column !important;">
            <label style="margin-bottom:6px !important; font-weight:600 !important;">Your Email</label>
            <input type="email" name="email" required placeholder="you@example.com"
                   style="padding:10px !important; 
                          border:1px solid #ccc !important; 
                          border-radius:5px !important; 
                          font-size:14px !important;"/>
        </div>

        <div class="form-group" style="display:flex !important; flex-direction:column !important;">
            <label style="margin-bottom:6px !important; font-weight:600 !important;">Password</label>
            <input type="password" name="password" required placeholder="Enter your password" autocomplete="current-password"
                   style="padding:10px !important; 
                          border:1px solid #ccc !important; 
                          border-radius:5px !important; 
                          font-size:14px !important;"/>
        </div>

        <div class="form-meta" style="display:flex !important; justify-content:flex-end !important;">
            <a href="${pageContext.request.contextPath}/forgot-password"
               style="color:var(--accent,#007bff) !important; text-decoration:none !important; font-size:14px !important;">Forgot password</a>
        </div>

        <div style="display:flex !important; justify-content:center !important; margin-top:10px !important;">
            <button type="submit" class="btn primary" 
                    style="background:var(--accent,#77a096) !important; 
                           color:white !important; 
                           padding:10px 20px !important; 
                           border:none !important; 
                           border-radius:5px !important; 
                           cursor:pointer !important;">Sign In</button>
        </div>

        <div style="text-align:center !important; color:var(--muted,#777) !important; margin-top:10px !important;">Or</div>

        <div class="form-meta" style="display:flex !important; justify-content:center !important; gap:6px !important;">
            <span style="color:#555 !important;">Don't have an account?</span>
            <a href="${pageContext.request.contextPath}/signup"
               style="color:var(--accent,#007bff) !important; font-weight:600 !important; text-decoration:none !important;">Sign Up</a>
        </div>
    </form>
</div>
