package it.kk_crm.model.mo;

import net.jqwik.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ServiziConsulenzaTest {

    @Property
    void testServiziConsulenzaGetterSetter(
            @ForAll Integer id,
            @ForAll String tipoServizio,
            @ForAll String deleted
    ) {
        ServiziConsulenza s = new ServiziConsulenza();

        // Setter
        s.setId(id);
        s.setTipo_servizio(tipoServizio);
        s.setDeleted(deleted);

        // Assert Getter
        assertEquals(id, s.getId());
        assertEquals(tipoServizio, s.getTipo_servizio());
        assertEquals(deleted, s.getDeleted());
    }
}
