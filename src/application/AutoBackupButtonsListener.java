package application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AutoBackupButtonsListener implements ActionListener{
	private FrameAutoBackup rp;
	
	public AutoBackupButtonsListener() {}
	
	public AutoBackupButtonsListener(FrameAutoBackup v) {
		this.rp = v;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton)e.getSource();
		
		if(b.getText().equals("Single Backup")) {
			rp.SingleBackup();
		}
		
		else if(b.getText().equals("Auto Backup (Enabled)") || b.getText().equals("Auto Backup (Disabled)")) {
			rp.SetSelected();	
		}
		
		else if(b.getText().equals(" ")) {
			rp.SelectionStart();
		}
		
		else if(b.getText().equals(".")) {
			rp.SelectionDestination();
		}
		
		else;
	}
}