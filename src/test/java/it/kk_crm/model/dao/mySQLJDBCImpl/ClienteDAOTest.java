package it.kk_crm.model.dao.mySQLJDBCImpl;

import it.kk_crm.model.dao.exception.DuplicatedObjectException;
import it.kk_crm.model.mo.Azienda;
import it.kk_crm.model.mo.Cliente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClienteDAOTest {

    // Parametri DB di Test
    private static final String URL = "jdbc:mysql://localhost:3306/crm_test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection conn;
    private ClienteDAOMySQLJDBCImpl clienteDAO;
    private AziendaDAOMySQLJDBCImpl aziendaDAO;
    private UtenteDAOMySQLJDBCImpl utenteDAO;

    // Costanti per i dati di test (Padri e Nonni)
    private final String TEST_OWNER = "test.owner";
    private final String TEST_PIVA = "12345678901";

    @BeforeEach
    void setUp() throws Exception {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(false);

        // 1. Inizializziamo tutti i DAO
        utenteDAO = new UtenteDAOMySQLJDBCImpl(conn);
        aziendaDAO = new AziendaDAOMySQLJDBCImpl(conn);
        clienteDAO = new ClienteDAOMySQLJDBCImpl(conn);

        // 2. Pulizia Totale (Ordine: Figli -> Padri -> Nonni)
        try (java.sql.Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM proposte");
            stmt.executeUpdate("DELETE FROM cliente");
            stmt.executeUpdate("DELETE FROM azienda");
            stmt.executeUpdate("DELETE FROM utente");
        }

        // 3. Creiamo l'Utente (Il Nonno)
        utenteDAO.create(TEST_OWNER, "pass", "admin", "CFOWNER123");

        // 4. Creiamo l'Azienda (Il Padre) collegata all'Utente
        createAziendaPadre(TEST_PIVA, TEST_OWNER);

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

    // Helper per creare l'azienda necessaria ai clienti
    private void createAziendaPadre(String piva, String owner) throws Exception {
        Azienda a = new Azienda();
        a.setP_Iva(piva);
        a.setNome("Azienda Test");
        a.setAssigned(owner);
        a.setDeleted("N");
        // Campi dummy
        a.setForma("SRL"); a.setIndirizzo("Via Test"); a.setEmail("az@test.it");
        a.setTelefono("000"); a.setCatMerce("IT"); a.setTipologia("Prospect");
        aziendaDAO.create(a);
    }

    @Test
    void testCreateEGetClienti() throws Exception {
        // 1. Creiamo un Cliente
        Cliente c = new Cliente();
        c.setCF("CLTMAR80A01H501Z");
        c.setNome("Mario");
        c.setCognome("Rossi");
        c.setTelefono("333123456");
        c.setDataNascita("1980-01-01");
        c.setEmail("mario@test.it");
        c.setPiva(TEST_PIVA); // Colleghiamo all'azienda creata nel setUp
        c.setDeleted("N");

        clienteDAO.create(c);
        conn.commit();

        // 2. Recuperiamo la lista clienti
        List<Cliente> lista = clienteDAO.getClienti();

        // 3. Verifiche
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Mario", lista.get(0).getNome());
        assertEquals("Rossi", lista.get(0).getCognome());
        assertEquals(TEST_PIVA, lista.get(0).getPiva());
    }

    @Test
    void testUpdate() throws Exception {
        // 1. Inserimento
        Cliente c = new Cliente();
        c.setCF("CLTUPD80A01H501X");
        c.setNome("VecchioNome");
        c.setCognome("VecchioCognome");
        c.setDataNascita("1990-01-01");
        c.setPiva(TEST_PIVA);
        c.setDeleted("N");
        // Campi dummy opzionali
        c.setTelefono("000"); c.setEmail("x@x.x");

        clienteDAO.create(c);
        conn.commit();

        // 2. Modifica
        c.setNome("NuovoNome");
        c.setCognome("NuovoCognome");
        clienteDAO.update(c);
        conn.commit();

        // 3. Verifica recuperando dal DB
        // Usiamo getClienti per filtrare e trovare quello giusto
        List<Cliente> lista = clienteDAO.getClienti();
        Cliente aggiornato = lista.stream()
                .filter(cl -> cl.getCF().equals("CLTUPD80A01H501X"))
                .findFirst()
                .orElse(null);

        assertNotNull(aggiornato);
        assertEquals("NuovoNome", aggiornato.getNome());
        assertEquals("NuovoCognome", aggiornato.getCognome());
    }

    @Test
    void testDeleteLogica() throws Exception {
        String cf = "CLTDEL99A01H501Y";
        Cliente c = new Cliente();
        c.setCF(cf);
        c.setNome("Da Cancellare");
        c.setCognome("Test");
        c.setDataNascita("2000-01-01");
        c.setPiva(TEST_PIVA);
        c.setDeleted("N");
        // Campi dummy
        c.setTelefono("0"); c.setEmail("0");

        clienteDAO.create(c);
        conn.commit();

        // Cancellazione logica
        clienteDAO.delete(cf);
        conn.commit();

        // Verifica: getClienti ha "WHERE client.Deleted = 'N'"
        List<Cliente> lista = clienteDAO.getClienti();
        boolean trovato = lista.stream().anyMatch(cl -> cl.getCF().equals(cf));

        assertFalse(trovato, "Il cliente cancellato non dovrebbe essere nella lista");
    }

    @Test
    void testResurrezioneCliente() throws Exception {
        String cf = "CLTZOM88A01H501K";

        // 1. Creiamo e cancelliamo
        Cliente c = new Cliente();
        c.setCF(cf);
        c.setNome("Zombie");
        c.setCognome("Uno");
        c.setDataNascita("1988-01-01");
        c.setPiva(TEST_PIVA);
        c.setDeleted("N");
        // dummy
        c.setTelefono("1"); c.setEmail("1");

        clienteDAO.create(c);
        conn.commit();

        clienteDAO.delete(cf);
        conn.commit();

        // 2. Ricreiamo lo stesso cliente (Resurrezione) con dati nuovi
        Cliente cNuovo = new Cliente();
        cNuovo.setCF(cf);
        cNuovo.setNome("Zombie Rinato");
        cNuovo.setCognome("Due");
        cNuovo.setDataNascita("1988-01-01");
        cNuovo.setPiva(TEST_PIVA);
        cNuovo.setDeleted("N"); // Importante
        // dummy
        cNuovo.setTelefono("2"); cNuovo.setEmail("2");

        clienteDAO.create(cNuovo); // Non deve lanciare eccezione
        conn.commit();

        // 3. Verifica
        List<Cliente> lista = clienteDAO.getClienti();
        Cliente rinato = lista.stream().filter(cl -> cl.getCF().equals(cf)).findFirst().orElse(null);

        assertNotNull(rinato);
        assertEquals("Zombie Rinato", rinato.getNome());
        assertEquals("N", rinato.getDeleted());
    }

    @Test
    void testGetClientiOwner() throws Exception {
        // Questo test verifica la JOIN complessa: Cliente -> Azienda -> Utente

        // 1. Inseriamo un cliente per TEST_OWNER (già configurato nel setUp)
        Cliente c1 = new Cliente();
        c1.setCF("CLTOWN11A01H501Q");
        c1.setNome("Cliente Mio");
        c1.setCognome("Mio");
        c1.setDataNascita("1990-01-01");
        c1.setPiva(TEST_PIVA);
        c1.setDeleted("N");
        // dummy
        c1.setTelefono("1"); c1.setEmail("1");
        clienteDAO.create(c1);
        conn.commit();

        // 2. Creiamo un altro owner, un'altra azienda e un altro cliente (Intruso)
        utenteDAO.create("other.user", "p", "a", "CFOTHER");
        createAziendaPadre("99999999999", "other.user");

        Cliente c2 = new Cliente();
        c2.setCF("CLTOTH22A01H501W");
        c2.setNome("Cliente Altri");
        c2.setCognome("Altri");
        c2.setDataNascita("1990-01-01");
        c2.setPiva("99999999999"); // Collegato all'altra azienda
        c2.setDeleted("N");
        // dummy
        c2.setTelefono("2"); c2.setEmail("2");
        clienteDAO.create(c2);
        conn.commit();

        // 3. Chiamata al metodo da testare
        List<Cliente> mieiClienti = clienteDAO.getClientiOwner(TEST_OWNER);

        // 4. Verifica: Dovremmo trovare solo c1, non c2
        assertEquals(1, mieiClienti.size());
        assertEquals("Cliente Mio", mieiClienti.get(0).getNome());
    }

    @Test
    void testGetClienteWAzienda() throws Exception {
        // Verifica il metodo che trova i clienti per P.IVA Azienda
        Cliente c = new Cliente();
        c.setCF("CLTAZI55A01H501L");
        c.setNome("Cliente Aziendale");
        c.setCognome("Test");
        c.setDataNascita("1955-01-01");
        c.setPiva(TEST_PIVA);
        c.setDeleted("N");
        // dummy
        c.setTelefono("5"); c.setEmail("5");

        clienteDAO.create(c);
        conn.commit();

        Cliente trovato = clienteDAO.getClienteWAzienda(TEST_PIVA);
        assertNotNull(trovato);
        assertEquals("Cliente Aziendale", trovato.getNome());
    }

    @Test
    void testDuplicatedException() throws Exception {
        Cliente c = new Cliente();
        c.setCF("CLTDUP00A01H501D");
        c.setNome("Originale");
        c.setCognome("Originale");
        c.setDataNascita("2000-01-01");
        c.setPiva(TEST_PIVA);
        c.setDeleted("N");
        // dummy
        c.setTelefono("0"); c.setEmail("0");

        clienteDAO.create(c);
        conn.commit();

        // Tentativo di duplicato
        assertThrows(DuplicatedObjectException.class, () -> {
            clienteDAO.create(c);
        });
    }
}
