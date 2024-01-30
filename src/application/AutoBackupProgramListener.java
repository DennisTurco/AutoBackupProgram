package application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AutoBackupProgramListener implements ActionListener{
	
	public AutoBackupProgramListener() {}
	
	
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton)e.getSource();
		
		if(b.getText().equals("Clear")) {
			AutoBackupProgram.Clear(); //chiamata funzione
		}
		
		else if(b.getText().equals("Single Backup")) {
			AutoBackupProgram.SingleBackup();
		}
		
		else if(b.getText().equals("Auto Backup (Enabled)") || b.getText().equals("Auto Backup (Disabled)")) {
			//! TODO: fixhere
			// frameAutoBackup.SetSelected();	
		}
		
		else if (b.getText().equals("History")) {
			try {
				AutoBackupProgram.viewHistory();
			} catch (Exception e1) {
				System.out.println("Exception --> " + e1);
			}
		}
		
		else if(b.getText().equals(" ")) {
			AutoBackupProgram.SelectionStart();
		}
		
		else if(b.getText().equals(".")) {
			AutoBackupProgram.SelectionDestination();
		}
		
		else {
			AutoBackupProgram.Exit(); //chiamata funzione
		}
	}
}