package it.kk_crm.model.mo;

public class ProposteS {
    private Integer id;
    private String tipo;
    private String piva;
    private String nome;
    private Integer codice_servizio;
    private String nome_servizio;
    private String deleted;

    public Integer getId(){
        return id;
    }

    public void setId(Integer id) {this.id = id; }

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setPIva(String piva) {
        this.piva = piva;
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

    public String getNome_servizio() {
        return nome_servizio;
    }

    public void setNome_servizio(String nomeservizio) {
        this.nome_servizio = nomeservizio;
    }

    public String getDeleted() { return deleted; }

    public void setDeleted(String deleted) {this.deleted = deleted;}

}

