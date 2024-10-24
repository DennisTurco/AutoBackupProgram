package com.mycompany.autobackupprogram;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BackupService {
    private Timer timer;
    private final JSONAutoBackup json = new JSONAutoBackup();
    private TrayIcon trayIcon;

    public void startService() throws IOException {
        timer = new Timer();
        long interval = json.ReadCheckForBackupTimeInterval(BackupManagerGUI.CONFIG_FILE_STRING, BackupManagerGUI.RES_DIRECTORY_STRING) * 60 * 1000;
        timer.schedule(new BackupTask(), 0, interval);
        
        createHiddenIcon();
    }

    public void stopService() {
        if (timer != null) {
            timer.cancel();
        }
        if (trayIcon != null) {
            SystemTray.getSystemTray().remove(trayIcon);
        }
    }

    class BackupTask extends TimerTask {
        @Override
        public void run() {
            Logger.logMessage("Checking for automatic backup...");
            try {
                List<Backup> backups = json.ReadBackupListFromJSON(BackupManagerGUI.BACKUP_FILE_STRING, BackupManagerGUI.RES_DIRECTORY_STRING);
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
                BackupManagerGUI gui = new BackupManagerGUI();
                for (Backup backup : backups) {
                    BackupManagerGUI.currentBackup = backup;
                    gui.SingleBackup(backup);
                }
            });
        }
    }

    private void createHiddenIcon() {
        try {
            Logger.logMessage("Creating system tray icon...");
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/img/logo.png")); 

            if (!SystemTray.isSupported()) {
                Logger.logMessage("System tray is not supported!");
                return;
            }

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
        
            tray.add(trayIcon);
        } catch (Exception e) {
            Logger.logMessage("TrayIcon could not be added: " + e.getMessage());
            BackupManagerGUI.OpenExceptionMessage(e.getMessage(), Arrays.toString(e.getStackTrace()));
        }

        trayIcon.addActionListener((ActionEvent e) -> {
            javax.swing.SwingUtilities.invokeLater(() -> {
                new BackupManagerGUI().setVisible(true);
            });
        });
    }
}
