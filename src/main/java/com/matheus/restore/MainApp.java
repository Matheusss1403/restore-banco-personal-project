package com.matheus.restore;

public class MainApp {
    public static void main(String[] args) {
        // Carrega as configurações do JSON
        ConfigManager configManager = new ConfigManager();
        Config config = configManager.getConfig();

        // Inicializa o gerenciador de banco de dados e serviço de restore
        DatabaseManager dbManager = new DatabaseManager(config);
        RestoreService restoreService = new RestoreService(dbManager);

        // Inicia a interface gráfica
        new RestoreUI(restoreService);
    }
}
