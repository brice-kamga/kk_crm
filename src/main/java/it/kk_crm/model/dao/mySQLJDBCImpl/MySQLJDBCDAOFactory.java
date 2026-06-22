package it.kk_crm.model.dao.mySQLJDBCImpl;

import it.kk_crm.model.dao.*;
import it.kk_crm.services.Configuration;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class  MySQLJDBCDAOFactory extends DAOFactory {

    private Map factoryParameters;

    private Connection connection;

    public MySQLJDBCDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }

    @Override
    public void beginTransaction() {

        try {
            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollbackTransaction() {

        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void closeTransaction() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UtenteDAO getUtenteDAO() {
        return new UtenteDAOMySQLJDBCImpl(connection);
    }

    public AziendaDAO getAziendaDAO() {
        return new AziendaDAOMySQLJDBCImpl(connection);
    }

    public ProposteSDAO getProposteSDAO() {return new ProposteSDAOMySQLJDBCImpl(connection);}

    public NoteDAO getNoteDAO() {
        return new NoteDAOMySQLJDBCImpl(connection);
    }

    public ServiziConsulenzaDAO getServiziConsulenzaDAO() { return new ServiziConsulenzaDAOMySQLJDBCImpl(connection); }

    public AppuntamentoDAO getAppuntamentoDAO() {return new AppuntamentoMySQLJDBCImpl(connection); }

    public ClienteDAO getClienteDAO() {return new ClienteDAOMySQLJDBCImpl(connection);}
}
