package com.matheus.restore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe responsável por criar a interface gráfica para restaurar o backup.
 */
public class RestoreUI {
    private RestoreService restoreService; // Serviço de restore

    // Construtor que recebe o serviço de restore
    public RestoreUI(RestoreService restoreService) {
        this.restoreService = restoreService;
        createUI(); // Cria a interface gráfica
    }

    /**
     * Método que cria a interface gráfica usando Swing.
     */
    private void createUI() {
        JFrame frame = new JFrame("Restore Backup"); // Janela principal

        // Campo de texto e label para o nome do banco de dados
        JLabel dbNameLabel = new JLabel("Database Name:");
        dbNameLabel.setBounds(10, 10, 120, 25);
        frame.add(dbNameLabel);

        JTextField dbNameField = new JTextField();
        dbNameField.setBounds(140, 10, 160, 25);
        frame.add(dbNameField);

        // Campo de texto e label para o caminho do arquivo de backup
        JLabel backupPathLabel = new JLabel("Backup Path:");
        backupPathLabel.setBounds(10, 40, 120, 25);
        frame.add(backupPathLabel);

        JTextField backupPathField = new JTextField();
        backupPathField.setBounds(140, 40, 160, 25);
        frame.add(backupPathField);

        // Botão de restore
        JButton restoreButton = new JButton("Restore Backup");
        restoreButton.setBounds(140, 70, 160, 25);
        frame.add(restoreButton);

        // Ação do botão de restore
        restoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dbName = dbNameField.getText();
                String backupPath = backupPathField.getText();
                // Validação básica dos campos
                if (dbName.isEmpty() || backupPath.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        restoreService.restoreBackup(backupPath, dbName); // Chama o método de restore
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Configurações finais da janela
        frame.setSize(350, 150);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true); // Torna a janela visível
    }
}