package it.kk_crm.model.mo;

public class Cliente {
    private String cf;
    private String nome;
    private String cognome;
    private String telefono;
    private String data_nascita;
    private String email;
    private String p_iva_azienda;
    private String deleted;

    public void setDeleted(String deleted) {this.deleted = deleted;}

    public String getDeleted() {return deleted;}

    public void setCF(String cf) {
        this.cf = cf;
    }

    public String getCF() {
        return cf;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setDataNascita(String data) {
        this.data_nascita = data;
    }

    public String getDataNascita() {
        return data_nascita;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPiva(String piva) {
        this.p_iva_azienda = piva;
    }

    public String getPiva() {
        return p_iva_azienda;
    }
}
