<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title><c:out value="${pageTitle != null ? pageTitle : 'GroupTrip'}"/></title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"/>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    </head>
    <body class="${empty sessionScope.currentUser ? 'theme-light' : ''}">
        <jsp:include page="partials/header.jsp"/>
        <main class="gt-main">
            <div class="gt-container">
                <jsp:include page="${contentPage}"/>
            </div>
        </main>
        <jsp:include page="partials/footer.jsp"/>
    </body>
</html>
