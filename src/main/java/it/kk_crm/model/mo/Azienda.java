package it.kk_crm.model.mo;

public class Azienda {
    private String p_iva;
    private String nome;
    private String forma;
    private String indirizzo;
    private String email;
    private String telefono;
    private String cat_merce;
    private String assigned;
    private String tipologia;
    private String deleted;
    private String cognomeRef; //cognome referente
    private String nomeRef; //nome referente

    public Azienda() {
        p_iva = "";
        nome = "";
        forma = "";
        indirizzo = "";
        email = "";
        telefono = "";
        cat_merce = "";
        assigned = "";
        tipologia = "";
        deleted = "";
        cognomeRef = "";
        nomeRef = "";
    }


    public void setP_Iva(String p_iva) {
        this.p_iva = p_iva;
    }

    public String getP_Iva() {
        return p_iva;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public String getForma() {
        return forma;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setCatMerce(String cat) {
        this.cat_merce = cat;
    }

    public String getCatMerce() {
        return cat_merce;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDeleted() {
        return deleted;
    }

    public String getCognomeRef( ) {return cognomeRef;}

    public void setCognomeRef(String cognome) {this.cognomeRef = cognome; }

    public String getNomeRef() {return nomeRef; }

    public void setNomeRef(String nome) {this.nomeRef = nome;}

}
