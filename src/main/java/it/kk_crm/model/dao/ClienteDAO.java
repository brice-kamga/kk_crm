package it.kk_crm.model.dao;

import it.kk_crm.model.dao.exception.DuplicatedObjectException;
import it.kk_crm.model.mo.Cliente;

import java.util.List;

public interface ClienteDAO {

    public List<Cliente> getClienti();

    public List<Cliente> getClientiOwner(String user);

    public Cliente getClienteWAzienda(String piva);

    public void create(Cliente cliente) throws DuplicatedObjectException;
    public void delete(String cf);
    public void update(Cliente cliente);
}
