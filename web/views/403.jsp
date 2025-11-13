<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Access Denied</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #f8f9fa, #e9ecef);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .container {
            background: white;
            padding: 40px 60px;
            border-radius: 15px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.1);
            text-align: center;
            max-width: 500px;
        }

        h1 {
            color: #dc3545;
            font-size: 24px;
            margin-bottom: 15px;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        p {
            color: #333;
            font-size: 16px;
            margin-bottom: 25px;
        }

        .error {
            background-color: #ffe3e3;
            color: #b02a37;
            border-left: 5px solid #dc3545;
            padding: 10px 15px;
            border-radius: 8px;
            margin-bottom: 25px;
        }

        a {
            text-decoration: none;
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            font-weight: 500;
            transition: all 0.2s ease;
        }

        a:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>ACCESS DENIED</h1>
        <div class="error">
            <c:if test="${not empty error}">
                ${error}
            </c:if>
        </div>
        <p>You do not have permission to access this page.</p>
        <a href="${pageContext.request.contextPath}/home">Return to Home</a>
    </div>
</body>
</html>
