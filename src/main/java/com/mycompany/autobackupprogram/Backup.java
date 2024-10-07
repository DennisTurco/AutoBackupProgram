package com.mycompany.autobackupprogram;

import java.time.LocalDateTime;

public class Backup {

    private String _backupName;
    private String _initialPath;
    private String _destinationPath;
    private LocalDateTime _lastBackup;
    private boolean _autoBackup;
    private LocalDateTime _nextDateBackup;
    private Integer _daysIntervalBackup;
    private LocalDateTime _creationDate;
    private LocalDateTime _lastUpdateDate;
    
    public Backup() {
        _backupName = "";
        _initialPath = "";
        _destinationPath = "";
        _lastBackup = null;
        _autoBackup = false;
        _nextDateBackup = null;
        _daysIntervalBackup = null;
        _creationDate = null;
        _lastUpdateDate = null;
    }
    
    public Backup(String backupName, String initialPath, String destinationPath, LocalDateTime lastBackup, Boolean autoBackup, LocalDateTime nextDateBackup, Integer daysIntervalBackup, LocalDateTime creationDate, LocalDateTime lastUpdateDate) {
        this._backupName = backupName;
        this._initialPath = initialPath;
        this._destinationPath = destinationPath;
        this._lastBackup = lastBackup;
        this._autoBackup = autoBackup;
        this._nextDateBackup = nextDateBackup;
        this._daysIntervalBackup = daysIntervalBackup;
        this._creationDate = creationDate;
        this._lastUpdateDate = lastUpdateDate;
    }
    
    public void UpdateBackup(Backup backupUpdated) {
        this._backupName = backupUpdated.getBackupName();
        this._initialPath = backupUpdated.getInitialPath();
        this._destinationPath = backupUpdated.getDestinationPath();
        this._lastBackup = backupUpdated.getLastBackup();
        this._autoBackup = backupUpdated.isAutoBackup();
        this._nextDateBackup = backupUpdated.getNextDateBackup();
        this._daysIntervalBackup = backupUpdated.getDaysIntervalBackup();
        this._creationDate = backupUpdated.getCreationDate();
        this._lastUpdateDate = backupUpdated.getLastUpdateDate();
    }
    
    @Override
    public String toString() {
        return _backupName + " " + _initialPath + " " + _destinationPath + " " + _lastBackup + " " + _autoBackup + " " + _nextDateBackup + " " + _daysIntervalBackup; 
    }

    public String getBackupName() {
        return _backupName;
    }

    public String getInitialPath() {
        return _initialPath;
    }

    public String getDestinationPath() {
        return _destinationPath;
    }

    public LocalDateTime getLastBackup() {
        return _lastBackup;
    }

    public boolean isAutoBackup() {
        return _autoBackup;
    }

    public LocalDateTime getNextDateBackup() {
        return _nextDateBackup;
    }

    public Integer getDaysIntervalBackup() {
        return _daysIntervalBackup;
    }

    public LocalDateTime getCreationDate() {
        return _creationDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return _lastUpdateDate;
    }
    
    public void setBackupName(String backupName) {
        this._backupName = backupName;
    }

    public void setInitialPath(String initialPath) {
        this._initialPath = initialPath;
    }

    public void setDestinationPath(String destinationPath) {
        this._destinationPath = destinationPath;
    }

    public void setLastBackup(LocalDateTime lastBackup) {
        this._lastBackup = lastBackup;
    }

    public void setAutoBackup(Boolean autoBackup) {
        this._autoBackup = autoBackup;
    }

    public void setNextDateBackup(LocalDateTime nextDateBackup) {
        this._nextDateBackup = nextDateBackup;
    }

    public void setDaysIntervalBackup(Integer daysIntervalBackup) {
        this._daysIntervalBackup = daysIntervalBackup;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this._creationDate = creationDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this._lastUpdateDate = lastUpdateDate;
    }
}