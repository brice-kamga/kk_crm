<%@ page import="java.time.LocalDate" %>
<%@ page import="it.kk_crm.model.mo.Note" %>
<%@ page import="it.kk_crm.model.mo.Cliente" %>
<%@ page import="java.util.List" %>
<%@ page import="it.kk_crm.model.mo.Utente" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    Note nota = (Note) request.getAttribute("nota");
    String nome = (String) request.getAttribute("nome");
    String cognome = (String) request.getAttribute("cognome");

    // Recuperiamo le liste dalla request (assicurati che il controller le passi sempre!)
    // Se sono null, inizializziamo liste vuote per evitare crash nel foreach
    List<Cliente> clienti = (List<Cliente>) request.getAttribute("clienti");
    List<Utente> utenti = (List<Utente>) request.getAttribute("utenti");

    String action = (nota != null) ? "modify" : "insert";
    LocalDate today = LocalDate.now();

    // Evita caching
    response.setHeader("Cache-Control","no-cache");
    response.setHeader("Cache-Control","no-store");
%>

<%@ include file="../../include/header.jsp" %>

<script>
    var status = "<%=action%>";

    function submitNote() {
        var f = document.insertForm;
        f.controllerAction.value = "NoteController." + status;
    }

    function deleteNote() {
        if(confirm("Sei sicuro di voler eliminare questa nota?")) {
            document.deleteForm.submit();
        }
    }

    function goback() {
        document.backForm.submit();
    }

    function onLoadHandler() {
        if(status == "modify") {
            document.getElementById("btnDelete").addEventListener("click", deleteNote);
        }
        document.getElementById("btnBack").addEventListener("click", goback);
    }

    window.addEventListener("load", onLoadHandler);
</script>

<h4 class="fw-bold py-3 mb-4">
    <span class="text-muted fw-light">Note /</span> <%=(action.equals("modify")) ? "Modifica Nota" : "Nuova Nota"%>
</h4>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0"><%=(action.equals("modify")) ? "Nota #" + nota.getId() : "Nuova Annotazione"%></h5>
                <small class="text-muted float-end">Dettagli interazione</small>
            </div>

            <div class="card-body">
                <form name="insertForm" action="Dispatcher" method="post" class="needs-validation" novalidate onsubmit="submitNote()">

                    <div class="row g-3">
                        <% if (action.equals("modify")) { %>
                        <div class="col-md-2">
                            <label class="form-label">ID</label>
                            <input type="text" class="form-control" disabled value="<%=nota.getId()%>">
                            <input type="hidden" name="id" value="<%=nota.getId()%>">
                        </div>
                        <div class="col-md-10"></div> <% } %>

                        <div class="col-md-4">
                            <label for="data" class="form-label">Data</label>
                            <input type="date" class="form-control" id="data" name="data"
                                   value="<%=(action.equals("modify")) ? nota.getData() : today%>" required>
                            <div class="invalid-feedback">Inserisci una data valida.</div>
                        </div>

                        <div class="col-md-8">
                            <label for="nota" class="form-label">Testo Nota</label>
                            <div class="input-group input-group-merge">
                                <span class="input-group-text"><i class="bx bx-pencil"></i></span>
                                <input type="text" class="form-control" id="nota" name="nota" maxlength="45"
                                       value="<%=(action.equals("modify")) ? nota.getNota() : ""%>"
                                       placeholder="Es: Cliente interessato..." required>
                                <div class="invalid-feedback">Il testo è obbligatorio.</div>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label for="cliente_cf" class="form-label">Cliente Riferimento</label>
                            <div class="input-group input-group-merge">
                                <span class="input-group-text"><i class="bx bx-user"></i></span>
                                <select class="form-select" id="cliente_cf" name="cliente_cf" required>
                                    <option value="" disabled selected>Seleziona Cliente...</option>
                                    <%
                                        String currentCf = (action.equals("modify")) ? nota.getCliente_cf() : "";
                                    %>
                                    <c:forEach items="${clienti}" var="c">
                                        <option value="${c.CF}" ${c.CF eq currentCf ? 'selected' : ''}>
                                                ${c.nome} ${c.cognome}
                                        </option>
                                    </c:forEach>
                                </select>
                                <div class="invalid-feedback">Seleziona un cliente.</div>
                            </div>
                        </div>

                        <% if (action.equals("modify")) { %>
                        <div class="col-md-6">
                            <label for="utente" class="form-label">Autore Nota</label>
                            <select class="form-select" id="utente" name="utente" required>
                                <%
                                    String currentUser = (action.equals("modify")) ? nota.getUtente() : "";
                                %>
                                <c:forEach items="${utenti}" var="u">
                                    <option value="${u.username}" ${u.username eq currentUser ? 'selected' : ''}>
                                            ${u.username}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <% } %>
                    </div>

                    <div class="mt-4 pt-3 border-top">
                        <button type="submit" class="btn btn-primary me-2">
                            <i class="bx bx-save me-1"></i> <%=(action.equals("modify")) ? "Salva Modifiche" : "Inserisci Nota"%>
                        </button>

                        <% if(action.equals("modify")) { %>
                        <input type="hidden" name="nome" value="<%=nome%>">
                        <input type="hidden" name="cognome" value="<%=cognome%>">
                        <button type="button" class="btn btn-danger me-2" id="btnDelete">
                            <i class="bx bx-trash me-1"></i> Elimina
                        </button>
                        <% } %>

                        <button type="button" class="btn btn-outline-secondary" id="btnBack">
                            <i class="bx bx-arrow-back me-1"></i> Annulla
                        </button>
                    </div>

                    <input type="hidden" name="controllerAction"/>
                </form>
            </div>
        </div>
    </div>
</div>

<% if(action.equals("modify")) { %>
<form name="deleteForm" method="post" action="Dispatcher">
    <input type="hidden" name="controllerAction" value="NoteController.delete"/>
    <input type="hidden" name="id" value="<%=nota.getId()%>">
    <input type="hidden" name="azione" value="delete">
    <input type="hidden" name="nome" value="<%=nome%>">
    <input type="hidden" name="cognome" value="<%=cognome%>">
</form>
<% } %>

<form name="backForm" method="post" action="Dispatcher">
    <% if(action.equals("modify")) { %>
    <input type="hidden" name="controllerAction" value="NoteController.loadNote"/>
    <% } else { %>
    <input type="hidden" name="controllerAction" value="NoteController.view"/>
    <% } %>
    <input type="hidden" name="nome" value="<%=nome%>">
    <input type="hidden" name="cognome" value="<%=cognome%>">
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
