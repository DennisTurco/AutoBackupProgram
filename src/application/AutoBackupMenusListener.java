package application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AutoBackupMenusListener implements ActionListener{
	private FrameAutoBackup rp;
	
	public AutoBackupMenusListener() {}
	
	public AutoBackupMenusListener(FrameAutoBackup v) {
		this.rp = v;
	}
	
	public void actionPerformed(ActionEvent e) {	
		String command = e.getActionCommand();
		
		if (command.equals("Open"));
		else if (command.equals("Save"));
		else if (command.equals("Save With Name"));
		else if (command.equals("List Of Backup"));
		else if (command.equals("History"));
		else if (command.equals("Share"));
		else if (command.equals("Help"));
		else if (command.equals("Quit")) rp.Exit();
		else;
		
	}
}