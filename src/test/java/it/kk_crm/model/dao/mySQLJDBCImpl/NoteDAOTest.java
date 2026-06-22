package it.kk_crm.model.dao.mySQLJDBCImpl;

import it.kk_crm.model.mo.Azienda;
import it.kk_crm.model.mo.Cliente;
import it.kk_crm.model.mo.Note;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NoteDAOTest {
    private static final String URL = "jdbc:mysql://localhost:3306/crm_test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // La tua password

    private Connection conn;
    private NoteDAOMySQLJDBCImpl noteDAO;
    private ClienteDAOMySQLJDBCImpl clienteDAO;
    private AziendaDAOMySQLJDBCImpl aziendaDAO;
    private UtenteDAOMySQLJDBCImpl utenteDAO;

    private final String TEST_OWNER = "test.owner";
    private final String TEST_PIVA = "12345678901";
    private final String TEST_CF = "CLTNOT80A01H501Z";

    @BeforeEach
    void setUp() throws Exception {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(false);

        utenteDAO = new UtenteDAOMySQLJDBCImpl(conn);
        aziendaDAO = new AziendaDAOMySQLJDBCImpl(conn);
        clienteDAO = new ClienteDAOMySQLJDBCImpl(conn);
        noteDAO = new NoteDAOMySQLJDBCImpl(conn);

        // Pulizia (Ordine inverso dipendenze)
        try (java.sql.Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM note");
            stmt.executeUpdate("DELETE FROM proposte");
            stmt.executeUpdate("DELETE FROM cliente");
            stmt.executeUpdate("DELETE FROM azienda");
            stmt.executeUpdate("DELETE FROM utente");
        }

        // Matrioska: Utente -> Azienda -> Cliente
        utenteDAO.create(TEST_OWNER, "pass", "admin", "CFOWNER123");
        createAziendaPadre(TEST_PIVA, TEST_OWNER);
        createClientePadre(TEST_CF, TEST_PIVA);

        conn.commit();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (conn != null) {
            conn.rollback();
            conn.close();
        }
    }

    private void eseguiUpdate(String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    private void createAziendaPadre(String piva, String owner) throws Exception {
        Azienda a = new Azienda();
        a.setP_Iva(piva); a.setNome("Azienda Test"); a.setAssigned(owner); a.setDeleted("N");
        a.setForma("S"); a.setIndirizzo("I"); a.setEmail("E"); a.setTelefono("T"); a.setCatMerce("C"); a.setTipologia("T");
        aziendaDAO.create(a);
    }

    private void createClientePadre(String cf, String piva) throws Exception {
        Cliente c = new Cliente();
        c.setCF(cf); c.setNome("Mario"); c.setCognome("Rossi"); c.setPiva(piva); c.setDeleted("N");
        c.setTelefono("T"); c.setDataNascita("2000-01-01"); c.setEmail("E");
        clienteDAO.create(c);
    }

    @Test
    void testCreateLoadAndDelete() throws Exception {
        // 1. Create
        Note nota = new Note();
        nota.setNota("Nota importante");
        nota.setData("2025-10-10");
        nota.setUtente(TEST_OWNER);
        nota.setCliente_cf(TEST_CF);

        noteDAO.create(nota);
        conn.commit();

        // 2. Load
        List<Note> lista = noteDAO.loadNoteAll();
        assertEquals(1, lista.size());
        assertEquals("Nota importante", lista.get(0).getNota());

        // 3. Delete
        Integer id = lista.get(0).getId();
        noteDAO.delete(id);
        conn.commit();

        // Verifica cancellazione logica (getNota cerca Deleted='N')
        Note recuperata = noteDAO.getNota(String.valueOf(id));
        assertNull(recuperata);
    }

    @Test
    void testLoadNoteFilter() throws Exception {
        // Test filtro per nome e cognome cliente
        Note nota = new Note();
        nota.setNota("Nota Filtro");
        nota.setData("2025-10-10");
        nota.setUtente(TEST_OWNER);
        nota.setCliente_cf(TEST_CF); // Questo cliente si chiama Mario Rossi
        noteDAO.create(nota);
        conn.commit();

        List<Note> filtrate = noteDAO.loadNoteFilter("Mario", "Rossi");
        assertEquals(1, filtrate.size());

        List<Note> filtrateMale = noteDAO.loadNoteFilter("Luigi", "Verdi");
        assertEquals(0, filtrateMale.size());
    }

    @Test
    void testUpdate() throws Exception {
        // 1. Setup
        Note nota = new Note();
        nota.setNota("Vecchia Nota");
        nota.setData("2025-01-01");
        nota.setUtente(TEST_OWNER);
        nota.setCliente_cf(TEST_CF);
        noteDAO.create(nota);
        conn.commit();

        // Recuperiamo l'ID generato
        List<Note> lista = noteDAO.loadNoteAll();
        Note notaSalvata = lista.get(0);

        // 2. Update
        notaSalvata.setNota("Nuova Nota Aggiornata");
        notaSalvata.setData("2025-02-02");
        noteDAO.update(notaSalvata);
        conn.commit();

        // 3. Verify
        Note aggiornata = noteDAO.getNota(String.valueOf(notaSalvata.getId()));
        assertEquals("Nuova Nota Aggiornata", aggiornata.getNota());
        assertEquals("2025-02-02", aggiornata.getData());
    }

    @Test
    void testDeleteC_ByCliente() throws Exception {
        // Inseriamo 2 note per lo stesso cliente
        createNotaDummy("Nota 1", TEST_CF);
        createNotaDummy("Nota 2", TEST_CF);
        conn.commit();

        // Cancelliamo tutto ciò che riguarda questo cliente
        noteDAO.deleteC(TEST_CF);
        conn.commit();

        // Verifica
        List<Note> lista = noteDAO.loadNoteAll();
        assertTrue(lista.isEmpty(), "Tutte le note del cliente dovevano essere cancellate");
    }

    @Test
    void testLoadNoteOwner_And_NotOwner() throws Exception {
        /*
         SCENARIO:
         Il TEST_OWNER possiede l'azienda e il cliente (definito nel setUp).
         1. Inseriamo una nota scritta da TEST_OWNER (loadNoteOwner deve vederla)
         2. Inseriamo una nota scritta da "other.user" (loadNoteNotOwner deve vederla)
         */

        // Nota scritta dal proprietario (Oggi)
        Note notaOwner = new Note();
        notaOwner.setNota("Scritta da Me");
        notaOwner.setData(java.time.LocalDate.now().toString()); // Data di oggi per la query CURDATE()
        notaOwner.setUtente(TEST_OWNER);
        notaOwner.setCliente_cf(TEST_CF);
        noteDAO.create(notaOwner);

        // Nota scritta da un altro
        Note notaOther = new Note();
        notaOther.setNota("Scritta da Altri");
        notaOther.setData(java.time.LocalDate.now().toString());
        notaOther.setUtente("other.user"); // Utente diverso
        notaOther.setCliente_cf(TEST_CF);
        noteDAO.create(notaOther);

        conn.commit();

        // TEST 1: loadNoteOwner (Note mie sui miei clienti)
        List<Note> noteMie = noteDAO.loadNoteOwner(TEST_OWNER);
        assertEquals(1, noteMie.size());
        assertEquals("Scritta da Me", noteMie.get(0).getNota());

        // TEST 2: loadNoteNotOwner (Note di altri sui miei clienti)
        List<Note> noteAltri = noteDAO.loadNoteNotOwner(TEST_OWNER);
        assertEquals(1, noteAltri.size());
        assertEquals("Scritta da Altri", noteAltri.get(0).getNota());
    }

    // Helper veloce
    private void createNotaDummy(String testo, String cfCliente) {
        Note n = new Note();
        n.setNota(testo); n.setData("2025-01-01"); n.setUtente(TEST_OWNER); n.setCliente_cf(cfCliente);
        noteDAO.create(n);
    }
}
