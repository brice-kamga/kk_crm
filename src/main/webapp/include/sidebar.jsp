<%@ page import="it.kk_crm.model.mo.Utente" %>
<aside id="layout-menu" class="layout-menu menu-vertical menu bg-menu-theme">
    <div class="app-brand demo">
        <a href="Dispatcher?controllerAction=DashboardController.view" class="app-brand-link">
            <span class="app-brand-logo demo">
                <img src="img/logoKK.png" alt="Logo K&K" width="25" height="auto">
            </span>
            <span class="app-brand-text demo menu-text fw-bolder ms-2">K&K CRM</span>
        </a>

        <a href="javascript:void(0);" class="layout-menu-toggle menu-link text-large ms-auto d-block d-xl-none">
            <i class="bx bx-chevron-left bx-sm align-middle"></i>
        </a>
    </div>

    <div class="menu-inner-shadow"></div>

    <ul class="menu-inner py-1">
        <li class="menu-item active">
            <a href="Dispatcher?controllerAction=DashboardController.view" class="menu-link">
                <i class="menu-icon tf-icons bx bx-home-circle"></i>
                <div data-i18n="Analytics">Dashboard</div>
            </a>
        </li>

        <li class="menu-header small text-uppercase">
            <span class="menu-header-text">Gestionale</span>
        </li>

        <li class="menu-item">
            <a href="Dispatcher?controllerAction=AziendaController.loadAziende" class="menu-link">
                <i class="menu-icon tf-icons bx bx-buildings"></i>
                <div data-i18n="Aziende">Aziende & Clienti</div>
            </a>
        </li>

        <li class="menu-item">
            <a href="Dispatcher?controllerAction=AppuntamentiController.view" class="menu-link">
                <i class="menu-icon tf-icons bx bx-calendar"></i>
                <div data-i18n="Appuntamenti">Appuntamenti</div>
            </a>
        </li>

        <li class="menu-item">
            <a href="Dispatcher?controllerAction=AziendaController.loadFiltri" class="menu-link">
                <i class="menu-icon tf-icons bx bx-search"></i>
                <div data-i18n="Ricerca">Ricerca Aziende</div>
            </a>
        </li>

        <li class="menu-item">
            <a href="Dispatcher?controllerAction=NoteController.view" class="menu-link">
                <i class="menu-icon tf-icons bx bx-note"></i>
                <div data-i18n="Note">Note</div>
            </a>
        </li>

        <li class="menu-item">
            <a href="Dispatcher?controllerAction=ProposteController.view" class="menu-link">
                <i class="menu-icon tf-icons bx bx-file"></i>
                <div data-i18n="Proposte">Proposte</div>
            </a>
        </li>

        <%
            Utente currentUserSidebar = (Utente) request.getAttribute("loggedUser");
            if (currentUserSidebar != null && "admin".equals(currentUserSidebar.getTipo())) {
        %>
        <li class="menu-header small text-uppercase">
            <span class="menu-header-text">Amministrazione</span>
        </li>
        <li class="menu-item">
            <a href="Dispatcher?controllerAction=AdminController.utenti" class="menu-link">
                <i class="menu-icon tf-icons bx bx-user"></i>
                <div data-i18n="Utenti">Gestione Utenti</div>
            </a>
        </li>
        <li class="menu-item">
            <a href="Dispatcher?controllerAction=AdminController.servizi" class="menu-link">
                <i class="menu-icon tf-icons bx bx-cog"></i>
                <div data-i18n="Servizi">Gestione Servizi</div>
            </a>
        </li>
        <% } %>
    </ul>
</aside>
