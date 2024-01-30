package application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AutoBackupMenusListener implements ActionListener{
	
	public AutoBackupMenusListener() {}
	
	public void actionPerformed(ActionEvent e) {	
		String command = e.getActionCommand();
		
		if (command.equals("Open"));
		else if (command.equals("Save"));
		else if (command.equals("Save With Name"));
		else if (command.equals("List Of Backup"));
		else if (command.equals("History"));
		else if (command.equals("Share"));
		else if (command.equals("Help"));
		else if (command.equals("Quit")) AutoBackupProgram.Exit();		
	}
}