package it.kk_crm.model.mo;

import net.jqwik.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Property
    void testClienteGetterSetter(
            @ForAll String cf,
            @ForAll String nome,
            @ForAll String cognome,
            @ForAll String telefono,
            @ForAll String dataNascita,
            @ForAll String email,
            @ForAll String pIvaAzienda,
            @ForAll String deleted
    ) {
        Cliente cliente = new Cliente();

        cliente.setCF(cf);
        cliente.setNome(nome);
        cliente.setCognome(cognome);
        cliente.setTelefono(telefono);
        cliente.setDataNascita(dataNascita);
        cliente.setEmail(email);
        cliente.setPiva(pIvaAzienda);
        cliente.setDeleted(deleted);

        assertEquals(cf, cliente.getCF());
        assertEquals(nome, cliente.getNome());
        assertEquals(cognome, cliente.getCognome());
        assertEquals(telefono, cliente.getTelefono());
        assertEquals(dataNascita, cliente.getDataNascita());
        assertEquals(email, cliente.getEmail());
        assertEquals(pIvaAzienda, cliente.getPiva());
        assertEquals(deleted, cliente.getDeleted());
    }
}
