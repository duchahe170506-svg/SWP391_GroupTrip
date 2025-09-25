<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:scriptlet>
    request.setAttribute("pageTitle", "Home");
    request.setAttribute("contentPage", "home_content.jsp");
</jsp:scriptlet>
<jsp:include page="layout.jsp"/>


