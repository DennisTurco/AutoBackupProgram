package com.mycompany.autobackupprogram;

import static com.mycompany.autobackupprogram.BackupManagerGUI.OpenExceptionMessage;
import java.io.IOException;
import java.util.Arrays;

public class MainApp {
    private static final String CONFIG = "src/main/resources/res/config.json";

    public static void main(String[] args) {
        ConfigKey.loadFromJson(CONFIG);
        Logger.configReader = new JSONConfigReader(ConfigKey.CONFIG_FILE_STRING.getValue(), ConfigKey.RES_DIRECTORY_STRING.getValue());
        
        boolean isBackgroundMode = args.length > 0 && args[0].equalsIgnoreCase("--background");
        
        if (!isBackgroundMode && args.length > 0) {
            Logger.logMessage("Argument \""+ args[0] +"\" not valid!", Logger.LogLevel.WARN);
            return;
        }
        
        Logger.logMessage("Application started", Logger.LogLevel.INFO);
        Logger.logMessage("Background mode: " + isBackgroundMode, Logger.LogLevel.DEBUG);
        
        if (isBackgroundMode) {
            Logger.logMessage("Backup service starting in the background", Logger.LogLevel.INFO);
            BackupService service = new BackupService();
            try {
                service.startService();
            } catch (IOException ex) {
                Logger.logMessage("An error occurred", Logger.LogLevel.ERROR, ex);
                OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
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
