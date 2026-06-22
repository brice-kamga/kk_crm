<%@ page import="java.time.LocalDate" %>
<%@ page import="it.kk_crm.model.mo.Cliente" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Recupero data odierna per il campo data
    LocalDate today = LocalDate.now();

    // Evita caching
    response.setHeader("Cache-Control","no-cache");
    response.setHeader("Cache-Control","no-store");
%>

<%@ include file="../../include/header.jsp" %>

<script>
    function submitAppuntamento() {
        var f = document.insertForm;
        f.controllerAction.value = "AppuntamentiController.insert";
        // Il submit avviene naturalmente se il form è valido grazie allo script di validazione in basso
    }

    function goback() {
        document.backForm.submit();
    }

    function onLoadHandler() {
        // Gestione del bottone Indietro
        document.getElementById("btnBack").addEventListener("click", goback);

        // La gestione del submit è delegata allo script di validazione Bootstrap in fondo
    }

    window.addEventListener("load", onLoadHandler);
</script>

<h4 class="fw-bold py-3 mb-4">
    <span class="text-muted fw-light">Appuntamenti /</span> Nuovo Appuntamento
</h4>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">Dettagli Appuntamento</h5>
                <small class="text-muted float-end">Inserisci i dati richiesti</small>
            </div>

            <div class="card-body">
                <form name="insertForm" action="Dispatcher" method="post" class="needs-validation" novalidate onsubmit="submitAppuntamento()">

                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label" for="data">Data</label>
                            <div class="input-group input-group-merge">
                                <span class="input-group-text"><i class="bx bx-calendar"></i></span>
                                <input type="date" class="form-control" id="data" name="data" value="<%=today%>" required />
                                <div class="invalid-feedback">Inserisci una data valida.</div>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="cf">Cliente</label>
                            <div class="input-group input-group-merge">
                                <span class="input-group-text"><i class="bx bx-user"></i></span>
                                <select class="form-select" id="cf" name="cf" required>
                                    <option value="" selected disabled>Seleziona un cliente...</option>
                                    <c:forEach items="${clienti}" var="c">
                                        <option value="${c.CF}">
                                                ${c.nome} ${c.cognome} (P.Iva: ${c.piva})
                                        </option>
                                    </c:forEach>
                                </select>
                                <div class="invalid-feedback">Seleziona un cliente dalla lista.</div>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label" for="nota">Nota Descrittiva</label>
                        <div class="input-group input-group-merge">
                            <span class="input-group-text"><i class="bx bx-comment-detail"></i></span>
                            <input type="text" class="form-control" id="nota" name="nota"
                                   placeholder="Es: Incontro per rinnovo contratto..." maxlength="70" required />
                            <div class="invalid-feedback">Inserisci una nota (max 70 caratteri).</div>
                        </div>
                    </div>

                    <div class="mt-4">
                        <button type="submit" class="btn btn-primary me-2">
                            <i class="bx bx-save me-1"></i> Salva Appuntamento
                        </button>
                        <button type="button" class="btn btn-outline-secondary" id="btnBack">
                            <i class="bx bx-arrow-back me-1"></i> Annulla
                        </button>
                    </div>

                    <input type="hidden" name="controllerAction" />
                </form>
            </div>
        </div>
    </div>
</div>

<form name="backForm" method="post" action="Dispatcher">
    <input type="hidden" name="controllerAction" value="AppuntamentiController.view"/>
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
                } else {
                    // Se valido, imposta l'azione corretta
                    // (anche se la funzione onsubmit inline lo fa già, è una sicurezza in più)
                    if(form.name === 'insertForm') {
                        form.controllerAction.value = "AppuntamentiController.insert";
                    }
                }
                form.classList.add('was-validated')
            }, false)
        })
    })()
</script>
