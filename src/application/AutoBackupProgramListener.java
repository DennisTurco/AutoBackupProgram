package application;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AutoBackupProgramListener implements ActionListener{
	private AutoBackupProgram rp;
	
	public AutoBackupProgramListener() {}
	
	public AutoBackupProgramListener(AutoBackupProgram v) {
		this.rp = v;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton)e.getSource();
		
		if(b.getText().equals("Clear")) {
			rp.Clear(); //chiamata funzione
		}
		
		else if(b.getText().equals("Single Backup")) {
			rp.SingleBackup();
		}
		
		else if(b.getText().equals("Auto Backup (Enabled)") || b.getText().equals("Auto Backup (Disabled)")) {
			rp.SetSelected();	
		}
		
		else if (b.getText().equals("History")) {
			try {
				rp.viewHistory();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		else if(b.getText().equals(" ")) {
			rp.SelectionStart();
		}
		
		else if(b.getText().equals(".")) {
			rp.SelectionDestination();
		}
		
		else {
			rp.Exit(); //chiamata funzione
		}
	}
}