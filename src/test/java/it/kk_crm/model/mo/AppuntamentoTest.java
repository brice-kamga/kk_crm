package it.kk_crm.model.mo;

import net.jqwik.api.*;
import static org.junit.jupiter.api.Assertions.*;

class AppuntamentoTest {

    @Property
    void testAppuntamentoGetterSetter(
            @ForAll Integer codice,
            @ForAll String data,
            @ForAll String note,
            @ForAll String cf,
            @ForAll String deleted,
            @ForAll String cognome,
            @ForAll String nome
    ) {
        Appuntamento appuntamento = new Appuntamento();

        appuntamento.setCodice(codice);
        appuntamento.setData(data);
        appuntamento.setNote(note);
        appuntamento.setCF(cf);
        appuntamento.setDeleted(deleted);
        appuntamento.setCognome(cognome);
        appuntamento.setNome(nome);

        assertEquals(codice, appuntamento.getCodice());
        assertEquals(data, appuntamento.getData());
        assertEquals(note, appuntamento.getNote());
        assertEquals(cf, appuntamento.getCF());
        assertEquals(deleted, appuntamento.getDeleted());
        assertEquals(cognome, appuntamento.getCognome());
        assertEquals(nome, appuntamento.getNome());
    }
}
