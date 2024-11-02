@echo off
set "EXE_PATH=%~dp0BackupManager.exe --background"
reg add "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "BackupManager" /t REG_SZ /d "%EXE_PATH%" /f
