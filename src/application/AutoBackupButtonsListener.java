package application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AutoBackupButtonsListener implements ActionListener{
	private FrameAutoBackup frameAutoBackup;
	
	public AutoBackupButtonsListener() {}
	
	public AutoBackupButtonsListener(FrameAutoBackup frameAutoBackup) {
		this.frameAutoBackup = frameAutoBackup;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton)e.getSource();
		
		if(b.getText().equals("Single Backup")) {
			frameAutoBackup.SingleBackup();
		}
		
		else if(b.getText().equals("Auto Backup (Enabled)") || b.getText().equals("Auto Backup (Disabled)")) {
			//! TODO: fixhere
			// frameAutoBackup.SetSelected();	
		}
		
		else if(b.getText().equals(" ")) {
			frameAutoBackup.SelectionStart();
		}
		
		else if(b.getText().equals(".")) {
			frameAutoBackup.SelectionDestination();
		}
		
		else;
	}
}