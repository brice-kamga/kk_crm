package it.kk_crm.model.dao.CookieImpl;

import it.kk_crm.model.dao.UtenteDAO;
import it.kk_crm.model.mo.Utente;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


public class UtenteDAOCookieImpl implements UtenteDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public UtenteDAOCookieImpl(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public Utente create(
            String username,
            String password,
            String tipo,
            String CF_Utente) {

        Utente loggedUser = new Utente();
        loggedUser.setUsername(username);
        loggedUser.setTipo(tipo);

        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

        return loggedUser;

    }

    @Override
    public void update(Utente loggedUser) {

        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public void delete(Utente loggedUser) {

        Cookie cookie;
        cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    @Override
    public Utente findLoggedUser() {

        Cookie[] cookies = request.getCookies();
        Utente loggedUser = null;

        if (cookies != null) {
            for (int i = 0; i < cookies.length && loggedUser == null; i++) {
                if (cookies[i].getName().equals("loggedUser")) {
                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }

        return loggedUser;

    }


    @Override
    public Utente findByUsername(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private String encode(Utente loggedUser) {
        // CORREZIONE: Salviamo sia Username che Tipo separati da un cancelletto '#'
        String encodedLoggedUser;
        encodedLoggedUser = loggedUser.getUsername() + "#" + loggedUser.getTipo();
        return encodedLoggedUser;
    }

    private Utente decode(String encodedLoggedUser) {
        Utente loggedUser = new Utente();

        // Separiamo la stringa in base al cancelletto
        String[] values = encodedLoggedUser.split("#");

        // Recuperiamo lo Username
        loggedUser.setUsername(values[0]);

        // CORREZIONE: Recuperiamo anche il Tipo se esiste
        if (values.length > 1) {
            loggedUser.setTipo(values[1]);
        }

        return loggedUser;
    }

    @Override
    public List<Utente> loadAllUtenti() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public List<Utente> loadAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}


