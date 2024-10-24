package com.mycompany.autobackupprogram;

import java.io.IOException;

public class MainApp {
    public static void main(String[] args) {
        
        BackupService service = new BackupService();
        try {
            service.startService();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Logger.logMessage("Backup service started in the background.");
            
        if (args.length == 0 || !args[0].equalsIgnoreCase("background")) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                new BackupManagerGUI().setVisible(true);
            });
        }
    }
}
