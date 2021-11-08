package application;

import java.io.IOException;
import javax.swing.JFrame;

class MainAutoBackupProgram {

	public static void main(String[] args) throws IOException {
		
		boolean verify = verifyAutoBackup();
		
		//if(verify == false) {
			AutoBackupProgram nb = new AutoBackupProgram();
			nb.setVisible(true);
			nb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//}
		
		//else {
			
		//}
		
	}

	
	public static boolean verifyAutoBackup() {
		//code to verify
		return false;
	}
}