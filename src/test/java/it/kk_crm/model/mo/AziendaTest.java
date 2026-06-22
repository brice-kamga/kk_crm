package it.kk_crm.model.mo;

import net.jqwik.api.*;
import static org.junit.jupiter.api.Assertions.*;

class AziendaTest {

    @Property
    void testAziendaGetterSetter(
            @ForAll String pIva,
            @ForAll String nome,
            @ForAll String forma,
            @ForAll String indirizzo,
            @ForAll String email,
            @ForAll String telefono,
            @ForAll String catMerce,
            @ForAll String assigned,
            @ForAll String tipologia,
            @ForAll String deleted,
            @ForAll String cognomeRef,
            @ForAll String nomeRef
    ) {
        // 1. Arrange
        Azienda azienda = new Azienda();

        // 2. Act
        azienda.setP_Iva(pIva);
        azienda.setNome(nome);
        azienda.setForma(forma);
        azienda.setIndirizzo(indirizzo);
        azienda.setEmail(email);
        azienda.setTelefono(telefono);
        azienda.setCatMerce(catMerce);
        azienda.setAssigned(assigned);
        azienda.setTipologia(tipologia);
        azienda.setDeleted(deleted);
        azienda.setCognomeRef(cognomeRef);
        azienda.setNomeRef(nomeRef);

        // 3. Assert - Verifichiamo che i valori tornino indietro intatti
        assertEquals(pIva, azienda.getP_Iva());
        assertEquals(nome, azienda.getNome());
        assertEquals(forma, azienda.getForma());
        assertEquals(indirizzo, azienda.getIndirizzo());
        assertEquals(email, azienda.getEmail());
        assertEquals(telefono, azienda.getTelefono());
        assertEquals(catMerce, azienda.getCatMerce());
        assertEquals(assigned, azienda.getAssigned());
        assertEquals(tipologia, azienda.getTipologia());
        assertEquals(deleted, azienda.getDeleted());
        assertEquals(cognomeRef, azienda.getCognomeRef());
        assertEquals(nomeRef, azienda.getNomeRef());
    }
}
