package application;

import java.util.Timer;
import java.util.TimerTask;

class CheckUpdateAutoBackup {

	private static boolean timer_running;
	private static Timer timer = new Timer();
	private static TimerTask task;
	
	CheckUpdateAutoBackup (FrameAutoBackup frameAutoBackup) {
	    
		timer = new Timer();
		
		task = new TimerTask() {
			String path1 = frameAutoBackup.start_path.getText();
            String path2 = frameAutoBackup.destination_path.getText();

			// update every second
			public void run() { 
				String name = AutoBackupProgram.current_file_opened;
				if (path1.equals(frameAutoBackup.start_path.getText()) == false) 
					frameAutoBackup.setCurrentFileName(name+"*");
				else if 
					(path2.equals(frameAutoBackup.destination_path.getText()) == false) frameAutoBackup.setCurrentFileName(name+"*");
				else 
					frameAutoBackup.setCurrentFileName(name);
			}
		};
		
		timer_running = false;
	}
	
	public void startTimer() {
		timer.scheduleAtFixedRate(task, 1000, 100); // update every 100 ms
		timer_running = true;
	}
	
	public void stopTimer() {
		timer.cancel();
		timer_running = false;
	}
	
	public boolean isTimeRunning() {
		return timer_running;
	}
}
