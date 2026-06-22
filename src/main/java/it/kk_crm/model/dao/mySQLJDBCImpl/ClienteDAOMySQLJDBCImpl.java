package it.kk_crm.model.dao.mySQLJDBCImpl;

import it.kk_crm.model.dao.ClienteDAO;
import it.kk_crm.model.dao.exception.DuplicatedObjectException;
import it.kk_crm.model.mo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOMySQLJDBCImpl implements ClienteDAO {

    Connection conn;
    public ClienteDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void create(Cliente cliente) throws DuplicatedObjectException
    {
        PreparedStatement ps;


        try {
            String sql = " SELECT CF, Deleted FROM cliente WHERE CF = ?";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, cliente.getCF());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();

            if (exist) {
                String deleted = resultSet.getString("Deleted");
                resultSet.close();
                if(deleted.equals("Y")){
                    sql = "UPDATE cliente SET Deleted = 'N', Nome = ?, Cognome = ?, Telefono = ?, Data_Nascita = ?," +
                            " Email = ?, P_Iva_Azienda = ? WHERE (CF = ?) ";
                    ps = conn.prepareStatement(sql);
                    i = 1;
                    ps.setString(i++, cliente.getNome());
                    ps.setString(i++, cliente.getCognome());
                    ps.setString(i++, cliente.getTelefono());
                    ps.setString(i++, cliente.getDataNascita());
                    ps.setString(i++, cliente.getEmail());
                    ps.setString(i++, cliente.getPiva());
                    ps.setString(i++, cliente.getCF());
                    ps.executeUpdate();
                    ps.close();
                    return;
                } else{
                    throw new DuplicatedObjectException("ClienteDAOJDBCImpl.create: Tentativo di inserimento di un referente già esistente.");
                }
            }
            sql
                    = " INSERT INTO cliente "
                    + "(CF, Nome, Cognome, Telefono, Data_Nascita, Email, P_Iva_Azienda, Deleted)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, 'N') ";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, cliente.getCF());
            ps.setString(i++, cliente.getNome());
            ps.setString(i++, cliente.getCognome());
            ps.setString(i++, cliente.getTelefono());
            ps.setString(i++, cliente.getDataNascita());
            ps.setString(i++, cliente.getEmail());
            ps.setString(i++, cliente.getPiva());


            ps.executeUpdate();
            ps.close();

        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }

        //return azienda;

    }
    public List<Cliente> getClienti() {
        PreparedStatement ps;
        Cliente cliente = null;
        ArrayList<Cliente> clienti = new ArrayList<>();

        try {
            String sql = "SELECT  CF, cliente.Nome, Cognome, cliente.Telefono, Data_Nascita, cliente.Email, P_Iva_Azienda, cliente.Deleted" +
                    " FROM cliente join azienda on P_Iva_Azienda = P_Iva WHERE cliente.Deleted = 'N'";
            ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                cliente = read(resultSet);
                clienti.add(cliente);
            }
            resultSet.close();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return clienti;

    }
    public List<Cliente> getClientiOwner(String owner) {
        PreparedStatement ps;
        Cliente cliente = null;
        ArrayList<Cliente> clienti = new ArrayList<>();

        try {
            String sql = "SELECT  CF, cliente.Nome, Cognome, cliente.Telefono, Data_Nascita, cliente.Email, P_Iva_Azienda, cliente.Deleted" +
                    " FROM cliente join azienda on P_Iva_Azienda = P_Iva WHERE azienda.Assigned = ? AND cliente.Deleted = 'N'";
            ps = conn.prepareStatement(sql);
            ps.setString(1,owner);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                cliente = read(resultSet);
                clienti.add(cliente);
            }
            resultSet.close();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return clienti;

    }
    @Override
    public Cliente getClienteWAzienda(String piva) {
        PreparedStatement ps;
        Cliente cliente = null;


        try {
            String sql = "SELECT  CF, cliente.Nome, Cognome, cliente.Telefono, Data_Nascita, cliente.Email, P_Iva_Azienda, cliente.Deleted" +
                    " FROM cliente join azienda on P_Iva_Azienda = P_Iva WHERE cliente.Deleted = 'N' AND P_Iva_Azienda = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,piva);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                cliente = read(resultSet);

            }
            resultSet.close();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return cliente;

    }

    @Override
    public void delete(String cf) {
        PreparedStatement ps;
        try {
            String sql = "UPDATE cliente SET Deleted = 'Y' WHERE (CF = ?) ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cf);
            ps.executeUpdate();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Cliente cliente) {

        PreparedStatement ps;

        try {
            String sql = "UPDATE cliente SET Nome = ?, Cognome = ?, Email = ?, Telefono = ?, Data_Nascita = ?" +
                    "WHERE (CF = ?) ";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, cliente.getNome());
            ps.setString(i++, cliente.getCognome());
            ps.setString(i++, cliente.getEmail());
            ps.setString(i++, cliente.getTelefono());
            ps.setString(i++, cliente.getDataNascita());
            ps.setString(i++, cliente.getCF());
            ps.executeUpdate();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Cliente read(ResultSet rs) {
        Cliente cliente = new Cliente();

        try {
            cliente.setCF(rs.getString("CF"));
        } catch (SQLException sqle) {
        }
        try {
            cliente.setNome(rs.getString("Nome"));
        } catch (SQLException sqle) {
        }
        try {
            cliente.setCognome(rs.getString("Cognome"));
        } catch (SQLException sqle) {
        }
        try {
            cliente.setTelefono(rs.getString("Telefono"));
        } catch (SQLException sqle) {
        }
        try {
            cliente.setDataNascita(rs.getString("Data_Nascita"));
        } catch (SQLException sqle) {
        }
        try {
            cliente.setEmail(rs.getString("Email"));
        } catch (SQLException sqle) {
        }
        try {
            cliente.setPiva(rs.getString("P_Iva_Azienda"));
        } catch (SQLException sqle) {
        }
        try {
            cliente.setDeleted(rs.getString("Deleted"));
        } catch (SQLException sqle) {
        }

        return cliente;
    }
}


