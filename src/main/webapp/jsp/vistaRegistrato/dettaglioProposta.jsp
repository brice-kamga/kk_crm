<%@ page import="java.util.List" %>
<%@ page import="it.kk_crm.model.mo.Azienda" %>
<%@ page import="it.kk_crm.model.mo.ServiziConsulenza" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Recupero dati dalla request
    // JSTL gestirà il rendering, ma recuperiamo qui eventuali logiche se servissero
    // (In questo caso le liste sono usate direttamente nel c:forEach)

    // Evita caching
    response.setHeader("Cache-Control","no-cache");
    response.setHeader("Cache-Control","no-store");
%>

<%@ include file="../../include/header.jsp" %>

<script>
    function submitProposta() {
        var f = document.insertForm;
        f.controllerAction.value = "ProposteController.insert";
    }

    function goback() {
        document.backForm.submit();
    }

    function onLoadHandler() {
        document.getElementById("btnBack").addEventListener("click", goback);
    }

    window.addEventListener("load", onLoadHandler);
</script>

<h4 class="fw-bold py-3 mb-4">
    <span class="text-muted fw-light">Proposte /</span> Nuova Proposta
</h4>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">Dettagli Opportunità</h5>
                <small class="text-muted float-end">Compila i dati commerciali</small>
            </div>

            <div class="card-body">
                <form name="insertForm" action="Dispatcher" method="post" class="needs-validation" novalidate onsubmit="submitProposta()">

                    <div class="row g-3">

                        <div class="col-12">
                            <label class="form-label" for="proposta">Titolo Proposta</label>
                            <div class="input-group input-group-merge">
                                <span class="input-group-text"><i class="bx bx-file"></i></span>
                                <input type="text" class="form-control" id="proposta" name="proposta"
                                       placeholder="Es: Rinnovo contratto annuale..." maxlength="45" required>
                                <div class="invalid-feedback">Inserisci un titolo per la proposta.</div>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label for="azienda" class="form-label">Azienda Cliente</label>
                            <div class="input-group input-group-merge">
                                <span class="input-group-text"><i class="bx bx-buildings"></i></span>
                                <select class="form-select" id="azienda" name="azienda" required>
                                    <option value="" selected disabled>Seleziona Azienda...</option>
                                    <c:forEach items="${aziende}" var="az">
                                        <option value="${az.p_Iva}">${az.nome}</option>
                                    </c:forEach>
                                </select>
                                <div class="invalid-feedback">Seleziona un'azienda.</div>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label for="servizio" class="form-label">Servizio Offerto</label>
                            <div class="input-group input-group-merge">
                                <span class="input-group-text"><i class="bx bx-cog"></i></span>
                                <select class="form-select" id="servizio" name="servizio" required>
                                    <option value="" selected disabled>Seleziona Servizio...</option>
                                    <c:forEach items="${servizi}" var="srv">
                                        <option value="${srv.id}">${srv.tipo_servizio}</option>
                                    </c:forEach>
                                </select>
                                <div class="invalid-feedback">Seleziona un servizio dal catalogo.</div>
                            </div>
                        </div>

                    </div>

                    <div class="mt-4 pt-3 border-top">
                        <button type="submit" class="btn btn-primary me-2">
                            <i class="bx bx-plus-circle me-1"></i> Inserisci Proposta
                        </button>
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

<form name="backForm" method="post" action="Dispatcher">
    <input type="hidden" name="controllerAction" value="ProposteController.view"/>
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
