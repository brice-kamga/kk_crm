package it.kk_crm.controller;

import it.kk_crm.model.dao.*;
import it.kk_crm.model.mo.ServiziConsulenza;
import it.kk_crm.model.mo.Utente;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Test
    void testServizi_CaricamentoLista() {
        // 1. ARRANGE
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Simuliamo l'utente loggato (Admin)
        Utente adminUser = new Utente();
        adminUser.setUsername("admin");
        adminUser.setTipo("admin");

        // Simuliamo una lista di servizi che il DB dovrebbe restituire
        List<ServiziConsulenza> listaFinta = new ArrayList<>();
        ServiziConsulenza s1 = new ServiziConsulenza();
        s1.setTipo_servizio("Marketing");
        listaFinta.add(s1);

        // Mock dei DAO
        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        ServiziConsulenzaDAO serviziDAOMock = mock(ServiziConsulenzaDAO.class);

        // --- AGGIUNTA MOCK MANCANTI PER DASHBOARD ---
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class);

        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);
        // --------------------------------------------

        // Colleghiamo i DAO alla factory
        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getServiziConsulenzaDAO()).thenReturn(serviziDAOMock);

        // Comportamento dei DAO
        when(utenteDAOMock.findLoggedUser()).thenReturn(adminUser); // L'utente è loggato
        when(serviziDAOMock.loadAllServizi()).thenReturn(listaFinta); // Il DB restituisce la lista

        // 2. ACT & ASSERT (dentro il try-with-resources per i statici)
        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any()))
                    .thenReturn(daoFactoryMock);

            // Chiamata al metodo da testare
            AdminController.servizi(request, response);

            // Verifiche
            // 1. Ha caricato la lista nella request?
            verify(request).setAttribute(eq("servizi"), eq(listaFinta));
            // 2. Ha mandato alla pagina giusta?
            verify(request).setAttribute(eq("viewUrl"), eq("vistaAdmin/servizi"));
            // 3. L'utente risulta loggato?
            verify(request).setAttribute(eq("loggedOn"), eq(true));
        }
    }

    @Test
    void testDeleteServizio_CancellazioneOk() {
        // 1. ARRANGE
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Simuliamo che l'utente clicchi "Elimina" sul servizio con ID 5
        when(request.getParameter("id")).thenReturn("5");

        // Mock dei DAO necessari
        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        ServiziConsulenzaDAO serviziDAOMock = mock(ServiziConsulenzaDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class); // Serve anche questo

        // --- AGGIUNTA MOCK MANCANTI PER DASHBOARD ---
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class);

        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);
        // --------------------------------------------

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getServiziConsulenzaDAO()).thenReturn(serviziDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);

        // Simuliamo utente loggato
        Utente adminUser = new Utente();
        adminUser.setUsername("admin");
        adminUser.setTipo("admin");
        when(utenteDAOMock.findLoggedUser()).thenReturn(adminUser);

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any()))
                    .thenReturn(daoFactoryMock);

            // 2. ACT
            AdminController.deleteServizio(request, response);

            // 3. ASSERT
            // Verifichiamo che il metodo deleteServizio del DAO sia stato chiamato con l'ID 5
            verify(serviziDAOMock).deleteServizio(5);

            // Verifichiamo che abbia provato a cancellare anche le proposte associate (come da logica del controller)
            verify(proposteSDAOMock).deleteWServizio(5);

            // Verifichiamo il messaggio di successo
            verify(request).setAttribute(eq("applicationMessage"), eq("Cancellazione completata"));

            // Verifichiamo che alla fine ricarichi la lista (chiama servizi())
            // Nota: Poiché 'servizi()' è un metodo statico nella stessa classe,
            // Mockito non può impedire che venga eseguito, quindi verifichiamo
            // che alla fine del giro venga richiamato loadAllServizi
            verify(serviziDAOMock).loadAllServizi();
        }
    }

    @Test
    void testDeleteUtente_Successo() {
        // 1. ARRANGE
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Simuliamo il parametro che arriva dal browser (username da cancellare)
        String usernameDaCancellare = "utente.cattivo";
        when(request.getParameter("username")).thenReturn(usernameDaCancellare);

        // Mock dei DAO
        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);

        // --- AGGIUNTA MOCK MANCANTI PER DASHBOARD ---
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class);

        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);
        // --------------------------------------------

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);

        // Simuliamo l'utente loggato (Admin)
        Utente adminUser = new Utente();
        adminUser.setUsername("admin");
        adminUser.setTipo("admin");
        when(utenteDAOMock.findLoggedUser()).thenReturn(adminUser);

        // Importante: deleteUtente chiama utenti() alla fine, che chiama loadAll().
        // Dobbiamo evitare che loadAll() restituisca null e faccia crashare il test.
        when(utenteDAOMock.loadAll()).thenReturn(new ArrayList<>());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any()))
                    .thenReturn(daoFactoryMock);

            // 2. ACT
            AdminController.deleteUtente(request, response);

            // 3. ASSERT
            // Qui usiamo un "Captor" per catturare l'oggetto Utente che è stato passato al metodo delete()
            ArgumentCaptor<Utente> utenteCaptor = ArgumentCaptor.forClass(Utente.class);

            // Verifichiamo che delete sia stato chiamato e catturiamo l'argomento
            verify(utenteDAOMock).delete(utenteCaptor.capture());

            // Controlliamo che l'utente passato al delete abbia lo username giusto
            assertEquals(usernameDaCancellare, utenteCaptor.getValue().getUsername());

            // Verifiche standard
            verify(request).setAttribute(eq("applicationMessage"), eq("Cancellazione completata"));

            // Verifichiamo che ricarichi la lista
            verify(request).setAttribute(eq("viewUrl"), eq("vistaAdmin/utenti"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testInsertUtente_Successo() {
        // 1. ARRANGE
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Simuliamo i dati inviati dal form
        when(request.getParameter("username")).thenReturn("nuovo.user");
        when(request.getParameter("password")).thenReturn("pass123");
        when(request.getParameter("tipo")).thenReturn("registrato");
        when(request.getParameter("cf")).thenReturn("CF123456");

        // Mock dei DAO
        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);

        // --- AGGIUNTA MOCK MANCANTI PER DASHBOARD ---
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class);

        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);
        // --------------------------------------------

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);

        // Simuliamo l'utente loggato (Admin)
        Utente adminUser = new Utente();
        adminUser.setUsername("admin");
        adminUser.setTipo("admin");
        when(utenteDAOMock.findLoggedUser()).thenReturn(adminUser);

        // Importante: insertUtente alla fine chiama utenti(), che chiama loadAll().
        // Dobbiamo dire al mock cosa restituire quando verrà richiamata la lista.
        when(utenteDAOMock.loadAll()).thenReturn(new ArrayList<>());

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any()))
                    .thenReturn(daoFactoryMock);

            // 2. ACT
            AdminController.insertUtente(request, response);

            // 3. ASSERT
            // Verifichiamo che il metodo create sia stato chiamato con i parametri giusti
            verify(utenteDAOMock).create("nuovo.user", "pass123", "registrato", "CF123456");

            // Verifichiamo il messaggio di successo
            verify(request).setAttribute(eq("applicationMessage"), eq("Inserimento completato"));

            // Verifichiamo che alla fine ricarichi la lista utenti
            verify(request).setAttribute(eq("viewUrl"), eq("vistaAdmin/utenti"));
        } catch (Exception e) {
            fail("Eccezione non prevista: " + e.getMessage());
        }
    }

    @Test
    void testUtenti_CaricamentoLista() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Simuliamo una lista di utenti
        List<Utente> listaUtenti = new ArrayList<>();
        listaUtenti.add(new Utente());

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);

        // --- AGGIUNTA MOCK MANCANTI PER DASHBOARD ---
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class);

        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);
        // --------------------------------------------

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        Utente adminUser = new Utente();
        adminUser.setUsername("admin");
        adminUser.setTipo("admin");
        when(utenteDAOMock.findLoggedUser()).thenReturn(adminUser);
        when(utenteDAOMock.loadAll()).thenReturn(listaUtenti); // Lista dal DB

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any()))
                    .thenReturn(daoFactoryMock);

            AdminController.utenti(request, response);

            verify(request).setAttribute(eq("utenti"), eq(listaUtenti));
            verify(request).setAttribute(eq("viewUrl"), eq("vistaAdmin/utenti"));
        }
    }

    @Test
    void testInsertServizio_Successo() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Parametri form
        when(request.getParameter("id")).thenReturn("99");
        when(request.getParameter("servizio")).thenReturn("Consulenza Legale");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        ServiziConsulenzaDAO serviziDAOMock = mock(ServiziConsulenzaDAO.class);

        // --- AGGIUNTA MOCK MANCANTI PER DASHBOARD ---
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class);

        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);
        // --------------------------------------------

        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);
        when(daoFactoryMock.getServiziConsulenzaDAO()).thenReturn(serviziDAOMock);

        Utente adminUser = new Utente();
        adminUser.setUsername("admin");
        adminUser.setTipo("admin");
        when(utenteDAOMock.findLoggedUser()).thenReturn(adminUser);
        when(serviziDAOMock.loadAllServizi()).thenReturn(new ArrayList<>()); // Per il ricaricamento finale

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any()))
                    .thenReturn(daoFactoryMock);

            AdminController.insertServizio(request, response);

            // Verifichiamo che venga passato un oggetto ServiziConsulenza al DAO
            verify(serviziDAOMock).create(any(ServiziConsulenza.class));

            verify(request).setAttribute(eq("applicationMessage"), eq("Inserimento completato"));
        } catch (Exception e) {
            fail(e);
        }
    }
}
