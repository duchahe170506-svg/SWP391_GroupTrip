<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="uri" value="${pageContext.request.requestURI}"/>
<header class="gt-header">
    <div class="gt-container gt-navbar">
        <a class="gt-logo" href="${pageContext.request.contextPath}/home">GROUP TRIP</a>
        <c:choose>
            <c:when test="${not empty sessionScope.currentUser}">
                <nav class="gt-nav gt-nav-auth">
                    <a class="pill" href="${pageContext.request.contextPath}/home">Home</a>
                    <a class="pill" href="#">Trips</a>
                    <a class="pill" href="#">Groups</a>
                    <a class="pill" href="#">About</a>
                    <form class="gt-search" action="#" method="get">
                        <input type="text" name="q" placeholder="search"/>
                        <button type="submit" class="btn">Search</button>
                    </form>
                    <a class="gt-user" href="${pageContext.request.contextPath}/profile">Welcome, ${sessionScope.currentUser.name}</a>
                    <a class="btn" href="${pageContext.request.contextPath}/logout">Logout</a>
                </nav>
            </c:when>
            <c:otherwise>
                <nav class="gt-nav gt-nav-guest">
                    <a class="btn ${fn:contains(uri, '/login') ? 'primary' : ''}" href="${pageContext.request.contextPath}/login">Sign in</a>
                    <a class="btn ${fn:contains(uri, '/signup') ? 'primary' : ''}" href="${pageContext.request.contextPath}/signup">Sign up</a>
                </nav>
            </c:otherwise>
        </c:choose>
    </div>
</header>
