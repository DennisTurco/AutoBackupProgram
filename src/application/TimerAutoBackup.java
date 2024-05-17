package application;

import java.util.Timer;
import java.util.TimerTask;

class TimerAutoBackup {
	
	private boolean timer_running;
	private Timer timer = new Timer();
	private TimerTask task;
	private float TIMER = 0;
	private FrameAutoBackup frameAutoBackup;
	
	TimerAutoBackup (FrameAutoBackup frameAutoBackup) {
	    this.frameAutoBackup = frameAutoBackup;
	    
		timer = new Timer();
		
		task = new TimerTask() {
			public void run() {
				TIMER += 1; //incremento di un millisecondo
				if (TIMER >= 2) {
					stopTimer();
				}
			}
		};
		
		timer_running = false;
	}
	
	public void startTimer() {
	    frameAutoBackup.message.setVisible(true);
		timer.scheduleAtFixedRate(task, 1000, 1000);
		timer_running = true;
	}
	
	public void stopTimer() {
	    frameAutoBackup.message.setVisible(false);
		timer.cancel();
		timer_running = false;
	}
	
	public boolean isTimeRunning() {
		return timer_running;
	}
}
