package com.mycompany.autobackupprogram;

public class MainApp {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("background")) {
            BackupService service = new BackupService();
            service.startService();
            System.out.println("Backup service started in the background.");
        } else {
            javax.swing.SwingUtilities.invokeLater(() -> {
                new BackupManagerGUI().setVisible(true);
            });
        }
    }
}
