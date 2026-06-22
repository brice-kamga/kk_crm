package it.kk_crm.model.dao.mySQLJDBCImpl;


import it.kk_crm.model.dao.ServiziConsulenzaDAO;
import it.kk_crm.model.dao.exception.DuplicatedObjectException;
import it.kk_crm.model.mo.ServiziConsulenza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiziConsulenzaDAOMySQLJDBCImpl implements ServiziConsulenzaDAO {

    Connection conn;
    public ServiziConsulenzaDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }


    public List<ServiziConsulenza> loadAllServizi(){
        PreparedStatement ps;
        ServiziConsulenza servizio;
        ArrayList<ServiziConsulenza> servizi = new ArrayList<ServiziConsulenza>();

        try {
            String sql = "SELECT * " +
                    "FROM servizi_consulenza " +
                    "WHERE Deleted = 'N'";
            ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()) {
                servizio = read(resultSet);
                servizi.add(servizio);
            }
            resultSet.close();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return servizi;

    }

    public void deleteServizio(Integer id){
        PreparedStatement ps;

        try {
            String sql = "UPDATE servizi_consulenza SET Deleted = 'Y' WHERE (id = ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,id);

            ps.executeUpdate();

            sql = "UPDATE proposte SET Deleted = 'Y' WHERE (codice_servizio = ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();

            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void create(ServiziConsulenza servizio) throws DuplicatedObjectException
    {
        PreparedStatement ps;


        try {
            String sql = " SELECT id, Deleted FROM servizi_consulenza WHERE id = ?";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, servizio.getId());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();

            if (exist) {
                String deleted = resultSet.getString("Deleted");
                resultSet.close();
                if(deleted.equals("Y")){
                    sql = "UPDATE servizi_consulenza SET Deleted = 'N', tipo_servizio = ? WHERE (id = ?) ";
                    ps = conn.prepareStatement(sql);
                    i = 1;
                    ps.setString(i++ ,servizio.getTipo_servizio());
                    ps.setInt(i++ , servizio.getId());
                    ps.executeUpdate();
                    ps.close();
                    return;
                } else{
                    throw new DuplicatedObjectException("ServiziConsulenzaDAOJDBCImpl.create: Tentativo di inserimento di un servizio già esistente.");
                }
            }
            sql = " INSERT INTO servizi_consulenza "
                    + "(id, tipo_servizio, Deleted)"
                    + " VALUES (?, ?, 'N') ";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setInt(i++, servizio.getId());
            ps.setString(i++, servizio.getTipo_servizio());

            ps.executeUpdate();


            ps.close();

        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }

    }

    ServiziConsulenza read(ResultSet rs) {
        ServiziConsulenza servizio = new ServiziConsulenza();

        try {
            servizio.setId(rs.getInt("id"));
        } catch (SQLException sqle) {
        }
        try {
            servizio.setTipo_servizio(rs.getString("tipo_servizio"));
        } catch (SQLException sqle) {
        }
        try {
            servizio.setDeleted(rs.getString("Deleted"));
        } catch (SQLException sqle) {
        }
        return servizio;
    }
}
