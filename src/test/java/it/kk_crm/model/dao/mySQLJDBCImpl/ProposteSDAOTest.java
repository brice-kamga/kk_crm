package it.kk_crm.model.dao.mySQLJDBCImpl;

import it.kk_crm.model.mo.Azienda;
import it.kk_crm.model.mo.Proposte;
import it.kk_crm.model.mo.ProposteS;
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

class ProposteSDAOTest {
    private static final String URL = "jdbc:mysql://localhost:3306/crm_test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection conn;
    private ProposteSDAOMySQLJDBCImpl proposteDAO;
    private AziendaDAOMySQLJDBCImpl aziendaDAO;
    private UtenteDAOMySQLJDBCImpl utenteDAO;
    private ServiziConsulenzaDAOMySQLJDBCImpl serviziDAO;

    private final String TEST_OWNER = "test.owner";
    private final String TEST_PIVA = "12345678901";
    private final int TEST_SERVIZIO_ID = 500;

    @BeforeEach
    void setUp() throws Exception {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(false);

        utenteDAO = new UtenteDAOMySQLJDBCImpl(conn);
        aziendaDAO = new AziendaDAOMySQLJDBCImpl(conn);
        serviziDAO = new ServiziConsulenzaDAOMySQLJDBCImpl(conn);
        proposteDAO = new ProposteSDAOMySQLJDBCImpl(conn);

        // Pulizia
        try (java.sql.Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM proposte");
            stmt.executeUpdate("DELETE FROM servizi_consulenza");
            stmt.executeUpdate("DELETE FROM cliente");
            stmt.executeUpdate("DELETE FROM azienda");
            stmt.executeUpdate("DELETE FROM utente");
        }

        // 1. Creiamo Utente e Azienda (Il Cliente della proposta)
        utenteDAO.create(TEST_OWNER, "pass", "admin", "CFOWNER123");
        createAziendaPadre(TEST_PIVA, TEST_OWNER);

        // 2. Creiamo il Servizio (L'Oggetto della proposta)
        ServiziConsulenza s = new ServiziConsulenza();
        s.setId(TEST_SERVIZIO_ID); s.setTipo_servizio("Servizio Test"); s.setDeleted("N");
        serviziDAO.create(s);

        conn.commit();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (conn != null) { conn.rollback(); conn.close(); }
    }

    private void eseguiUpdate(String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) { ps.executeUpdate(); }
    }

    private void createAziendaPadre(String piva, String owner) throws Exception {
        Azienda a = new Azienda();
        a.setP_Iva(piva); a.setNome("Azienda Test"); a.setAssigned(owner); a.setDeleted("N");
        a.setForma("S"); a.setIndirizzo("I"); a.setEmail("E"); a.setTelefono("T"); a.setCatMerce("C"); a.setTipologia("T");
        aziendaDAO.create(a);
    }

    @Test
    void testCreateLoadDelete() throws Exception {
        // 1. Create
        Proposte p = new Proposte(); // Nota: Usiamo la classe POJO base per l'inserimento
        p.setTipo("Sconto 50%");
        p.setCodice_servizio(TEST_SERVIZIO_ID);
        p.setPIva(TEST_PIVA);
        p.setDeleted("N");

        proposteDAO.create(p);
        conn.commit();

        // 2. Load
        List<ProposteS> lista = proposteDAO.loadAllProposte();
        assertEquals(1, lista.size());

        ProposteS recuperata = lista.get(0);
        assertEquals("Sconto 50%", recuperata.getTipo());
        // Verifica JOIN: deve aver caricato il nome del servizio e dell'azienda
        assertEquals("Servizio Test", recuperata.getNome_servizio());
        assertEquals("Azienda Test", recuperata.getNome()); // Mappa su azienda.Nome

        // 3. Delete
        proposteDAO.delete(recuperata.getId());
        conn.commit();

        List<ProposteS> listaDopo = proposteDAO.loadAllProposte();
        assertTrue(listaDopo.isEmpty());
    }

    @Test
    void testLoadProposteOwner() throws Exception {
        // Test filtro per owner (azienda.Assigned)

        // Creiamo una proposta per il nostro owner
        Proposte p1 = new Proposte();
        p1.setTipo("Mia"); p1.setCodice_servizio(TEST_SERVIZIO_ID); p1.setPIva(TEST_PIVA); p1.setDeleted("N");
        proposteDAO.create(p1);

        // Creiamo un intruso (altro owner, altra azienda)
        utenteDAO.create("other", "p", "a", "CFOTHER");
        createAziendaPadre("999", "other");
        Proposte p2 = new Proposte();
        p2.setTipo("Altra"); p2.setCodice_servizio(TEST_SERVIZIO_ID); p2.setPIva("999"); p2.setDeleted("N");
        proposteDAO.create(p2);

        conn.commit();

        List<ProposteS> mie = proposteDAO.loadProposteOwner(TEST_OWNER);
        assertEquals(1, mie.size());
        assertEquals("Mia", mie.get(0).getTipo());
    }

    @Test
    void testDeleteWServizio() throws Exception {
        // Creiamo una proposta collegata al servizio TEST_SERVIZIO_ID (definito nel file precedente)
        Proposte p = new Proposte();
        p.setTipo("P1"); p.setCodice_servizio(TEST_SERVIZIO_ID); p.setPIva(TEST_PIVA); p.setDeleted("N");
        proposteDAO.create(p);
        conn.commit();

        // Cancelliamo tutte le proposte legate a questo servizio
        proposteDAO.deleteWServizio(TEST_SERVIZIO_ID);
        conn.commit();

        assertTrue(proposteDAO.loadAllProposte().isEmpty());
    }

    @Test
    void testDeleteC_ByAzienda() throws Exception {
        // Creiamo una proposta per l'azienda TEST_PIVA
        Proposte p = new Proposte();
        p.setTipo("P2"); p.setCodice_servizio(TEST_SERVIZIO_ID); p.setPIva(TEST_PIVA); p.setDeleted("N");
        proposteDAO.create(p);
        conn.commit();

        // Cancelliamo tutte le proposte di questa azienda
        proposteDAO.deleteC(TEST_PIVA);
        conn.commit();

        assertTrue(proposteDAO.loadAllProposte().isEmpty());
    }
}
