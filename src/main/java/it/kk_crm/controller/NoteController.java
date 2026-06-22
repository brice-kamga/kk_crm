package it.kk_crm.controller;

import it.kk_crm.model.dao.ClienteDAO;
import it.kk_crm.model.dao.DAOFactory;
import it.kk_crm.model.dao.NoteDAO;
import it.kk_crm.model.dao.UtenteDAO;
import it.kk_crm.model.mo.Cliente;
import it.kk_crm.model.mo.Note;
import it.kk_crm.model.mo.Utente;
import it.kk_crm.services.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteController {

    private NoteController() {}

    //carica tutte le note dopo la ricerca
    public static void loadNote(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
        List<Note> note;


        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);

            //IMPLEMENTAZIONE COOKIES
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            /* richiamo implementazione MYSQL */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String nome = "";
            String cognome = "";

            //String azione = (String) request.getAttribute("azione");
            nome = request.getParameter("nome");
            cognome = request.getParameter("cognome");
            NoteDAO noteDAO = daoFactory.getNoteDAO();

            // Gestione parametri null per evitare crash
            if (nome == null) nome = "";
            if (cognome == null) cognome = "";

            note = noteDAO.loadNoteFilter(nome.trim(), cognome.trim());

            // Commit transazioni
            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("note", note);
            request.setAttribute("nome", nome);
            request.setAttribute("cognome", cognome);

            request.setAttribute("viewUrl", "vistaRegistrato/note");


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

    //carica view principale delle note E LA LISTA COMPLETA
    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
        List<Note> note;

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);

            //IMPLEMENTAZIONE COOKIES
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            /* richiamo implementazione MYSQL */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            // --- MODIFICA QUI: CARICHIAMO TUTTE LE NOTE SUBITO ---
            NoteDAO noteDAO = daoFactory.getNoteDAO();
            // Carica tutte le note (senza filtri)
            note = noteDAO.loadNoteAll();
            // -----------------------------------------------------

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);

            // Passiamo la lista alla JSP
            request.setAttribute("note", note);
            request.setAttribute("applicationMessage", request.getAttribute("applicationMessage"));

            request.setAttribute("viewUrl", "vistaRegistrato/note");


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

    //carica pagina dettaglio nota
    public static void modifyView(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String id = "";
            if(request.getParameter("status") != null && request.getParameter("status").equals("modify")){
                id = new String(request.getParameter("id"));
                NoteDAO noteDAO = daoFactory.getNoteDAO();
                Note nota = noteDAO.getNota(id);
                request.setAttribute("nota", nota);
            }

            ClienteDAO clienteDAO = daoFactory.getClienteDAO();
            List<Cliente> clienti = clienteDAO.getClienti();

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            List<Utente> utenti = utenteDAO.loadAllUtenti();

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);

            request.setAttribute("viewUrl", "vistaRegistrato/dettaglioNota");
            request.setAttribute("nome", request.getParameter("nome"));
            request.setAttribute("cognome", request.getParameter("cognome"));
            request.setAttribute("clienti", clienti);
            request.setAttribute("utenti", utenti);

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

    //metodo che modifica nota su db
    public static void modify(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            NoteDAO noteDAO = daoFactory.getNoteDAO();


            Note nota = new Note();
            nota.setId(Integer.parseInt(request.getParameter("id")));
            nota.setNota(request.getParameter("nota"));
            nota.setData(request.getParameter("data"));
            nota.setCliente_cf(request.getParameter("cliente_cf"));
            // Se admin può cambiare autore, altrimenti usa loggedUser
            if (request.getParameter("utente") != null) {
                nota.setUtente(request.getParameter("utente"));
            } else {
                nota.setUtente(loggedUser.getUsername());
            }

            request.setAttribute("nome", request.getParameter("nome"));
            request.setAttribute("cognome", request.getParameter("cognome"));


            try {

                noteDAO.update(nota);
                request.setAttribute("applicationMessage", "Modifica completata");
            } catch (Exception e) {
                request.setAttribute("applicationMessage", "Errore nella modifica nota");
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            // Ricarica la lista o torna alla view
            // Meglio tornare alla lista completa per vedere la modifica
            view(request, response);

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

    //metodo che elimina logicamente nota su db
    public static void delete(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            NoteDAO noteDAO = daoFactory.getNoteDAO();

            Integer id = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("nome", request.getParameter("nome"));
            request.setAttribute("cognome", request.getParameter("cognome"));
            //request.setAttribute("azione", request.getParameter("azione"));


            try {

                noteDAO.delete(id);
                request.setAttribute("applicationMessage", "Cancellazione completata");

            } catch (Exception e) {
                request.setAttribute("applicationMessage", "Errore nella cancellazione");
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            // Dopo la cancellazione, ricarica la lista completa
            view(request, response);

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

    //metodo che inserisce nota su db
    public static void insert(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Utente loggedUser;

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            NoteDAO noteDAO = daoFactory.getNoteDAO();

            Note nota = new Note();
            nota.setNota(request.getParameter("nota"));
            nota.setData(request.getParameter("data"));
            // Se admin seleziona un altro utente, altrimenti usa loggedUser
            if (request.getParameter("utente") != null && !request.getParameter("utente").isEmpty()) {
                nota.setUtente(request.getParameter("utente"));
            } else {
                nota.setUtente(loggedUser.getUsername());
            }
            nota.setCliente_cf(request.getParameter("cliente_cf"));

            try {

                noteDAO.create(nota);
                request.setAttribute("applicationMessage", "Inserimento completato");
            } catch (Exception e) {
                request.setAttribute("applicationMessage", "Errore nell' inserimento nota");
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            // Torna alla lista
            view(request,response);

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
