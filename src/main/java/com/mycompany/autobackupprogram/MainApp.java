package com.mycompany.autobackupprogram;

import java.io.IOException;

public class MainApp {
    private static final String CONFIG = "src/main/resources/res/config.json";
    
    public static void main(String[] args) {
        ConfigKey.loadFromJson(CONFIG);
        
        Logger.logMessage("Application started", Logger.LogLevel.INFO);

        BackupService service = new BackupService();
        try {
            service.startService();
        } catch (IOException ex) {
            Logger.logMessage("An error occurred", Logger.LogLevel.ERROR, ex);
            ex.printStackTrace();
        }
        
        Logger.logMessage("Backup service started in the background", Logger.LogLevel.INFO);

        if (args.length == 0 || !args[0].equalsIgnoreCase("background")) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                BackupManagerGUI gui = new BackupManagerGUI(); // Istanza normale di GUI
                gui.showWindow();
            });
        }
    }
}
