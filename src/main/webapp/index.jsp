<%@ page session="false" %>
<% String contextPath = request.getContextPath(); %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="it" class="light-style" dir="ltr" data-theme="theme-default" data-assets-path="<%=contextPath%>/">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />

    <title>Caricamento... | CRM BK</title>

    <link rel="icon" type="image/x-icon" href="<%=contextPath%>/img/favicon/favicon.ico" />

    <link rel="stylesheet" href="<%=contextPath%>/vendor/css/core.css" />
    <link rel="stylesheet" href="<%=contextPath%>/vendor/css/theme-default.css" />
    <link rel="stylesheet" href="<%=contextPath%>/css/demo.css" />

    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background-color: #f5f5f9;
            margin: 0;
            font-family: "Inter", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Fira Sans", "Droid Sans", "Helvetica Neue", sans-serif;
        }
        .loader-container {
            text-align: center;
        }
        .app-brand-logo svg {
            width: 50px;
            height: 50px;
            margin-bottom: 20px;
        }
    </style>

    <meta http-equiv="refresh" content="0; url=<%=contextPath%>/Dispatcher">
    <script type="text/javascript">
        function onLoadHandler() {
            // Piccolo timeout opzionale per evitare "flickering" istantaneo,
            // oppure redirect immediato. Qui è immediato.
            window.location.href = "<%=contextPath%>/Dispatcher";
        }
        window.addEventListener("load", onLoadHandler);
    </script>
</head>

<body>
<div class="loader-container">
    <div class="app-brand-logo">
        <svg viewBox="0 0 25 42" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
            <path d="M13.7918663,0.358365126 L3.39788168,7.44174259 C0.566865006,9.69408886 -0.379795268,12.4788597 0.557900856,15.7960551 C0.68998853,16.2305145 1.09562888,17.7872135 3.12357076,19.2293357 C3.8146334,19.7207684 5.32369333,20.3834223 7.65075054,21.2172976 L7.59773219,21.2525164 L2.63468769,24.5493413 C0.445452254,26.3002124 0.0884951797,28.5083815 1.56381646,31.1738486 C2.83770406,32.8170431 5.20850219,33.2640127 7.09180128,32.5391577 C8.347334,32.0559211 11.4559176,30.0011079 16.4175519,26.3747182 C18.0338572,24.4997857 18.6973423,22.4544883 18.4080071,20.2388261 C17.963753,17.5346866 16.1776345,15.5799961 13.0496516,14.3747546 L10.9194936,13.4715819 L18.6192054,7.984237 L13.7918663,0.358365126 Z" fill="#696cff"></path>
        </svg>
    </div>

    <div class="spinner-border text-primary" role="status" style="width: 3rem; height: 3rem;">
        <span class="visually-hidden">Caricamento...</span>
    </div>

    <h5 class="mt-3 text-muted">Avvio CRM...</h5>

    <noscript>
        <p>Se non vieni reindirizzato automaticamente, <a href="<%=contextPath%>/Dispatcher">clicca qui</a>.</p>
    </noscript>
</div>
</body>
</html>
