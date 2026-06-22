package it.kk_crm.model.mo;

public class Utente {

    private String username;
    private String password;
    private String tipo;
    private String cf_utente;
    private String deleted;


    public void setDeleted(String deleted) {this.deleted = deleted;}

    public String getDeleted() {return deleted;}

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setCF(String CF) {
        this.cf_utente = CF;
    }

    public String getCF() {
        return cf_utente;
    }
}

