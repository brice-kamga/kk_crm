<%@ page import="java.util.List" %>
<%@ page import="it.kk_crm.model.mo.Note" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Recupero dati
    List<Note> note = (List<Note>) request.getAttribute("note");
    String action = (note != null) ? "view" : "notview";
    String nome = (String) request.getAttribute("nome");
    String cognome = (String) request.getAttribute("cognome");
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

    function modifyNota(id) {
        var f = document.modifyForm;
        f.id.value = id;
        f.submit();
    }

    function searchNota() {
        var f = document.searchForm;
        f.controllerAction.value = "NoteController.loadNote";
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
    <span class="text-muted fw-light">Gestionale /</span> Note
</h4>

<div class="row">

    <div class="col-md-4 mb-4">
        <div class="card mb-4">
            <h5 class="card-header">Cerca Note Cliente</h5>
            <div class="card-body">
                <form name="searchForm" action="Dispatcher" method="post" class="needs-validation" novalidate>
                    <div class="mb-3">
                        <label for="nome" class="form-label">Nome Cliente</label>
                        <div class="input-group input-group-merge">
                            <span class="input-group-text"><i class="bx bx-user"></i></span>
                            <input type="text" class="form-control" id="nome" name="nome" placeholder="Es: Mario"
                                   value="<%= (nome != null) ? nome : "" %>" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="cognome" class="form-label">Cognome Cliente</label>
                        <div class="input-group input-group-merge">
                            <span class="input-group-text"><i class="bx bx-user"></i></span>
                            <input type="text" class="form-control" id="cognome" name="cognome" placeholder="Es: Rossi"
                                   value="<%= (cognome != null) ? cognome : "" %>" required>
                        </div>
                    </div>

                    <button type="button" class="btn btn-primary w-100" onclick="javascript:searchNota()">
                        <span class="tf-icons bx bx-search me-1"></span> Cerca
                    </button>

                    <input type="hidden" name="controllerAction"/>
                </form>

                <% if(note != null && note.size() < 1) { %>
                <div class="alert alert-warning mt-3 mb-0" role="alert">
                    Nessuna nota trovata per questo cliente.
                </div>
                <% } %>

                <%-- Mostra il box azzurro SOLO se nome o cognome non sono vuoti --%>
                <%
                    boolean ricercaAttiva = (nome != null && !nome.trim().isEmpty()) || (cognome != null && !cognome.trim().isEmpty());
                    if(note != null && note.size() > 0 && ricercaAttiva) {
                %>
                <div class="alert alert-info mt-3 mb-0" role="alert">
                    Risultati per: <strong><%= (nome != null) ? nome : "" %> <%= (cognome != null) ? cognome : "" %></strong>
                </div>
                <% } %>
            </div>
        </div>
    </div>

    <div class="col-md-8">
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">Elenco Note</h5>
                <button type="button" class="btn btn-primary" onclick="javascript:insert()">
                    <span class="tf-icons bx bx-plus me-1"></span> Nuova Nota
                </button>
            </div>

            <div class="table-responsive text-nowrap">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>Data</th>
                        <th>Nota</th>
                        <th>Autore</th>
                        <th>CF Cliente</th>
                        <th>Azioni</th>
                    </tr>
                    </thead>
                    <tbody class="table-border-bottom-0">
                    <c:choose>
                        <c:when test="${not empty note}">
                            <c:forEach items="${note}" var="n">
                                <tr>
                                    <td><span class="badge bg-label-info me-1">${n.data}</span></td>
                                    <td>
                                            <span class="d-inline-block text-truncate" style="max-width: 150px;" title="${n.nota}">
                                                    ${n.nota}
                                            </span>
                                    </td>
                                    <td><small>${n.utente}</small></td>
                                    <td>${n.cliente_cf}</td>
                                    <td>
                                        <a href="javascript:modifyNota(${n.id})" class="btn btn-sm btn-icon btn-outline-primary" title="Modifica">
                                            <span class="tf-icons bx bx-edit-alt"></span>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="text-center py-5 text-muted">
                                    <i class="bx bx-search-alt-2 fs-1 mb-2"></i>
                                    <p>Nessuna Nota</p>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<form name="modifyForm" method="post" action="Dispatcher">
    <input type="hidden" name="id"/>
    <input type="hidden" name="controllerAction" value="NoteController.modifyView"/>
    <input type="hidden" name="status" value="modify">
    <input type="hidden" name="nome" value="<%= (nome != null) ? nome : "" %>">
    <input type="hidden" name="cognome" value="<%= (cognome != null) ? cognome : "" %>">
</form>

<form name="insertForm" method="post" action="Dispatcher">
    <input type="hidden" name="controllerAction" value="NoteController.modifyView">
    <input type="hidden" name="status" value="insert">
</form>

<%@ include file="../../include/footer.jsp" %>

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
