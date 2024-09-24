package com.mycompany.autobackupprogram;

import java.time.LocalDateTime;

public class Backup {

    private String _filename;
    private String _initialPath;
    private String _destinationPath;
    private LocalDateTime _lastBackup;
    private boolean _autoBackup;
    private LocalDateTime _nextDateBackup;
    private Integer _daysIntervalBackup;
    
    public Backup() {
        _filename = "";
        _initialPath = "";
        _destinationPath = "";
        _lastBackup = null;
        _autoBackup = false;
        _nextDateBackup = null;
        _daysIntervalBackup = null;
    }
    
    public Backup(String filename, String initialPath, String destinationPath, LocalDateTime lastBackup, Boolean autoBackup, LocalDateTime nextDateBackup, Integer daysIntervalBackup) {
        this._filename = filename;
        this._initialPath = initialPath;
        this._destinationPath = destinationPath;
        this._lastBackup = lastBackup;
        this._autoBackup = autoBackup;
        this._nextDateBackup = nextDateBackup;
        this._daysIntervalBackup = daysIntervalBackup;
    }
    
    public void UpdateBackup(Backup backupUpdated) {
        this._filename = backupUpdated.getFilename();
        this._initialPath = backupUpdated.getInitialPath();
        this._destinationPath = backupUpdated.getDestinationPath();
        this._lastBackup = backupUpdated.getLastBackup();
        this._autoBackup = backupUpdated.isAutoBackup();
        this._nextDateBackup = backupUpdated.getNextDateBackup();
        this._daysIntervalBackup = backupUpdated.getDaysIntervalBackup();
    }
    
    @Override
    public String toString() {
        return _filename + " " + _initialPath + " " + _destinationPath + " " + _lastBackup + " " + _autoBackup + " " + _nextDateBackup + " " + _daysIntervalBackup; 
    }

    public String getFilename() {
        return _filename;
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

    public void setFilename(String _filename) {
        this._filename = _filename;
    }

    public void setInitialPath(String _initialPath) {
        this._initialPath = _initialPath;
    }

    public void setDestinationPath(String _destinationPath) {
        this._destinationPath = _destinationPath;
    }

    public void setLastBackup(LocalDateTime _lastBackup) {
        this._lastBackup = _lastBackup;
    }

    public void setAutoBackup(Boolean _autoBackup) {
        this._autoBackup = _autoBackup;
    }

    public void setNextDateBackup(LocalDateTime _nextDateBackup) {
        this._nextDateBackup = _nextDateBackup;
    }

    public void setDaysIntervalBackup(Integer _daysIntervalBackup) {
        this._daysIntervalBackup = _daysIntervalBackup;
    }
}