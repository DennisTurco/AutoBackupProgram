package com.mycompany.autobackupprogram;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BackupService {
    private Timer timer;
    private final JSONAutoBackup json = new JSONAutoBackup();

    public void startService() {
        timer = new Timer();
        timer.schedule(new BackupTask(), 0, 3600000); // timer every hour
    }

    public void stopService() {
        if (timer != null) {
            timer.cancel();
        }
    }

    class BackupTask extends TimerTask {
        @Override
        public void run() {
            try {
                List<Backup> backups = json.ReadBackupListFromJSON(BackupManagerGUI.BACKUP_FILE_STRING, BackupManagerGUI.RES_DIRECTORY_STRING);
                List<Backup> needsBackup = getBackupsToDo(backups);
                if (needsBackup != null && !needsBackup.isEmpty()) {
                    System.out.println("Start backup process.");
                    openMainGUI(needsBackup);
                } else {
                    System.out.println("No backup needed at this time.");
                }
            } catch (IOException ex) {
                
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
                    gui.currentBackup = backup;
                    gui.SingleBackup(backup.getInitialPath(), backup.getDestinationPath());
                }
            });
        }
    }
}
