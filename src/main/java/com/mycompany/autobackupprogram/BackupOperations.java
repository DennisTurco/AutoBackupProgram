package com.mycompany.autobackupprogram;

import static com.mycompany.autobackupprogram.BackupManagerGUI.OpenExceptionMessage;
import static com.mycompany.autobackupprogram.BackupManagerGUI.dateForfolderNameFormatter;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;

public class BackupOperations{
    
    private static final JSONAutoBackup JSON = new JSONAutoBackup();
    private static Thread zipThread;
    
    public static void SingleBackup(Backup backup, TrayIcon trayIcon) {
        Logger.logMessage("Event --> automatic single backup automatic", Logger.LogLevel.INFO);
		
        String temp = "\\";
        String path1 = backup.getInitialPath();
        String path2 = backup.getDestinationPath();

        //------------------------------INPUT CONTROL ERRORS------------------------------
        if(!CheckInputCorrect(backup.getBackupName(), path1, path2, trayIcon)) return;

        //------------------------------TO GET THE CURRENT DATE------------------------------
        LocalDateTime dateNow = LocalDateTime.now();

        //------------------------------SET ALL THE VARIABLES------------------------------
        String name1; // folder name/initial file
        String date = dateNow.format(dateForfolderNameFormatter);

        //------------------------------SET ALL THE STRINGS------------------------------
        name1 = path1.substring(path1.length()-1, path1.length()-1);

        for(int i=path1.length()-1; i>=0; i--) {
            if(path1.charAt(i) != temp.charAt(0)) name1 = path1.charAt(i) + name1;
            else break;
        }

        path2 = path2 + "\\" + name1 + " (Backup " + date + ")";

        //------------------------------COPY THE FILE OR DIRECTORY------------------------------
        Logger.logMessage("date backup: " + date, Logger.LogLevel.INFO);
        
        try {
            zipDirectory(path1, path2+".zip");
        } catch (IOException e) {
            System.err.println("Exception (SingleBackup) --> " + e);
            Logger.logMessage("Error during the backup operation: the initial path is incorrect!", Logger.LogLevel.WARN);
            JOptionPane.showMessageDialog(null, "Error during the backup operation: the initial path is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } 

        TimeInterval time = backup.getTimeIntervalBackup();
        LocalDateTime nextDateBackup = dateNow.plusDays(time.getDays())
                .plusHours(time.getHours())
                .plusMinutes(time.getMinutes());
        backup.setNextDateBackup(nextDateBackup);
        backup.setBackupCount(backup.getBackupCount()+1);
        
        List<Backup> backups;
        try {
            backups = JSON.ReadBackupListFromJSON(ConfigKey.BACKUP_FILE_STRING.getValue(), ConfigKey.RES_DIRECTORY_STRING.getValue());
        } catch (IOException ex) {
            backups = null;
            Logger.logMessage(ex.getMessage());
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        }
        for (Backup b : backups) {
            if (b.getBackupName().equals(backup.getBackupName())) {
                b.UpdateBackup(backup);
                break;
            }
        }

        JSON.UpdateSingleBackupInJSON(ConfigKey.BACKUP_FILE_STRING.getValue(), ConfigKey.RES_DIRECTORY_STRING.getValue(), backup);
        if (trayIcon != null) {
            trayIcon.displayMessage("Backup Manager", "Backup: "+ backup.getBackupName() +"\nThe backup was successfully completed:\nFrom: " + path1 + "\nTo: " + path2, TrayIcon.MessageType.INFO);
        }
    }
    
    public static boolean CheckInputCorrect(String backupName, String path1, String path2, TrayIcon trayIcon) {
        //check if inputs are null
        if(path1.length() == 0 || path2.length() == 0) {
            Logger.logMessage("Input Missing!", Logger.LogLevel.WARN);
            
            if (trayIcon != null){ 
                trayIcon.displayMessage("Backup Manager", "Backup: "+ backupName +"\nError during automatic backup.\nInput Missing!", TrayIcon.MessageType.ERROR);
            } else {
                JOptionPane.showMessageDialog(null, "Input Missing!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
        
        if (!Files.exists(Path.of(path1)) || !Files.exists(Path.of(path2))) {
            Logger.logMessage("Input Error! One or both paths do not exist.", Logger.LogLevel.WARN);

            if (trayIcon != null) { 
                trayIcon.displayMessage("Backup Manager", "Backup: "+ backupName +"\nError during automatic backup.\nOne or both paths do not exist!", TrayIcon.MessageType.ERROR);
            } else {
                JOptionPane.showMessageDialog(null, "One or both paths do not exist!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }

        return true;
    }
    
    public static void zipDirectory(String sourceDirectoryPath, String targetZipPath) throws IOException { // Track copied files
        zipThread = new Thread(() -> {
            File file = new File(sourceDirectoryPath);
            Path sourceDir = Paths.get(sourceDirectoryPath);
            String rootFolderName = sourceDir.getFileName().toString(); // Get the root folder name

            try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(targetZipPath))) {
                if (file.isFile()) {
                    addFileToZip(zipOut, file.toPath(), "");
                } else {
                    Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            if (Thread.currentThread().isInterrupted()) {
                                Logger.logMessage("Zipping process manually interrupted", Logger.LogLevel.INFO);
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

                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                            if (Thread.currentThread().isInterrupted()) {
                                Logger.logMessage("Zipping process manually interrupted", Logger.LogLevel.INFO);
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
                Logger.logMessage("An error occurred", Logger.LogLevel.ERROR, ex);
                ex.printStackTrace();  // Handle the exception as necessary
            }
        });

        zipThread.start(); // Start the zipping thread
    }
    
    private static void addFileToZip(ZipOutputStream zipOut, Path file, String zipEntryName) throws IOException {
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
    }
}
