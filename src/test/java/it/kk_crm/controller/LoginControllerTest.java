package it.kk_crm.controller;

import it.kk_crm.model.dao.*;
import it.kk_crm.model.mo.Utente;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Test
    void testLogonSuccesso_UtenteAdmin() throws Exception {
        // 1. ARRANGE (PREPARAZIONE)

        // Creiamo i finti oggetti Request e Response
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Simuliamo i dati inviati dal form HTML
        when(request.getParameter("username")).thenReturn("mario.admin");
        when(request.getParameter("password")).thenReturn("pass123");

        // Creiamo un finto Utente che il database dovrebbe restituire
        Utente mockUser = new Utente();
        mockUser.setUsername("mario.admin");
        mockUser.setPassword("pass123");
        mockUser.setTipo("admin");
        mockUser.setDeleted("N");

        // --- AGGIUNTA 1: Inganniamo la request ---
        // Il controller dopo il login prova a rileggere l'utente dalla request per caricare la dashboard
        when(request.getAttribute("loggedUser")).thenReturn(mockUser);
        // -----------------------------------------

        // Creiamo i finti DAO
        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);

        // --- AGGIUNTA 2: Mocks per la Dashboard ---
        // Dopo un login riuscito, il controller prepara la vista, che necessita di questi DAO
        AppuntamentoDAO appuntamentoDAOMock = mock(AppuntamentoDAO.class);
        ProposteSDAO proposteSDAOMock = mock(ProposteSDAO.class);
        NoteDAO noteDAOMock = mock(NoteDAO.class);

        when(daoFactoryMock.getAppuntamentoDAO()).thenReturn(appuntamentoDAOMock);
        when(daoFactoryMock.getProposteSDAO()).thenReturn(proposteSDAOMock);
        when(daoFactoryMock.getNoteDAO()).thenReturn(noteDAOMock);
        // ------------------------------------------

        // Colleghiamo la Factory al DAO
        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);

        // Istruiamo il DAO su cosa rispondere (Mocking del DB)
        // 1. Quando cerca l'utente loggato (Cookie), diciamo che non c'è nessuno (null)
        when(utenteDAOMock.findLoggedUser()).thenReturn(null);
        // 2. Quando cerca per username, restituisce il nostro utente
        when(utenteDAOMock.findByUsername("mario.admin")).thenReturn(mockUser);
        // 3. Quando crea il cookie (sessionUtenteDAO.create), restituisce l'utente loggato
        when(utenteDAOMock.create(any(), any(), any(), any())).thenReturn(mockUser);

        // QUI AVVIENE LA MAGIA: Intercettiamo i metodi statici di DAOFactory
        // Usiamo try-with-resources per chiudere il mock alla fine del test
        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {

            // Quando il controller chiama DAOFactory.getDAOFactory, restituiamo il nostro mock
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any()))
                    .thenReturn(daoFactoryMock);

            // 2. ACT (AZIONE)
            // Chiamiamo il metodo vero del controller
            LoginController.logon(request, response);

            // 3. ASSERT (VERIFICA)

            // Verifichiamo che il controller abbia impostato l'attributo "loggedUser"
            verify(request, atLeastOnce()).setAttribute(eq("loggedUser"), eq(mockUser));

            // Verifichiamo che il reindirizzamento sia verso la vista Admin (perché l'utente è admin)
            verify(request).setAttribute("viewUrl", "vistaRegistrato/dashboard");

            // Verifichiamo che NON ci siano messaggi di errore
            verify(request, never()).setAttribute(eq("applicationMessage"), anyString());
        }
    }

    @Test
    void testLogonFallito_PasswordErrata() throws Exception {
        // 1. ARRANGE
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Simuliamo input sbagliati
        when(request.getParameter("username")).thenReturn("mario.admin");
        when(request.getParameter("password")).thenReturn("PASSWORD_SBAGLIATA");

        // L'utente esiste nel DB, ma ha una password diversa
        Utente realUser = new Utente();
        realUser.setUsername("mario.admin");
        realUser.setPassword("passwordGiusta");

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO utenteDAOMock = mock(UtenteDAO.class);
        when(daoFactoryMock.getUtenteDAO()).thenReturn(utenteDAOMock);

        // Mock del DB
        when(utenteDAOMock.findLoggedUser()).thenReturn(null);
        when(utenteDAOMock.findByUsername("mario.admin")).thenReturn(realUser);

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any()))
                    .thenReturn(daoFactoryMock);

            // 2. ACT
            LoginController.logon(request, response);

            // 3. ASSERT
            // Verifichiamo che sia stato impostato il messaggio di errore
            verify(request).setAttribute(eq("applicationMessage"), eq("Username e password errati!"));

            // Verifichiamo che siamo rimasti sulla pagina di login
            verify(request).setAttribute("viewUrl", "login/view");

            // Verifichiamo che loggedUser sia null o non settato come attivo
            verify(request).setAttribute("loggedUser", null);
        }
    }

    @Test
    void testLogout_Successo() {
        // 1. ARRANGE
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO sessionDAOMock = mock(UtenteDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(sessionDAOMock);

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any()))
                    .thenReturn(daoFactoryMock);

            // 2. ACT
            LoginController.logout(request, response);

            // 3. ASSERT
            // Verifichiamo che venga chiamato il delete sul DAO di sessione (cancella il cookie)
            verify(sessionDAOMock).delete(null);

            // Verifichiamo che loggedOn sia false
            verify(request).setAttribute(eq("loggedOn"), eq(false));
            verify(request).setAttribute(eq("loggedUser"), isNull());

            // Verifichiamo il ritorno alla pagina di login
            verify(request).setAttribute(eq("viewUrl"), eq("login/view"));
        }
    }

    @Test
    void testView_CaricamentoIniziale() {
        // 1. ARRANGE
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        DAOFactory daoFactoryMock = mock(DAOFactory.class);
        UtenteDAO sessionDAOMock = mock(UtenteDAO.class);

        when(daoFactoryMock.getUtenteDAO()).thenReturn(sessionDAOMock);

        // Simuliamo che NON ci sia nessun utente già loggato (caso normale)
        when(sessionDAOMock.findLoggedUser()).thenReturn(null);

        try (MockedStatic<DAOFactory> staticFactory = mockStatic(DAOFactory.class)) {
            staticFactory.when(() -> DAOFactory.getDAOFactory(anyString(), any()))
                    .thenReturn(daoFactoryMock);

            // 2. ACT
            LoginController.view(request, response);

            // 3. ASSERT
            // Deve cercare se c'è un cookie attivo
            verify(sessionDAOMock).findLoggedUser();

            // Deve impostare loggedOn a false
            verify(request).setAttribute(eq("loggedOn"), eq(false));

            // Deve mandare alla JSP di login
            verify(request).setAttribute(eq("viewUrl"), eq("login/view"));
        }
    }
}
