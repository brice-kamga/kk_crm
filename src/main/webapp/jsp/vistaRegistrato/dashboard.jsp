<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="../../include/header.jsp" %>

<h4 class="fw-bold py-3 mb-4">
    <span class="text-muted fw-light">Home /</span> Dashboard
</h4>

<div class="row">
    <div class="col-lg-3 col-md-6 col-12 mb-4">
        <div class="card">
            <div class="card-body">
                <div class="card-title d-flex align-items-start justify-content-between">
                    <div class="avatar flex-shrink-0">
                        <span class="avatar-initial rounded bg-label-primary"><i class="bx bx-calendar"></i></span>
                    </div>
                </div>
                <span class="fw-semibold d-block mb-1">Appuntamenti (7gg)</span>
                <h3 class="card-title mb-2">${appuntamenti.size()}</h3>
            </div>
        </div>
    </div>

    <div class="col-lg-3 col-md-6 col-12 mb-4">
        <div class="card">
            <div class="card-body">
                <div class="card-title d-flex align-items-start justify-content-between">
                    <div class="avatar flex-shrink-0">
                        <span class="avatar-initial rounded bg-label-success"><i class="bx bx-file"></i></span>
                    </div>
                </div>
                <span class="fw-semibold d-block mb-1">Proposte Attive</span>
                <h3 class="card-title mb-2">${proposte.size()}</h3>
            </div>
        </div>
    </div>

    <div class="col-lg-3 col-md-6 col-12 mb-4">
        <div class="card">
            <div class="card-body">
                <div class="card-title d-flex align-items-start justify-content-between">
                    <div class="avatar flex-shrink-0">
                        <span class="avatar-initial rounded bg-label-info"><i class="bx bx-note"></i></span>
                    </div>
                </div>
                <span class="fw-semibold d-block mb-1">Mie Note</span>
                <h3 class="card-title mb-2">${note.size()}</h3>
            </div>
        </div>
    </div>

    <div class="col-lg-3 col-md-6 col-12 mb-4">
        <div class="card">
            <div class="card-body">
                <div class="card-title d-flex align-items-start justify-content-between">
                    <div class="avatar flex-shrink-0">
                        <span class="avatar-initial rounded bg-label-warning"><i class="bx bx-bell"></i></span>
                    </div>
                </div>
                <span class="fw-semibold d-block mb-1">Note da Altri</span>
                <h3 class="card-title mb-2">${noteNot.size()}</h3>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-12">
        <div class="nav-align-top mb-4">
            <ul class="nav nav-pills mb-3" role="tablist">
                <li class="nav-item">
                    <button type="button" class="nav-link active" role="tab" data-bs-toggle="tab" data-bs-target="#navs-pills-appuntamenti" aria-controls="navs-pills-appuntamenti" aria-selected="true">
                        <i class="tf-icons bx bx-calendar me-1"></i> Appuntamenti
                    </button>
                </li>
                <li class="nav-item">
                    <button type="button" class="nav-link" role="tab" data-bs-toggle="tab" data-bs-target="#navs-pills-proposte" aria-controls="navs-pills-proposte" aria-selected="false">
                        <i class="tf-icons bx bx-file me-1"></i> Proposte
                    </button>
                </li>
                <li class="nav-item">
                    <button type="button" class="nav-link" role="tab" data-bs-toggle="tab" data-bs-target="#navs-pills-note" aria-controls="navs-pills-note" aria-selected="false">
                        <i class="tf-icons bx bx-note me-1"></i> Mie Note
                    </button>
                </li>
                <li class="nav-item">
                    <button type="button" class="nav-link" role="tab" data-bs-toggle="tab" data-bs-target="#navs-pills-notenot" aria-controls="navs-pills-notenot" aria-selected="false">
                        <i class="tf-icons bx bx-bell me-1"></i> Altre Note
                    </button>
                </li>
            </ul>

            <div class="tab-content shadow-none border p-0">

                <div class="tab-pane fade show active" id="navs-pills-appuntamenti" role="tabpanel">
                    <div class="card">
                        <div class="table-responsive text-nowrap">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>Data</th>
                                    <th>Nota</th>
                                    <th>Azienda</th>
                                    <th>Referente</th>
                                    <th>CF Referente</th>
                                </tr>
                                </thead>
                                <tbody class="table-border-bottom-0">
                                <c:choose>
                                    <c:when test="${not empty appuntamenti}">
                                        <c:forEach items="${appuntamenti}" var="app">
                                            <tr>
                                                <td><span class="badge bg-label-primary me-1">${app.data}</span></td>
                                                <td><strong>${app.note}</strong></td>
                                                <td>${app.nome}</td>
                                                <td>${app.cognome}</td>
                                                <td><small class="text-muted">${app.CF}</small></td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr><td colspan="5" class="text-center py-4">Nessun appuntamento in programma.</td></tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="tab-pane fade" id="navs-pills-proposte" role="tabpanel">
                    <div class="card">
                        <div class="table-responsive text-nowrap">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>Tipo Servizio</th>
                                    <th>Descrizione Proposta</th>
                                    <th>Azienda</th>
                                    <th>P. IVA</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${not empty proposte}">
                                        <c:forEach items="${proposte}" var="prop">
                                            <tr>
                                                <td><span class="badge bg-label-success me-1">${prop.nome_servizio}</span></td>
                                                <td><strong>${prop.tipo}</strong></td>
                                                <td>${prop.nome}</td>
                                                <td><small class="text-muted">${prop.PIva}</small></td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr><td colspan="4" class="text-center py-4">Nessuna proposta attiva.</td></tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="tab-pane fade" id="navs-pills-note" role="tabpanel">
                    <div class="card">
                        <div class="table-responsive text-nowrap">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>Data</th>
                                    <th>Nota</th>
                                    <th>Utente</th>
                                    <th>CF Cliente</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${not empty note}">
                                        <c:forEach items="${note}" var="n">
                                            <tr>
                                                <td>${n.data}</td>
                                                <td>${n.nota}</td>
                                                <td><span class="badge bg-label-info">${n.utente}</span></td>
                                                <td>${n.cliente_cf}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr><td colspan="4" class="text-center py-4">Nessuna nota trovata.</td></tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="tab-pane fade" id="navs-pills-notenot" role="tabpanel">
                    <div class="card">
                        <div class="table-responsive text-nowrap">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>Data</th>
                                    <th>Nota</th>
                                    <th>Autore</th>
                                    <th>CF Cliente</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${not empty noteNot}">
                                        <c:forEach items="${noteNot}" var="nn">
                                            <tr>
                                                <td>${nn.data}</td>
                                                <td>${nn.nota}</td>
                                                <td><span class="badge bg-label-warning">${nn.utente}</span></td>
                                                <td>${nn.cliente_cf}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr><td colspan="4" class="text-center py-4">Nessuna nota da altri utenti.</td></tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

<%@ include file="../../include/footer.jsp" %>
