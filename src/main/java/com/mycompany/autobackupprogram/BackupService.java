package com.mycompany.autobackupprogram;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BackupService {
    private Timer timer;
    private final JSONAutoBackup json = new JSONAutoBackup();
    private static BackupService instance;
    private static TrayIcon trayIcon = null;

    private BackupService() {}

    public static synchronized BackupService getInstance() {
        if (instance == null) {
            instance = new BackupService();
        }
        return instance;
    }

    public void startService() throws IOException {
        if (timer == null) {
            timer = new Timer();
            long interval = json.ReadCheckForBackupTimeInterval(
                    ConfigKey.CONFIG_FILE_STRING.getValue(), ConfigKey.RES_DIRECTORY_STRING.getValue()) * 60 * 1000;
            timer.schedule(new BackupTask(), 0, interval);

            if (trayIcon == null) {
                createHiddenIcon();
            }
        }
    }

    public void stopService() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (trayIcon != null) {
            SystemTray.getSystemTray().remove(trayIcon);
            trayIcon = null;
        }
    }

    private void createHiddenIcon() {
        if (!SystemTray.isSupported()) {
            Logger.logMessage("System tray is not supported!");
            return;
        }

        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource(ConfigKey.LOGO_IMG.getValue()));
        SystemTray tray = SystemTray.getSystemTray();
        PopupMenu popup = new PopupMenu();

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener((ActionEvent e) -> {
            stopService();
            System.exit(0);
        });
        popup.add(exitItem);

        trayIcon = new TrayIcon(image, "Backup Service", popup);
        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            Logger.logMessage("TrayIcon could not be added: " + e.getMessage());
        }

        trayIcon.addActionListener((ActionEvent e) -> {
            javax.swing.SwingUtilities.invokeLater(() -> {
                BackupManagerGUI.getInstance().showWindow();
            });
        });
    }
    
    class BackupTask extends TimerTask {
        @Override
        public void run() {
            Logger.logMessage("Checking for automatic backup...");
            try {
                List<Backup> backups = json.ReadBackupListFromJSON(ConfigKey.BACKUP_FILE_STRING.getValue(), ConfigKey.RES_DIRECTORY_STRING.getValue());
                List<Backup> needsBackup = getBackupsToDo(backups);
                if (needsBackup != null && !needsBackup.isEmpty()) {
                    Logger.logMessage("Start backup process.");
                    openMainGUI(needsBackup);
                } else {
                    Logger.logMessage("No backup needed at this time.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private List<Backup> getBackupsToDo(List<Backup> backups) {
            List<Backup> backupsToDo = new ArrayList<>();
            for (Backup backup : backups) {
                if (backup.getNextDateBackup() != null && backup.getNextDateBackup().isBefore(LocalDateTime.now())) {
                    backupsToDo.add(backup);
                }
            }
            return backupsToDo;
        }

        private void openMainGUI(List<Backup> backups) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                BackupManagerGUI gui = BackupManagerGUI.getInstance();
                for (Backup backup : backups) {
                    BackupManagerGUI.currentBackup = backup;
                    gui.SingleBackup(backup);
                }
                gui.showWindow();
            });
        }
    }
}