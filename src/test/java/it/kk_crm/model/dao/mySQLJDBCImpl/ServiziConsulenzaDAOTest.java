package it.kk_crm.model.dao.mySQLJDBCImpl;

import it.kk_crm.model.mo.Azienda;
import it.kk_crm.model.mo.Proposte;
import it.kk_crm.model.mo.ServiziConsulenza;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiziConsulenzaDAOTest {
    private static final String URL = "jdbc:mysql://localhost:3306/crm_test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection conn;
    private ServiziConsulenzaDAOMySQLJDBCImpl serviziDAO;
    // DAO extra necessari per testare l'effetto a catena sulle proposte
    private ProposteSDAOMySQLJDBCImpl proposteDAO;
    private AziendaDAOMySQLJDBCImpl aziendaDAO;
    private UtenteDAOMySQLJDBCImpl utenteDAO;

    @BeforeEach
    void setUp() throws Exception {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(false);

        serviziDAO = new ServiziConsulenzaDAOMySQLJDBCImpl(conn);
        proposteDAO = new ProposteSDAOMySQLJDBCImpl(conn);
        aziendaDAO = new AziendaDAOMySQLJDBCImpl(conn);
        utenteDAO = new UtenteDAOMySQLJDBCImpl(conn);

        eseguiUpdate("DELETE FROM proposte");
        eseguiUpdate("DELETE FROM servizi_consulenza");
        eseguiUpdate("DELETE FROM azienda");
        eseguiUpdate("DELETE FROM utente");

        // Setup base per poter inserire proposte
        utenteDAO.create("owner", "p", "a", "CF1");
        Azienda a = new Azienda();
        a.setP_Iva("123"); a.setNome("A"); a.setAssigned("owner"); a.setDeleted("N");
        // dummy
        a.setForma("S"); a.setIndirizzo("I"); a.setEmail("E"); a.setTelefono("T"); a.setCatMerce("C"); a.setTipologia("T");
        aziendaDAO.create(a);

        conn.commit();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (conn != null) { conn.rollback(); conn.close(); }
    }

    private void eseguiUpdate(String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) { ps.executeUpdate(); }
    }

    @Test
    void testCreateLoadDelete() throws Exception {
        // Create
        ServiziConsulenza s = new ServiziConsulenza();
        s.setId(100); s.setTipo_servizio("Gold"); s.setDeleted("N");
        serviziDAO.create(s);
        conn.commit();

        // Load
        List<ServiziConsulenza> lista = serviziDAO.loadAllServizi();
        assertEquals(1, lista.size());

        // Delete
        serviziDAO.deleteServizio(100);
        conn.commit();
        assertTrue(serviziDAO.loadAllServizi().isEmpty());
    }

    @Test
    void testDeleteServizio_CascadeOnProposte() throws Exception {
        // Questo test verifica che cancellando un servizio, si cancellino anche le proposte associate

        // 1. Creiamo Servizio
        ServiziConsulenza s = new ServiziConsulenza();
        s.setId(500); s.setTipo_servizio("Service To Delete"); s.setDeleted("N");
        serviziDAO.create(s);

        // 2. Creiamo Proposta collegata
        Proposte p = new Proposte();
        p.setTipo("Prop"); p.setCodice_servizio(500); p.setPIva("123"); p.setDeleted("N");
        proposteDAO.create(p);

        conn.commit();

        // 3. Cancelliamo il Servizio
        serviziDAO.deleteServizio(500);
        conn.commit();

        // 4. Verifica: Il servizio deve essere sparito...
        assertTrue(serviziDAO.loadAllServizi().isEmpty());

        // ... E LA PROPOSTA DEVE ESSERE SPARITA ANCHE LEI!
        assertTrue(proposteDAO.loadAllProposte().isEmpty(), "La deleteServizio deve cancellare logicamente anche le proposte");
    }

    @Test
    void testResurrezioneServizio() throws Exception {
        ServiziConsulenza s = new ServiziConsulenza();
        s.setId(200); s.setTipo_servizio("Vecchio"); s.setDeleted("N");
        serviziDAO.create(s);
        conn.commit();
        serviziDAO.deleteServizio(200);
        conn.commit();

        ServiziConsulenza sNuovo = new ServiziConsulenza();
        sNuovo.setId(200); sNuovo.setTipo_servizio("Nuovo"); sNuovo.setDeleted("N");
        serviziDAO.create(sNuovo);
        conn.commit();

        assertEquals("Nuovo", serviziDAO.loadAllServizi().get(0).getTipo_servizio());
    }
}
