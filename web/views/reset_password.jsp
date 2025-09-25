<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:scriptlet>
    request.setAttribute("pageTitle", "Reset password");
    request.setAttribute("contentPage", "reset_password_content.jsp");
</jsp:scriptlet>
<jsp:include page="layout.jsp"/>
