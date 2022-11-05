package application;

import java.util.Timer;
import java.util.TimerTask;

class CheckUpdateAutoBackup {

	private static boolean timer_running;
	private static Timer timer = new Timer();
	private static TimerTask task;
	
	CheckUpdateAutoBackup (FrameAutoBackup frame) {
	    
		timer = new Timer();
		
		task = new TimerTask() {
			String path1 = frame.start_path.getText();
            String path2 = frame.destination_path.getText();
            
            
			public void run() { // si aggiorna ogni secondo
				String name = AutoBackupProgram.current_file_opened;
				if (path1.equals(frame.start_path.getText()) == false) frame.setCurrentFileName(name+"*");
				else if (path2.equals(frame.destination_path.getText()) == false) frame.setCurrentFileName(name+"*");
				
				else frame.setCurrentFileName(name);
			}
		};
		
		timer_running = false;
	}
	
	public void startTimer() {
		timer.scheduleAtFixedRate(task, 1000, 100); // aggiornamento ogni 100 ms
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
