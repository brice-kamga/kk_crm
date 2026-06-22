package it.kk_crm.model.dao;

import it.kk_crm.model.dao.CookieImpl.CookieDAOFactory;
import it.kk_crm.model.dao.mySQLJDBCImpl.MySQLJDBCDAOFactory;

import java.util.Map;

public abstract class DAOFactory {

    public static final String MYSQLJDBCIMPL = "MySQLJDBCImpl";
    public static final String COOKIEIMPL= "CookieImpl";

    public abstract void beginTransaction();
    public abstract void commitTransaction();
    public abstract void rollbackTransaction();
    public abstract void closeTransaction();


    public abstract UtenteDAO getUtenteDAO();

    public abstract AziendaDAO getAziendaDAO();

    public abstract NoteDAO getNoteDAO();

    public abstract ProposteSDAO getProposteSDAO();

    public abstract ServiziConsulenzaDAO getServiziConsulenzaDAO();

    public abstract AppuntamentoDAO getAppuntamentoDAO();

    public abstract ClienteDAO getClienteDAO();

    public static DAOFactory getDAOFactory(String whichFactory, Map factoryParameters) {

        if (whichFactory.equals(MYSQLJDBCIMPL)) {
            return new MySQLJDBCDAOFactory(factoryParameters);
        } else if (whichFactory.equals(COOKIEIMPL)) {
            return new CookieDAOFactory(factoryParameters);
        } else {
            return null;
        }
    }
}


