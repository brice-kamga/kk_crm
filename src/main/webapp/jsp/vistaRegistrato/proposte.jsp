<%@ page import="java.util.List" %>
<%@ page import="it.kk_crm.model.mo.ProposteS" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Recupero dati
    List<ProposteS> proposte = (List<ProposteS>) request.getAttribute("proposte");
    String applicationMessage = (String) request.getAttribute("applicationMessage");

    // Evita caching
    response.setHeader("Cache-Control","no-cache");
    response.setHeader("Cache-Control","no-store");
%>

<%@ include file="../../include/header.jsp" %>

<script>
    var applicationMessage;
    <% if (applicationMessage != null) { %>
    applicationMessage = "<%=applicationMessage%>";
    <% } %>

    function insert() {
        document.insertForm.submit();
    }

    function deleteProposta(id){
        let text = "Sicuro di voler eliminare la proposta selezionata?";
        if (confirm(text) == true) {
            var f = document.deleteForm;
            f.id.value = id;
            f.submit();
        }
    }

    function onLoadHandler() {
        if (applicationMessage != undefined) alert(applicationMessage);
    }

    window.addEventListener("load", onLoadHandler);
</script>

<h4 class="fw-bold py-3 mb-4">
    <span class="text-muted fw-light">Gestionale /</span> Proposte
</h4>

<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="mb-0">Proposte Attive</h5>
        <button type="button" class="btn btn-primary" onclick="javascript:insert()">
            <span class="tf-icons bx bx-plus me-1"></span> Nuova Proposta
        </button>
    </div>

    <div class="table-responsive text-nowrap">
        <table class="table table-hover">
            <thead>
            <tr>
                <th>Descrizione Proposta</th>
                <th>Servizio Offerto</th>
                <th>Azienda Cliente</th>
                <th>Codici Rif.</th>
                <th>Azioni</th>
            </tr>
            </thead>
            <tbody class="table-border-bottom-0">
            <c:choose>
                <c:when test="${not empty proposte}">
                    <c:forEach items="${proposte}" var="p">
                        <tr>
                            <td>
                                <i class="bx bx-file text-success me-2"></i>
                                <strong>${p.tipo}</strong>
                            </td>
                            <td>
                                <span class="badge bg-label-primary">${p.nome_servizio}</span>
                            </td>
                            <td>
                                <div class="d-flex flex-column">
                                    <span>${p.nome}</span>
                                    <small class="text-muted">P.IVA: ${p.PIva}</small>
                                </div>
                            </td>
                            <td>
                                <small class="text-muted">ID: ${p.id} / SRV: ${p.codice_servizio}</small>
                            </td>
                            <td>
                                <a href="javascript:deleteProposta(${p.id})" class="btn btn-icon btn-outline-danger" title="Elimina">
                                    <i class="bx bx-trash-alt"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="5" class="text-center py-5">
                            <i class="bx bx-folder-open fs-1 text-muted mb-2"></i>
                            <p class="text-muted">Nessuna proposta attiva trovata.</p>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>

<form name="deleteForm" method="post" action="Dispatcher">
    <input type="hidden" name="id"/>
    <input type="hidden" name="controllerAction" value="ProposteController.delete"/>
</form>

<form name="insertForm" method="post" action="Dispatcher">
    <input type="hidden" name="controllerAction" value="ProposteController.insertView"/>
</form>

<%@ include file="../../include/footer.jsp" %>
