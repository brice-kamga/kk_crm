package it.kk_crm.model.mo;

public class Proposte {
    private Integer id;
    private String tipo;
    private String piva;
    private Integer codice_servizio;
    private String deleted;

    public Integer getId(){
        return id;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setPIva(String iva) {
        this.piva = iva;
    }

    public String getPIva() {
        return piva;
    }

    public void setCodice_servizio(Integer cod) {
        this.codice_servizio = cod;
    }

    public Integer getCodice_servizio() {
        return codice_servizio;
    }

    public String getDeleted() {return deleted;}

    public void setDeleted(String deleted) {this.deleted = deleted;}
}

