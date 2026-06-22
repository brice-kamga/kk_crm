package it.kk_crm.model.dao.mySQLJDBCImpl;

import it.kk_crm.model.dao.NoteDAO;
import it.kk_crm.model.mo.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteDAOMySQLJDBCImpl implements NoteDAO {

    Connection conn;
    public NoteDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }


    @Override
    public void create(Note nota)
    {
        PreparedStatement ps;

        try {
            int i;
            String sql
                    = " INSERT INTO note "
                    + "(nota, data, utente, cliente_cf, Deleted)"
                    + " VALUES (?, ?, ?, ?, 'N') ";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, nota.getNota());
            ps.setString(i++, nota.getData());
            ps.setString(i++, nota.getUtente());
            ps.setString(i++, nota.getCliente_cf());


            ps.executeUpdate();
            ps.close();

        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void update(Note nota) {
        PreparedStatement ps;

        try {
            String sql = "UPDATE note SET nota = ?, data = ?, utente = ?, cliente_cf = ? WHERE (id = ?) ";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, nota.getNota());
            ps.setString(i++, nota.getData());
            ps.setString(i++, nota.getUtente());
            ps.setString(i++, nota.getCliente_cf());
            ps.setInt(i++, nota.getId());

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
            String sql = "UPDATE note SET Deleted = 'Y' WHERE (id = ?) ";
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
    public List<Note> loadNoteAll() {
        PreparedStatement ps;
        Note nota;
        ArrayList<Note> note = new ArrayList<Note>();

        try {
            String sql = " SELECT * FROM note WHERE Deleted = 'N'";
            ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                nota = read(resultSet);
                note.add(nota);
            }
            resultSet.close();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }

        return note;
    }

    @Override
    public List<Note> loadNoteFilter(String nome, String cognome) {
        PreparedStatement ps;
        Note nota;
        ArrayList<Note> note = new ArrayList<Note>();

        try {
            String sql = " SELECT id, nota, data, utente, cliente_cf FROM note join cliente on cliente_cf = CF WHERE Nome LIKE ? AND Cognome LIKE ? AND note.Deleted = 'N'";
            ps = conn.prepareStatement(sql);
            ps.setString(1, nome.concat("%"));
            ps.setString(2, cognome.concat("%"));

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                nota = read(resultSet);
                note.add(nota);
            }
            resultSet.close();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }

        return note;
    }

    public Note getNota(String id) {
        PreparedStatement ps;
        Note nota = null;

        try {
            String sql = " SELECT * FROM note WHERE id = ? AND Deleted = 'N'";
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                nota = read(resultSet);
            }
            resultSet.close();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return nota;
    }

    //VISUALIZZA NOTE JOIN CLIENTI JOIN AZIENDA WHERE NOT OWNER, ULTIMI 30 Giorni(note messe da altri utenti sui miei clienti)
    @Override
    public List<Note> loadNoteNotOwner(String owner) {
        PreparedStatement ps;
        Note nota;
        ArrayList<Note> note = new ArrayList<Note>();

        try {
            //select note.nota, note.data, cliente.nome, cliente.cognome, azienda.nome  as azienda, note.utente from note join cliente on cliente_cf = CF join azienda on P_Iva_Azienda = P_Iva where note.data <= CURDATE() AND note.data >= (CURDATE() -interval 1 month) AND cliente.owner = @owner AND note.utente <> @owner
            String sql = " SELECT note.id, note.nota, note.data, note.utente, note.cliente_cf FROM note join cliente on cliente_cf = CF join azienda on P_Iva_Azienda = P_Iva WHERE note.data <= CURDATE() AND note.data >= (CURDATE() -interval 1 month) AND azienda.Assigned = ? AND note.utente <> ? AND note.Deleted = 'N'";
            ps = conn.prepareStatement(sql);
            ps.setString(1, owner);
            ps.setString(2, owner);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                nota = read(resultSet);
                note.add(nota);
            }
            resultSet.close();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }

        return note;
    }

    //VISUALIZZA NOTE JOIN CLIENTI JOIN AZIENDA WHERE OWNER, ULTIMI 30 Giorni(note messe da owner sui miei clienti)
    @Override
    public List<Note> loadNoteOwner(String owner) {
        PreparedStatement ps;
        Note nota;
        ArrayList<Note> note = new ArrayList<Note>();

        try {
            String sql = " SELECT note.id, note.nota, note.data, note.utente, note.cliente_cf FROM note join cliente on cliente_cf = CF join azienda on P_Iva_Azienda = P_Iva WHERE note.data <= CURDATE() AND note.data >= (CURDATE() -interval 1 month) AND azienda.Assigned = ? AND note.utente = ? AND note.Deleted = 'N'";
            ps = conn.prepareStatement(sql);
            ps.setString(1, owner);
            ps.setString(2, owner);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                nota = read(resultSet);
                note.add(nota);
            }
            resultSet.close();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }

        return note;
    }
    @Override
    public void deleteC(String cf) {

        PreparedStatement ps;

        try {
            String sql = "UPDATE note SET Deleted = 'Y' WHERE (cliente_cf = ?) ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cf);

            ps.executeUpdate();
            ps.close();
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Note read(ResultSet rs) {
        Note nota = new Note();

        try {
            nota.setId(rs.getInt("id"));
        } catch (SQLException sqle) {
        }
        try {
            nota.setNota(rs.getString("nota"));
        } catch (SQLException sqle) {
        }
        try {
            nota.setData(rs.getString("data"));
        } catch (SQLException sqle) {
        }
        try {
            nota.setUtente(rs.getString("utente"));
        } catch (SQLException sqle) {
        }
        try {
            nota.setCliente_cf(rs.getString("cliente_cf"));
        } catch (SQLException sqle) {
        }

        return nota;
    }
}

