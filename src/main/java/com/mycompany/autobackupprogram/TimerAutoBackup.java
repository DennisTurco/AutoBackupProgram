package com.mycompany.autobackupprogram;

import static com.mycompany.autobackupprogram.BackupManagerGUI.OpenExceptionMessage;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

class TimerAutoBackup {

    private boolean timerRunning;
    private Timer timer;
    private float timeCounter;
    
    public TimerAutoBackup() {
        this.timerRunning = false;
        this.timeCounter = 0;
    }

    public synchronized void startTimer() {
        if (timerRunning) {
            stopTimer();
        }

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeCounter += 1; // increment by one second
                if (timeCounter >= 2) {
                    stopTimer();
                }
            }
        };
        
        try {
            timer.scheduleAtFixedRate(task, 1000, 1000); // update every second
            timerRunning = true;
        } catch (Exception ex) {
            System.err.println("Exception --> " + ex);
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        }
        
    }

    public synchronized void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        
        try {
            timeCounter = 0; // reset the counter
            timerRunning = false;
        } catch (Exception ex) {
            System.err.println("Exception --> " + ex);
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        }
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }
}
