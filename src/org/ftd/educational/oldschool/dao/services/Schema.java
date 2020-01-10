package org.ftd.educational.oldschool.dao.services;

/**
 *
 * @author Fabio Tavares Dippold
 * @version 2017-03-08 - 1.0.0
 *
 */
public enum Schema {
    DATA_BASE1("com.mysql.jdbc.Driver","jdbc:mysql://localhost/oldschool", "root", "lazaro"),
    DATA_BASE2("com.mysql.jdbc.Driver","jdbc:mysql://localhost/oldschool", "root", "lazaro");
    
    private final String driver;
    private final String url;
    private final String usuario;
    private final String senha;

    Schema(String driver, String url, String usuario, String senha) {
        this.driver = driver;
        this.url = url;
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getDriver() {
        return driver;
    }
    
    public String getSenha() {
        return senha;
    }

    public String getUrl() {
        return url;
    }

    public String getUsuario() {
        return usuario;
    }
    
}
