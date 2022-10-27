:: Author: Dennis Turco 2022

:: batch file for Windows.
:: this is to move "CheckerBackup.exe.lnk" into the "Startup" folder 
:: during the installation of AutoBackup program.


if exist ./CheckerBackup.exe.lnk (
  move ./CheckerBackup.exe.lnk "%USERPROFILE%\AppData\Roaming\Microsoft\Windows\Start Menu\Programs\Startup"
)

if not exist ./CheckerBackup.exe.lnk (
  echo "Error moving file to "Startup" folder, please retry installation..."
)