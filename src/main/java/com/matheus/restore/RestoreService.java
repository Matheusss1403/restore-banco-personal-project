package com.matheus.restore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe responsável por realizar o restore do banco de dados e executar
 * comandos SQL.
 */
public class RestoreService {
    private final DatabaseManager dbManager; // Gerenciador de conexões com o banco

    // Construtor que recebe o DatabaseManager
    public RestoreService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Método responsável por restaurar o backup do banco de dados.
     * 
     * @param backupPath      Caminho do arquivo de backup.
     * @param newDatabaseName Nome do novo banco de dados que será criado.
     */
    public void restoreBackup(String backupPath, String newDatabaseName)
            throws SQLException, IOException, InterruptedException {
        try (Connection connection = dbManager.getConnection("postgres")) {
            // Verifica se o banco de dados já existe
            if (databaseExists(connection, newDatabaseName)) {
                // Se o banco existir, ele será excluído
                dropDatabase(connection, newDatabaseName);
            }

            // Executa o processo de restore
            executeRestore(backupPath, newDatabaseName);
        }
    }

    /**
     * Verifica se o banco de dados com o nome fornecido já existe.
     * 
     * @param connection Conexão com o banco de dados.
     * @param dbName     Nome do banco de dados a ser verificado.
     * @return true se o banco de dados existir, false caso contrário.
     */
    private boolean databaseExists(Connection connection, String dbName) throws SQLException {
        // Consulta para verificar a existência do banco
        String query = "SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'";
        try (Statement stmt = connection.createStatement()) {
            // Executa a consulta e verifica se há resultados
            return stmt.executeQuery(query).next();
        }
    }

    /**
     * Exclui o banco de dados especificado.
     * 
     * @param connection Conexão com o banco de dados.
     * @param dbName     Nome do banco de dados a ser excluído.
     */
    private void dropDatabase(Connection connection, String dbName) throws SQLException {
        // Comando SQL para deletar o banco de dados
        String command = "DROP DATABASE " + dbName;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(command); // Executa o comando
        }
    }

    /**
     * Executa o comando pg_restore para restaurar o banco de dados a partir de um
     * arquivo de backup.
     * 
     * @param backupPath      Caminho do arquivo de backup.
     * @param newDatabaseName Nome do novo banco de dados.
     */
    private void executeRestore(String backupPath, String newDatabaseName) throws IOException, InterruptedException {
        // Monta o comando pg_restore usando o usuário, nome do banco e caminho do
        // arquivo de backup
        String restoreCommand = String.format("pg_restore --username=%s --dbname=%s --verbose %s",
                dbManager.getConfig().username, newDatabaseName, backupPath);
        // Cria o processo para executar o comando no shell
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", restoreCommand);
        // Define a senha do PostgreSQL no ambiente de execução do processo
        processBuilder.environment().put("PGPASSWORD", dbManager.getConfig().password);
        Process process = processBuilder.start(); // Inicia o processo

        // Loga a saída do comando e possíveis erros
        logProcessOutput(process);
        int exitCode = process.waitFor(); // Espera o processo terminar
        if (exitCode == 0) {
            System.out.println("Backup restaurado com sucesso para o banco de dados " + newDatabaseName + ".");
        } else {
            System.err.println("Erro ao restaurar o backup. Código de saída: " + exitCode);
        }
    }

    /**
     * Lê e imprime a saída do processo (logs e erros).
     * 
     * @param process O processo executado.
     */
    private void logProcessOutput(Process process) throws IOException {
        // Lê a saída padrão do processo (stdout)
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        // Lê a saída de erros do processo (stderr)
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String line;
        // Exibe a saída padrão
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        // Exibe mensagens de erro
        while ((line = errorReader.readLine()) != null) {
            System.err.println(line);
        }
    }
}