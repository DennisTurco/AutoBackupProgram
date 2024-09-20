package com.mycompany.autobackupprogram;

public class Backup {

    private String _filename;
    private String _initialPath;
    private String _destinationPath;
    private String _lastBackup;
    private Boolean _autoBackup;
    private String _nextDateBackup;
    private String _daysIntervalBackup;
    
    public Backup(String filename, String initialPath, String destinationPath, String lastBackup, Boolean autoBackup, String nextDateBackup, String daysIntervalBackup) {
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

    public String getFilename() {
        return _filename;
    }

    public String getInitialPath() {
        return _initialPath;
    }

    public String getDestinationPath() {
        return _destinationPath;
    }

    public String getLastBackup() {
        return _lastBackup;
    }

    public boolean isAutoBackup() {
        return _autoBackup;
    }

    public String getNextDateBackup() {
        return _nextDateBackup;
    }

    public String getDaysIntervalBackup() {
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

    public void setLastBackup(String _lastBackup) {
        this._lastBackup = _lastBackup;
    }

    public void setAutoBackup(Boolean _autoBackup) {
        this._autoBackup = _autoBackup;
    }

    public void setNextDateBackup(String _nextDateBackup) {
        this._nextDateBackup = _nextDateBackup;
    }

    public void setDaysIntervalBackup(String _daysIntervalBackup) {
        this._daysIntervalBackup = _daysIntervalBackup;
    }
}