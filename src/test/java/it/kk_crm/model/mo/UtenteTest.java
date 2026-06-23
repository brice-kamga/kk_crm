package it.kk_crm.model.mo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UtenteTest {

    @Test
    void testCreazioneEAccessoDati() {
        // Preparazione dei dati
        Utente utente = new Utente();
        String username = "mario.rossi";
        String password = "passwordSegreta123";
        String tipo = "Admin";
        String cf = "RSSMRA80A01H501Z";
        String isDeleted = "N";

        // 2. Act (Esecuzione dei setter)
        utente.setUsername(username);
        utente.setPassword(password);
        utente.setTipo(tipo);
        utente.setCF(cf);
        utente.setDeleted(isDeleted);

        // Verifica dei getter
        assertEquals(username, utente.getUsername(), "Lo username dovrebbe corrispondere");
        assertEquals(password, utente.getPassword(), "La password dovrebbe corrispondere");
        assertEquals(tipo, utente.getTipo(), "Il tipo dovrebbe corrispondere");
        assertEquals(cf, utente.getCF(), "Il Codice Fiscale (CF) dovrebbe corrispondere");
        assertEquals(isDeleted, utente.getDeleted(), "Lo stato 'deleted' dovrebbe corrispondere");
    }

    @Test
    void testValoriNull() {
        // Verifichiamo che la classe accetti valori null senza lanciare errori (NullPointerException)
        Utente utente = new Utente();

        utente.setCF(null);
        assertNull(utente.getCF(), "Il CF dovrebbe essere null se impostato a null");
    }
}
