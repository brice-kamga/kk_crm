package it.kk_crm.model.mo;

import net.jqwik.api.*;
import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    @Property
    void testNoteGetterSetter(
            @ForAll Integer id,
            @ForAll String testoNota,
            @ForAll String data,
            @ForAll String utente,
            @ForAll String clienteCf
    ) {
        Note nota = new Note();

        // Setter
        nota.setId(id);
        nota.setNota(testoNota);
        nota.setData(data);
        nota.setUtente(utente);
        nota.setCliente_cf(clienteCf);

        // Assert Getter
        assertEquals(id, nota.getId());
        assertEquals(testoNota, nota.getNota());
        assertEquals(data, nota.getData());
        assertEquals(utente, nota.getUtente());
        assertEquals(clienteCf, nota.getCliente_cf());
    }
}
