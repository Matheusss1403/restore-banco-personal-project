package com.matheus.restore;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * Classe responsável por carregar e salvar as configurações do sistema em um arquivo JSON.
 */
public class ConfigManager {
    // Caminho do arquivo de configuração JSON
    private static final String CONFIG_PATH = "config.json";
    
    // Objeto Config que contém os dados carregados do JSON
    private Config config;

    // Construtor que carrega a configuração assim que o objeto é instanciado
    public ConfigManager() {
        loadConfig();
    }

    /**
     * Método para carregar as configurações do arquivo JSON.
     */
    public void loadConfig() {
        ObjectMapper mapper = new ObjectMapper(); // Objeto usado para manipulação de JSON
        try {
            // Lê o arquivo JSON e converte para um objeto Config
            config = mapper.readValue(new File(CONFIG_PATH), Config.class);
        } catch (IOException e) {
            e.printStackTrace(); // Imprime erros, se houver
        }
    }

    /**
     * Retorna as configurações atuais.
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Salva as configurações modificadas no arquivo JSON.
     */
    public void saveConfig(Config newConfig) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Escreve as novas configurações no arquivo JSON
            mapper.writeValue(new File(CONFIG_PATH), newConfig);
            config = newConfig; // Atualiza o objeto Config local
        } catch (IOException e) {
            e.printStackTrace(); // Imprime erros, se houver
        }
    }
}

/**
 * Classe que representa a estrutura das configurações armazenadas no arquivo JSON.
 */
class Config {
    public String dbIp; // IP do banco de dados
    public String username; // Nome de usuário do PostgreSQL
    public String password; // Senha do PostgreSQL
    public String postgresVersion; // Versão do PostgreSQL

    // Getters e Setters (opcional, depende da necessidade de acesso externo)
}