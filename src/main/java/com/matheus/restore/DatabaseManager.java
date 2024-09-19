package com.matheus.restore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por gerenciar as conexões com o banco de dados.
 */
public class DatabaseManager {
    private final Config config; // Objeto Config contendo as configurações

    // Construtor que recebe as configurações de banco de dados
    public DatabaseManager(Config config) {
        this.config = config;
    }

    /**
     * Método que cria e retorna uma conexão com um banco de dados específico.
     * @param dbName Nome do banco de dados para se conectar.
     * @return Connection Objeto de conexão com o banco de dados.
     * @throws SQLException Em caso de erro na conexão.
     */
    public Connection getConnection(String dbName) throws SQLException {
        // Monta a URL para conexão com o banco usando o IP e o nome do banco de dados
        String url = "jdbc:postgresql://" + config.dbIp + ":5432/" + dbName;
        // Retorna a conexão com o banco de dados usando nome de usuário e senha fornecidos no config
        return DriverManager.getConnection(url, config.username, config.password);
    }

    public Config getConfig(){
        return config;
    }
}