package com.mycompany.autobackupprogram;

import java.io.IOException;
import java.util.List;

public interface IJSONAutoBackup {
    public List<Backup> ReadBackupListFromJSON(String filename, String directoryPath) throws IOException;
    public void UpdateBackupListJSON(String filename, String directoryPath, List<Backup> backups);
    public void UpdateSingleBackupInJSON(String filename, String directoryPath, Backup updatedBackup);
    public int ReadCheckForBackupTimeInterval(String filename, String directoryPath) throws IOException;
}
