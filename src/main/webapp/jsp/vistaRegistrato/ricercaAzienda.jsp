<%@ page import="java.util.List" %>
<%@ page import="it.kk_crm.model.mo.Azienda" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Recupero dati
    List<Azienda> aziende = (List<Azienda>) request.getAttribute("aziende");
    String action = (aziende != null) ? "view" : "notview";

    // Le liste sono passate dal controller, JSTL le leggerà direttamente
    // Recupero variabili per feedback utente
    String filtro = (String) request.getAttribute("filtro");
    String tipo = (String) request.getAttribute("type");

    // Evita caching
    response.setHeader("Cache-Control","no-cache");
    response.setHeader("Cache-Control","no-store");
%>

<%@ include file="../../include/header.jsp" %>

<script>
    function loadFiltri(tipo, param) {
        var f = document.searchForm;
        f.filtro.value = param;
        f.type.value = tipo;
        f.submit();
    }
</script>

<h4 class="fw-bold py-3 mb-4">
    <span class="text-muted fw-light">Gestionale /</span> Ricerca Aziende
</h4>

<div class="card mb-4">
    <h5 class="card-header">Filtri di Ricerca</h5>
    <div class="card-body">
        <div class="row gx-3 gy-2 align-items-center">

            <div class="col-md-3">
                <label class="form-label" for="catmerce">Categoria Merceologica</label>
                <select class="form-select" id="catmerce" onchange="loadFiltri('Cat_merce', this.value)">
                    <option selected disabled>Seleziona Categoria...</option>
                    <c:forEach items="${aziendeCat}" var="cat">
                        <option value="${cat}">${cat}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="col-md-3">
                <label class="form-label" for="owner">Utente Assegnatario (Owner)</label>
                <select class="form-select" id="owner" onchange="loadFiltri('Assigned', this.value)">
                    <option selected disabled>Seleziona Utente...</option>
                    <c:forEach items="${aziendeOwner}" var="own">
                        <option value="${own}">${own}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="col-md-3">
                <label class="form-label" for="tipologia">Tipologia Cliente</label>
                <select class="form-select" id="tipologia" onchange="loadFiltri('Tipologia', this.value)">
                    <option selected disabled>Seleziona Tipologia...</option>
                    <c:forEach items="${aziendeTipo}" var="tip">
                        <option value="${tip}">${tip}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="col-md-3">
                <label class="form-label d-block">&nbsp;</label>
                <button type="button" class="btn btn-outline-primary w-100" onclick="loadFiltri('Libera', 'vuoto')">
                    <i class="bx bx-search-alt me-1"></i> Mostra Tutto
                </button>
            </div>
        </div>
    </div>
</div>

<% if(action.equals("view")) { %>
<div class="card">
    <div class="card-header border-bottom">
        <h5 class="mb-0">Risultati Ricerca</h5>
        <% if(!tipo.equals("Libera")) { %>
        <small class="text-muted">Filtro applicato: <strong><%=tipo%> = <%=filtro%></strong></small>
        <% } else { %>
        <small class="text-muted">Elenco completo</small>
        <% } %>
    </div>

    <div class="table-responsive text-nowrap">
        <table class="table table-hover">
            <thead>
            <tr>
                <th>Nome Azienda</th>
                <th>P. IVA</th>
                <th>Forma</th>
                <th>Indirizzo</th>
                <th>Contatti</th>
                <th>Categoria</th>
                <th>Tipologia</th>
                <th>Owner</th>
            </tr>
            </thead>
            <tbody class="table-border-bottom-0">
            <c:choose>
                <c:when test="${not empty aziende}">
                    <c:forEach items="${aziende}" var="az">
                        <tr>
                            <td>
                                <i class="bx bxs-business text-primary me-2"></i>
                                <strong>${az.nome}</strong>
                            </td>
                            <td>${az.p_Iva}</td>
                            <td><span class="badge bg-label-secondary">${az.forma}</span></td>
                            <td>${az.indirizzo}</td>
                            <td>
                                <div class="d-flex flex-column">
                                    <span>${az.email}</span>
                                    <small class="text-muted">${az.telefono}</small>
                                </div>
                            </td>
                            <td>${az.catMerce}</td>
                            <td><span class="badge bg-label-warning">${az.tipologia}</span></td>
                            <td><span class="badge bg-label-info">${az.assigned}</span></td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="8" class="text-center py-5 text-muted">
                            <i class="bx bx-search-alt fs-1 mb-2"></i>
                            <p>Nessuna azienda trovata con questi criteri.</p>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>
<% } %>

<form name="searchForm" method="post" action="Dispatcher">
    <input type="hidden" name="controllerAction" value="AziendaController.loadTable"/>
    <input type="hidden" name="filtro">
    <input type="hidden" name="type">
</form>

<%@ include file="../../include/footer.jsp" %>
