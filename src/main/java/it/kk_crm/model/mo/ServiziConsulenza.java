package it.kk_crm.model.mo;

public class ServiziConsulenza {
    private Integer id;
    private String tipo_servizio;
    private String deleted;

    public Integer getId(){
        return id;
    }

    public void setId(Integer id) { this.id = id; }

    public String getTipo_servizio(){
        return tipo_servizio;
    }

    public void setTipo_servizio(String tipo_servizio){
        this.tipo_servizio = tipo_servizio;
    }

    public void setDeleted(String deleted) {this.deleted = deleted; }

    public String getDeleted() {return deleted; }
}
