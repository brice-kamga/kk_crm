<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="it" class="light-style layout-menu-fixed" dir="ltr" data-theme="theme-default"
      data-assets-path="${pageContext.request.contextPath}/" data-template="vertical-menu-template-free">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />

    <title>K&K CRM</title>

    <meta name="description" content="" />

    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon/favicon.ico" />

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/vendor/fonts/iconify-icons.css" />
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/vendor/css/core.css" class="template-customizer-core-css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/vendor/css/theme-default.css" class="template-customizer-theme-css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/demo.css" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

    <script src="${pageContext.request.contextPath}/vendor/js/helpers.js"></script>
    <script src="${pageContext.request.contextPath}/js/config.js"></script>
</head>

<body>
<div class="layout-wrapper layout-content-navbar">
    <div class="layout-container">

        <%@ include file="sidebar.jsp" %>
        <div class="layout-page">

            <nav class="layout-navbar container-xxl navbar navbar-expand-xl navbar-detached align-items-center bg-navbar-theme" id="layout-navbar">
                <div class="layout-menu-toggle navbar-nav align-items-xl-center me-3 me-xl-0 d-xl-none">
                    <a class="nav-item nav-link px-0 me-xl-4" href="javascript:void(0)">
                        <i class="mdi mdi-menu mdi-24px"></i>
                    </a>
                </div>

                <div class="navbar-nav-right d-flex align-items-center" id="navbar-collapse">
                    <ul class="navbar-nav flex-row align-items-center ms-auto">
                        <li class="nav-item navbar-dropdown dropdown-user dropdown">
                            <a class="nav-link dropdown-toggle hide-arrow" href="javascript:void(0);" data-bs-toggle="dropdown">
                                <div class="avatar avatar-online">
                                    <img src="${pageContext.request.contextPath}/img/avatars/${loggedUser.username}.png"
                                         onerror="this.src='${pageContext.request.contextPath}/img/avatars/1.png'"
                                         alt class="w-px-40 h-auto rounded-circle" />
                                </div>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li>
                                    <a class="dropdown-item" href="#">
                                        <div class="d-flex">
                                            <div class="flex-shrink-0 me-3">
                                                <div class="avatar avatar-online">
                                                    <img src="${pageContext.request.contextPath}/img/avatars/${loggedUser.username}.png"
                                                         onerror="this.src='${pageContext.request.contextPath}/img/avatars/1.png'"
                                                         alt class="w-px-40 h-auto rounded-circle" />
                                                </div>
                                            </div>
                                            <div class="flex-grow-1">
                                                <span class="fw-semibold d-block">${loggedUser.username}</span>
                                                <small class="text-muted">${loggedUser.tipo}</small>
                                            </div>
                                        </div>
                                    </a>
                                </li>
                                <li>
                                    <div class="dropdown-divider"></div>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="Dispatcher?controllerAction=LoginController.logout">
                                        <i class="mdi mdi-power me-2"></i>
                                        <span class="align-middle">Log Out</span>
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </nav>
            <div class="content-wrapper">
                <div class="container-xxl flex-grow-1 container-p-y">
