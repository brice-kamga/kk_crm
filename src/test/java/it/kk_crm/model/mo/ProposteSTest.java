package it.kk_crm.model.mo;

import net.jqwik.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ProposteSTest {

    @Property
    void testProposteSGetterSetter(
            @ForAll Integer id,
            @ForAll String tipo,
            @ForAll String piva,
            @ForAll String nome,
            @ForAll Integer codiceServizio,
            @ForAll String nomeServizio,
            @ForAll String deleted
    ) {
        ProposteS p = new ProposteS();

        // Setter
        p.setId(id);
        p.setTipo(tipo);
        p.setPIva(piva);
        p.setNome(nome);
        p.setCodice_servizio(codiceServizio);
        p.setNome_servizio(nomeServizio);
        p.setDeleted(deleted);

        // Assert Getter
        assertEquals(id, p.getId());
        assertEquals(tipo, p.getTipo());
        assertEquals(piva, p.getPIva());
        assertEquals(nome, p.getNome());
        assertEquals(codiceServizio, p.getCodice_servizio());
        assertEquals(nomeServizio, p.getNome_servizio());
        assertEquals(deleted, p.getDeleted());
    }
}
