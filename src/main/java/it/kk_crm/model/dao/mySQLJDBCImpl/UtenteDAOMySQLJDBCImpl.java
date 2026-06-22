package it.kk_crm.model.dao.mySQLJDBCImpl;

import it.kk_crm.model.dao.UtenteDAO;
import it.kk_crm.model.dao.exception.DuplicatedObjectException;
import it.kk_crm.model.mo.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UtenteDAOMySQLJDBCImpl implements UtenteDAO {

    Connection conn;

    public UtenteDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }


    @Override
    public Utente create(String username, String password, String tipo, String CF_Utente) throws DuplicatedObjectException {
        PreparedStatement ps;


        try {
            String sql = " SELECT Username, Deleted FROM utente WHERE Username = ?";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, username);

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            //resultSet.close();
            if (exist) {
                String deleted = resultSet.getString("Deleted");
                resultSet.close();
                if(deleted.equals("Y")){
                    sql = "UPDATE utente SET Deleted = 'N', Password = ?, Tipo = ?, CF_Utente = ? WHERE (Username = ?) ";
                    ps = conn.prepareStatement(sql);
                    i = 1;
                    ps.setString(i++, password);
                    ps.setString(i++, tipo);
                    ps.setString(i++, CF_Utente);
                    ps.setString(i++, username);
                    ps.executeUpdate();
                    ps.close();
                    return null;
                } else{
                    throw new DuplicatedObjectException("AziendaDAOJDBCImpl.create: Tentativo di inserimento di un azienda già esistente.");                }

            }
            sql = " INSERT INTO utente "
                    + "(Username, Password, Tipo, CF_Utente, Deleted)"
                    + " VALUES (?, ?, ?, ?, 'N') ";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, username);
            ps.setString(i++, password);
            ps.setString(i++, tipo);
            ps.setString(i++, CF_Utente);

            ps.executeUpdate();


            ps.close();
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Utente utente) {
        String sql = "UPDATE utente SET tipo = ? WHERE username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, utente.getTipo());
            ps.setString(2, utente.getUsername());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Utente utente) {
        PreparedStatement ps;

        try {
            String sql = "UPDATE utente SET Deleted = 'Y' WHERE (username = ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1,utente.getUsername());

            ps.executeUpdate();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    //dedicato al DAO dei Cookies
    public Utente findLoggedUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Utente findByUsername(String username) {

        PreparedStatement ps;
        Utente user = null;

        try {

            String sql
                    = " SELECT * "
                    + "   FROM utente "
                    + " WHERE "
                    + "   Username = ?" +
                    " AND Deleted = 'N'";

            ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                user = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;

    }

    public List<Utente> loadAllUtenti() {
        PreparedStatement ps;
        Utente utente = null;
        ArrayList<Utente> utenti = new ArrayList<>();

        try {
            String sql = "SELECT *" +
                    " FROM utente where Tipo = 'registrato' AND Deleted = 'N'";
            ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                utente = read(resultSet);
                utenti.add(utente);
            }
            resultSet.close();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return utenti;

    }

    public List<Utente> loadAll() {
        PreparedStatement ps;
        Utente utente = null;
        ArrayList<Utente> utenti = new ArrayList<>();

        try {
            String sql = "SELECT *" +
                    " FROM utente";
            ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                utente = read(resultSet);
                utenti.add(utente);
            }
            resultSet.close();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return utenti;

    }

    Utente read(ResultSet rs) {

        Utente user = new Utente();
        try {
            user.setUsername(rs.getString("Username"));
        } catch (SQLException sqle) {
        }
        try {
            user.setPassword(rs.getString("Password"));
        } catch (SQLException sqle) {
        }
        try {
            user.setTipo(rs.getString("Tipo"));
        } catch (SQLException sqle) {
        }
        try {
            user.setCF(rs.getString("CF_Utente"));
        } catch (SQLException sqle) {
        }
        try {
            user.setDeleted(rs.getString("Deleted"));
        } catch (SQLException sqle) {
        }
        return user;
    }

}

