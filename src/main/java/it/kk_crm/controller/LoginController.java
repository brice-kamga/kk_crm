package it.kk_crm.controller;

import it.kk_crm.model.dao.DAOFactory;
import it.kk_crm.model.dao.UtenteDAO;
import it.kk_crm.model.mo.Utente;
import it.kk_crm.services.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    private LoginController() {}

    //carica view principale di login
    public static void view(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        Utente loggedUser;

        try {
            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "login/view");

        } catch (Exception e) {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    //logon utente
    public static void logon(HttpServletRequest request, HttpServletResponse response) {

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

            /* implementazione cookies utente */
            UtenteDAO sessionUtenteDAO = sessionDAOFactory.getUtenteDAO();
            loggedUser = sessionUtenteDAO.findLoggedUser();

            /* richiamo implementazione utente MYSQL */
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            String username = request.getParameter("username");
            String password = request.getParameter("password");


            UtenteDAO utenteDAO = daoFactory.getUtenteDAO();
            Utente utente = utenteDAO.findByUsername(username);

            if (utente == null || !utente.getPassword().equals(password)) {
                sessionUtenteDAO.delete(null);
                applicationMessage = "Username e password errati!";
                loggedUser=null;
                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("applicationMessage", applicationMessage);
                request.setAttribute("viewUrl", "login/view");
            } else {
                // Login riuscito: Creiamo la sessione (Cookie)
                loggedUser = sessionUtenteDAO.create(utente.getUsername(), utente.getPassword(),utente.getTipo(),utente.getCF());

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

                // Impostiamo l'utente loggato nella request per i prossimi controller
                request.setAttribute("loggedOn", true);
                request.setAttribute("loggedUser", loggedUser);


                if(loggedUser.getTipo().equals("registrato") || loggedUser.getTipo().equals("admin")) {
                    DashboardController.view(request, response);
                    return;
                }
            }

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

    //logout utente
    public static void logout(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UtenteDAO sessionUserDAO = sessionDAOFactory.getUtenteDAO();
            sessionUserDAO.delete(null);

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",false);
            request.setAttribute("loggedUser", null);
            request.setAttribute("viewUrl", "login/view");

        } catch (Exception e) {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }
}
