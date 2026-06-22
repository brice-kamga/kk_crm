package it.kk_crm.model.dao.mySQLJDBCImpl;

import it.kk_crm.model.mo.Appuntamento;
import it.kk_crm.model.mo.Azienda;
import it.kk_crm.model.mo.Cliente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppuntamentoDAOTest {

    // Parametri DB di Test
    private static final String URL = "jdbc:mysql://localhost:3306/crm_test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection conn;
    private AppuntamentoMySQLJDBCImpl appuntamentoDAO;
    private ClienteDAOMySQLJDBCImpl clienteDAO;
    private AziendaDAOMySQLJDBCImpl aziendaDAO;
    private UtenteDAOMySQLJDBCImpl utenteDAO;

    // Costanti per la catena di dipendenze
    private final String TEST_OWNER = "test.owner";
    private final String TEST_PIVA = "12345678901";
    private final String TEST_CF = "CLTAPP80A01H501Z";

    @BeforeEach
    void setUp() throws Exception {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(false);

        // 1. Inizializziamo tutti i DAO
        utenteDAO = new UtenteDAOMySQLJDBCImpl(conn);
        aziendaDAO = new AziendaDAOMySQLJDBCImpl(conn);
        clienteDAO = new ClienteDAOMySQLJDBCImpl(conn);
        appuntamentoDAO = new AppuntamentoMySQLJDBCImpl(conn);

        // 2. Pulizia Totale (Ordine inverso alle dipendenze)
        try (java.sql.Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM appuntamento");
            stmt.executeUpdate("DELETE FROM proposte");
            stmt.executeUpdate("DELETE FROM cliente");
            stmt.executeUpdate("DELETE FROM azienda");
            stmt.executeUpdate("DELETE FROM utente");
        }

        // 3. COSTRUIAMO LA MATRIOSKA (La base dati necessaria)

        // A. Creiamo il Nonno (Utente)
        utenteDAO.create(TEST_OWNER, "pass", "admin", "CFOWNER123");

        // B. Creiamo il Padre (Azienda)
        createAziendaPadre(TEST_PIVA, TEST_OWNER);

        // C. Creiamo il Figlio (Cliente)
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

    // Helper per Azienda
    private void createAziendaPadre(String piva, String owner) throws Exception {
        Azienda a = new Azienda();
        a.setP_Iva(piva);
        a.setNome("Azienda Test");
        a.setAssigned(owner);
        a.setDeleted("N");
        a.setForma("S");
        a.setIndirizzo("I");
        a.setEmail("E");
        a.setTelefono("T");
        a.setCatMerce("C");
        a.setTipologia("T");
        aziendaDAO.create(a);
    }

    // Helper per Cliente
    private void createClientePadre(String cf, String piva) throws Exception {
        Cliente c = new Cliente();
        c.setCF(cf);
        c.setNome("Mario");
        c.setCognome("Rossi");
        c.setPiva(piva);
        c.setDeleted("N");
        c.setTelefono("T");
        c.setDataNascita("2000-01-01");
        c.setEmail("E");
        clienteDAO.create(c);
    }

    @Test
    void testCreateLoadAndDelete() throws Exception {
        // 1. CREATE
        Appuntamento app = new Appuntamento();
        app.setData("2025-12-25"); // Natale 2025
        app.setNote("Incontro Importante");
        app.setCF(TEST_CF); // Colleghiamo al cliente creato nel setUp

        appuntamentoDAO.create(app);
        conn.commit();

        // 2. LOAD ALL (Verifica inserimento e Join)
        List<Appuntamento> lista = appuntamentoDAO.loadAllAppuntamenti(TEST_OWNER);

        assertNotNull(lista);
        assertEquals(1, lista.size());

        Appuntamento recuperato = lista.get(0);
        assertEquals("2025-12-25", recuperato.getData());
        assertEquals("Incontro Importante", recuperato.getNote());

        // Verifica che le JOIN abbiano funzionato (recuperando dati da Cliente e Azienda)
        assertEquals("Rossi", recuperato.getCognome()); // Dal Cliente
        assertEquals("Azienda Test", recuperato.getNome()); // Dall'Azienda (nota: getNome() in Appuntamento mappa su azienda.Nome nella query)

        // 3. DELETE (Fisica o Logica a seconda dell'implementazione, qui testiamo delete by Codice)
        Integer codice = recuperato.getCodice();
        appuntamentoDAO.delete(codice);
        conn.commit();

        // Verifica cancellazione
        List<Appuntamento> listaDopoDelete = appuntamentoDAO.loadAllAppuntamenti(TEST_OWNER);
        assertTrue(listaDopoDelete.isEmpty(), "La lista dovrebbe essere vuota dopo la cancellazione logica");
    }

    @Test
    void testLoadSelected_DataSpecifica() throws Exception {
        // Inseriamo due appuntamenti in date diverse
        createAppuntamentoDummy("2025-01-01", "Anno Nuovo");
        createAppuntamentoDummy("2025-05-01", "Primo Maggio");
        conn.commit();

        // Cerchiamo solo quello del 1° Gennaio
        List<Appuntamento> risultati = appuntamentoDAO.loadSelected("2025-01-01", TEST_OWNER);

        assertEquals(1, risultati.size());
        assertEquals("Anno Nuovo", risultati.get(0).getNote());
    }

    @Test
    void testLoadAppuntamenti7Days() throws Exception {
        // Test logica "Prossimi 7 giorni" (CURDATE)
        // Questo test è delicato perché dipende dalla data di oggi del sistema.

        LocalDate oggi = LocalDate.now();
        String domani = oggi.plusDays(1).toString();
        String traUnMese = oggi.plusMonths(1).toString();

        // Appuntamento tra 1 giorno (Dovrebbe vederlo)
        createAppuntamentoDummy(domani, "Domani");

        // Appuntamento tra 1 mese (NON Dovrebbe vederlo)
        createAppuntamentoDummy(traUnMese, "Futuro Lontano");

        conn.commit();

        List<Appuntamento> prossimi = appuntamentoDAO.loadAppuntamenti7Days(TEST_OWNER);

        // Dovrebbe trovarne solo 1 (quello di domani)
        // Nota: Se loadAppuntamenti7Days usa CURDATE() di MySQL, assicurati che la timezone sia allineata
        // Ma per un test locale base, di solito funziona.
        assertEquals(1, prossimi.size(), "Dovrebbe trovare solo l'appuntamento nei prossimi 7 giorni");
        assertEquals("Domani", prossimi.get(0).getNote());
    }

    @Test
    void testDeleteC_ByClienteCF() throws Exception {
        // Test cancellazione massiva per Codice Fiscale Cliente
        createAppuntamentoDummy("2025-10-10", "App 1");
        createAppuntamentoDummy("2025-10-11", "App 2");
        conn.commit();

        // Cancelliamo tutti gli appuntamenti di questo cliente
        appuntamentoDAO.deleteC(TEST_CF);
        conn.commit();

        List<Appuntamento> lista = appuntamentoDAO.loadAllAppuntamenti(TEST_OWNER);
        assertTrue(lista.isEmpty(), "Tutti gli appuntamenti del cliente dovevano essere cancellati");
    }

    // Helper privato
    private void createAppuntamentoDummy(String data, String note) {
        Appuntamento app = new Appuntamento();
        app.setData(data);
        app.setNote(note);
        app.setCF(TEST_CF);
        appuntamentoDAO.create(app);
    }
}
