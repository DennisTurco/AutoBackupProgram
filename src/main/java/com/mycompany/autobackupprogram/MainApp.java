package com.mycompany.autobackupprogram;

import java.io.IOException;

public class MainApp {

    private static final String CONFIG = "src/main/resources/res/config.json";
       
    public static void main(String[] args) {
        Logger.logMessage("Application started");
        
        ConfigKey.loadFromJson(CONFIG);

        BackupService service = BackupService.getInstance();
        try {
            service.startService();
        } catch (IOException ex) {
            Logger.logMessage(ex.getMessage());
            ex.printStackTrace();
        }
        Logger.logMessage("Backup service started in the background.");

        if (args.length == 0 || !args[0].equalsIgnoreCase("background")) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                BackupManagerGUI.getInstance().showWindow();
            });
        }
    }
}
