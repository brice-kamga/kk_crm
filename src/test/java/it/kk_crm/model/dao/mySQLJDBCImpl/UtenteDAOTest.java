package it.kk_crm.model.dao.mySQLJDBCImpl;

import it.kk_crm.model.dao.exception.DuplicatedObjectException;
import it.kk_crm.model.mo.Utente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtenteDAOTest {

    // Parametri di connessione al DB di TEST (crm_test)
    private static final String URL = "jdbc:mysql://localhost:3306/crm_test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection conn;
    private UtenteDAOMySQLJDBCImpl dao;

    @BeforeEach
    void setUp() throws SQLException {
        // 1. Apriamo la connessione vera al DB di test
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(false); // Disabilitiamo l'autocommit per poter fare rollback

        // 2. Inizializziamo il DAO con la connessione
        dao = new UtenteDAOMySQLJDBCImpl(conn);

        // 3. Pulizia preventiva: assicuriamoci che l'utente di test non esista
        cancellaUtenteTest("test.user");
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Alla fine di ogni test, facciamo rollback per lasciare il DB pulito
        if (conn != null) {
            conn.rollback();
            conn.close();
        }
    }

    // Metodo di aiuto per pulire il DB manualmente se serve
    private void cancellaUtenteTest(String username) throws SQLException {
        String sql = "DELETE FROM utente WHERE Username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        }
        conn.commit(); // Conferma la cancellazione
    }

    @Test
    void testCreateEFind() throws Exception {
        // 1. CREATE
        System.out.println("Test: Creazione Utente...");
        dao.create("test.user", "password123", "admin", "TSTUSR80A01H501Z");

        // Committo temporaneamente per vedere se la find lo trova (opzionale se stessa connessione)
        conn.commit();

        // 2. READ (Find)
        System.out.println("Test: Ricerca Utente...");
        Utente utenteTrovato = dao.findByUsername("test.user");

        // 3. ASSERT
        assertNotNull(utenteTrovato, "L'utente dovrebbe essere stato trovato nel DB");
        assertEquals("test.user", utenteTrovato.getUsername());
        assertEquals("password123", utenteTrovato.getPassword());
        assertEquals("admin", utenteTrovato.getTipo());
        assertEquals("N", utenteTrovato.getDeleted());
    }

    @Test
    void testDeleteLogica() throws Exception {
        // 1. Creiamo un utente
        dao.create("test.delete", "pass", "user", "CFDELETE123");
        conn.commit();

        Utente u = new Utente();
        u.setUsername("test.delete");

        // 2. Eseguiamo la delete (che nel tuo codice è una UPDATE Deleted='Y')
        System.out.println("Test: Cancellazione Logica...");
        dao.delete(u);
        conn.commit();

        // 3. Verifichiamo
        // findByUsername nel tuo codice ha "WHERE Deleted = 'N'"
        // Quindi ora dovrebbe restituire NULL
        Utente utenteCancellato = dao.findByUsername("test.delete");
        assertNull(utenteCancellato, "L'utente cancellato logicamente non dovrebbe essere trovato da findByUsername");
    }

    @Test
    void testDuplicatedException() throws Exception {
        // 1. Creiamo il primo utente
        dao.create("test.duplicate", "pass", "user", "CFDUP123");
        conn.commit();

        // 2. Proviamo a creare lo stesso utente una seconda volta
        // Ci aspettiamo che lanci DuplicatedObjectException
        System.out.println("Test: Controllo Duplicati...");

        assertThrows(DuplicatedObjectException.class, () -> {
            dao.create("test.duplicate", "passNew", "admin", "CFDUP123");
        });
    }

    @Test
    void testRiattivazioneUtenteCancellato() throws Exception {
        // 1. Creiamo un utente
        String username = "test.zombie";
        dao.create(username, "passOld", "admin", "CFOLD123");
        conn.commit();

        // 2. Lo cancelliamo logicamente (Deleted = 'Y')
        Utente u = new Utente();
        u.setUsername(username);
        dao.delete(u);
        conn.commit();

        // Verifica intermedia: deve essere sparito/cancellato
        assertNull(dao.findByUsername(username));

        // 3. Proviamo a RICREARLO con lo stesso username ma dati nuovi
        // NON deve lanciare eccezione, deve riattivarlo!
        System.out.println("Test: Riattivazione Utente Cancellato...");
        dao.create(username, "passNew", "registrato", "CFNEW123");
        conn.commit();

        // 4. Verifichiamo che sia tornato attivo con i nuovi dati
        Utente rinato = dao.findByUsername(username);
        assertNotNull(rinato);
        assertEquals("passNew", rinato.getPassword());
        assertEquals("registrato", rinato.getTipo());
        assertEquals("N", rinato.getDeleted());
    }

    @Test
    void testLoadAll() throws Exception {
        // 1. Pulizia completa per rispettare i vincoli di chiave esterna (Foreign Keys)
        try (java.sql.Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM proposte");
            stmt.executeUpdate("DELETE FROM cliente");
            stmt.executeUpdate("DELETE FROM azienda");
            stmt.executeUpdate("DELETE FROM utente");
        }

        // 2. Inseriamo 3 utenti diversi
        dao.create("user1", "p", "admin", "CF1");
        dao.create("user2", "p", "registrato", "CF2");
        dao.create("user3", "p", "pubblico", "CF3");
        conn.commit();

        // 3. Carichiamo tutti
        System.out.println("Test: Load All...");
        List<Utente> lista = dao.loadAll();

        // 4. Verifichiamo
        assertNotNull(lista);
        assertEquals(3, lista.size(), "Dovrebbero eccerci 3 utenti nel DB");
    }

    @Test
    void testLoadAllUtenti_FiltroRegistrato() throws Exception {
        // 1. Pulizia preventiva rispettando l'ordine gerarchico delle tabelle
        try (java.sql.Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM proposte");
            stmt.executeUpdate("DELETE FROM cliente");
            stmt.executeUpdate("DELETE FROM azienda");
            stmt.executeUpdate("DELETE FROM utente");
        }

        // 2. Inserimento dei dati di test
        dao.create("mario.registrato", "pass", "registrato", "CFREG1");

        // Candidato TIPO ERRATO: Tipo 'admin' (non deve essere caricato)
        dao.create("luigi.admin", "pass", "admin", "CFADM1");

        // Candidato CANCELLATO: Tipo 'registrato' ma Deleted='Y' (non deve essere caricato)
        // Lo creiamo prima...
        dao.create("zombie.user", "pass", "registrato", "CFDEL1");
        conn.commit();
        // ...e poi lo cancelliamo logicamente
        Utente zombie = new Utente();
        zombie.setUsername("zombie.user");
        dao.delete(zombie);
        conn.commit();

        // 3. Azione: Chiamiamo il metodo specifico
        System.out.println("Test: Load All Utenti (Solo Registrati)...");
        List<Utente> listaFiltrata = dao.loadAllUtenti();

        // 4. Verifica
        assertNotNull(listaFiltrata);

        // Ci aspettiamo di trovare ESATTAMENTE 1 solo utente (mario.registrato)
        // Gli altri due devono essere ignorati dalla query SQL
        assertEquals(1, listaFiltrata.size(), "Deve caricare solo gli utenti 'registrato' attivi");

        // Verifichiamo che si tratti effettivamente di lui
        assertEquals("mario.registrato", listaFiltrata.get(0).getUsername());
    }
}