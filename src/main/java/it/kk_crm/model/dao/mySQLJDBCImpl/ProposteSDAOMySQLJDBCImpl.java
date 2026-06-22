package it.kk_crm.model.dao.mySQLJDBCImpl;

import it.kk_crm.model.dao.ProposteSDAO;
import it.kk_crm.model.mo.Proposte;
import it.kk_crm.model.mo.ProposteS;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProposteSDAOMySQLJDBCImpl implements ProposteSDAO {

    Connection conn;
    public ProposteSDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void create(Proposte proposta)
    {
        PreparedStatement ps;

        try {
            int i;
            String sql
                    = " INSERT INTO proposte "
                    + "(tipo, codice_servizio, PIva, Deleted)"
                    + " VALUES (?, ?, ?, 'N') ";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, proposta.getTipo());
            ps.setInt(i++, proposta.getCodice_servizio() );
            ps.setString(i++, proposta.getPIva());
            ps.executeUpdate();
            ps.close();

        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(Integer id) {

        PreparedStatement ps;

        try {
            String sql = "UPDATE proposte SET Deleted = 'Y' WHERE (id = ?) ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteWServizio(Integer codice) {

        PreparedStatement ps;

        try {
            String sql = "UPDATE proposte SET Deleted = 'Y' WHERE (codice_servizio = ?) ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, codice);

            ps.executeUpdate();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProposteS> loadAllProposte() {

        PreparedStatement ps;
        ProposteS proposta;
        ArrayList<ProposteS> proposte = new ArrayList<ProposteS>();

        try {
            String sql = "SELECT proposte.id, tipo, codice_servizio, PIva, proposte.Deleted, tipo_servizio, Nome " +
                    "FROM azienda join proposte on P_Iva = PIva join servizi_consulenza on codice_servizio = servizi_consulenza.id " +
                    "WHERE proposte.Deleted = 'N'";
            ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()) {
                proposta = read(resultSet);
                proposte.add(proposta);
            }

            resultSet.close();
            ps.close();

        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }

        return proposte;

    }

    //Visualizza TUTTE LE PROPOSTE JOIN CLIENTI WHERE OWNER(proposte attive sui miei clienti)
    public List<ProposteS> loadProposteOwner(String owner) {

        PreparedStatement ps;
        ProposteS proposta;
        ArrayList<ProposteS> proposte = new ArrayList<ProposteS>();

        try {
            String sql = "SELECT proposte.id, tipo, codice_servizio, PIva, proposte.Deleted, tipo_servizio, Nome " +
                    "FROM azienda join proposte on P_Iva = PIva join servizi_consulenza on codice_servizio = servizi_consulenza.id " +
                    "WHERE proposte.Deleted = 'N' AND azienda.Assigned = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, owner);

            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()) {
                proposta = read(resultSet);
                proposte.add(proposta);
            }

            resultSet.close();
            ps.close();

        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }

        return proposte;

    }

    @Override
    public void deleteC(String piva) {

        PreparedStatement ps;

        try {
            String sql = "UPDATE proposte SET Deleted = 'Y' WHERE (PIva = ?) ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, piva);

            ps.executeUpdate();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    ProposteS read(ResultSet rs) {
        ProposteS proposta = new ProposteS();

        try {
            proposta.setId(rs.getInt("id"));
        } catch (SQLException sqle) {
        }
        try {
            proposta.setTipo(rs.getString("tipo"));
        } catch (SQLException sqle) {
        }
        try {
            proposta.setCodice_servizio(rs.getInt("codice_servizio"));
        } catch (SQLException sqle) {
        }
        try {
            proposta.setPIva(rs.getString("PIva"));
        } catch (SQLException sqle) {
        }
        try {
            proposta.setNome_servizio(rs.getString("tipo_servizio"));
        } catch (SQLException sqle) {
        }
        try {
            proposta.setDeleted(rs.getString("Deleted"));
        } catch (SQLException sqle) {
        }
        try {
            proposta.setNome(rs.getString("Nome"));
        } catch (SQLException sqle) {
        }


        return proposta;
    }
}

