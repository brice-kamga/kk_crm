package it.kk_crm.model.dao;

import it.kk_crm.model.dao.exception.DuplicatedObjectException;
import it.kk_crm.model.mo.ServiziConsulenza;

import java.util.List;

public interface ServiziConsulenzaDAO {

    public void create (ServiziConsulenza servizio) throws DuplicatedObjectException;

    public List<ServiziConsulenza> loadAllServizi();

    public void deleteServizio(Integer id);

}
