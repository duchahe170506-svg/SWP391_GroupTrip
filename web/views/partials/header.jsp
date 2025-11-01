<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="uri" value="${pageContext.request.requestURI}"/>
<!DOCTYPE html>
<html>

    <head>
        <title>Group Trip</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="format-detection" content="telephone=no">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="author" content="TemplateJungle">
        <meta name="keywords" content="">
        <meta name="description" content="">
        <!--Bootstrap ================================================== -->
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">

        <!--vendor css ================================================== -->
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/vendor.css">

        <!--Link Swiper's CSS ================================================== -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@9/swiper-bundle.min.css" />

        <!-- Style Sheet ================================================== -->
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/style.css">

        <!-- Google Fonts ================================================== -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link
            href="https://fonts.googleapis.com/css2?family=Anek+Bangla:wght@100..800&family=Cormorant+Garamond:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&family=Mulish:ital,wght@0,200..1000;1,200..1000&family=WindSong:wght@400;500&display=swap"
            rel="stylesheet">
        <link
            href="https://fonts.googleapis.com/css2?family=Anek+Bangla:wght@100..800&family=Cormorant+Garamond:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet">
        <link
            href="https://fonts.googleapis.com/css2?family=Anek+Bangla:wght@100..800&family=Cormorant+Garamond:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&family=Mulish:ital,wght@0,200..1000;1,200..1000&display=swap"
            rel="stylesheet">
    </head>

    <body>

        <svg xmlns="http://www.w3.org/2000/svg" style="display: none;">
    <symbol id="user" viewBox="0 0 512 512">
        <path fill="currentColor"
              d="M332.64 64.58C313.18 43.57 286 32 256 32c-30.16 0-57.43 11.5-76.8 32.38c-19.58 21.11-29.12 49.8-26.88 80.78C156.76 206.28 203.27 256 256 256s99.16-49.71 103.67-110.82c2.27-30.7-7.33-59.33-27.03-80.6M432 480H80a31 31 0 0 1-24.2-11.13c-6.5-7.77-9.12-18.38-7.18-29.11C57.06 392.94 83.4 353.61 124.8 326c36.78-24.51 83.37-38 131.2-38s94.42 13.5 131.2 38c41.4 27.6 67.74 66.93 76.18 113.75c1.94 10.73-.68 21.34-7.18 29.11A31 31 0 0 1 432 480" />
    </symbol>
    <symbol id="search" viewBox="0 0 512 512">
        <path fill="currentColor"
              d="M456.69 421.39L362.6 327.3a173.8 173.8 0 0 0 34.84-104.58C397.44 126.38 319.06 48 222.72 48S48 126.38 48 222.72s78.38 174.72 174.72 174.72A173.8 173.8 0 0 0 327.3 362.6l94.09 94.09a25 25 0 0 0 35.3-35.3M97.92 222.72a124.8 124.8 0 1 1 124.8 124.8a124.95 124.95 0 0 1-124.8-124.8" />
    </symbol>
    <symbol id="cart" viewBox="0 0 512 512">
        <circle cx="176" cy="416" r="32" fill="currentColor" />
        <circle cx="400" cy="416" r="32" fill="currentColor" />
        <path fill="currentColor"
              d="M456.8 120.78a23.92 23.92 0 0 0-18.56-8.78H133.89l-6.13-34.78A16 16 0 0 0 112 64H48a16 16 0 0 0 0 32h50.58l45.66 258.78A16 16 0 0 0 160 368h256a16 16 0 0 0 0-32H173.42l-5.64-32h241.66A24.07 24.07 0 0 0 433 284.71l28.8-144a24 24 0 0 0-5-19.93" />
    </symbol>
    <symbol id="arrow-right" viewBox="0 0 32 32">
        <path fill="currentColor"
              d="M18.719 6.781L17.28 8.22L24.063 15H4v2h20.063l-6.782 6.781l1.438 1.438l8.5-8.5l.687-.719l-.687-.719z" />
    </symbol>
    <symbol id="arrow-left" viewBox="0 0 32 32">
        <path fill="currentColor"
              d="m13.281 6.781l-8.5 8.5l-.687.719l.687.719l8.5 8.5l1.438-1.438L7.938 17H28v-2H7.937l6.782-6.781z" />
    </symbol>
    <symbol id="mail" viewBox="0 0 100 100">
        <path fill="currentColor"
              d="M85.944 20.189H14.056a2.56 2.56 0 0 0-2.556 2.557v5.144c0 .237.257.509.467.619l37.786 21.583a.63.63 0 0 0 .318.083a.63.63 0 0 0 .324-.088L87.039 28.53c.206-.115.752-.419.957-.559c.248-.169.504-.322.504-.625v-4.601a2.56 2.56 0 0 0-2.556-2.556m2.237 15.457a.64.64 0 0 0-.645.004L66.799 47.851a.637.637 0 0 0-.145.985l20.74 22.357a.63.63 0 0 0 .467.204a.64.64 0 0 0 .639-.639V36.201a.64.64 0 0 0-.319-.555M60.823 51.948a.636.636 0 0 0-.791-.118l-8.312 4.891a3.24 3.24 0 0 1-3.208.021l-7.315-4.179a.64.64 0 0 0-.751.086L12.668 78.415a.64.64 0 0 0 .114 1.02c.432.254.849.375 1.273.375h71.153a.64.64 0 0 0 .585-.385a.64.64 0 0 0-.118-.689zm-26.489-2.347a.64.64 0 0 0-.115-1.023L12.453 36.146a.638.638 0 0 0-.953.556v32.62a.637.637 0 0 0 1.073.468z" />
    </symbol>
    <symbol id="facebook" viewBox="0 0 24 24">
        <path fill="currentColor"
              d="M9.198 21.5h4v-8.01h3.604l.396-3.98h-4V7.5a1 1 0 0 1 1-1h3v-4h-3a5 5 0 0 0-5 5v2.01h-2l-.396 3.98h2.396z" />
    </symbol>
    <symbol id="twitter" viewBox="0 0 24 24">
        <path fill="currentColor"
              d="M22.46 6c-.77.35-1.6.58-2.46.69c.88-.53 1.56-1.37 1.88-2.38c-.83.5-1.75.85-2.72 1.05C18.37 4.5 17.26 4 16 4c-2.35 0-4.27 1.92-4.27 4.29c0 .34.04.67.11.98C8.28 9.09 5.11 7.38 3 4.79c-.37.63-.58 1.37-.58 2.15c0 1.49.75 2.81 1.91 3.56c-.71 0-1.37-.2-1.95-.5v.03c0 2.08 1.48 3.82 3.44 4.21a4.2 4.2 0 0 1-1.93.07a4.28 4.28 0 0 0 4 2.98a8.52 8.52 0 0 1-5.33 1.84q-.51 0-1.02-.06C3.44 20.29 5.7 21 8.12 21C16 21 20.33 14.46 20.33 8.79c0-.19 0-.37-.01-.56c.84-.6 1.56-1.36 2.14-2.23" />
    </symbol>
    <symbol id="pinterest" viewBox="0 0 24 24">
        <path fill="currentColor"
              d="M9.04 21.54c.96.29 1.93.46 2.96.46a10 10 0 0 0 10-10A10 10 0 0 0 12 2A10 10 0 0 0 2 12c0 4.25 2.67 7.9 6.44 9.34c-.09-.78-.18-2.07 0-2.96l1.15-4.94s-.29-.58-.29-1.5c0-1.38.86-2.41 1.84-2.41c.86 0 1.26.63 1.26 1.44c0 .86-.57 2.09-.86 3.27c-.17.98.52 1.84 1.52 1.84c1.78 0 3.16-1.9 3.16-4.58c0-2.4-1.72-4.04-4.19-4.04c-2.82 0-4.48 2.1-4.48 4.31c0 .86.28 1.73.74 2.3c.09.06.09.14.06.29l-.29 1.09c0 .17-.11.23-.28.11c-1.28-.56-2.02-2.38-2.02-3.85c0-3.16 2.24-6.03 6.56-6.03c3.44 0 6.12 2.47 6.12 5.75c0 3.44-2.13 6.2-5.18 6.2c-.97 0-1.92-.52-2.26-1.13l-.67 2.37c-.23.86-.86 2.01-1.29 2.7z" />
    </symbol>
    <symbol id="instagram" viewBox="0 0 24 24">
        <path fill="currentColor"
              d="M13.028 2c1.125.003 1.696.009 2.189.023l.194.007c.224.008.445.018.712.03c1.064.05 1.79.218 2.427.465c.66.254 1.216.598 1.772 1.153a4.9 4.9 0 0 1 1.153 1.772c.247.637.415 1.363.465 2.428c.012.266.022.487.03.712l.006.194c.015.492.021 1.063.023 2.188l.001.746v1.31a79 79 0 0 1-.023 2.188l-.006.194c-.008.225-.018.446-.03.712c-.05 1.065-.22 1.79-.466 2.428a4.9 4.9 0 0 1-1.153 1.772a4.9 4.9 0 0 1-1.772 1.153c-.637.247-1.363.415-2.427.465l-.712.03l-.194.006c-.493.014-1.064.021-2.189.023l-.746.001h-1.309a78 78 0 0 1-2.189-.023l-.194-.006a63 63 0 0 1-.712-.031c-1.064-.05-1.79-.218-2.428-.465a4.9 4.9 0 0 1-1.771-1.153a4.9 4.9 0 0 1-1.154-1.772c-.247-.637-.415-1.363-.465-2.428l-.03-.712l-.005-.194A79 79 0 0 1 2 13.028v-2.056a79 79 0 0 1 .022-2.188l.007-.194c.008-.225.018-.446.03-.712c.05-1.065.218-1.79.465-2.428A4.9 4.9 0 0 1 3.68 3.678a4.9 4.9 0 0 1 1.77-1.153c.638-.247 1.363-.415 2.428-.465c.266-.012.488-.022.712-.03l.194-.006a79 79 0 0 1 2.188-.023zM12 7a5 5 0 1 0 0 10a5 5 0 0 0 0-10m0 2a3 3 0 1 1 .001 6a3 3 0 0 1 0-6m5.25-3.5a1.25 1.25 0 0 0 0 2.5a1.25 1.25 0 0 0 0-2.5" />
    </symbol>
    <symbol id="youtube" viewBox="0 0 24 24">
        <g fill="none" fill-rule="evenodd">
        <path
            d="M24 0v24H0V0zM12.593 23.258l-.011.002l-.071.035l-.02.004l-.014-.004l-.071-.035q-.016-.005-.024.005l-.004.01l-.017.428l.005.02l.01.013l.104.074l.015.004l.012-.004l.104-.074l.012-.016l.004-.017l-.017-.427q-.004-.016-.017-.018m.265-.113l-.013.002l-.185.093l-.01.01l-.003.011l.018.43l.005.012l.008.007l.201.093q.019.005.029-.008l.004-.014l-.034-.614q-.005-.019-.02-.022m-.715.002a.02.02 0 0 0-.027.006l-.006.014l-.034.614q.001.018.017.024l.015-.002l.201-.093l.01-.008l.004-.011l.017-.43l-.003-.012l-.01-.01z" />
        <path fill="currentColor"
              d="M12 4c.855 0 1.732.022 2.582.058l1.004.048l.961.057l.9.061l.822.064a3.8 3.8 0 0 1 3.494 3.423l.04.425l.075.91c.07.943.122 1.971.122 2.954s-.052 2.011-.122 2.954l-.075.91l-.04.425a3.8 3.8 0 0 1-3.495 3.423l-.82.063l-.9.062l-.962.057l-1.004.048A62 62 0 0 1 12 20a62 62 0 0 1-2.582-.058l-1.004-.048l-.961-.057l-.9-.062l-.822-.063a3.8 3.8 0 0 1-3.494-3.423l-.04-.425l-.075-.91A41 41 0 0 1 2 12c0-.983.052-2.011.122-2.954l.075-.91l.04-.425A3.8 3.8 0 0 1 5.73 4.288l.821-.064l.9-.061l.962-.057l1.004-.048A62 62 0 0 1 12 4m-2 5.575v4.85c0 .462.5.75.9.52l4.2-2.425a.6.6 0 0 0 0-1.04l-4.2-2.424a.6.6 0 0 0-.9.52Z" />
        </g>
    </symbol>
    <symbol id="navbar-icon" viewBox="0 0 20 20">
        <path fill="currentColor" fill-rule="evenodd"
              d="M3 5a1 1 0 0 1 1-1h12a1 1 0 1 1 0 2H4a1 1 0 0 1-1-1m0 5a1 1 0 0 1 1-1h12a1 1 0 1 1 0 2H4a1 1 0 0 1-1-1m6 5a1 1 0 0 1 1-1h6a1 1 0 1 1 0 2h-6a1 1 0 0 1-1-1"
              clip-rule="evenodd" />
    </symbol>
    <symbol id="send" viewBox="0 0 32 32">
        <path fill="currentColor"
              d="M26.07 3.996a3 3 0 0 0-.933.223h-.004c-.285.113-1.64.683-3.7 1.547l-7.382 3.109c-5.297 2.23-10.504 4.426-10.504 4.426l.062-.024s-.359.118-.734.375a2 2 0 0 0-.586.567c-.184.27-.332.683-.277 1.11c.09.722.558 1.155.894 1.394c.34.242.664.355.664.355h.008l4.883 1.645c.219.703 1.488 4.875 1.793 5.836c.18.574.355.933.574 1.207q.157.21.379.351a1 1 0 0 0 .246.106l-.05-.012c.015.004.027.016.038.02c.04.011.067.015.118.023c.773.234 1.394-.246 1.394-.246l.035-.028l2.883-2.625l4.832 3.707l.11.047c1.007.442 2.027.196 2.566-.238c.543-.437.754-.996.754-.996l.035-.09l3.734-19.129c.106-.472.133-.914.016-1.343a1.8 1.8 0 0 0-.781-1.047a1.87 1.87 0 0 0-1.067-.27m-.101 2.05c-.004.063.008.056-.02.177v.011l-3.699 18.93c-.016.027-.043.086-.117.145c-.078.062-.14.101-.465-.028l-5.91-4.531l-3.57 3.254l.75-4.79l9.656-9c.398-.37.265-.448.265-.448c.028-.454-.601-.133-.601-.133l-12.176 7.543l-.004-.02l-5.836-1.965v-.004l-.015-.003l.03-.012l.032-.016l.031-.011s5.211-2.196 10.508-4.426c2.652-1.117 5.324-2.242 7.379-3.11a807 807 0 0 1 3.66-1.53c.082-.032.043-.032.102-.032z" />
    </symbol>
    <symbol id="google" viewBox="0 0 16 16">
        <path fill="currentColor"
              d="M15.545 6.558a9.4 9.4 0 0 1 .139 1.626c0 2.434-.87 4.492-2.384 5.885h.002C11.978 15.292 10.158 16 8 16A8 8 0 1 1 8 0a7.7 7.7 0 0 1 5.352 2.082l-2.284 2.284A4.35 4.35 0 0 0 8 3.166c-2.087 0-3.86 1.408-4.492 3.304a4.8 4.8 0 0 0 0 3.063h.003c.635 1.893 2.405 3.301 4.492 3.301c1.078 0 2.004-.276 2.722-.764h-.003a3.7 3.7 0 0 0 1.599-2.431H8v-3.08z" />
    </symbol>
    </svg>
    <header id="header">
        <nav class="header-top py-1">
            <div class="container">
                <div class="d-flex flex-wrap justify-content-between align-items-center text-white">
                    <div class="newsletter"><a href="#">
                            <svg class="me-1" width="18" height="18">
                            <use xlink:href="#mail"></use>
                            </svg>
                        </a>
                        NEWSLETTER
                    </div>
                    <ul class="social-links text-white d-flex flex-wrap list-unstyled m-0">
                        <li class="social">
                            <a href="#">
                                <svg class="me-1" width="16" height="16">
                                <use xlink:href="#facebook"></use>
                                </svg>
                            </a>
                        </li>
                        <li class="social ms-4">
                            <a href="#">
                                <svg class="me-1" width="16" height="16">
                                <use xlink:href="#twitter"></use>
                                </svg>
                            </a>
                        </li>
                        <li class="social ms-4">
                            <a href="#">
                                <svg class="me-1" width="16" height="16">
                                <use xlink:href="#pinterest"></use>
                                </svg>
                            </a>
                        </li>
                        <li class="social ms-4">
                            <a href="#">
                                <svg class="me-1" width="16" height="16">
                                <use xlink:href="#instagram"></use>
                                </svg>
                            </a>
                        </li>
                        <li class="social ms-4">
                            <a href="#">
                                <svg class="me-1" width="16" height="16">
                                <use xlink:href="#youtube"></use>
                                </svg>
                            </a>
                        </li>
                    </ul>

                </div>
            </div>
        </nav>
        <nav class="logo text-center my-2">
            <div class="container ">
                <a href="index.jsp"> <img src="${pageContext.request.contextPath}/images/logo.png" alt="logo" width="200"></a>
            </div>

        </nav>
        <nav id="primary-header " class="navbar navbar-expand-lg py-2 border-top">

            <div class="container ">

                <button class="navbar-toggler border-0 d-flex d-lg-none order-3 p-2 shadow-none" type="button"
                        data-bs-toggle="offcanvas" data-bs-target="#bdNavbar" aria-controls="bdNavbar" aria-expanded="false">
                    <svg class="navbar-icon" width="60" height="60">
                    <use xlink:href="#navbar-icon"></use>
                    </svg>
                </button>
                <div class="header-bottom offcanvas offcanvas-end" id="bdNavbar" aria-labelledby="bdNavbarOffcanvasLabel">
                    <div class="offcanvas-header px-4 pb-0">
                        <button type="button" class="btn-close btn-close-black mt-2" data-bs-dismiss="offcanvas" aria-label="Close"
                                data-bs-target="#bdNavbar"></button>
                    </div>
                    <div class="offcanvas-body align-items-center justify-content-center">
                        <ul class="navbar-nav align-items-center mb-2 mb-lg-0">
                            <li class="nav-item px-3">
                                <a class="nav-link active p-0" aria-current="page" href="${pageContext.request.contextPath}/index.html">Home</a>
                            </li>
                            <li class="nav-item px-3">
                                <a class="nav-link p-0" href="${pageContext.request.contextPath}/trips">List Trip</a>
                            </li>
                            <li class="nav-item px-3">
                                <a class="nav-link p-0" href="${pageContext.request.contextPath}/mytrips">My Trip</a>
                            </li>
                            <li class="nav-item px-3">
                                <a class="nav-link p-0" href="gallery.html">About Us</a>
                            </li>

                        </ul>
                    </div>
                </div>
                <div class="nav-icon" style="
                     position: absolute !important;
                     right: 20px !important;
                     top: 50% !important;
                     transform: translateY(-50%) !important;
                     ">
                    <ul class="list-unstyled d-flex justify-content-between align-items-center m-0">
                        <c:if test="${not empty sessionScope.currentUser}">
                            <li class="nav-icons pe-3 d-flex align-items-center text-black">
                                <span>Welcome, ${sessionScope.currentUser.name}!</span>
                            </li>
                            <li class="nav-icons pe-3">
                                <a href="/group-trip/login">
                                    <svg class="me-1" width="16" height="16">
                                    <use xlink:href="#user"></use>
                                    </svg>
                                </a>
                            </li>
                            <li class="nav-icons search-item pe-3">
                                <a href="#" class="search-button">
                                    <svg class="me-1" width="16" height="16">
                                    <use xlink:href="#search"></use>
                                    </svg>
                                </a>
                            </li>
                            <li class="nav-icons pe-3">
                                <a href="/group-trip/logout" class="text-black">LOGOUT</a>
                            </li>
                        </c:if>

                        <!-- Nếu chưa đăng nhập -->
                        <c:if test="${empty sessionScope.currentUser}">
                            <li class="nav-icons pe-3">
                                <a href="/group-trip/login">
                                    <svg class="me-1" width="16" height="16">
                                    <use xlink:href="#user"></use>
                                    </svg>
                                </a>
                            </li>

                            <li class="nav-icons search-item pe-3">
                                <a href="#" class="search-button">
                                    <svg class="me-1" width="16" height="16">
                                    <use xlink:href="#search"></use>
                                    </svg>
                                </a>
                            </li>
                        </c:if>
                    </ul>
                </div>

            </div>
        </nav>
    </header>
    <script src="${pageContext.request.contextPath}/assets/js/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/plugins.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/swiper/swiper-bundle.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/script.js"></script>
    <script src="https://code.iconify.design/iconify-icon/1.0.7/iconify-icon.min.js"></script>
</html>