package it.kk_crm.controller;

import it.kk_crm.model.dao.*;
import it.kk_crm.model.mo.ServiziConsulenza;
import it.kk_crm.model.mo.Utente;
import it.kk_crm.services.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminController {

    private AdminController() {

    }

    //carica tutti i servizi di consulenza
    public static void servizi(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
        List<ServiziConsulenza> servizi;

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);

            //IMPLEMENTAZIONE COOKIES
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            if (!checkAuth(loggedUser, request, response, sessionDAOFactory)) return;

            /* richiamo implementazione MYSQL */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            ServiziConsulenzaDAO serviziDAO = daoFactory.getServiziConsulenzaDAO();
            servizi = serviziDAO.loadAllServizi();

            request.setAttribute("viewUrl", "vistaAdmin/servizi");
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("servizi", servizi);
            request.setAttribute("applicationMessage", request.getAttribute("applicationMessage"));

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

    //elimina logicamente servizio su db
    public static void deleteServizio(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
        String applicationMessage = null;

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            if (!checkAuth(loggedUser, request, response, sessionDAOFactory)) return;

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            AziendaDAO aziendaDAO = daoFactory.getAziendaDAO();

            Integer id = Integer.parseInt(request.getParameter("id"));
            ServiziConsulenzaDAO serviziConsulenzaDAO = daoFactory.getServiziConsulenzaDAO();
            ProposteSDAO proposteSDAO = daoFactory.getProposteSDAO();


            try {

                serviziConsulenzaDAO.deleteServizio(id);
                proposteSDAO.deleteWServizio(id);
                request.setAttribute("applicationMessage", "Cancellazione completata");

            } catch (Exception e) {
                //applicationMessage = "Contatto già esistente";
                request.setAttribute("applicationMessage", "Errore nella cancellazione");
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();
            servizi(request, response);

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

    //inserisce servizio su db
    public static void insertServizio(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;

        String applicationMessage = null;

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            if (!checkAuth(loggedUser, request, response, sessionDAOFactory)) return;

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            AziendaDAO aziendaDAO = daoFactory.getAziendaDAO();
            ServiziConsulenzaDAO serviziConsulenzaDAO = daoFactory.getServiziConsulenzaDAO();

            ServiziConsulenza servizio = new ServiziConsulenza();
            servizio.setId(Integer.parseInt(request.getParameter("id")));
            servizio.setTipo_servizio(request.getParameter("servizio"));

            try {

                serviziConsulenzaDAO.create(servizio);
                request.setAttribute("applicationMessage", "Inserimento completato");



            } catch (Exception e) {
                request.setAttribute("applicationMessage", "Tentativo di inserimento di servizio esistente");
                servizi(request, response);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();
            servizi(request, response);

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

    //carica tutti gli utenti dell'app
    public static void utenti(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
        List<Utente> utenti;

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);

            //IMPLEMENTAZIONE COOKIES
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            if (!checkAuth(loggedUser, request, response, sessionDAOFactory)) return;

            /* richiamo implementazione MYSQL */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            utenti = utenteDAO.loadAll();

            request.setAttribute("viewUrl", "vistaAdmin/utenti");
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("utenti", utenti);
            request.setAttribute("applicationMessage", request.getAttribute("applicationMessage"));

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

    //elimina logicamente utente su db
    public static void deleteUtente(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
        String applicationMessage = null;

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            if (!checkAuth(loggedUser, request, response, sessionDAOFactory)) return;

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            AziendaDAO aziendaDAO = daoFactory.getAziendaDAO();

            java.lang.String user = request.getParameter("username");
            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            Utente utente = new Utente();
            utente.setUsername(user);

            try {

                utenteDAO.delete(utente);
                request.setAttribute("applicationMessage", "Cancellazione completata");

            } catch (Exception e) {
                //applicationMessage = "Contatto già esistente";
                request.setAttribute("applicationMessage", "Errore nella cancellazione");
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();
            utenti(request, response);

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

    //inserisce utente su db
    public static void insertUtente(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;

        String applicationMessage = null;

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            if (!checkAuth(loggedUser, request, response, sessionDAOFactory)) return;

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();



            try {
                utenteDAO.create(request.getParameter("username"), request.getParameter("password"), request.getParameter("tipo"), request.getParameter("cf"));
                request.setAttribute("applicationMessage", "Inserimento completato");
            } catch (Exception e) {
                request.setAttribute("applicationMessage", "Tentativo di inserimento di utente esistente");
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();
            utenti(request, response);

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

    // Modifica il ruolo di un utente esistente
    public static void modifyUtente(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
        String applicationMessage = null;

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            if (!checkAuth(loggedUser, request, response, sessionDAOFactory)) return;

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();

            // Recuperiamo i parametri dal form (o dalla chiamata fetch/ajax)
            String username = request.getParameter("username");

            // BLOCCO DI SICUREZZA
            if (username != null && username.equals(loggedUser.getUsername())) {
                request.setAttribute("applicationMessage", "Non puoi modificare il tuo stesso ruolo!");
                utenti(request, response);
                return;
            }

            String nuovoTipo = request.getParameter("tipo");

            try {
                // Creiamo un oggetto utente temporaneo o passiamo i parametri
                // Assumiamo che nel DAO esista un metodo update o updateTipo
                Utente utente = new Utente();
                utente.setUsername(username);
                utente.setTipo(nuovoTipo);

                // Nota: Qui stiamo assumendo che nel tuo UtenteDAO ci sia un metodo 'update'
                // che aggiorna i campi in base allo username.
                utenteDAO.update(utente);

                request.setAttribute("applicationMessage", "Ruolo aggiornato con successo");

            } catch (Exception e) {
                request.setAttribute("applicationMessage", "Errore durante l'aggiornamento del ruolo");
                e.printStackTrace();
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            // Ricarichiamo la lista utenti per mostrare le modifiche
            utenti(request, response);

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

    // Metodo Helper aggiornato
    private static boolean checkAuth(Utente loggedUser, HttpServletRequest request, HttpServletResponse response, DAOFactory sessionDAOFactory) {

        if (loggedUser == null || !"admin".equals(loggedUser.getTipo())) {

            // 1. Impostiamo il messaggio di errore
            request.setAttribute("applicationMessage", "ACCESSO NEGATO: Area riservata agli amministratori.");

            // 2. Chiudiamo la transazione della sessione corrente (AdminController)
            // È fondamentale chiuderla PRIMA di chiamare un altro controller per evitare conflitti.
            try {
                sessionDAOFactory.commitTransaction();
            } catch (Exception e) {
                // Gestione silenziosa o log
            }

            // 3. Chiamiamo il controller della Dashboard per caricare i dati reali
            // Sostituisci 'load' con il nome del metodo nel tuo DashboardController (es. view, home, main)
            DashboardController.view(request, response);

            // 4. Ritorniamo false per bloccare l'AdminController
            return false;
        }

        return true;
    }
}
