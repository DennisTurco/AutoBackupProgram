package com.mycompany.autobackupprogram;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class JSONAutoBackup implements IJSONAutoBackup {

    private JSONArray backupList;
    private static CheckUpdateAutoBackup threadCheckUpdate;
    private final AutoBackupGUI frame;

    public JSONAutoBackup(AutoBackupGUI frame) {
        this.frame = frame;
    }

    @Override
    public void ReadJSONFile(String filename, String directoryPath) {
        if (filename == null || filename.isEmpty()) throw new IllegalArgumentException();
        
        JSONParser jsonP = new JSONParser();
        Path filePath = Paths.get(directoryPath, filename);

        try (FileReader reader = new FileReader(filePath.toFile())) {
            Object obj = jsonP.parse(reader);
            JSONObject list = (JSONObject) obj;

            String name = (String) list.get("filename");

            if (filename.equals("info.json")) {
                System.out.println("Event --> current file: " + name);
                ReadJSONFile(name, AutoBackupGUI.SAVES_DIRECTORY_STRING);
                return; // return is essential to stop the recursion
            }

            String path1 = (String) list.get("start_path");
            String path2 = (String) list.get("destination_path");
            String lastBackup = (String) list.get("last_backup");
            String nextDate = (String) list.get("next_date_backup");
            String daysInterval = (String) list.get("days_interval_backup");
            Object value = list.get("automatic_backup");
            Boolean automaticBackup = null;
            if (value instanceof Boolean) {
                automaticBackup = (Boolean) value;
            } else if (value instanceof String string) {
                automaticBackup = Boolean.valueOf(string);
            } else if (value instanceof Integer integer) {
                automaticBackup = (integer == 1);
            }

            // update the variables
            AutoBackupGUI.currentFileOpened = name;
            frame.SetStartPathField(path1);
            frame.SetDestinationPathField(path2);
            frame.SetLastBackupLabel(lastBackup);
            frame.setAutoBackupPreference(automaticBackup);
            AutoBackupGUI.nextDateBackup = nextDate;
            AutoBackupGUI.autoBackupOption = automaticBackup;
            Optional.ofNullable(daysInterval).ifPresent(di -> AutoBackupGUI.daysIntervalBackup = Integer.parseInt(di));
            frame.setCurrentFileName(name);

            // thread to set '*' to the file name, in case the last changes were not saved
            checkTimer();

        } catch (FileNotFoundException e) {
            System.err.println("Exception --> " + e);
        } catch (IOException | ParseException e) {
            System.err.println("Exception --> " + e);
        }
    }
    
    @Override
    public Backup WriteJSONFile(String filename, String directoryPath) {
        JSONObject list = new JSONObject();
        
        if (filename == null) throw new IllegalArgumentException();
        
        
        Backup backup;
        if (filename.equals(AutoBackupGUI.INFO_FILE_STRING)) {
            list.put("filename", AutoBackupGUI.currentFileOpened);
            return null;
        } else {
            list.put("filename", filename);
            list.put("start_path", frame.GetStartPathField());
            list.put("destination_path", frame.GetDestinationPathField());
            String lastBackup = frame.GetLastBackupLabel();
            lastBackup = lastBackup.replace("last backup: ", "");
            list.put("last_backup", lastBackup);
            Boolean autoBackup = frame.GetAutomaticBackupPreference();
            list.put("automatic_backup", autoBackup);

            if (autoBackup) {
                list.put("next_date_backup", AutoBackupGUI.nextDateBackup);
                list.put("days_interval_backup", String.valueOf(AutoBackupGUI.daysIntervalBackup));
            } else {
                list.put("next_date_backup", null);
                list.put("days_interval_backup", null);
            }
            
            backup = new Backup(filename, frame.GetStartPathField(), frame.GetDestinationPathField(), lastBackup, autoBackup, null, null);
        }

        // thread to set '*' to the file name, in case the last changes were not saved
        checkTimer();

        try {
            // file writing
            PrintToFile(list.toJSONString(), directoryPath, filename);
            return backup;
        } catch (IOException ex) {
            System.err.println("Exception --> " + ex);
            return null;
        }
    }
    
    @Override
    public List<Backup> ReadBackupListFromJSON(String filename, String directoryPath) {
        List<Backup> backupList = new ArrayList<>();
        String filePath = directoryPath + filename;

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            JSONArray backupArray = (JSONArray) parser.parse(reader);

            for (Object obj : backupArray) {
                JSONObject backupObj = (JSONObject) obj;

                String filenameValue = (String) backupObj.get("filename");
                String startPathValue = (String) backupObj.get("start_path");
                String destinationPathValue = (String) backupObj.get("destination_path");
                String lastBackupValue = (String) backupObj.get("last_backup");
                Object value = backupObj.get("automatic_backup");
                Boolean automaticBackupValue = null;
                if (value instanceof Boolean) {
                    automaticBackupValue = (Boolean) value;
                } else if (value instanceof String string) {
                    automaticBackupValue = Boolean.valueOf(string);
                } else if (value instanceof Integer integer) {
                    automaticBackupValue = (integer == 1);
                }
                String nextDateBackup = (String) backupObj.get("next_date_backup");
                String daysIntervalBackup = (String) backupObj.get("days_interval_backup");

                Backup backup = new Backup(
                        filenameValue,
                        startPathValue,
                        destinationPathValue,
                        lastBackupValue,
                        automaticBackupValue,
                        nextDateBackup,
                        daysIntervalBackup
                );

                backupList.add(backup);
            }

        } catch (IOException | ParseException e) {
            System.err.println("Exception --> " + e);
        }

        return backupList;
    }
    
    @Override
    public void UpdateBackupListJSON(String filename, String directoryPath, List<Backup> backups) {
        String filePath = directoryPath + filename;
        
        JSONArray updatedBackupArray = new JSONArray();
        for (Backup backup : backups) {
            JSONObject backupObject = new JSONObject();
            backupObject.put("filename", backup.getFilename());
            backupObject.put("start_path", backup.getInitialPath());
            backupObject.put("destination_path", backup.getDestinationPath());
            backupObject.put("last_backup", backup.getLastBackup());
            backupObject.put("automatic_backup", backup.isAutoBackup());
            backupObject.put("next_date_backup", backup.getNextDateBackup());
            backupObject.put("days_interval_backup", backup.getDaysIntervalBackup());

            updatedBackupArray.add(backupObject);
        }

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(updatedBackupArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.err.println("Exception --> " + e);
        }
    }
    
    @Override
    public void LoadJSONBackupList() {
        backupList = new JSONArray();

        try {
            PrintToFile("", AutoBackupGUI.INFO_FILE_DIRECTORY_STRING, AutoBackupGUI.BACKUP_FILE_STRING); // clean the file
        } catch (IOException ex) {
            return;
        }

        File directory = new File(AutoBackupGUI.SAVES_DIRECTORY_STRING);
        File[] listOfFiles = directory.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    JSONParser jsonP = new JSONParser();

                    try (FileReader reader = new FileReader(file)) {
                        Object obj = jsonP.parse(reader);
                        JSONObject list = (JSONObject) obj;

                        // reading
                        String name = (String) list.get("filename");
                        String path1 = (String) list.get("start_path");
                        String path2 = (String) list.get("destination_path");
                        String lastBackup = (String) list.get("last_backup");
                        String nextDate = (String) list.get("next_date_backup");
                        String daysInterval = (String) list.get("days_interval_backup");
                        Object value = list.get("automatic_backup");
                        Boolean automaticBackup = null;

                        if (value instanceof Boolean) {
                            automaticBackup = (Boolean) value;
                        } else if (value instanceof String string) {
                            automaticBackup = Boolean.valueOf(string);
                        } else if (value instanceof Integer integer) {
                            automaticBackup = (integer == 1);
                        }

                        // writing
                        list.put("filename", name);
                        list.put("start_path", path1);
                        list.put("destination_path", path2);
                        list.put("last_backup", lastBackup);
                        list.put("automatic_backup", automaticBackup);

                        if (automaticBackup) {
                            list.put("next_date_backup", nextDate);
                            list.put("days_interval_backup", daysInterval);
                        } else {
                            list.put("next_date_backup", null);
                            list.put("days_interval_backup", null);
                        }

                        // add to the list
                        backupList.add(list);

                        // save the first auto_backup to do
                        UpdateNextAutoBackup(nextDate);

                    } catch (FileNotFoundException e) {
                        System.err.println("Exception --> " + e);
                    } catch (IOException | ParseException e) {
                        System.err.println("Exception --> " + e);
                    }
                }
            }
        }

        try {
            // write to file
            PrintToFile(backupList.toJSONString(), AutoBackupGUI.INFO_FILE_DIRECTORY_STRING, AutoBackupGUI.BACKUP_FILE_STRING);
        } catch (IOException ex) {
            Logger.getLogger(JSONAutoBackup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void UpdateNextAutoBackup(String newDate) throws IOException {
        if (AutoBackupGUI.nextDateBackup == null || newDate == null) return;

        LocalDate nextDate = LocalDate.parse(AutoBackupGUI.nextDateBackup);
        LocalDate newNextDate = LocalDate.parse(newDate);

        // the earliest date, save it in the file
        if (nextDate.compareTo(newNextDate) >= 0) {
            PrintToFile(newNextDate.toString(), AutoBackupGUI.INFO_FILE_DIRECTORY_STRING, AutoBackupGUI.NEXT_BACKUP_FILE_STRING);
        }
    }

    private void PrintToFile(String text, String directoryPath, String filename) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(directoryPath, filename))) {
            writer.write(text);
        } catch (IOException ex) {
            System.err.println("Exception --> " + ex);
            throw ex;
        }
    }

    private void checkTimer() {
        if (threadCheckUpdate != null && threadCheckUpdate.isTimerRunning()) {
            threadCheckUpdate.stopTimer();
        }
        threadCheckUpdate = new CheckUpdateAutoBackup(frame);
        threadCheckUpdate.startTimer();
        frame.SetMessageLabel(false);
    }
}