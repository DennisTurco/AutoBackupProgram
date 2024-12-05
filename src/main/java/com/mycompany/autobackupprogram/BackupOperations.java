package com.mycompany.autobackupprogram;

import static com.mycompany.autobackupprogram.GUI.BackupManagerGUI.OpenExceptionMessage;
import static com.mycompany.autobackupprogram.GUI.BackupManagerGUI.dateForfolderNameFormatter;
import static com.mycompany.autobackupprogram.GUI.BackupManagerGUI.formatter;

import java.awt.TrayIcon;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import com.mycompany.autobackupprogram.Entities.Backup;
import com.mycompany.autobackupprogram.Enums.ConfigKey;
import com.mycompany.autobackupprogram.Enums.TranslationLoaderEnum.TranslationCategory;
import com.mycompany.autobackupprogram.Enums.TranslationLoaderEnum.TranslationKey;
import com.mycompany.autobackupprogram.Entities.Preferences;
import com.mycompany.autobackupprogram.GUI.BackupManagerGUI;
import com.mycompany.autobackupprogram.GUI.BackupProgressGUI;
import com.mycompany.autobackupprogram.Entities.TimeInterval;
import com.mycompany.autobackupprogram.Logger.LogLevel;

public class BackupOperations {
    
    private static final JSONAutoBackup JSON = new JSONAutoBackup();
    private static Thread zipThread;
    
    public static void SingleBackup(Backup backup, TrayIcon trayIcon, BackupProgressGUI progressBar, JButton singleBackupBtn, JToggleButton autoBackupBtn) {
        if (backup == null) throw new IllegalArgumentException("Backup cannot be null!");
        
        Logger.logMessage("Event --> automatic single backup started", Logger.LogLevel.INFO);

        if (singleBackupBtn != null) singleBackupBtn.setEnabled(false);
        if (autoBackupBtn != null) autoBackupBtn.setEnabled(false);
        
        try {
            String temp = "\\";
            String path1 = backup.getInitialPath();
            String path2 = backup.getDestinationPath();

            if(!CheckInputCorrect(backup.getBackupName(), path1, path2, trayIcon)) return;

            LocalDateTime dateNow = LocalDateTime.now();
            String date = dateNow.format(dateForfolderNameFormatter);
            String name1 = path1.substring(path1.length() - 1);

            for(int i = path1.length() - 1; i >= 0; i--) {
                if(path1.charAt(i) != temp.charAt(0)) name1 = path1.charAt(i) + name1;
                else break;
            }

            path2 = path2 + "\\" + name1 + " (Backup " + date + ")";

            zipDirectory(path1, path2+".zip", backup, trayIcon, progressBar, singleBackupBtn, autoBackupBtn);
        } catch (IOException e) {
            Logger.logMessage("Error during the backup operation: the initial path is incorrect!", Logger.LogLevel.WARN);
            JOptionPane.showMessageDialog(null, TranslationCategory.DIALOGS.getTranslation(TranslationKey.ERROR_MESSAGE_FOR_INCORRECT_INITIAL_PATH), TranslationCategory.DIALOGS.getTranslation(TranslationKey.ERROR_GENERIC_TITLE), JOptionPane.ERROR_MESSAGE);
            if (singleBackupBtn != null) singleBackupBtn.setEnabled(true);
            if (autoBackupBtn != null) autoBackupBtn.setEnabled(true);
        } catch (Exception ex) {
            Logger.logMessage("An error occurred: " + ex.getMessage(), Logger.LogLevel.ERROR, ex);
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
            if (singleBackupBtn != null) singleBackupBtn.setEnabled(true);
            if (autoBackupBtn != null) autoBackupBtn.setEnabled(true);
        }
    }
    
    private static void updateAfterBackup(String path1, String path2, Backup backup, TrayIcon trayIcon, JButton singleBackupBtn, JToggleButton autoBackupBtn) {
        if (backup == null) throw new IllegalArgumentException("Backup cannot be null!");
        if (path1 == null) throw new IllegalArgumentException("Initial path cannot be null!");
        if (path2 == null) throw new IllegalArgumentException("Destination path cannot be null!");
        
        LocalDateTime dateNow = LocalDateTime.now();
           
        Logger.logMessage("Backup completed!", Logger.LogLevel.INFO);

        if (singleBackupBtn != null) singleBackupBtn.setEnabled(true);
        if (autoBackupBtn != null) autoBackupBtn.setEnabled(true);
        
        // next day backup update
        if (backup.isAutoBackup() == true) {
            TimeInterval time = backup.getTimeIntervalBackup();
            LocalDateTime nextDateBackup = dateNow.plusDays(time.getDays())
                    .plusHours(time.getHours())
                    .plusMinutes(time.getMinutes());
            backup.setNextDateBackup(nextDateBackup);
            Logger.logMessage("Next date backup setted to: " + nextDateBackup, Logger.LogLevel.INFO);
        }
        backup.setLastBackup(dateNow);
        backup.setBackupCount(backup.getBackupCount()+1);
                    
        try {
            List<Backup> backups = JSON.ReadBackupListFromJSON(Preferences.getBackupList().getDirectory(), Preferences.getBackupList().getFile());
                        
            for (Backup b : backups) {
                if (b.getBackupName().equals(backup.getBackupName())) {
                    b.UpdateBackup(backup);
                    break;
                }
            }
            
            updateBackup(backups, backup);
            
            if (trayIcon != null) { 
                trayIcon.displayMessage(TranslationCategory.GENERAL.getTranslation(TranslationKey.APP_NAME), TranslationCategory.GENERAL.getTranslation(TranslationKey.BACKUP) + ": " + backup.getBackupName() + TranslationCategory.TRAY_ICON.getTranslation(TranslationKey.SUCCESS_MESSAGE) + "\n" + TranslationCategory.GENERAL.getTranslation(TranslationKey.FROM) + ": " + path1 + "\n" + TranslationCategory.GENERAL.getTranslation(TranslationKey.TO) + ": " + path2, TrayIcon.MessageType.INFO);
            }
        } catch (IllegalArgumentException ex) {
            Logger.logMessage("An error occurred: " + ex.getMessage(), Logger.LogLevel.ERROR, ex);
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        } catch (Exception e) {
            Logger.logMessage("Error saving file", Logger.LogLevel.WARN);
            JOptionPane.showMessageDialog(null, TranslationCategory.DIALOGS.getTranslation(TranslationKey.ERROR_MESSAGE_SAVING_FILE), TranslationCategory.DIALOGS.getTranslation(TranslationKey.ERROR_GENERIC_TITLE), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static boolean CheckInputCorrect(String backupName, String path1, String path2, TrayIcon trayIcon) {
        //check if inputs are null
        if(path1.length() == 0 || path2.length() == 0) {
            Logger.logMessage("Input Missing!", Logger.LogLevel.WARN);
 
            if (trayIcon != null) {
                trayIcon.displayMessage(TranslationCategory.GENERAL.getTranslation(TranslationKey.APP_NAME), TranslationCategory.GENERAL.getTranslation(TranslationKey.BACKUP) + ": " + backupName + TranslationCategory.TRAY_ICON.getTranslation(TranslationKey.ERROR_MESSAGE_INPUT_MISSING), TrayIcon.MessageType.ERROR);
            } else {
                JOptionPane.showMessageDialog(null, TranslationCategory.DIALOGS.getTranslation(TranslationKey.ERROR_MESSAGE_INPUT_MISSING_GENERIC), TranslationCategory.DIALOGS.getTranslation(TranslationKey.ERROR_GENERIC_TITLE), JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
        
        if (!Files.exists(Path.of(path1)) || !Files.exists(Path.of(path2))) {
            Logger.logMessage("Input Error! One or both paths do not exist.", Logger.LogLevel.WARN);

            if (trayIcon != null) { 
                trayIcon.displayMessage(TranslationCategory.GENERAL.getTranslation(TranslationKey.APP_NAME), TranslationCategory.GENERAL.getTranslation(TranslationKey.BACKUP) + ": " + backupName + TranslationCategory.TRAY_ICON.getTranslation(TranslationKey.ERROR_MESSAGE_FILES_NOT_EXISTING), TrayIcon.MessageType.ERROR);
            } else {
                JOptionPane.showMessageDialog(null, TranslationCategory.DIALOGS.getTranslation(TranslationKey.ERROR_MESSAGE_PATH_NOT_EXISTING), TranslationCategory.DIALOGS.getTranslation(TranslationKey.ERROR_GENERIC_TITLE), JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }

        if (path1.equals(path2)) {
            Logger.logMessage("The initial path and destination path cannot be the same. Please choose different paths", Logger.LogLevel.WARN);

            if (trayIcon != null) { 
                trayIcon.displayMessage(TranslationCategory.GENERAL.getTranslation(TranslationKey.APP_NAME), TranslationCategory.GENERAL.getTranslation(TranslationKey.BACKUP) + ": " + backupName + TranslationCategory.TRAY_ICON.getTranslation(TranslationKey.ERROR_MESSAGE_SAME_PATHS), TrayIcon.MessageType.ERROR);
            } else {
                JOptionPane.showMessageDialog(null, TranslationCategory.DIALOGS.getTranslation(TranslationKey.ERROR_MESSAGE_SAME_PATHS_GENERIC), TranslationCategory.DIALOGS.getTranslation(TranslationKey.ERROR_GENERIC_TITLE), JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }

        return true;
    }
    
    public static void zipDirectory(String sourceDirectoryPath, String targetZipPath, Backup backup, TrayIcon trayIcon, BackupProgressGUI progressBar, JButton singleBackupBtn, JToggleButton autoBackupBtn) throws IOException { // Track copied files
        Logger.logMessage("Starting zipping process", LogLevel.INFO);

        File file = new File(sourceDirectoryPath);
        int totalFilesCount = file.isDirectory() ? countFilesInDirectory(file) : 1;
        
        AtomicInteger copiedFilesCount = new AtomicInteger(0);  // Track copied files
        
        zipThread = new Thread(() -> {
            Path sourceDir = Paths.get(sourceDirectoryPath);
            String rootFolderName = sourceDir.getFileName().toString(); // Get the root folder name

                try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(targetZipPath))) {
                    if (file.isFile()) {
                        addFileToZip(sourceDirectoryPath, zipOut, file.toPath(), "", copiedFilesCount, totalFilesCount, backup, trayIcon, progressBar, singleBackupBtn, autoBackupBtn);
                    } else {
                        Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                                if (Thread.currentThread().isInterrupted()) {
                                    Logger.logMessage("Zipping process manually interrupted", Logger.LogLevel.INFO);
                                    if (singleBackupBtn != null) singleBackupBtn.setEnabled(true);
                                    if (autoBackupBtn != null) autoBackupBtn.setEnabled(true);
                                    return FileVisitResult.TERMINATE; // Stop if interrupted
                                }

                                // Calculate the relative path inside the zip
                                Path targetFilePath = sourceDir.relativize(file);
                                String zipEntryName = rootFolderName + "/" + targetFilePath.toString();

                                // Create a new zip entry for the file
                                zipOut.putNextEntry(new ZipEntry(zipEntryName));

                                // Copy the file content to the zip output stream
                                try (InputStream in = Files.newInputStream(file)) {
                                    byte[] buffer = new byte[1024];
                                    int len;
                                    while ((len = in.read(buffer)) > 0) {
                                        zipOut.write(buffer, 0, len);
                                    }
                                }

                                zipOut.closeEntry(); // Close the zip entry after the file is written
                                
                                // Update progress
                                int filesCopiedSoFar = copiedFilesCount.incrementAndGet();
                                int actualProgress = (int) (((double) filesCopiedSoFar / totalFilesCount) * 100);
                                UpdateProgressPercentage(actualProgress, sourceDirectoryPath, targetZipPath, backup, trayIcon, progressBar, singleBackupBtn, autoBackupBtn);  // Update progress percentage

                                return FileVisitResult.CONTINUE;
                            }

                            @Override
                            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                                if (Thread.currentThread().isInterrupted()) {
                                    Logger.logMessage("Zipping process manually interrupted", Logger.LogLevel.INFO);
                                    if (singleBackupBtn != null) singleBackupBtn.setEnabled(true);
                                    if (autoBackupBtn != null) autoBackupBtn.setEnabled(true);
                                    return FileVisitResult.TERMINATE; // Stop if interrupted
                                }

                                // Create directory entry in the zip if needed
                                Path targetDir = sourceDir.relativize(dir);
                                zipOut.putNextEntry(new ZipEntry(rootFolderName + "/" + targetDir.toString() + "/"));
                                zipOut.closeEntry();
                                return FileVisitResult.CONTINUE;
                            }
                        });
                    }
                } catch (IOException ex) {
                    Logger.logMessage("An error occurred: " + ex.getMessage() , Logger.LogLevel.ERROR, ex);
                    if (singleBackupBtn != null) singleBackupBtn.setEnabled(true);
                    if (autoBackupBtn != null) autoBackupBtn.setEnabled(true);
                }
            });

            zipThread.start(); // Start the zipping thread
        }

    private static void addFileToZip(String sourceDirectoryPath, ZipOutputStream zipOut, Path file, String zipEntryName, AtomicInteger copiedFilesCount, int totalFilesCount, Backup backup, TrayIcon trayIcon, BackupProgressGUI progressBar, JButton singleBackupBtn, JToggleButton autoBackupBtn) throws IOException {
        if (zipEntryName.isEmpty()) {
            zipEntryName = file.getFileName().toString();
        }

        zipOut.putNextEntry(new ZipEntry(zipEntryName));

        try (InputStream in = Files.newInputStream(file)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                zipOut.write(buffer, 0, len);
            }
        }

        zipOut.closeEntry();
        
        int filesCopiedSoFar = copiedFilesCount.incrementAndGet();
        int actualProgress = (int) (((double) filesCopiedSoFar / totalFilesCount) * 100);
        UpdateProgressPercentage(actualProgress, sourceDirectoryPath, zipOut.toString(), backup, trayIcon, progressBar, singleBackupBtn, autoBackupBtn);
    }
    
    public static void updateBackupList(List<Backup> backups) {
        if (backups == null) throw new IllegalArgumentException("Backup list is null!");
            
        JSON.UpdateBackupListJSON(Preferences.getBackupList().getDirectory(), Preferences.getBackupList().getFile(), backups);
        
        if (BackupManagerGUI.model != null)
            updateTableWithNewBackupList(backups);
    }
    
    public static void updateBackup(List<Backup> backups, Backup updatedBackup) {
        if (updatedBackup == null) throw new IllegalArgumentException("Backup is null!");
        
        JSON.UpdateSingleBackupInJSON(Preferences.getBackupList().getDirectory(), Preferences.getBackupList().getFile(), updatedBackup);
        
        if (BackupManagerGUI.model != null)
            updateTableWithNewBackupList(backups);
    }
    
    public static void UpdateProgressPercentage(int value, String path1, String path2, Backup backup, TrayIcon trayIcon, BackupProgressGUI progressBar, JButton singleBackupBtn, JToggleButton autoBackupBtn) {
        
        if (value == 0 || value == 25 || value == 50 || value == 75 || value == 100)
            Logger.logMessage("Progress: " + value, Logger.LogLevel.INFO);
        
        if (progressBar != null)
            progressBar.UpdateProgressBar(value);
        
        if (value == 100) {
            updateAfterBackup(path1, path2, backup, trayIcon, singleBackupBtn, autoBackupBtn);
        }
    }
    
    public static void updateTableWithNewBackupList(List<Backup> updatedBackups) { 
        Logger.logMessage("updating backup list", Logger.LogLevel.DEBUG);
        
        SwingUtilities.invokeLater(() -> {
            BackupManagerGUI.model.setRowCount(0);

            for (Backup backup : updatedBackups) {
                BackupManagerGUI.model.addRow(new Object[]{
                    backup.getBackupName(),
                    backup.getInitialPath(),
                    backup.getDestinationPath(),
                    backup.getLastBackup() != null ? backup.getLastBackup().format(formatter) : "",
                    backup.isAutoBackup(),
                    backup.getNextDateBackup() != null ? backup.getNextDateBackup().format(formatter) : "",
                    backup.getTimeIntervalBackup() != null ? backup.getTimeIntervalBackup().toString() : ""
                });
            }
        });
    }
    
    private static int countFilesInDirectory(File directory) {
    	int count = 0;
    	
    	for (File file : directory.listFiles()) {
            if (file.isFile()) {
                count++;
            }
            if (file.isDirectory()) {
                count += countFilesInDirectory(file);
            }
    	}
    	return count;
    }
    
    public static void StopCopyFiles() {
        zipThread.interrupt();
    }
}
