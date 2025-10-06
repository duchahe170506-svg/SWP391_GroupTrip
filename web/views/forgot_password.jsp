<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:scriptlet>
    request.setAttribute("pageTitle", "Forgot password");
    request.setAttribute("contentPage", "forgot_password_content.jsp");
</jsp:scriptlet>
<jsp:include page="layout.jsp"/>
