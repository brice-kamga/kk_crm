package it.kk_crm.controller;

import it.kk_crm.model.dao.AppuntamentoDAO;
import it.kk_crm.model.dao.ClienteDAO;
import it.kk_crm.model.dao.DAOFactory;
import it.kk_crm.model.dao.UtenteDAO;
import it.kk_crm.model.mo.Appuntamento;
import it.kk_crm.model.mo.Cliente;
import it.kk_crm.model.mo.Utente;
import it.kk_crm.services.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppuntamentiController {

    private AppuntamentiController() {}

    //carica view principale
    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
        List<Appuntamento> appuntamenti;

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

            AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();
            appuntamenti = appuntamentoDAO.loadAllAppuntamenti(loggedUser.getUsername());
            if(request.getAttribute("mode") != null){
                request.setAttribute("appuntamentiToday",request.getAttribute("appuntamentiToday"));
                request.setAttribute("data", request.getAttribute("data"));
            }


            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("appuntamenti", appuntamenti);
            request.setAttribute("applicationMessage", request.getAttribute("applicationMessage"));


            request.setAttribute("viewUrl", "vistaRegistrato/appuntamenti");


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

    //logicamente l'appuntamento su db
    public static void delete(HttpServletRequest request, HttpServletResponse response) {

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

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();

            Integer id = Integer.parseInt(request.getParameter("codice"));
            try {

                appuntamentoDAO.delete(id);
                request.setAttribute("applicationMessage", "Eliminazione completata");

            } catch (Exception e) {
                request.setAttribute("applicationMessage", "Errore nella cancellazione");
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

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

    //ricerca l'appuntamento su db per data
    public static void loadFilter(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
        String applicationMessage = null;
        List<Appuntamento> appuntamenti;

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
            String data = request.getParameter("data");

            AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();
            appuntamenti = appuntamentoDAO.loadSelected(data, loggedUser.getUsername());
            request.setAttribute("appuntamentiToday",appuntamenti);
            request.setAttribute("mode", "filter");
            request.setAttribute("data", data);


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

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

    //carica pagina dettaglio appuntamento
    public static void insertView(HttpServletRequest request, HttpServletResponse response) {

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

            ClienteDAO clienteDAO = daoFactory.getClienteDAO();
            List<Cliente> clienti = clienteDAO.getClientiOwner(loggedUser.getUsername());

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "vistaRegistrato/dettaglioAppuntamento");
            request.setAttribute("clienti", clienti);

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

    //metodo che inserisce appuntamento su db
    public static void insert(HttpServletRequest request, HttpServletResponse response) {

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

            AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();
            Appuntamento appuntamento = new Appuntamento();
            appuntamento.setData(request.getParameter("data"));
            appuntamento.setNote(request.getParameter("nota"));
            appuntamento.setCF(request.getParameter("cf"));

            try {
                appuntamentoDAO.create(appuntamento);
                request.setAttribute("applicationMessage", "Inserimento appuntamento completato");

            } catch (Exception e) {
                request.setAttribute("applicationMessage", "Errore nell'inserimento");
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

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

}

