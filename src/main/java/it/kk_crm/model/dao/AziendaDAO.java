package it.kk_crm.model.dao;

import it.kk_crm.model.dao.exception.DuplicatedObjectException;
import it.kk_crm.model.mo.Azienda;

import java.util.List;

public interface AziendaDAO {

    public void create(Azienda azienda) throws DuplicatedObjectException;

    public void update(Azienda azienda);

    public void delete(String piva);

    public List<Azienda> selectOwner(String owner);

    public Azienda getAzienda(String piva);

    public List<String> getDistinctCat();
    public List<String> getDistinctTipologia();
    public List<String> getDistinctOwner();

    public List<Azienda> getClientiFilter(String tipo, String filtro);

    public List<Azienda> selectOwnerWReferente(String owner);

}
