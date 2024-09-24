package com.mycompany.autobackupprogram;

import java.io.IOException;
import java.util.List;

public interface IJSONAutoBackup {
    public Backup ReadJSONFile(String filename, String directoryPath);
    public List<Backup> ReadBackupListFromJSON(String filename, String directoryPath) throws IOException;
    public void UpdateBackupListJSON(String filename, String directoryPath, List<Backup> backups);
    public Backup WriteJSONFile(String filename, String directoryPath) throws IOException;
    public void WriteInfoJSONFile(String filename, String directoryPath) throws IOException;
    public void LoadJSONBackupList() throws IOException;
}
