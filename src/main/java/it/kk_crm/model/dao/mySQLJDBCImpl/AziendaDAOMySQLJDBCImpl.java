package it.kk_crm.model.dao.mySQLJDBCImpl;

import it.kk_crm.model.dao.AziendaDAO;
import it.kk_crm.model.dao.exception.DuplicatedObjectException;
import it.kk_crm.model.mo.Azienda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AziendaDAOMySQLJDBCImpl implements AziendaDAO {
    Connection conn;

    public AziendaDAOMySQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void create(Azienda azienda) throws DuplicatedObjectException {
        PreparedStatement ps;

        try {
            // RIMOSSO "crm."
            String sql = " SELECT P_Iva, Deleted FROM azienda WHERE P_Iva = ?";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, azienda.getP_Iva());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();

            if (exist) {
                String deleted = resultSet.getString("Deleted");
                resultSet.close();
                if (deleted.equals("Y")) {
                    // RIMOSSO "crm."
                    sql = "UPDATE azienda SET Deleted = 'N', Nome = ?, Forma = ?, Email = ?, Telefono = ?, Indirizzo = ?," +
                            "Cat_merce = ?, Assigned = ?, Tipologia = ?  WHERE (P_Iva = ?) ";
                    ps = conn.prepareStatement(sql);
                    i = 1;
                    ps.setString(i++, azienda.getNome());
                    ps.setString(i++, azienda.getForma());
                    ps.setString(i++, azienda.getEmail());
                    ps.setString(i++, azienda.getTelefono());
                    ps.setString(i++, azienda.getIndirizzo());
                    ps.setString(i++, azienda.getCatMerce());
                    ps.setString(i++, azienda.getAssigned());
                    ps.setString(i++, azienda.getTipologia());
                    ps.setString(i++, azienda.getP_Iva());
                    ps.executeUpdate();
                    ps.close();
                    return;
                } else {
                    throw new DuplicatedObjectException("AziendaDAOJDBCImpl.create: Tentativo di inserimento di un azienda già esistente.");
                }

            }
            // RIMOSSO "crm."
            sql = " INSERT INTO azienda "
                    + "(P_Iva, Nome, Forma, Email, Telefono, Indirizzo, Cat_merce, Assigned, Tipologia, Deleted)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

            ps = conn.prepareStatement(sql);
            i = 1;
            ps.setString(i++, azienda.getP_Iva());
            ps.setString(i++, azienda.getNome());
            ps.setString(i++, azienda.getForma());
            ps.setString(i++, azienda.getEmail());
            ps.setString(i++, azienda.getTelefono());
            ps.setString(i++, azienda.getIndirizzo());
            ps.setString(i++, azienda.getCatMerce());
            ps.setString(i++, azienda.getAssigned());
            ps.setString(i++, azienda.getTipologia());
            ps.setString(i++, azienda.getDeleted());

            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String piva) {

        PreparedStatement ps;

        try {
            // RIMOSSO "crm."
            String sql = "UPDATE azienda SET Deleted = 'Y' WHERE (P_Iva = ?) ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, piva);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Azienda azienda) {

        PreparedStatement ps;

        try {
            // RIMOSSO "crm."
            String sql = "UPDATE azienda SET Nome = ?, Forma = ?, Email = ?, Telefono = ?, Indirizzo = ?," +
                    " Cat_merce = ?, Tipologia = ?, Deleted = 'N' WHERE (P_Iva = ?) ";
            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, azienda.getNome());
            ps.setString(i++, azienda.getForma());
            ps.setString(i++, azienda.getEmail());
            ps.setString(i++, azienda.getTelefono());
            ps.setString(i++, azienda.getIndirizzo());
            ps.setString(i++, azienda.getCatMerce());
            ps.setString(i++, azienda.getTipologia());
            ps.setString(i++, azienda.getP_Iva());


            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Azienda> selectOwner(String owner) {
        PreparedStatement ps;
        Azienda azienda;
        ArrayList<Azienda> aziende = new ArrayList<Azienda>();

        try {
            // RIMOSSO "crm."
            String sql = " SELECT * FROM azienda WHERE Assigned = ? AND Deleted = 'N'";
            ps = conn.prepareStatement(sql);
            ps.setString(1, owner);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                azienda = read(resultSet);
                aziende.add(azienda);
            }
            resultSet.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return aziende;
    }

    @Override
    public List<Azienda> selectOwnerWReferente(String owner) {
        PreparedStatement ps;
        Azienda azienda;
        ArrayList<Azienda> aziende = new ArrayList<Azienda>();

        try {
            // CORRETTO: Aggiunto "AS NomeRef" e rimosso "crm."
            String sql = " SELECT azienda.P_Iva, azienda.Nome, azienda.Forma, azienda.Indirizzo, azienda.Email, azienda.Telefono, azienda.Cat_merce, " +
                    "azienda.Assigned, azienda.Tipologia, azienda.Deleted, cliente.Cognome, cliente.Nome AS NomeRef " +
                    "FROM azienda join cliente on P_Iva = P_Iva_Azienda WHERE Assigned = ? AND azienda.Deleted = 'N'";
            ps = conn.prepareStatement(sql);
            ps.setString(1, owner);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                azienda = read2(resultSet); // read2 usa correttamente l'alias ora
                aziende.add(azienda);
            }
            resultSet.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return aziende;
    }


    public Azienda getAzienda(String piva) {
        PreparedStatement ps;
        Azienda azienda = null;

        try {
            // RIMOSSO "crm."
            String sql = " SELECT * FROM azienda WHERE P_Iva = ? AND Deleted = 'N'";
            ps = conn.prepareStatement(sql);
            ps.setString(1, piva);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                azienda = read(resultSet);
            }
            resultSet.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return azienda;
    }

    public List<String> getDistinctCat() {
        PreparedStatement ps;
        ArrayList<String> Categorie = new ArrayList<>();

        try {
            String sql = "SELECT distinct Cat_merce FROM azienda WHERE Deleted = 'N'";
            ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Categorie.add(resultSet.getString("Cat_merce"));
            }
            resultSet.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Categorie;
    }

    public List<String> getDistinctTipologia() {
        PreparedStatement ps;
        ArrayList<String> Tipologie = new ArrayList<>();

        try {
            String sql = "SELECT distinct Tipologia FROM azienda WHERE Deleted = 'N'";
            ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Tipologie.add(resultSet.getString("Tipologia"));
            }
            resultSet.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Tipologie;
    }

    public List<String> getDistinctOwner() {
        PreparedStatement ps;
        ArrayList<String> Owner = new ArrayList<>();

        try {
            String sql = "SELECT distinct Assigned FROM azienda WHERE Deleted = 'N'";
            ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Owner.add(resultSet.getString("Assigned"));
            }
            resultSet.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Owner;
    }

    public List<Azienda> getClientiFilter(String tipo, String filtro) {
        PreparedStatement ps;
        Azienda azienda = null;
        ArrayList<Azienda> aziende = new ArrayList<>();

        try {
            if (!tipo.equals("Libera")) {
                String sql = "SELECT * FROM azienda WHERE " + tipo + "=? AND Deleted = 'N'";
                ps = conn.prepareStatement(sql);
                ps.setString(1, filtro);

                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    azienda = read(resultSet);
                    aziende.add(azienda);
                }
                resultSet.close();
                ps.close();
            } else {
                String sql = "select * from azienda where Deleted = 'N'";
                ps = conn.prepareStatement(sql);

                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    azienda = read(resultSet);
                    aziende.add(azienda);
                }
                resultSet.close();
                ps.close();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return aziende;
    }

    Azienda read(ResultSet rs) {
        Azienda azienda = new Azienda();

        try {
            azienda.setP_Iva(rs.getString("P_Iva"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setNome(rs.getString("Nome"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setForma(rs.getString("Forma"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setIndirizzo(rs.getString("Indirizzo"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setEmail(rs.getString("Email"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setTelefono(rs.getString("Telefono"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setCatMerce(rs.getString("Cat_merce"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setAssigned(rs.getString("Assigned"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setTipologia(rs.getString("Tipologia"));
        } catch (SQLException sqle) {
        }
        // AGGIUNTA LETTURA DELETED
        try {
            azienda.setDeleted(rs.getString("Deleted"));
        } catch (SQLException sqle) {
        }
        return azienda;
    }

    Azienda read2(ResultSet rs) {
        Azienda azienda = new Azienda();

        try {
            azienda.setP_Iva(rs.getString("P_Iva"));
        } catch (SQLException sqle) {
        }
        try {
            // CORRETTO: Legge il nome dell'azienda dalla colonna "Nome"
            azienda.setNome(rs.getString("Nome"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setForma(rs.getString("Forma"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setIndirizzo(rs.getString("Indirizzo"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setEmail(rs.getString("Email"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setTelefono(rs.getString("Telefono"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setCatMerce(rs.getString("Cat_merce"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setAssigned(rs.getString("Assigned"));
        } catch (SQLException sqle) {
        }
        try {
            azienda.setTipologia(rs.getString("Tipologia"));
        } catch (SQLException sqle) {
        }
        // AGGIUNTA LETTURA DELETED
        try {
            azienda.setDeleted(rs.getString("Deleted"));
        } catch (SQLException sqle) {
        }

        try {
            azienda.setCognomeRef(rs.getString("Cognome"));
        } catch (SQLException sqle) {
        }
        try {
            // CORRETTO: Legge il nome del referente dall'alias "NomeRef"
            azienda.setNomeRef(rs.getString("NomeRef"));
        } catch (SQLException sqle) {
        }
        return azienda;
    }
}
