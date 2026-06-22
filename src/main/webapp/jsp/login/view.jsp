<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.kk_crm.model.mo.Utente" %>

<%
    // Recupero messaggi dal controller
    String applicationMessage = (String) request.getAttribute("applicationMessage");
%>

<!DOCTYPE html>
<html lang="it" class="light-style customizer-hide" dir="ltr" data-theme="theme-default"
      data-assets-path="${pageContext.request.contextPath}/" data-template="vertical-menu-template-free">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />

    <title>Login</title>

    <meta name="description" content="" />

    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon/favicon.ico" />

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/vendor/fonts/iconify-icons.css" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/vendor/css/core.css" class="template-customizer-core-css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/vendor/css/theme-default.css" class="template-customizer-theme-css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/demo.css" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/vendor/css/pages/page-auth.css" />

    <script src="${pageContext.request.contextPath}/vendor/js/helpers.js"></script>
    <script src="${pageContext.request.contextPath}/js/config.js"></script>
</head>

<body>
<div class="container-xxl">
    <div class="authentication-wrapper authentication-basic container-p-y">
        <div class="authentication-inner">
            <div class="card">
                <div class="card-body">
                    <div class="app-brand justify-content-center">
                        <a href="#" class="app-brand-link gap-2">
                            <img src="img/logoKK.png" alt="Logo K&K" width="25" height="auto">
                            <span class="app-brand-text demo text-body fw-bolder">K&K CRM</span>
                        </a>
                    </div>
                    <hr>
                    <p class="mb-4">Accedi al tuo account per gestire i clienti.</p>

                    <% if (applicationMessage != null && !applicationMessage.isEmpty()) { %>
                    <div class="alert alert-danger d-flex align-items-center" role="alert">
                        <i class="bx bx-error-circle me-2"></i>
                        <div><%= applicationMessage %></div>
                    </div>
                    <% } %>

                    <form id="formAuthentication" class="mb-3 needs-validation" action="Dispatcher" method="POST" novalidate>

                        <div class="mb-3">
                            <label for="username" class="form-label">Username</label>
                            <input type="text" class="form-control" id="username" name="username"
                                   placeholder="Inserisci il tuo username" autofocus required />
                            <div class="invalid-feedback">Inserisci lo username.</div>
                        </div>

                        <div class="mb-3 form-password-toggle">
                            <div class="d-flex justify-content-between">
                                <label class="form-label" for="password">Password</label>
                            </div>
                            <div class="input-group input-group-merge">
                                <input type="password" id="password" class="form-control" name="password"
                                       placeholder="&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;&#xb7;"
                                       aria-describedby="password" required />
                                <span class="input-group-text cursor-pointer"><i class="bx bx-hide"></i></span>
                                <div class="invalid-feedback">Inserisci la password.</div>
                            </div>
                        </div>

                        <input type="hidden" name="controllerAction" value="LoginController.logon"/>

                        <div class="mb-3">
                            <button class="btn btn-primary d-grid w-100" type="submit">Login</button>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/vendor/libs/jquery/jquery.js"></script>
<script src="${pageContext.request.contextPath}/vendor/libs/popper/popper.js"></script>
<script src="${pageContext.request.contextPath}/vendor/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
<script src="${pageContext.request.contextPath}/vendor/js/menu.js"></script>
<script src="${pageContext.request.contextPath}/js/main.js"></script>

<script>
    (function () {
        'use strict'
        var forms = document.querySelectorAll('.needs-validation')
        Array.prototype.slice.call(forms).forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }
                form.classList.add('was-validated')
            }, false)
        })
    })()
</script>
</body>
</html>
