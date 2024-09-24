package com.mycompany.autobackupprogram;

import static com.mycompany.autobackupprogram.AutoBackupGUI.OpenExceptionMessage;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class JSONAutoBackup implements IJSONAutoBackup {

    private JSONArray backupList;
    private final AutoBackupGUI frame;

    public JSONAutoBackup(AutoBackupGUI frame) {
        this.frame = frame;
    }

    @Override
    public Backup ReadJSONFile(String filename, String directoryPath) {
        if (filename == null || filename.isEmpty()) throw new IllegalArgumentException();
        
        JSONParser jsonP = new JSONParser();
        Path filePath = Paths.get(directoryPath, filename);

        try (FileReader reader = new FileReader(filePath.toFile())) {
            Object obj = jsonP.parse(reader);
            JSONObject list = (JSONObject) obj;

            String name = (String) list.get("filename");

            if (filename.equals(AutoBackupGUI.INFO_FILE_STRING)) {
                System.out.println("Event --> current file: " + name);
                ReadJSONFile(name, AutoBackupGUI.SAVES_DIRECTORY_STRING);
                return null; // return is essential to stop the recursion
            }

            String path1 = (String) list.get("start_path");
            String path2 = (String) list.get("destination_path");
            String lastBackupStr = (String) list.get("last_backup");
            String nextDateStr = (String) list.get("next_date_backup");
            Integer daysInterval = (Integer) list.get("days_interval_backup");
            Object value = list.get("automatic_backup");
            Boolean automaticBackup = null;
            if (value instanceof Boolean aBoolean) {
                automaticBackup = aBoolean;
            } else if (value instanceof String string) {
                automaticBackup = Boolean.valueOf(string);
            } else if (value instanceof Integer integer) {
                automaticBackup = (integer == 1);
            }
            
            LocalDateTime lastBackupValue = lastBackupStr != null && !lastBackupStr.isEmpty() ? LocalDateTime.parse(lastBackupStr) : null;
            LocalDateTime nextDateBackupValue = nextDateStr != null && !nextDateStr.isEmpty() ? LocalDateTime.parse(lastBackupStr) : null;

            Backup backup = new Backup(
                name,
                path1,
                path2,
                lastBackupValue,
                automaticBackup,
                nextDateBackupValue,
                daysInterval
            );
            
            frame.SetStartPathField(path1);
            frame.SetDestinationPathField(path2);
            frame.SetLastBackupLabel(lastBackupValue);
            frame.setAutoBackupPreference(automaticBackup);
            frame.setCurrentFileName(name);

            return backup;
        } catch (FileNotFoundException ex) {
            System.err.println("FileNotFoundException (ReadJSONFile) --> " + ex);
            ex.printStackTrace();
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        } catch (IOException | ParseException ex) {
            System.err.println("IOException | ParseException (ReadJSONFile) --> " + ex);
            ex.printStackTrace();
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        } catch (Exception ex) {
            System.err.println("Exception (ReadJSONFile) --> " + ex);
            ex.printStackTrace();
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        }
        return null;
    }
    
    @Override
    public Backup WriteJSONFile(String filename, String directoryPath) throws IOException {
        if (filename == null || filename.isEmpty()) throw new IllegalArgumentException("filename cannot be null or empty");
        
        Backup backup;
        
        JSONObject list = new JSONObject();
        list.put("filename", filename);
        list.put("start_path", frame.GetStartPathField());
        list.put("destination_path", frame.GetDestinationPathField());
        LocalDateTime date = AutoBackupGUI.currentBackup.getLastBackup();
        list.put("last_backup", date != null ? date.toString() : null);
        Boolean autoBackup = frame.GetAutomaticBackupPreference();
        list.put("automatic_backup", autoBackup);
        LocalDateTime nextDate = AutoBackupGUI.currentBackup.getNextDateBackup();
        Integer daysInterval = AutoBackupGUI.currentBackup.getDaysIntervalBackup();
        list.put("next_date_backup", autoBackup == true && nextDate != null ? nextDate.toString() : null);  
        list.put("days_interval_backup", autoBackup == true ? daysInterval : null);
        
        backup = new Backup(filename, frame.GetStartPathField(), frame.GetDestinationPathField(), date, autoBackup, nextDate, daysInterval);

        // file writing
        PrintToFile(list.toJSONString(), directoryPath, filename);
        
        return backup;
    }
    
    @Override
    public void WriteInfoJSONFile(String filename, String directoryPath) {
        if (filename == null) throw new IllegalArgumentException();
        
        new JSONObject().put("filename", AutoBackupGUI.currentBackup.getFilename());
    }
    
    @Override
    public List<Backup> ReadBackupListFromJSON(String filename, String directoryPath) throws IOException {

        //PrintToFile("", AutoBackupGUI.INFO_FILE_DIRECTORY_STRING, AutoBackupGUI.BACKUP_FILE_STRING); // clean the file

        List<Backup> backupList = new ArrayList<>();
        String filePath = directoryPath + filename;

        // Check if the file exists and is not empty
        
        File file = new File(filePath);
        
        System.out.println(filePath + " lenght = " + file.length());
        
        if (!file.exists() || file.length() == 0) {
            System.err.println("The file does not exist or is empty: " + filePath);
            
            System.out.println(filePath + " lenght = " + file.length());
            return backupList;
        }
        
        System.out.println(filePath + " lenght = " + file.length());

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            JSONArray backupArray = (JSONArray) parser.parse(reader);

            for (Object obj : backupArray) {
                JSONObject backupObj = (JSONObject) obj;

                String filenameValue = (String) backupObj.get("filename");
                String startPathValue = (String) backupObj.get("start_path");
                String destinationPathValue = (String) backupObj.get("destination_path");
                String lastBackupStr = (String) backupObj.get("last_backup");
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

                backupList.add(new Backup(
                    filenameValue,
                    startPathValue,
                    destinationPathValue,
                    lastBackupValue,
                    automaticBackupValue,
                    nextDateBackupValue,
                    daysIntervalBackup != null ? daysIntervalBackup.intValue() : null // Convert Long to Integer
                ));
            }

        } catch (IOException | ParseException e) {
            System.err.println("IOException | ParseException (ReadBackupListFromJSON) --> " + e);
            e.printStackTrace();
            OpenExceptionMessage(e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
        return backupList;
    }
    
    @Override
    public void LoadJSONBackupList() throws IOException {
        backupList = new JSONArray();

        PrintToFile("", AutoBackupGUI.INFO_FILE_DIRECTORY_STRING, AutoBackupGUI.BACKUP_FILE_STRING); // clean the file

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
                        Long daysInterval = (Long) list.get("days_interval_backup");
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
                        list.put("last_backup", lastBackup != null ? lastBackup : null);
                        list.put("automatic_backup", automaticBackup);
                        list.put("next_date_backup", automaticBackup == true ? nextDate : null);
                        list.put("days_interval_backup", automaticBackup == true ? daysInterval : null); 
                        
                        if (automaticBackup) {
                            // save the first auto_backup to do
                            LocalDateTime nextDateTime = LocalDateTime.parse(nextDate);
                            UpdateNextAutoBackup(nextDateTime);
                        }

                        // add to the list
                        backupList.add(list);

                    } catch (FileNotFoundException ex) {
                        System.err.println("FileNotFoundException (LoadJSONBackupList) --> " + ex);
                        OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
                    } catch (IOException | ParseException ex) {
                        System.err.println("IOException | ParseException (LoadJSONBackupList) --> " + ex);
                        OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
                    }
                }
            }
        }

        // write to file
        PrintToFile(backupList.toJSONString(), AutoBackupGUI.INFO_FILE_DIRECTORY_STRING, AutoBackupGUI.BACKUP_FILE_STRING);
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
        } catch (IOException ex) {
            System.err.println("IOException (UpdateBackupListJSON) --> " + ex);
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        }
    }

    public void UpdateNextAutoBackup(LocalDateTime newNextDate) throws IOException {
        if (AutoBackupGUI.currentBackup.getNextDateBackup() == null || newNextDate == null) return;

        LocalDateTime nextDate = AutoBackupGUI.currentBackup.getNextDateBackup();

        // the earliest date, save it in the file
        if (nextDate.compareTo(newNextDate) >= 0) {
            PrintToFile(newNextDate.toString(), AutoBackupGUI.INFO_FILE_DIRECTORY_STRING, AutoBackupGUI.NEXT_BACKUP_FILE_STRING);
        }
    }

    private void PrintToFile(String text, String directoryPath, String filename) throws IOException {
        if (directoryPath == null) throw new IllegalArgumentException("Directory path is null or empty");
        if (filename == null) throw new IllegalArgumentException("Filename is null or empty");
                
        File file = new File(directoryPath, filename);
        
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            writer.write(text);
        } catch (IOException ex) {
            System.err.println("IOException (PrintToFile) --> " + ex);
            throw ex;
        }
    }
}