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

public class JSONAutoBackup implements IJSONAutoBackup {
    @Override
    public List<Backup> ReadBackupListFromJSON(String filename, String directoryPath) throws IOException {

        List<Backup> backupList = new ArrayList<>();
        String filePath = directoryPath + filename;

        // Check if the file exists and is not empty
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            Logger.logMessage("The file does not exist or is empty: " + filePath, Logger.LogLevel.WARN);
            return backupList;
        }
        
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            JSONArray backupArray = (JSONArray) parser.parse(reader);

            for (Object obj : backupArray) {
                JSONObject backupObj = (JSONObject) obj;

                String backupNameValue = (String) backupObj.get("backup_name");
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
                String daysIntervalBackupStr = (String) backupObj.get("time_interval_backup");

                LocalDateTime lastBackupValue = lastBackupStr != null ? LocalDateTime.parse(lastBackupStr) : null;
                LocalDateTime nextDateBackupValue = nextDateBackupStr != null ? LocalDateTime.parse(nextDateBackupStr) : null;
                LocalDateTime creationDateValue = creationDateStr != null ? LocalDateTime.parse(creationDateStr) : null;
                LocalDateTime lastUpdateDateValue = lastUpdateDateStr != null ? LocalDateTime.parse(lastUpdateDateStr) : null;

                backupList.add(new Backup(
                    backupNameValue,
                    startPathValue,
                    destinationPathValue,
                    lastBackupValue,
                    automaticBackupValue,
                    nextDateBackupValue,
                    TimeInterval.getTimeIntervalFromString(daysIntervalBackupStr),
                    notesValue,    
                    creationDateValue,
                    lastUpdateDateValue,
                    backupCountValue
                ));
            }

        } catch (IOException | ParseException ex) {
            Logger.logMessage("An error occurred", Logger.LogLevel.ERROR, ex);
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
            ex.printStackTrace();
        }
        return backupList;
    }
    
    @Override
    public void UpdateBackupListJSON(String filename, String directoryPath, List<Backup> backups) {
        String filePath = directoryPath + filename;
        
        JSONArray updatedBackupArray = new JSONArray();
        for (Backup backup : backups) {
            JSONObject backupObject = new JSONObject();
            backupObject.put("backup_name", backup.getBackupName());
            backupObject.put("start_path", backup.getInitialPath());
            backupObject.put("destination_path", backup.getDestinationPath());
            backupObject.put("last_backup", backup.getLastBackup() != null ? backup.getLastBackup().toString() : null);
            backupObject.put("automatic_backup", backup.isAutoBackup());
            backupObject.put("next_date_backup", backup.getNextDateBackup() != null ? backup.getNextDateBackup().toString() : null);
            backupObject.put("time_interval_backup", backup.getTimeIntervalBackup() != null ? backup.getTimeIntervalBackup().toString() : null);
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
            Logger.logMessage("An error occurred", Logger.LogLevel.ERROR, ex);
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        }
    }
    
    @Override
    public void UpdateSingleBackupInJSON(String filename, String directoryPath, Backup updatedBackup) {
        String filePath = directoryPath + filename;

        try (FileReader reader = new FileReader(filePath)) {
            JSONParser jsonParser = new JSONParser();
            JSONArray backupArray = (JSONArray) jsonParser.parse(reader);

            for (Object obj : backupArray) {
                JSONObject backupObject = (JSONObject) obj;

                String backupName = (String) backupObject.get("backup_name");
                if (backupName.equals(updatedBackup.getBackupName())) {
                    backupObject.put("start_path", updatedBackup.getInitialPath());
                    backupObject.put("destination_path", updatedBackup.getDestinationPath());
                    backupObject.put("last_backup", updatedBackup.getLastBackup() != null ? updatedBackup.getLastBackup().toString() : null);
                    backupObject.put("automatic_backup", updatedBackup.isAutoBackup());
                    backupObject.put("next_date_backup", updatedBackup.getNextDateBackup() != null ? updatedBackup.getNextDateBackup().toString() : null);
                    backupObject.put("time_interval_backup", updatedBackup.getTimeIntervalBackup() != null ? updatedBackup.getTimeIntervalBackup().toString() : null);
                    backupObject.put("notes", updatedBackup.getNotes());
                    backupObject.put("creation_date", updatedBackup.getCreationDate() != null ? updatedBackup.getCreationDate().toString() : null);
                    backupObject.put("last_update_date", updatedBackup.getLastUpdateDate() != null ? updatedBackup.getLastUpdateDate().toString() : null);
                    backupObject.put("backup_count", updatedBackup.getBackupCount());
                    break;
                }
            }

            try (FileWriter file = new FileWriter(filePath)) {
                file.write(backupArray.toJSONString());
                file.flush();
            } catch (IOException ex) {
                OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
            }

        } catch (IOException | ParseException ex) {
            Logger.logMessage("An error occurred", Logger.LogLevel.ERROR, ex);
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        }
    }
}