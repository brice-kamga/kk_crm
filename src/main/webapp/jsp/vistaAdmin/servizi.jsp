<%@ page import="it.kk_crm.model.mo.ServiziConsulenza" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    List<ServiziConsulenza> servizi = (List<ServiziConsulenza>) request.getAttribute("servizi");
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Cache-Control", "no-store");
%>

<%@ include file="../../include/header.jsp" %>

<script>
    var applicationMessage;
    <% if (applicationMessage != null) { %>
    applicationMessage = "<%=applicationMessage%>";
    <% } %>

    function insert() {
        var f = document.insertForm;
        f.controllerAction.value = "AdminController.insertServizio";
        // Submit gestito dalla validazione in fondo
    }

    function deleteDialog(id) {
        if (confirm("Sicuro di voler eliminare il servizio ID " + id + "?")) {
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
    <span class="text-muted fw-light">Amministrazione /</span> Servizi di Consulenza
</h4>

<div class="row">

    <div class="col-md-8 mb-4">
        <div class="card h-100">
            <h5 class="card-header">Catalogo Servizi</h5>
            <div class="table-responsive text-nowrap" style="max-height: 500px; overflow-y: auto;">
                <table class="table table-hover">
                    <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Descrizione Servizio</th>
                        <th>Azioni</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty servizi}">
                            <c:forEach items="${servizi}" var="s">
                                <tr>
                                    <td><strong>#${s.id}</strong></td>
                                    <td>
                                        <span class="badge bg-label-primary me-1">${s.tipo_servizio}</span>
                                    </td>
                                    <td>
                                        <a href="javascript:deleteDialog(${s.id})" class="btn btn-sm btn-icon btn-outline-danger" title="Elimina">
                                            <span class="tf-icons bx bx-trash"></span>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="3" class="text-center py-5">
                                    <p class="text-muted">Nessun servizio nel catalogo.</p>
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
                <h5 class="mb-0">Nuovo Servizio</h5>
            </div>
            <div class="card-body">
                <form name="insertForm" method="post" action="Dispatcher" class="needs-validation" novalidate onsubmit="insert()">
                    <input type="hidden" name="controllerAction"/>

                    <div class="mb-3">
                        <label class="form-label" for="id">Codice ID</label>
                        <div class="input-group input-group-merge">
                            <span class="input-group-text"><i class="bx bx-hash"></i></span>
                            <input type="number" class="form-control" id="id" name="id" placeholder="Es: 101" required />
                            <div class="invalid-feedback">Inserisci un ID numerico valido.</div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label" for="servizio">Nome Servizio</label>
                        <div class="input-group input-group-merge">
                            <span class="input-group-text"><i class="bx bx-briefcase"></i></span>
                            <input type="text" class="form-control" id="servizio" name="servizio" placeholder="Es: Audit Sicurezza" required />
                            <div class="invalid-feedback">Inserisci il nome del servizio.</div>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary w-100">
                        <span class="tf-icons bx bx-plus-circle me-1"></span> Aggiungi al Catalogo
                    </button>
                </form>
            </div>
        </div>

        <div class="card bg-primary text-white mb-3">
            <div class="card-body">
                <h5 class="card-title text-white">Info Utili</h5>
                <p class="card-text">L'ID del servizio deve essere univoco. I servizi eliminati vengono disattivati logicamente.</p>
            </div>
        </div>
    </div>
</div>

<form name="deleteForm" method="post" action="Dispatcher">
    <input type="hidden" name="id"/>
    <input type="hidden" name="controllerAction" value="AdminController.deleteServizio"/>
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
