package com.mycompany.autobackupprogram.Interfaces;

import java.io.IOException;
import java.util.List;

import com.mycompany.autobackupprogram.Entities.Backup;

public interface IJSONAutoBackup {
    public List<Backup> ReadBackupListFromJSON(String filename, String directoryPath) throws IOException;
    public void UpdateBackupListJSON(String filename, String directoryPath, List<Backup> backups);
    public void UpdateSingleBackupInJSON(String filename, String directoryPath, Backup updatedBackup);
}
