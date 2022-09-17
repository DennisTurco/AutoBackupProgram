package application;

import java.util.Timer;
import java.util.TimerTask;

class TimerAutoBackup {
	
	static int VISIBILITY_ERROR = 1;
	static int VISIBILITY_LOADING = 2;
	
	private static boolean timer_running;
	private static Timer timer = new Timer();
	private static TimerTask task;
	private static float TIMER = 0;
	
	TimerAutoBackup (int visibility) {
		
		timer = new Timer();
		
		if (visibility == VISIBILITY_LOADING) {
			task = new TimerTask() {
				public void run() {
					TIMER += 1; //incremento di un millisecondo
					FrameAutoBackup.message.setVisible(true);
					if (TIMER == 0 || TIMER == 4) { FrameAutoBackup.message.setText("LOADING"); TIMER = 0;}
					if (TIMER == 1) FrameAutoBackup.message.setText("LOADING.");
					if (TIMER == 2) FrameAutoBackup.message.setText("LOADING..");
					if (TIMER == 3) FrameAutoBackup.message.setText("LOADING...");
				}
			};
		}
		
		if (visibility == VISIBILITY_ERROR) {
			task = new TimerTask() {
				public void run() {
					TIMER += 1; //incremento di un millisecondo
					if (TIMER >= 2) {
						stopTimer();
					}
				}
			};
		}
		
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
