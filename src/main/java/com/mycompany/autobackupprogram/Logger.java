package com.mycompany.autobackupprogram;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    
    public static void logMessage(String message) {
        File logFile = new File(BackupManagerGUI.RES_DIRECTORY_STRING + BackupManagerGUI.LOG_FILE_STRING);

        try {
            List<String> lines = new ArrayList<>();
            if (logFile.exists()) {
                lines = Files.readAllLines(logFile.toPath());
            }
            
            // keep only 20 rows if file has more than 1000 rows
            if (lines.size() > 1500) {
                lines = lines.subList(lines.size() - 150, lines.size());
            }

            // add message in the top of the file
            lines.add(0, LocalDateTime.now() + " " + message);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

        } catch (IOException ex) {
            Logger.logMessage("Exception --> " + ex);
        }

        System.out.println(message);
    }
    
}
