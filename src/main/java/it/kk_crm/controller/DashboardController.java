package it.kk_crm.controller;

import it.kk_crm.model.dao.*;
import it.kk_crm.model.mo.Appuntamento;
import it.kk_crm.model.mo.Note;
import it.kk_crm.model.mo.ProposteS;
import it.kk_crm.model.mo.Utente;
import it.kk_crm.services.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardController {

    private DashboardController() {}

    //carica pagina principale con tutti i relativi dati
    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
        List<Appuntamento> appuntamenti7;
        List<ProposteS> proposte;
        List<Note> noteNot;
        List<Note> note;

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);

            // 1. PRIMA controlliamo se l'utente ci è stato passato dal LoginController (nella Request)
            loggedUser = (Utente) request.getAttribute("loggedUser");

            // 2. SE NON C'È nella request (navigazione normale), lo cerchiamo nei Cookie
            if (loggedUser == null) {
                sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
                sessionDAOFactory.beginTransaction();
                UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
                loggedUser = sessionUserDAO.findLoggedUser();
                sessionDAOFactory.commitTransaction();
            }

            // Sicurezza: Se ancora null, rimandiamo al login
            if (loggedUser == null) {
                request.setAttribute("loggedOn", false);
                request.setAttribute("viewUrl", "login/view");
                return;
            }

            /* richiamo implementazione MYSQL */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            // Ora loggedUser non è null, quindi possiamo chiamare getUsername() senza errori
            AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();
            appuntamenti7 = appuntamentoDAO.loadAppuntamenti7Days(loggedUser.getUsername());

            ProposteSDAO propostesDAO = daoFactory.getProposteSDAO();
            proposte = propostesDAO.loadProposteOwner(loggedUser.getUsername());

            NoteDAO noteDAO = daoFactory.getNoteDAO();
            noteNot = noteDAO.loadNoteNotOwner(loggedUser.getUsername());
            note = noteDAO.loadNoteOwner(loggedUser.getUsername());


            request.setAttribute("loggedOn", true);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("appuntamenti", appuntamenti7);
            request.setAttribute("proposte", proposte);
            request.setAttribute("noteNot", noteNot);
            request.setAttribute("note", note);


            request.setAttribute("viewUrl", "vistaRegistrato/dashboard");


        } catch (Exception e) {
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }
}
