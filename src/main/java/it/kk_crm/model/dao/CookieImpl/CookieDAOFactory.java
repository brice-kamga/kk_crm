package it.kk_crm.model.dao.CookieImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import it.kk_crm.model.dao.*;

public class CookieDAOFactory extends DAOFactory {

    private Map factoryParameters;

    private HttpServletRequest request;
    private HttpServletResponse response;

    public CookieDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }

    @Override
    public void beginTransaction() {

        try {
            this.request=(HttpServletRequest) factoryParameters.get("request");
            this.response=(HttpServletResponse) factoryParameters.get("response");;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {}

    @Override
    public void rollbackTransaction() {}

    @Override
    public void closeTransaction() {}

    @Override
    public UtenteDAO getUtenteDAO() {
        return new UtenteDAOCookieImpl(request,response);
    }
    @Override
    public AziendaDAO getAziendaDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public NoteDAO getNoteDAO() { throw new UnsupportedOperationException("Not supported yet."); }
    @Override
    public ProposteSDAO getProposteSDAO() { throw new UnsupportedOperationException("Not supported yet."); }
    @Override
    public ServiziConsulenzaDAO getServiziConsulenzaDAO() { throw new UnsupportedOperationException("Not supported yet."); }
    @Override
    public AppuntamentoDAO getAppuntamentoDAO() { throw new UnsupportedOperationException("Not supported yet."); }

    @Override
    public ClienteDAO getClienteDAO() { throw new UnsupportedOperationException("Not supported yet."); }
}
