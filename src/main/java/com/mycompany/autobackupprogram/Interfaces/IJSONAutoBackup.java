package com.mycompany.autobackupprogram.Interfaces;

import java.io.IOException;
import java.util.List;

import com.mycompany.autobackupprogram.Entities.Backup;

public interface IJSONAutoBackup {
    public List<Backup> ReadBackupListFromJSON(String directoryPath, String filename) throws IOException;
    public void UpdateBackupListJSON(String directoryPath, String filename, List<Backup> backups);
    public void UpdateSingleBackupInJSON(String directoryPath, String filename, Backup updatedBackup);
}
