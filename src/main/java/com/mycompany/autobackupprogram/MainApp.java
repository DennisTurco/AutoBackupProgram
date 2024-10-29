package com.mycompany.autobackupprogram;

import java.io.IOException;

public class MainApp {
    private static final String CONFIG = "src/main/resources/res/config.json";

    public static void main(String[] args) {
        ConfigKey.loadFromJson(CONFIG);
        
        boolean isBackgroundMode = args.length > 0 && args[0].equalsIgnoreCase("--background");
        
        Logger.logMessage("Application started", Logger.LogLevel.INFO);
        Logger.logMessage("Background mode: " + isBackgroundMode, Logger.LogLevel.DEBUG);
        
        if (isBackgroundMode) {
            Logger.logMessage("Backup service starting in the background", Logger.LogLevel.INFO);
            BackupService service = new BackupService();
            try {
                service.startService();
            } catch (IOException ex) {
                Logger.logMessage("An error occurred", Logger.LogLevel.ERROR, ex);
                ex.printStackTrace();
            }
        }
        else if (!isBackgroundMode) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                BackupManagerGUI gui = new BackupManagerGUI();
                gui.showWindow();
            });
        }
    }
}
