<%@ page import="it.kk_crm.model.mo.Azienda" %>
<%@ page import="it.kk_crm.model.mo.Cliente" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    Azienda azienda = (Azienda) request.getAttribute("azienda");
    Cliente cliente = (Cliente) request.getAttribute("cliente");
    String action = (azienda != null) ? "modify" : "insert";

    // Evita caching
    response.setHeader("Cache-Control","no-cache");
    response.setHeader("Cache-Control","no-store");
%>

<%@ include file="../../include/header.jsp" %>

<script>
    var status = "<%=action%>";

    function submitAzienda() {
        var f = document.insertForm;
        f.controllerAction.value = "AziendaController." + status;
        // Il submit avviene se il form è valido
    }

    function deleteAzienda() {
        if(confirm("Sei sicuro di voler eliminare questa azienda e il relativo referente?")) {
            document.deleteForm.submit();
        }
    }

    function goback() {
        document.backForm.submit();
    }

    function onLoadHandler() {
        // Gestione click bottoni
        if(status == "modify") {
            document.getElementById("btnDelete").addEventListener("click", deleteAzienda);
        }
        document.getElementById("btnBack").addEventListener("click", goback);
    }

    window.addEventListener("load", onLoadHandler);
</script>

<h4 class="fw-bold py-3 mb-4">
    <span class="text-muted fw-light">Aziende /</span> <%=(action.equals("modify")) ? "Modifica Azienda" : "Nuova Azienda"%>
</h4>

<div class="row justify-content-center">
    <div class="col-md-10">
        <div class="card mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0"><%=(action.equals("modify")) ? azienda.getNome() : "Inserimento Dati"%></h5>
                <small class="text-muted float-end"><%=(action.equals("modify")) ? "Modifica i dati esistenti" : "Compila tutti i campi"%></small>
            </div>

            <div class="card-body">
                <form name="insertForm" action="Dispatcher" method="post" class="needs-validation" novalidate onsubmit="submitAzienda()">

                    <h6 class="fw-bold text-primary"><i class="bx bxs-business me-1"></i> Dati Aziendali</h6>
                    <div class="row g-3 mb-4">

                        <div class="col-md-6">
                            <label for="piva" class="form-label">Partita IVA</label>
                            <% if (action.equals("modify")) { %>
                            <input type="text" class="form-control" disabled value="<%=azienda.getP_Iva()%>">
                            <input type="hidden" id="piva" name="piva" value="<%=azienda.getP_Iva()%>">
                            <% } else { %>
                            <div class="input-group input-group-merge">
                                <span class="input-group-text"><i class="bx bx-id-card"></i></span>
                                <input type="text" class="form-control" id="piva" name="piva"
                                       maxlength="11" minlength="11" pattern="[0-9]*" required>
                                <div class="invalid-feedback">Inserisci una P.IVA valida (11 cifre).</div>
                            </div>
                            <% } %>
                        </div>

                        <div class="col-md-6">
                            <label for="nome" class="form-label">Ragione Sociale</label>
                            <input type="text" class="form-control" id="nome" name="nome"
                                   value="<%=(action.equals("modify")) ? azienda.getNome() : ""%>" required>
                            <div class="invalid-feedback">Inserisci il nome dell'azienda.</div>
                        </div>

                        <div class="col-md-4">
                            <label for="forma" class="form-label">Forma Giuridica</label>
                            <input type="text" class="form-control" id="forma" name="forma" placeholder="SPA, SRL..."
                                   value="<%=(action.equals("modify")) ? azienda.getForma() : ""%>" required>
                        </div>

                        <div class="col-md-8">
                            <label for="indirizzo" class="form-label">Indirizzo Sede</label>
                            <div class="input-group input-group-merge">
                                <span class="input-group-text"><i class="bx bx-map"></i></span>
                                <input type="text" class="form-control" id="indirizzo" name="indirizzo"
                                       value="<%=(action.equals("modify")) ? azienda.getIndirizzo() : ""%>" required>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label for="email" class="form-label">Email Aziendale</label>
                            <div class="input-group input-group-merge">
                                <span class="input-group-text"><i class="bx bx-envelope"></i></span>
                                <input type="email" class="form-control" id="email" name="email"
                                       value="<%=(action.equals("modify")) ? azienda.getEmail() : ""%>" required>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label for="telefono" class="form-label">Telefono</label>
                            <div class="input-group input-group-merge">
                                <span class="input-group-text"><i class="bx bx-phone"></i></span>
                                <input type="text" class="form-control" id="telefono" name="telefono"
                                       value="<%=(action.equals("modify")) ? azienda.getTelefono() : ""%>" required>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label for="catMerce" class="form-label">Settore / Categoria</label>
                            <select class="form-select" id="catMerce" name="catMerce">
                                <%
                                    String[] cats = {"meccanica", "informatica", "alimentari", "chimica", "metalmeccanica", "conoscenza"};
                                    String currentCat = (action.equals("modify")) ? azienda.getCatMerce() : "";
                                    for(String c : cats) {
                                %>
                                <option value="<%=c%>" <%=c.equals(currentCat) ? "selected" : ""%>><%=c%></option>
                                <% } %>
                            </select>
                        </div>

                        <div class="col-md-6">
                            <label for="tipologia" class="form-label">Tipologia Cliente</label>
                            <select class="form-select" id="tipologia" name="tipologia">
                                <%
                                    String[] tipos = {"Fidelizzato", "Prospect", "Nuovo", "Rischio abbandono"};
                                    String currentTipo = (action.equals("modify")) ? azienda.getTipologia() : "";
                                    for(String t : tipos) {
                                %>
                                <option value="<%=t%>" <%=t.equals(currentTipo) ? "selected" : ""%>><%=t%></option>
                                <% } %>
                            </select>
                        </div>
                    </div>

                    <hr class="my-4">

                    <h6 class="fw-bold text-info"><i class="bx bx-user me-1"></i> Referente Aziendale</h6>

                    <div class="row g-3">
                        <% if (action.equals("modify")) { %>
                        <input type="hidden" id="cfR" name="cfR" value="<%=cliente.getCF()%>">

                        <div class="col-md-6">
                            <label class="form-label">Codice Fiscale (Non modificabile)</label>
                            <input type="text" class="form-control" disabled value="<%=cliente.getCF()%>">
                        </div>

                        <div class="col-md-6">
                            <label for="DataR" class="form-label">Data di Nascita</label>
                            <input type="date" id="DataR" class="form-control" name="DataR" value="<%=cliente.getDataNascita()%>" required>
                        </div>

                        <div class="col-md-6">
                            <label for="nomeR" class="form-label">Nome</label>
                            <input type="text" id="nomeR" class="form-control" name="nomeR" value="<%=cliente.getNome()%>" required>
                        </div>

                        <div class="col-md-6">
                            <label for="cognomeR" class="form-label">Cognome</label>
                            <input type="text" id="cognomeR" class="form-control" name="cognomeR" value="<%=cliente.getCognome()%>" required>
                        </div>

                        <div class="col-md-6">
                            <label for="emailR" class="form-label">Email Personale</label>
                            <input type="text" id="emailR" class="form-control" name="emailR" value="<%=cliente.getEmail()%>" required>
                        </div>

                        <div class="col-md-6">
                            <label for="telefonoR" class="form-label">Cellulare</label>
                            <input type="text" id="telefonoR" class="form-control" name="telefonoR" value="<%=cliente.getTelefono()%>" required>
                        </div>

                        <% } else { %>
                        <div class="col-md-6">
                            <label for="cfRef" class="form-label">Codice Fiscale</label>
                            <input type="text" class="form-control" id="cfRef" name="cfRef" maxlength="16" minlength="16" required>
                        </div>

                        <div class="col-md-6">
                            <label for="dataNascitaRef" class="form-label">Data di Nascita</label>
                            <input type="date" class="form-control" id="dataNascitaRef" name="dataNascitaRef" required>
                        </div>

                        <div class="col-md-6">
                            <label for="nomeRef" class="form-label">Nome</label>
                            <input type="text" class="form-control" id="nomeRef" name="nomeRef" required>
                        </div>

                        <div class="col-md-6">
                            <label for="cognomeRef" class="form-label">Cognome</label>
                            <input type="text" class="form-control" id="cognomeRef" name="cognomeRef" required>
                        </div>

                        <div class="col-md-6">
                            <label for="emailRef" class="form-label">Email Personale</label>
                            <input type="email" class="form-control" id="emailRef" name="emailRef" required>
                        </div>

                        <div class="col-md-6">
                            <label for="telefonoRef" class="form-label">Cellulare</label>
                            <input type="text" class="form-control" id="telefonoRef" name="telefonoRef" required>
                        </div>
                        <% } %>
                    </div>

                    <div class="mt-4 pt-2 border-top">
                        <button type="submit" class="btn btn-primary me-2">
                            <i class="bx bx-save me-1"></i> <%=(action.equals("modify")) ? "Salva Modifiche" : "Inserisci Azienda"%>
                        </button>

                        <% if(action.equals("modify")) { %>
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
    <input type="hidden" name="controllerAction" value="AziendaController.delete"/>
    <input type="hidden" name="piva" value="<%=azienda.getP_Iva()%>">
    <input type="hidden" name="cf" value="<%=cliente.getCF()%>">
</form>
<% } %>

<form name="backForm" method="post" action="Dispatcher">
    <input type="hidden" name="controllerAction" value="AziendaController.loadAziende"/>
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
