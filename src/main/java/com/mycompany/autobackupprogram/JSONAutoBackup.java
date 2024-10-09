package com.mycompany.autobackupprogram;

import static com.mycompany.autobackupprogram.BackupManagerGUI.OpenExceptionMessage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class JSONAutoBackup implements IJSONAutoBackup {
    @Override
    public List<Backup> ReadBackupListFromJSON(String filename, String directoryPath) throws IOException {

        List<Backup> backupList = new ArrayList<>();
        String filePath = directoryPath + filename;

        // Check if the file exists and is not empty
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            System.err.println("The file does not exist or is empty: " + filePath);
            return backupList;
        }
        
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            JSONArray backupArray = (JSONArray) parser.parse(reader);

            for (Object obj : backupArray) {
                JSONObject backupObj = (JSONObject) obj;

                String filenameValue = (String) backupObj.get("filename");
                String startPathValue = (String) backupObj.get("start_path");
                String destinationPathValue = (String) backupObj.get("destination_path");
                String lastBackupStr = (String) backupObj.get("last_backup");
                String notesValue = (String) backupObj.get("notes");
                String creationDateStr = (String) backupObj.get("creation_date");
                String lastUpdateDateStr = (String) backupObj.get("last_update_date");
                int backupCountValue = Math.toIntExact((Long) backupObj.get("backup_count"));

                Object value = backupObj.get("automatic_backup");
                Boolean automaticBackupValue = null;
                if (value instanceof Boolean aBoolean) {
                    automaticBackupValue = aBoolean;
                } else if (value instanceof String string) {
                    automaticBackupValue = Boolean.valueOf(string);
                } else if (value instanceof Integer integer) {
                    automaticBackupValue = (integer == 1);
                }
                String nextDateBackupStr = (String) backupObj.get("next_date_backup");
                Long daysIntervalBackup = (Long) backupObj.get("days_interval_backup"); // Use Long for JSON parsing

                LocalDateTime lastBackupValue = lastBackupStr != null ? LocalDateTime.parse(lastBackupStr) : null;
                LocalDateTime nextDateBackupValue = nextDateBackupStr != null ? LocalDateTime.parse(nextDateBackupStr) : null;
                LocalDateTime creationDateValue = creationDateStr != null ? LocalDateTime.parse(creationDateStr) : null;
                LocalDateTime lastUpdateDateValue = lastUpdateDateStr != null ? LocalDateTime.parse(lastUpdateDateStr) : null;

                backupList.add(new Backup(
                    filenameValue,
                    startPathValue,
                    destinationPathValue,
                    lastBackupValue,
                    automaticBackupValue,
                    nextDateBackupValue,
                    daysIntervalBackup != null ? daysIntervalBackup.intValue() : null, // Convert Long to Integer
                    notesValue,    
                    creationDateValue,
                    lastUpdateDateValue,
                    backupCountValue
                ));
            }

        } catch (IOException | ParseException e) {
            System.err.println("IOException | ParseException (ReadBackupListFromJSON) --> " + e);
            OpenExceptionMessage(e.getMessage(), Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return backupList;
    }
    
    @Override
    public void UpdateBackupListJSON(String filename, String directoryPath, List<Backup> backups) {
        String filePath = directoryPath + filename;
        
        JSONArray updatedBackupArray = new JSONArray();
        for (Backup backup : backups) {
            JSONObject backupObject = new JSONObject();
            backupObject.put("filename", backup.getBackupName());
            backupObject.put("start_path", backup.getInitialPath());
            backupObject.put("destination_path", backup.getDestinationPath());
            backupObject.put("last_backup", backup.getLastBackup() != null ? backup.getLastBackup().toString() : null);
            backupObject.put("automatic_backup", backup.isAutoBackup());
            backupObject.put("next_date_backup", backup.getNextDateBackup() != null ? backup.getNextDateBackup().toString() : null);
            backupObject.put("days_interval_backup", backup.getDaysIntervalBackup());
            backupObject.put("notes", backup.getNotes());
            backupObject.put("creation_date", backup.getCreationDate() != null ? backup.getCreationDate().toString() : null);
            backupObject.put("last_update_date", backup.getLastUpdateDate() != null ? backup.getLastUpdateDate().toString() : null);
            backupObject.put("backup_count", backup.getBackupCount());

            updatedBackupArray.add(backupObject);
        }

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(updatedBackupArray.toJSONString());
            file.flush();
        } catch (IOException ex) {
            System.err.println("IOException (UpdateBackupListJSON) --> " + ex);
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        }
    }
}