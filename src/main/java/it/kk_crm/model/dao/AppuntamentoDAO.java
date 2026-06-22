package it.kk_crm.model.dao;

import it.kk_crm.model.mo.Appuntamento;

import java.util.List;

public interface AppuntamentoDAO {

    public void create(Appuntamento appuntamento);

    public void delete(Integer codice);

    public List<Appuntamento> loadAllAppuntamenti(String owner);

    public List<Appuntamento> loadSelected(String data, String owner);

    public List<Appuntamento> loadAppuntamenti7Days(String owner);

    public void deleteC(String cf);
}
