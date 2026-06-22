package it.kk_crm.model.mo;

import net.jqwik.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ProposteTest {

    @Property
    void testProposteGetterSetter(
            @ForAll String tipo,
            @ForAll String piva,
            @ForAll Integer codiceServizio,
            @ForAll String deleted
    ) {
        Proposte p = new Proposte();

        // Setter
        p.setTipo(tipo);
        p.setPIva(piva);
        p.setCodice_servizio(codiceServizio);
        p.setDeleted(deleted);

        // Assert Getter
        assertEquals(tipo, p.getTipo());
        assertEquals(piva, p.getPIva());
        assertEquals(codiceServizio, p.getCodice_servizio());
        assertEquals(deleted, p.getDeleted());

        // Verifica che l'ID sia null (poiché non c'è il setter)
        assertNull(p.getId());
    }
}
