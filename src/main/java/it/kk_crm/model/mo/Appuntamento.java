package it.kk_crm.model.mo;

public class Appuntamento {
    private Integer codice;
    private String data;
    private String note;
    private String cf_c;
    private String deleted;
    private String cognome;
    private String nome;

    public void setCognome(String cognome) {this.cognome = cognome;}

    public String getCognome() {return cognome;}

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public void setCodice (Integer codice) { this.codice = codice;}
    public Integer getCodice() { return codice; }

    public String getDeleted() {return deleted; }

    public void setDeleted(String deleted) {this.deleted = deleted;}

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public String getCF() { return cf_c; }

    public void setCF(String cf) {this.cf_c = cf;}
}
