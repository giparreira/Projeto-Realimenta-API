package br.com.fiap.realimenta.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static ConnectionFactory instance;

    private final String url;
    private final String user;
    private final String password;

    private ConnectionFactory() {
        this.url = valor("DB_URL", "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL");
        this.user = valor("DB_USER", "");
        this.password = valor("DB_PASSWORD", "");
    }

    public static synchronized ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private String valor(String chave, String padrao) {
        String valor = System.getenv(chave);
        if (valor == null || valor.isBlank()) {
            return padrao;
        }
        return valor;
    }
}
