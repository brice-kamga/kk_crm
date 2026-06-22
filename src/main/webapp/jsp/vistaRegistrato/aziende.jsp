<%@ page import="java.util.List" %>
<%@ page import="it.kk_crm.model.mo.Azienda" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Recupero dati (Logica esistente mantenuta)
    List<Azienda> aziende = (List<Azienda>) request.getAttribute("aziende");
    String applicationMessage = (String) request.getAttribute("applicationMessage");

    response.setHeader("Cache-Control","no-cache");
    response.setHeader("Cache-Control","no-store");
%>

<%@ include file="../../include/header.jsp" %>

<script>
    var applicationMessage;
    <% if (applicationMessage != null) { %>
    applicationMessage = "<%=applicationMessage%>";
    <% } %>

    function modifyAzienda(P_Iva) {
        var f = document.modifyForm;
        f.P_Iva.value = P_Iva;
        f.submit();
    }

    function insert() {
        document.insertForm.submit();
    }

    function onLoadHandler() {
        if (applicationMessage != undefined) alert(applicationMessage);
    }

    window.addEventListener("load", onLoadHandler);
</script>

<h4 class="fw-bold py-3 mb-4">
    <span class="text-muted fw-light">Gestionale /</span> Aziende
</h4>

<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="mb-0">Elenco Aziende</h5>
        <button type="button" class="btn btn-primary" onclick="javascript:insert()">
            <span class="tf-icons bx bx-plus me-1"></span> Nuova Azienda
        </button>
    </div>

    <div class="table-responsive text-nowrap">
        <table class="table table-hover">
            <thead>
            <tr>
                <th>Nome</th>
                <th>P. IVA</th>
                <th>Forma</th>
                <th>Indirizzo</th>
                <th>Contatti</th>
                <th>Cat. Merce</th>
                <th>Tipologia</th>
                <th>Referente</th>
                <th>Azioni</th>
            </tr>
            </thead>
            <tbody class="table-border-bottom-0">
            <c:choose>
                <c:when test="${not empty aziende}">
                    <c:forEach items="${aziende}" var="az">
                        <tr>
                            <td>
                                <i class="bx bxs-business text-info me-3"></i>
                                <strong>${az.nome}</strong>
                            </td>
                            <td>${az.p_Iva}</td>
                            <td><span class="badge bg-label-primary me-1">${az.forma}</span></td>
                            <td>${az.indirizzo}</td>
                            <td>
                                <div class="d-flex flex-column">
                                    <span class="text-truncate" style="max-width: 150px;">${az.email}</span>
                                    <small class="text-muted">${az.telefono}</small>
                                </div>
                            </td>
                            <td>${az.catMerce}</td>
                            <td><span class="badge bg-label-warning">${az.tipologia}</span></td>
                            <td>${az.cognomeRef}</td>
                            <td>
                                <div class="dropdown">
                                    <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                        <i class="bx bx-dots-vertical-rounded"></i>
                                    </button>
                                    <div class="dropdown-menu">
                                        <a class="dropdown-item" href="javascript:modifyAzienda('${az.p_Iva}')">
                                            <i class="bx bx-edit-alt me-1"></i> Modifica
                                        </a>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr><td colspan="9" class="text-center py-4">Nessuna azienda presente.</td></tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>

<form name="modifyForm" method="post" action="Dispatcher">
    <input type="hidden" name="P_Iva"/>
    <input type="hidden" name="controllerAction" value="AziendaController.modifyView"/>
    <input type="hidden" name="status" value="modify">
</form>

<form name="insertForm" method="post" action="Dispatcher">
    <input type="hidden" name="controllerAction" value="AziendaController.modifyView">
    <input type="hidden" name="status" value="insert">
</form>

<%@ include file="../../include/footer.jsp" %>
