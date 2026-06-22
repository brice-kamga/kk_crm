package it.kk_crm.model.dao.mySQLJDBCImpl;

import it.kk_crm.model.dao.AppuntamentoDAO;
import it.kk_crm.model.mo.Appuntamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppuntamentoMySQLJDBCImpl implements AppuntamentoDAO {

    Connection conn;
    public AppuntamentoMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void create(Appuntamento appuntamento)
    {
        PreparedStatement ps;

        try {
            int i;
            String sql
                    = " INSERT INTO appuntamento "
                    + "(Data, Note, CF_C, Deleted)"
                    + " VALUES (?, ?, ?, 'N') ";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, appuntamento.getData());
            ps.setString(i++, appuntamento.getNote());
            ps.setString(i++, appuntamento.getCF());
            ps.executeUpdate();
            ps.close();

        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer codice) {

        PreparedStatement ps;

        try {
            String sql = "UPDATE appuntamento SET Deleted = 'Y' WHERE (Codice = ?) ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, codice);

            ps.executeUpdate();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Appuntamento> loadAllAppuntamenti(String owner){
        PreparedStatement ps;
        Appuntamento appuntamento;
        ArrayList<Appuntamento> appuntamenti = new ArrayList<Appuntamento>();

        try {
            String sql = " SELECT Codice, Data, Note, CF_C, appuntamento.Deleted, Cognome, azienda.Nome " +
                    "FROM appuntamento JOIN cliente on CF_C=CF JOIN azienda on P_Iva_Azienda = P_Iva WHERE Assigned=? AND appuntamento.Deleted = 'N' order by Data DESC";

            ps = conn.prepareStatement(sql);
            ps.setString(1, owner);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                appuntamento = read(resultSet);
                appuntamenti.add(appuntamento);
            }
            resultSet.close();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }

        return appuntamenti;
    }

    public List<Appuntamento> loadSelected(String data, String owner){
        PreparedStatement ps;
        Appuntamento appuntamento;
        ArrayList<Appuntamento> appuntamenti = new ArrayList<Appuntamento>();

        try {
            String sql = " SELECT Codice, Data, Note, CF_C, appuntamento.Deleted, Cognome, azienda.Nome " +
                    "FROM appuntamento JOIN cliente on CF_C=CF JOIN azienda on P_Iva_Azienda = P_Iva WHERE Assigned=? AND appuntamento.Deleted = 'N' AND Data = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, owner);
            ps.setString(2, data);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                appuntamento = read(resultSet);
                appuntamenti.add(appuntamento);
            }
            resultSet.close();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }

        return appuntamenti;
    }

    public List<Appuntamento> loadAppuntamenti7Days(String owner){
        PreparedStatement ps;
        Appuntamento appuntamento;
        ArrayList<Appuntamento> appuntamenti = new ArrayList<Appuntamento>();

        try {
            String sql = " select Codice, Data, cliente.Cognome, Note, azienda.Nome, appuntamento.Deleted, appuntamento.CF_C  " +
                    "FROM appuntamento join cliente on CF_C=CF join azienda on P_Iva_Azienda = P_Iva where Assigned = ? AND appuntamento.Deleted = 'N' AND appuntamento.Data > (CURDATE()) AND appuntamento.Data <=(CURDATE() + INTERVAL  7 day)";

            ps = conn.prepareStatement(sql);
            ps.setString(1, owner);


            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                appuntamento = read(resultSet);
                appuntamenti.add(appuntamento);
            }
            resultSet.close();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }

        return appuntamenti;
    }
    @Override
    public void deleteC(String cf) {

        PreparedStatement ps;

        try {
            String sql = "UPDATE appuntamento SET Deleted = 'Y' WHERE (CF_C = ?) ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cf);

            ps.executeUpdate();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Appuntamento read(ResultSet rs) {
        Appuntamento appuntamento = new Appuntamento();

        try {
            appuntamento.setCodice(rs.getInt("Codice"));
        } catch (SQLException sqle) {
        }
        try {
            appuntamento.setData(rs.getString("Data"));
        } catch (SQLException sqle) {
        }
        try {
            appuntamento.setNote(rs.getString("Note"));
        } catch (SQLException sqle) {
        }
        try {
            appuntamento.setCF(rs.getString("CF_C"));
        } catch (SQLException sqle) {
        }
        try {
            appuntamento.setDeleted(rs.getString("Deleted"));
        } catch (SQLException sqle) {
        }
        try {
            appuntamento.setCognome(rs.getString("Cognome"));
        } catch (SQLException sqle) {
        }
        try {
            appuntamento.setNome(rs.getString("Nome"));
        } catch (SQLException sqle) {
        }

        return appuntamento;
    }
}
