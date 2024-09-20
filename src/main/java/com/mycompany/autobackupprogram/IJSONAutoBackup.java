package com.mycompany.autobackupprogram;

import java.util.List;

public interface IJSONAutoBackup {
    public void ReadJSONFile(String filename, String directoryPath);
    public List<Backup> ReadBackupListFromJSON(String filename, String directoryPath);
    public void UpdateBackupListJSON(String filename, String directoryPath, List<Backup> backups);
    public Backup WriteJSONFile(String filename, String directoryPath);
    public void LoadJSONBackupList();
}
