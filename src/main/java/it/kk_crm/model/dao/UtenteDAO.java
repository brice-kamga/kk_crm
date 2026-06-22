package it.kk_crm.model.dao;

import it.kk_crm.model.dao.exception.DuplicatedObjectException;
import it.kk_crm.model.mo.Utente;

import java.util.List;

public interface UtenteDAO {

    public Utente create(
            String Username,
            String Password,
            String Tipo,
            String CF_Utente) throws DuplicatedObjectException;

    public void update(Utente utente);

    public void delete(Utente utente);

    public Utente findLoggedUser();

    public Utente findByUsername(String username);

    public List<Utente> loadAllUtenti();

    public List<Utente> loadAll();
}
