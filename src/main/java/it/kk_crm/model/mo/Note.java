package it.kk_crm.model.mo;

public class Note {
    private Integer id;
    private String nota;
    private String data;
    private String utente;
    private String cliente_cf;

    public Note() {
        id = null;
        nota = "";
        data = "";
        utente = "";
        cliente_cf = "";
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getNota() {
        return nota;
    }

    public void setId(Integer id) { this.id = id; }

    public Integer getId() {
        return id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public String getUtente() {
        return utente;
    }

    public void setCliente_cf(String cf) {
        this.cliente_cf = cf;
    }

    public String getCliente_cf() {
        return cliente_cf;
    }
}

