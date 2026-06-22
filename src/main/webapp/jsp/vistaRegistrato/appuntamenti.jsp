<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="it.kk_crm.model.mo.Appuntamento" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Recupero dati (Logica esistente mantenuta)
    List<Appuntamento> appuntamenti = (List<Appuntamento>) request.getAttribute("appuntamenti");
    List<Appuntamento> appuntamentiToday = (List<Appuntamento>) request.getAttribute("appuntamentiToday");
    String action =(appuntamentiToday != null) ? "view" : "notview";
    String selected = (String) request.getAttribute("data");
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

    function deleteAppuntamento(codice) {
        f = document.deleteForm;
        f.codice.value = codice;
        f.submit();
    }

    function search(data) {
        var f;
        f = document.searchForm;
        f.controllerAction.value = "AppuntamentiController.loadFilter";
        f.data.value = data;
        f.submit();
    }

    function insert() {
        document.insertForm.submit();
    }

    function deleteDialog(id){
        let text = "Sicuro di voler eliminare l'appuntamento selezionato?";
        if (confirm(text) == true) {
            deleteAppuntamento(id)
        }
    }

    function message() {
        // Usa i Toast di Bootstrap se possibile, altrimenti alert classico
        alert(applicationMessage);
    }

    function onLoadHandler() {
        if (applicationMessage != undefined) message();
    }

    window.addEventListener("load", onLoadHandler);
</script>

<h4 class="fw-bold py-3 mb-4">
    <span class="text-muted fw-light">Gestionale /</span> Appuntamenti
</h4>

<div class="row">

    <div class="col-md-4 mb-4">
        <div class="card mb-4">
            <h5 class="card-header">Filtra per Data</h5>
            <div class="card-body">
                <form name="searchForm" action="Dispatcher" method="post">
                    <div class="mb-3">
                        <label for="data" class="form-label">Seleziona Data</label>
                        <input type="date" class="form-control" id="data" name="data"
                               onchange="javascript:search(this.value)" required />
                    </div>
                    <input type="hidden" name="controllerAction"/>
                    <input type="hidden" name="data"/>
                </form>

                <% if(appuntamentiToday != null && appuntamentiToday.size() < 1) { %>
                <div class="alert alert-warning mt-3" role="alert">
                    Nessun appuntamento trovato per questa data.
                </div>
                <% } %>

                <% if(appuntamentiToday != null && appuntamentiToday.size() > 0) { %>
                <div class="alert alert-info mt-3" role="alert">
                    Risultati trovati per: <strong><%=selected%></strong>
                </div>
                <% } %>
            </div>
        </div>
    </div>

    <div class="col-md-8">

        <% if(action.equals("view") && appuntamentiToday.size() > 0) { %>
        <div class="card mb-4 border border-info"> <div class="card-header bg-label-info">
            <h5 class="mb-0 text-info">Risultati Ricerca (<%=selected%>)</h5>
        </div>
            <div class="table-responsive text-nowrap">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>Data</th>
                        <th>Nota</th>
                        <th>Azienda</th>
                        <th>Azioni</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Appuntamento app : appuntamentiToday) { %>
                    <tr>
                        <td><span class="badge bg-info"><%=app.getData()%></span></td>
                        <td><strong><%=app.getNote()%></strong></td>
                        <td><%=app.getNome()%></td>
                        <td>
                            <a href="javascript:deleteDialog(<%=app.getCodice()%>)" class="btn btn-sm btn-icon btn-outline-danger">
                                <span class="tf-icons bx bx-trash"></span>
                            </a>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
        <% } %>

        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">Elenco Completo Appuntamenti</h5>
                <button type="button" class="btn btn-primary" onclick="javascript:insert()">
                    <span class="tf-icons bx bx-plus me-1"></span> Nuovo Appuntamento
                </button>
            </div>

            <div class="table-responsive text-nowrap">
                <% if(appuntamenti != null && appuntamenti.size() > 0) { %>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>Data</th>
                        <th>Nota</th>
                        <th>Azienda</th>
                        <th>Referente</th>
                        <th>Azioni</th>
                    </tr>
                    </thead>
                    <tbody class="table-border-bottom-0">
                    <% for (Appuntamento app : appuntamenti) { %>
                    <tr>
                        <td><span class="badge bg-label-primary me-1"><%=app.getData()%></span></td>
                        <td><strong><%=app.getNote()%></strong></td>
                        <td><%=app.getNome()%></td>
                        <td>
                            <div class="d-flex flex-column">
                                <span class="fw-semibold"><%=app.getCognome()%></span>
                                <small class="text-muted"><%=app.getCF()%></small>
                            </div>
                        </td>
                        <td>
                            <a href="javascript:deleteDialog(<%=app.getCodice()%>)" class="btn btn-icon btn-outline-danger" title="Elimina">
                                <i class="bx bx-trash-alt"></i>
                            </a>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
                <% } else { %>
                <div class="p-4 text-center">
                    <p class="text-muted">Nessun appuntamento presente.</p>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</div>

<form name="deleteForm" method="post" action="Dispatcher">
    <input type="hidden" name="codice"/>
    <input type="hidden" name="controllerAction" value="AppuntamentiController.delete"/>
</form>

<form name="insertForm" method="post" action="Dispatcher">
    <input type="hidden" name="controllerAction" value="AppuntamentiController.insertView">
</form>

<%@ include file="../../include/footer.jsp" %>
