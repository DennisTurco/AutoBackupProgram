package com.mycompany.autobackupprogram;

import java.util.Timer;
import java.util.TimerTask;

class CheckUpdateAutoBackup {
    private boolean timerRunning;
    private Timer timer;
    private TimerTask task;
    private final AutoBackupGUI autoBackupFrame;

    public CheckUpdateAutoBackup(AutoBackupGUI autoBackupFrame) {
        this.autoBackupFrame = autoBackupFrame;
        this.timerRunning = false;
    }

    public synchronized void startTimer() {
        if (timerRunning) {
            stopTimer();
        }

        timer = new Timer();
        task = new TimerTask() {
            String initialStartPath = autoBackupFrame.GetStartPathField();
            String initialDestinationPath = autoBackupFrame.GetDestinationPathField();

            @Override
            public void run() {
                String currentStartPath = autoBackupFrame.GetStartPathField();
                String currentDestinationPath = autoBackupFrame.GetDestinationPathField();
                String currentFileName = AutoBackupGUI.currentFileOpened;

                if (!initialStartPath.equals(currentStartPath) || !initialDestinationPath.equals(currentDestinationPath)) {
                    autoBackupFrame.setCurrentFileName(currentFileName + "*");
                } else {
                    autoBackupFrame.setCurrentFileName(currentFileName);
                }
            }
        };

        timer.scheduleAtFixedRate(task, 1000, 100); // update every 100 ms
        timerRunning = true;
    }

    public synchronized void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timerRunning = false;
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }
}
