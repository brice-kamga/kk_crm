package it.kk_crm.controller;

import it.kk_crm.model.dao.*;
import it.kk_crm.model.mo.Azienda;
import it.kk_crm.model.mo.Cliente;
import it.kk_crm.model.mo.Utente;
import it.kk_crm.services.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AziendaController {

    private AziendaController() {}

    //caricamento di tutte le aziende
    public static void loadAziende(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
        List<Azienda> aziende;


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

            AziendaDAO aziendaDAO = daoFactory.getAziendaDAO();
            aziende = aziendaDAO.selectOwnerWReferente(loggedUser.getUsername());

            request.setAttribute("aziende", aziende);
            request.setAttribute("viewUrl", "vistaRegistrato/aziende");
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
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

    //carica pagina dettaglio azienda
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

            String PIva = "";
            if(request.getParameter("status").equals("modify")){
                PIva = new String(request.getParameter("P_Iva"));
            }

            AziendaDAO aziendaDAO = daoFactory.getAziendaDAO();
            Azienda azienda = aziendaDAO.getAzienda(PIva);

            ClienteDAO clienteDAO = daoFactory.getClienteDAO();
            Cliente cliente = clienteDAO.getClienteWAzienda(PIva);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("azienda", azienda);
            request.setAttribute("cliente", cliente);
            request.setAttribute("viewUrl", "vistaRegistrato/dettaglioAzienda");

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

    //modifica azienda su db
    public static void modify(HttpServletRequest request, HttpServletResponse response) {

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

            AziendaDAO aziendaDAO = daoFactory.getAziendaDAO();
            ClienteDAO clienteDAO = daoFactory.getClienteDAO();

            String piva = request.getParameter("piva");
            Azienda azienda = new Azienda();
            azienda.setP_Iva(request.getParameter("piva"));
            azienda.setNome(request.getParameter("nome"));
            azienda.setForma(request.getParameter("forma"));
            azienda.setIndirizzo(request.getParameter("indirizzo"));
            azienda.setEmail(request.getParameter("email"));
            azienda.setCatMerce(request.getParameter("catMerce"));
            azienda.setTelefono(request.getParameter("telefono"));
            azienda.setTipologia(request.getParameter("tipologia"));

            Cliente cliente = new Cliente();
            cliente.setCF(request.getParameter("cfR"));
            cliente.setNome(request.getParameter("nomeR"));
            cliente.setCognome(request.getParameter("cognomeR"));
            cliente.setDataNascita(request.getParameter("DataR"));
            cliente.setTelefono(request.getParameter("telefonoR"));
            cliente.setEmail(request.getParameter("emailR"));

            try {
                aziendaDAO.update(azienda);
                clienteDAO.update(cliente);
                request.setAttribute("applicationMessage", "Modifica riuscita");
            } catch (Exception e) {
                request.setAttribute("applicationMessage", "Errore nella modifica");
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            loadAziende(request, response);

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
    //elimina logicamente azienda su db
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

            AziendaDAO aziendaDAO = daoFactory.getAziendaDAO();
            ClienteDAO clienteDAO = daoFactory.getClienteDAO();
            NoteDAO noteDAO = daoFactory.getNoteDAO();
            ProposteSDAO proposteDAO = daoFactory.getProposteSDAO();
            AppuntamentoDAO appuntamentoDAO = daoFactory.getAppuntamentoDAO();

            String piva = request.getParameter("piva");
            String cf = request.getParameter("cf");

            try {

                aziendaDAO.delete(piva);
                clienteDAO.delete(cf);
                noteDAO.deleteC(cf);
                proposteDAO.deleteC(piva);
                appuntamentoDAO.deleteC(cf);
                request.setAttribute("applicationMessage", "Cancellazione riuscita");
            } catch (Exception e) {
                request.setAttribute("applicationMessage", "Errore nella cancellazione azienda");
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            loadAziende(request, response);

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

    //inserisce azienda su db
    public static void insert(HttpServletRequest request, HttpServletResponse response) {

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

            AziendaDAO aziendaDAO = daoFactory.getAziendaDAO();

            ClienteDAO clienteDAO = daoFactory.getClienteDAO();

            Azienda azienda = new Azienda();
            azienda.setP_Iva(request.getParameter("piva"));
            azienda.setNome(request.getParameter("nome"));
            azienda.setForma(request.getParameter("forma"));
            azienda.setIndirizzo(request.getParameter("indirizzo"));
            azienda.setEmail(request.getParameter("email"));
            azienda.setCatMerce(request.getParameter("catMerce"));
            azienda.setTelefono(request.getParameter("telefono"));
            azienda.setTipologia(request.getParameter("tipologia"));
            azienda.setDeleted("N");
            azienda.setAssigned(loggedUser.getUsername());

            Cliente cliente = new Cliente();
            cliente.setPiva(request.getParameter("piva"));
            cliente.setNome(request.getParameter("nomeRef"));
            cliente.setCognome(request.getParameter("cognomeRef"));
            cliente.setDataNascita(request.getParameter("dataNascitaRef"));
            cliente.setCF(request.getParameter("cfRef"));
            cliente.setEmail(request.getParameter("emailRef"));
            cliente.setTelefono(request.getParameter("telefonoRef"));


            try {

                aziendaDAO.create(azienda);
                clienteDAO.create(cliente);

                request.setAttribute("applicationMessage", "Inserimento riuscito");


            } catch (Exception e) {
                request.setAttribute("applicationMessage", "Tentativo di inserimento di azienda esistente");
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();
            loadAziende(request, response);


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

    //carica filtri per ricerca aziende
    public static void loadFiltri(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
        List<String> aziendeCat;
        List<String> aziendeTipo;
        List<String> aziendeOwner;

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

            AziendaDAO aziendaDAO = daoFactory.getAziendaDAO();
            aziendeCat = aziendaDAO.getDistinctCat();
            aziendeTipo = aziendaDAO.getDistinctTipologia();
            aziendeOwner = aziendaDAO.getDistinctOwner();


            request.setAttribute("aziendeCat", aziendeCat);
            request.setAttribute("aziendeTipo", aziendeTipo);
            request.setAttribute("aziendeOwner", aziendeOwner);
            request.setAttribute("viewUrl", "vistaRegistrato/ricercaAzienda");
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);

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

    //carica la tabella con le azienda a seconda dei filtri inseriti (ricercazienda.jsp)
    public static void loadTable(HttpServletRequest request, HttpServletResponse response) {

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        Utente loggedUser;
        List<Azienda> aziende;

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

            String filtro = request.getParameter("filtro");
            String tipo = request.getParameter("type");
            AziendaDAO aziendaDAO = daoFactory.getAziendaDAO();
            aziende = aziendaDAO.getClientiFilter(tipo, filtro);

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("tableloaded", "true");
            request.setAttribute("aziende", aziende);
            request.setAttribute("action", "view");
            request.setAttribute("filtro", filtro);
            request.setAttribute("type", tipo);

            AziendaController.loadFiltri(request, response);


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

