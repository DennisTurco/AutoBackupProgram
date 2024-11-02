![logo](src/main/resources/res/img/logo.ico)

# Backup Manager

**Backup Manager** is a user-friendly program with an intuitive graphical interface, designed to simplify and automate the backup of folders and subfolders. Users can configure a custom time interval between automatic backups, setting the desired number of days between each operation. Additionally, manual backups can be performed at any time, providing maximum flexibility.

Each backup is carefully saved, and the program maintains a detailed log of all completed operations. Users can also view, manage, and edit the details of each backup, ensuring complete control and customization over saved data. This tool is an ideal solution for efficiently and securely protecting files, minimizing the risk of data loss.

## Startup Logic
```mermaid
graph TD
  n1(((PC Startup))) --> n2(Start Background Service)
  n2 --> n3(Start TrayIcon)
  n2 --> n4(Periodic Auto Backup Check)
  n3 -->|click| n5(Start GUI)
  n3 -->|exit| n6(Shutdown Backup Service)
```

## Important Notes:
* this program is executed at the pc startup, if you disable it, it won't do any automatic backup


## Screenshots




## Licence

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)

## Authors

- [@DennisTurco](https://www.github.com/DennisTurco)


## Support

For support, email: dennisturco@gmail.com