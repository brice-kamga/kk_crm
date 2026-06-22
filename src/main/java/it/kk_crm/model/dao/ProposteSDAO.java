package it.kk_crm.model.dao;

import it.kk_crm.model.mo.Proposte;
import it.kk_crm.model.mo.ProposteS;

import java.util.List;

public interface ProposteSDAO {

    public void create (Proposte proposta);

    public void delete(Integer id);

    public List<ProposteS> loadAllProposte();

    public List<ProposteS> loadProposteOwner(String owner);

    public void deleteC(String piva);

    public void deleteWServizio(Integer codice);

}
