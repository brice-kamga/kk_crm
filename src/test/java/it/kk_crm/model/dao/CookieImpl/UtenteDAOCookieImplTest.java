package it.kk_crm.model.dao.CookieImpl;

import it.kk_crm.model.mo.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtenteDAOCookieImplTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private UtenteDAOCookieImpl cookieDAO;

    @BeforeEach
    void setUp() {
        // 1. Mockiamo gli oggetti del server web
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        // 2. Inizializziamo il DAO iniettando i mock
        cookieDAO = new UtenteDAOCookieImpl(request, response);
    }

    @Test
    void testCreate_Login() {
        // ACT: Simuliamo il login (creazione cookie)
        Utente result = cookieDAO.create("mario", "pass", "admin", "CF123");

        // ASSERT 1: Verifichiamo l'oggetto ritornato
        assertNotNull(result);
        assertEquals("mario", result.getUsername());
        assertEquals("admin", result.getTipo());

        // ASSERT 2: Verifichiamo che il cookie sia stato aggiunto alla risposta
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());

        Cookie cookieSalvato = cookieCaptor.getValue();
        assertEquals("loggedUser", cookieSalvato.getName());
        assertEquals("mario#admin", cookieSalvato.getValue());
        assertEquals("/", cookieSalvato.getPath());
    }

    @Test
    void testUpdate() {
        Utente u = new Utente();
        u.setUsername("luigi");

        cookieDAO.update(u);

        // Verifichiamo che sovrascriva il cookie
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());

        assertEquals("luigi#null", cookieCaptor.getValue().getValue());
    }

    @Test
    void testDelete_Logout() {
        Utente u = new Utente(); // Dati irrilevanti per il delete

        cookieDAO.delete(u);

        // Verifichiamo che il cookie venga invalidato (MaxAge 0)
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());

        Cookie cookieCancellato = cookieCaptor.getValue();
        assertEquals("loggedUser", cookieCancellato.getName());
        assertEquals(0, cookieCancellato.getMaxAge(), "Il MaxAge deve essere 0 per cancellare il cookie");
        assertEquals("", cookieCancellato.getValue());
    }

    @Test
    void testFindLoggedUser_Successo() {
        // ARRANGE: Simuliamo che il browser invii il cookie "loggedUser"
        Cookie cookieLogin = new Cookie("loggedUser", "mario.rossi#admin");
        Cookie[] cookies = { new Cookie("JSESSIONID", "123"), cookieLogin };

        when(request.getCookies()).thenReturn(cookies);

        // ACT
        Utente loggedUser = cookieDAO.findLoggedUser();

        // ASSERT
        assertNotNull(loggedUser);
        assertEquals("mario.rossi", loggedUser.getUsername());
    }

    @Test
    void testFindLoggedUser_NessunCookie() {
        // ARRANGE: Il browser non invia cookie (null)
        when(request.getCookies()).thenReturn(null);

        // ACT
        Utente loggedUser = cookieDAO.findLoggedUser();

        // ASSERT
        assertNull(loggedUser);
    }

    @Test
    void testFindLoggedUser_CookieNonTrovato() {
        // ARRANGE: Ci sono cookie, ma non quello di login
        Cookie[] cookies = { new Cookie("JSESSIONID", "123") };
        when(request.getCookies()).thenReturn(cookies);

        // ACT
        Utente loggedUser = cookieDAO.findLoggedUser();

        // ASSERT
        assertNull(loggedUser);
    }

    @Test
    void testMetodiNonSupportati() {
        // Verifichiamo che lancino l'eccezione corretta (per la copertura 100%)
        assertThrows(UnsupportedOperationException.class, () -> cookieDAO.findByUsername("test"));
        assertThrows(UnsupportedOperationException.class, () -> cookieDAO.loadAll());
        assertThrows(UnsupportedOperationException.class, () -> cookieDAO.loadAllUtenti());
    }
}
