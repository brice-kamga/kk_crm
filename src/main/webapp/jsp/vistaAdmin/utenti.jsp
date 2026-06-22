<%@ page import="it.kk_crm.model.mo.Utente" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    List<Utente> utenti = (List<Utente>) request.getAttribute("utenti");
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

    function insert() {
        var f = document.insertForm;
        f.controllerAction.value = "AdminController.insertUtente";
        // submit gestito dalla validazione
    }

    function deleteDialog(username) {
        if (confirm("Sicuro di voler eliminare l'utente " + username + "?")) {
            var f = document.deleteForm;
            f.username.value = username;
            f.submit();
        }
    }

    // Funzione per aprire il modale e settare i dati
    function openModifyModal(username, currentTipo) {
        // Imposta lo username nel campo hidden o readonly
        document.getElementById('mod_username').value = username;
        document.getElementById('mod_username_display').value = username; // Solo per visualizzazione

        // Imposta la select box al valore attuale dell'utente
        document.getElementById('mod_tipo').value = currentTipo;

        // Apre il modale di Bootstrap
        var myModal = new bootstrap.Modal(document.getElementById('modifyUserModal'));
        myModal.show();
    }

    function onLoadHandler() {
        if (applicationMessage != undefined) alert(applicationMessage);
    }

    window.addEventListener("load", onLoadHandler);
</script>

<h4 class="fw-bold py-3 mb-4">
    <span class="text-muted fw-light">Amministrazione /</span> Gestione Utenti
</h4>

<div class="row">

    <div class="col-md-8 mb-4">
        <div class="card h-100">
            <h5 class="card-header">Elenco Utenti Sistema</h5>
            <div class="table-responsive text-nowrap" style="max-height: 600px; overflow-y: auto;">
                <table class="table table-hover">
                    <thead class="table-light sticky-top">
                    <tr>
                        <th>Username</th>
                        <th>Ruolo</th>
                        <th>Codice Fiscale</th>
                        <th>Stato</th>
                        <th>Azioni</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty utenti}">
                            <c:forEach items="${utenti}" var="u">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <div class="avatar avatar-xs me-2">
                                                    <span class="avatar-initial rounded-circle bg-label-primary">
                                                            ${u.username.substring(0,1).toUpperCase()}
                                                    </span>
                                            </div>
                                            <strong>${u.username}</strong>
                                        </div>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${u.tipo == 'admin'}">
                                                <span class="badge bg-label-danger">Admin</span>
                                            </c:when>
                                            <c:when test="${u.tipo == 'registrato'}">
                                                <span class="badge bg-label-success">Registrato</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-label-secondary">${u.tipo}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><small class="text-muted">${u.CF}</small></td>
                                    <td>
                                        <c:if test="${u.deleted == 'Y'}"><span class="badge bg-danger">Eliminato</span></c:if>
                                        <c:if test="${u.deleted == 'N'}"><span class="badge bg-success">Attivo</span></c:if>
                                    </td>
                                    <td>
                                        <c:if test="${u.deleted == 'N'}">
                                            <%-- Controllo: Mostra i pulsanti SOLO se l'utente non è quello loggato --%>
                                            <c:choose>
                                                <c:when test="${u.username != loggedUser.username}">
                                                    <a href="javascript:openModifyModal('${u.username}', '${u.tipo}')" class="btn btn-sm btn-icon btn-outline-primary me-1" title="Modifica Ruolo">
                                                        <span class="tf-icons bx bx-pencil"></span>
                                                    </a>
                                                    <a href="javascript:deleteDialog('${u.username}')" class="btn btn-sm btn-icon btn-outline-danger" title="Elimina">
                                                        <span class="tf-icons bx bx-trash"></span>
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-label-secondary">Tu</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="text-center py-5">
                                    <p class="text-muted">Nessun utente trovato.</p>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="col-md-4">
        <div class="card mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">Nuovo Utente</h5>
            </div>
            <div class="card-body">
                <form name="insertForm" method="post" action="Dispatcher" class="needs-validation" novalidate onsubmit="insert()">
                    <input type="hidden" name="controllerAction"/>

                    <div class="mb-3">
                        <label class="form-label" for="username">Username</label>
                        <div class="input-group input-group-merge">
                            <span class="input-group-text"><i class="bx bx-user"></i></span>
                            <input type="text" class="form-control" id="username" name="username" placeholder="Es: mario.rossi" required />
                            <div class="invalid-feedback">Inserisci un username.</div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label" for="password">Password</label>
                        <div class="input-group input-group-merge">
                            <span class="input-group-text"><i class="bx bx-lock-alt"></i></span>
                            <input type="password" class="form-control" id="password" name="password" placeholder="············" required />
                            <div class="invalid-feedback">Inserisci una password.</div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label" for="tipo">Ruolo</label>
                        <select class="form-select" id="tipo" name="tipo" required>
                            <option value="registrato" selected>Registrato (Consulente)</option>
                            <option value="admin">Amministratore</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label" for="cf">Codice Fiscale</label>
                        <input type="text" class="form-control" id="cf" name="cf"
                               placeholder="RSSMRA80A01H501Z" maxlength="16" minlength="16" required />
                        <div class="invalid-feedback">Inserisci un codice fiscale valido (16 caratteri).</div>
                    </div>

                    <button type="submit" class="btn btn-primary w-100">
                        <span class="tf-icons bx bx-user-plus me-1"></span> Crea Utente
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<form name="deleteForm" method="post" action="Dispatcher">
    <input type="hidden" name="username" value=""/>
    <input type="hidden" name="controllerAction" value="AdminController.deleteUtente"/>
</form>

<div class="modal fade" id="modifyUserModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel2">Modifica Ruolo</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="Dispatcher" method="post">
                <div class="modal-body">
                    <input type="hidden" name="controllerAction" value="AdminController.modifyUtente">

                    <div class="row">
                        <div class="col mb-3">
                            <label for="mod_username_display" class="form-label">Username</label>
                            <input type="text" id="mod_username_display" class="form-control" disabled>
                            <input type="hidden" id="mod_username" name="username">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col mb-0">
                            <label for="mod_tipo" class="form-label">Nuovo Ruolo</label>
                            <select class="form-select" id="mod_tipo" name="tipo" required>
                                <option value="registrato">Registrato (Consulente)</option>
                                <option value="admin">Amministratore</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Annulla</button>
                    <button type="submit" class="btn btn-primary">Salva Modifiche</button>
                </div>
            </form>
        </div>
    </div>
</div>

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
