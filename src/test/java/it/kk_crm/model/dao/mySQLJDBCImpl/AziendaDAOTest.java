package it.kk_crm.model.dao.mySQLJDBCImpl;

import it.kk_crm.model.dao.exception.DuplicatedObjectException;
import it.kk_crm.model.mo.Azienda;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AziendaDAOTest {

    // Parametri DB di Test
    private static final String URL = "jdbc:mysql://localhost:3306/crm_test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection conn;
    private AziendaDAOMySQLJDBCImpl aziendaDAO;
    private UtenteDAOMySQLJDBCImpl utenteDAO;

    // Utente "Padre" che useremo come proprietario delle aziende
    private final String TEST_OWNER = "test.owner";

    @BeforeEach
    void setUp() throws Exception {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(false);

        // 1. Inizializziamo i DAO
        utenteDAO = new UtenteDAOMySQLJDBCImpl(conn);
        aziendaDAO = new AziendaDAOMySQLJDBCImpl(conn);

        // 2. Pulizia: Cancelliamo tutto per partire da zero
        // L'ordine è importante per le Foreign Key: prima le figlie (aziende), poi i padri (utenti)
        eseguiUpdate("DELETE FROM cliente");
        eseguiUpdate("DELETE FROM azienda");
        eseguiUpdate("DELETE FROM utente");

        // 3. CRUCIALE: Creiamo l'utente che possiederà le aziende
        // Se non lo facciamo, l'inserimento dell'azienda fallirà (Foreign Key error)
        utenteDAO.create(TEST_OWNER, "pass", "admin", "CFOWNER123");
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

    @Test
    void testCreateEGet() throws Exception {
        // 1. Creiamo un'azienda collegata al nostro TEST_OWNER
        Azienda a = new Azienda();
        a.setP_Iva("12345678901");
        a.setNome("Azienda Test SRL");
        a.setForma("SRL");
        a.setIndirizzo("Via Roma 1");
        a.setEmail("info@test.it");
        a.setTelefono("012345678");
        a.setCatMerce("Informatica");
        a.setAssigned(TEST_OWNER); // Qui usiamo l'utente creato nel setUp
        a.setTipologia("Prospect");
        a.setDeleted("N");

        aziendaDAO.create(a);
        conn.commit();

        // 2. Recuperiamo l'azienda
        Azienda recuperata = aziendaDAO.getAzienda("12345678901");

        // 3. Verifiche
        assertNotNull(recuperata);
        assertEquals("Azienda Test SRL", recuperata.getNome());
        assertEquals("Informatica", recuperata.getCatMerce());
        assertEquals(TEST_OWNER, recuperata.getAssigned());
    }

    @Test
    void testUpdate() throws Exception {
        // 1. Inserimento
        Azienda a = new Azienda();
        a.setP_Iva("11111111111");
        a.setNome("Nome Vecchio");
        a.setAssigned(TEST_OWNER);
        a.setDeleted("N");
        // ... settare altri campi obbligatori per evitare null pointer se il DB è strict ...
        a.setForma("SPA");
        a.setIndirizzo("Via");
        a.setTipologia("Client");

        aziendaDAO.create(a);
        conn.commit();

        // 2. Modifica
        a.setNome("Nome Nuovo");
        a.setForma("SRL");
        aziendaDAO.update(a);
        conn.commit();

        // 3. Verifica
        Azienda aggiornata = aziendaDAO.getAzienda("11111111111");
        assertEquals("Nome Nuovo", aggiornata.getNome());
        assertEquals("SRL", aggiornata.getForma());
    }

    @Test
    void testDeleteLogica() throws Exception {
        Azienda a = new Azienda();
        a.setP_Iva("99999999999");
        a.setNome("Da Cancellare");
        a.setAssigned(TEST_OWNER);
        a.setDeleted("N");
        // campi dummy
        a.setForma("X"); a.setIndirizzo("X"); a.setTipologia("X");

        aziendaDAO.create(a);
        conn.commit();

        // Cancellazione logica
        aziendaDAO.delete("99999999999");
        conn.commit();

        // Verifica: getAzienda ha "WHERE Deleted = 'N'", quindi deve ritornare null
        assertNull(aziendaDAO.getAzienda("99999999999"));
    }

    @Test
    void testResurrezioneAzienda() throws Exception {
        // Test per il blocco if(deleted.equals("Y")) nella create
        String piva = "88888888888";

        // 1. Creiamo e cancelliamo
        Azienda a = new Azienda();
        a.setP_Iva(piva);
        a.setNome("Zombie Corp");
        a.setAssigned(TEST_OWNER);
        a.setDeleted("N");
        // campi dummy
        a.setForma("X"); a.setIndirizzo("X"); a.setTipologia("X");

        aziendaDAO.create(a);
        conn.commit();

        aziendaDAO.delete(piva);
        conn.commit();

        // 2. Ricreiamo la stessa azienda (Resurrezione)
        Azienda aNuova = new Azienda();
        aNuova.setP_Iva(piva);
        aNuova.setNome("Zombie Corp Rinata");
        aNuova.setAssigned(TEST_OWNER);
        aNuova.setDeleted("N"); // Importante
        // campi dummy
        aNuova.setForma("Y"); aNuova.setIndirizzo("Y"); aNuova.setTipologia("Y");

        aziendaDAO.create(aNuova); // Non deve lanciare eccezione
        conn.commit();

        // 3. Verifica
        Azienda rinata = aziendaDAO.getAzienda(piva);
        assertNotNull(rinata);
        assertEquals("Zombie Corp Rinata", rinata.getNome());
        assertEquals("N", rinata.getDeleted()); // Deve essere attiva
    }

    @Test
    void testFiltriEListe() throws Exception {
        // Inseriamo due aziende con caratteristiche diverse
        Azienda a1 = new Azienda();
        a1.setP_Iva("101"); a1.setCatMerce("IT"); a1.setTipologia("Prospect"); a1.setAssigned(TEST_OWNER); a1.setDeleted("N");
        a1.setNome("A1"); a1.setForma("S"); a1.setIndirizzo("I"); // dummy

        Azienda a2 = new Azienda();
        a2.setP_Iva("102"); a2.setCatMerce("Food"); a2.setTipologia("Client"); a2.setAssigned(TEST_OWNER); a2.setDeleted("N");
        a2.setNome("A2"); a2.setForma("S"); a2.setIndirizzo("I"); // dummy

        aziendaDAO.create(a1);
        aziendaDAO.create(a2);
        conn.commit();

        // Test getDistinctCat
        List<String> categorie = aziendaDAO.getDistinctCat();
        assertTrue(categorie.contains("IT"));
        assertTrue(categorie.contains("Food"));

        // Test getClientiFilter (es. filtrare per Categoria = IT)
        List<Azienda> filtrate = aziendaDAO.getClientiFilter("Cat_merce", "IT");
        assertEquals(1, filtrate.size());
        assertEquals("101", filtrate.get(0).getP_Iva());
    }

    @Test
    void testDuplicatedException() throws Exception {
        Azienda a = new Azienda();
        a.setP_Iva("555"); a.setAssigned(TEST_OWNER); a.setDeleted("N");
        a.setNome("Originale"); a.setForma("S"); a.setIndirizzo("I"); a.setTipologia("T");

        aziendaDAO.create(a);
        conn.commit();

        // Tentativo di duplicato
        assertThrows(DuplicatedObjectException.class, () -> {
            aziendaDAO.create(a);
        });
    }

    @Test
    void testSelectOwner_FiltroUtente() throws Exception {
        // 1. Creiamo un secondo utente "intruso"
        utenteDAO.create("other.user", "pass", "admin", "CFOTHER");
        conn.commit();

        // 2. Creiamo due aziende: una del nostro TEST_OWNER, una dell'altro
        createAziendaDummy("111", "Azienda Mia", TEST_OWNER);
        createAziendaDummy("222", "Azienda Altri", "other.user");
        conn.commit();

        // 3. Testiamo selectOwner
        List<Azienda> mieAziende = aziendaDAO.selectOwner(TEST_OWNER);

        // 4. Verifica
        assertEquals(1, mieAziende.size());
        assertEquals("111", mieAziende.get(0).getP_Iva());
    }

    @Test
    void testSelectOwnerWReferente_Join() throws Exception {
        // Questo test verifica la JOIN con la tabella CLIENTE

        // 1. Creiamo un'azienda
        String pIva = "333";
        createAziendaDummy(pIva, "Azienda Ref", TEST_OWNER);
        conn.commit();

        // 2. Creiamo un CLIENTE collegato a questa azienda
        // (Il metodo selectOwnerWReferente cerca il referente nella tabella Cliente)
        insertClienteRapido("CFCLIENTE1", "Mario", "Rossi", pIva);
        conn.commit();

        // 3. Chiamata al metodo con JOIN
        List<Azienda> risultati = aziendaDAO.selectOwnerWReferente(TEST_OWNER);

        // 4. Verifica
        assertNotNull(risultati);
        assertEquals(1, risultati.size());

        Azienda a = risultati.get(0);
        assertEquals("Azienda Ref", a.getNome());
        // Verifichiamo che abbia caricato i dati del referente (JOIN riuscita)
        assertEquals("Mario", a.getNomeRef());
        assertEquals("Rossi", a.getCognomeRef());
    }

    @Test
    void testDistinctValues() throws Exception {
        // Inseriamo dati vari
        Azienda a1 = new Azienda();
        a1.setP_Iva("A1"); a1.setTipologia("TIPO_A"); a1.setAssigned(TEST_OWNER);
        a1.setNome("N1"); a1.setDeleted("N"); // campi minimi
        // dummy per evitare null pointer se il DB è strict
        a1.setForma("S"); a1.setIndirizzo("I"); a1.setEmail("E"); a1.setTelefono("T"); a1.setCatMerce("C");

        Azienda a2 = new Azienda();
        a2.setP_Iva("A2"); a2.setTipologia("TIPO_B"); a2.setAssigned(TEST_OWNER);
        a2.setNome("N2"); a2.setDeleted("N");
        a2.setForma("S"); a2.setIndirizzo("I"); a2.setEmail("E"); a2.setTelefono("T"); a2.setCatMerce("C");

        // A3 è uguale ad A1 per tipologia (non deve duplicare nel distinct)
        Azienda a3 = new Azienda();
        a3.setP_Iva("A3"); a3.setTipologia("TIPO_A"); a3.setAssigned(TEST_OWNER);
        a3.setNome("N3"); a3.setDeleted("N");
        a3.setForma("S"); a3.setIndirizzo("I"); a3.setEmail("E"); a3.setTelefono("T"); a3.setCatMerce("C");

        aziendaDAO.create(a1);
        aziendaDAO.create(a2);
        aziendaDAO.create(a3);
        conn.commit();

        // Test getDistinctTipologia
        List<String> tipologie = aziendaDAO.getDistinctTipologia();
        assertEquals(2, tipologie.size()); // TIPO_A e TIPO_B
        assertTrue(tipologie.contains("TIPO_A"));
        assertTrue(tipologie.contains("TIPO_B"));

        // Test getDistinctOwner
        List<String> owners = aziendaDAO.getDistinctOwner();
        assertEquals(1, owners.size()); // Solo TEST_OWNER
        assertTrue(owners.contains(TEST_OWNER));
    }

    @Test
    void testGetClientiFilter_Libera() throws Exception {
        // Test per il ramo 'else' (filtro "Libera") che carica tutto
        createAziendaDummy("100", "Az 1", TEST_OWNER);
        createAziendaDummy("200", "Az 2", TEST_OWNER);
        conn.commit();

        List<Azienda> tutte = aziendaDAO.getClientiFilter("Libera", "QualsiasiCosa");
        assertEquals(2, tutte.size());
    }

    // Helper privato per creare aziende velocemente nei test qui sopra
    private void createAziendaDummy(String piva, String nome, String owner) throws Exception {
        Azienda a = new Azienda();
        a.setP_Iva(piva);
        a.setNome(nome);
        a.setAssigned(owner);
        a.setDeleted("N");
        // Campi dummy obbligatori
        a.setForma("SRL"); a.setIndirizzo("Via Test"); a.setEmail("x@x.it");
        a.setTelefono("000"); a.setCatMerce("Gen"); a.setTipologia("T");
        aziendaDAO.create(a);
    }

    // Helper per inserire un cliente rapidamente (serve per testSelectOwnerWReferente)
    private void insertClienteRapido(String cf, String nome, String cognome, String pIvaAzienda) throws SQLException {
        String sql = "INSERT INTO cliente (CF, Nome, Cognome, Telefono, Data_Nascita, Email, P_Iva_Azienda, Deleted) " +
                "VALUES (?, ?, ?, '123', '2000-01-01', 'mail@test.it', ?, 'N')";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cf);
            ps.setString(2, nome);
            ps.setString(3, cognome);
            ps.setString(4, pIvaAzienda);
            ps.executeUpdate();
        }
    }
}
