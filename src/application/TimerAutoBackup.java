package application;

import java.util.Timer;
import java.util.TimerTask;

class TimerAutoBackup {
	
	private static boolean timer_running;
	private static Timer timer;
	private static TimerTask task;
	
	TimerAutoBackup () {
		timer = new Timer();
		task = new TimerTask() {
			public void run() {
				
			}
		};
		
		timer_running = false;
	}
	
	public void startTimer() {
		timer.scheduleAtFixedRate(task, 1000, 1000);
		timer_running = true;
	}
	
	public void stopTimer() {
		timer.cancel();
		timer_running = false;
	}
	
	// GETTER & SETTER
	public boolean isTimeRunning() {
		return timer_running;
	}
	
	
	
}
