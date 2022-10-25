package application;

import java.util.Timer;
import java.util.TimerTask;

class TimerAutoBackup {
	
	private static boolean timer_running;
	private static Timer timer = new Timer();
	private static TimerTask task;
	private static float TIMER = 0;
	
	TimerAutoBackup () {
		
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
		timer.scheduleAtFixedRate(task, 1000, 1000);
		timer_running = true;
	}
	
	public void stopTimer() {
		FrameAutoBackup.message.setVisible(false);
		timer.cancel();
		timer_running = false;
	}
	
	// GETTER & SETTER
	public boolean isTimeRunning() {
		return timer_running;
	}
}
