package application;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

@SuppressWarnings("serial") //per togliere il warning
class AutoBackupProgram extends JFrame{
	private static Timer timer;
	
	public AutoBackupProgram() {  //costruttore senza parametri
		
		//-------------------------------------------SET TEXT VALUES-------------------------------------------
		setTextValues();
		
		//---------------------------------------AUTO BACKUP COLTROL---------------------------------------
		autoBackupControl();
		
	}
	
	// JMenuItem function
	public void Clear() { 
		System.out.println("Event --> clear");
		FrameAutoBackup.start_path.setText("");
		FrameAutoBackup.destination_path.setText("");
		FrameAutoBackup.message.setText("");
	}
	
	// JMenuItem function
	public void Exit() {
		System.out.println("Event --> exit");
		System.exit(EXIT_ON_CLOSE);
	}
	
	// JMenuItem function
	public void viewHistory() throws Exception {
		System.out.println("Event --> history");
		Runtime.getRuntime().exec("notepad.exe res//log_file");
	}
	
	// button function
	public void SingleBackup() {
		System.out.println("Event --> single backup");
		
		String temp = "\\";
		
		//------------------------------INPUT CONTROL ERRORS------------------------------
		if(checkInputCorrect() == false) return;  //controllo errori tramite funzione
		
		//------------------------------SET ALL THE VARIABLES------------------------------
		String path1 = FrameAutoBackup.start_path.getText();
		String path2 = FrameAutoBackup.destination_path.getText();
		String name1; //nome cartella/file iniziale
		String date;
		
		//------------------------------TO GET THE CURRENT DATE------------------------------
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDateTime now = LocalDateTime.now();
		
		//------------------------------SET ALL THE STRINGS------------------------------
		date = dtf.format(now);
		int grandezza = path1.length()-1;
		name1 = path1.substring(grandezza, grandezza);
		
		for(int i=path1.length()-1; i>=0; i--) {
			if(path1.charAt(i) != temp.charAt(0)) name1 = path1.charAt(i) + name1;
			else break;
		}

		path2 = path2 + "\\" + name1 + " (Backup " + date + ")";
		
		//------------------------------COPY THE FILE OR DIRECTORY------------------------------
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
		LocalDateTime now2 = LocalDateTime.now();
		date = dtf2.format(now2);
		try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("res//log_file", true));
            System.out.println("date backup: " + date);
            bw.write("\ndate backup: " + date + "\n");
            bw.close();
        	setStringToText(); //chiamata alla funzione
            copyDirectoryFileVisitor(path1, path2); // funzione per copiare   
            
        } catch (IOException e) {
            System.out.println("Exception --> " + e);
            return;
        }
        
		try {
			BufferedReader br = new BufferedReader(new FileReader("res//info"));
			System.out.println("Done");
	        JOptionPane.showMessageDialog(null, "Files Copied!\nFrom: " + br.readLine() + "\nTo: " + br.readLine(), "AutoBackupProgram", 1);
	        FrameAutoBackup.message.setForeground(Color.GREEN);
	        FrameAutoBackup.message.setText("Files Copied!");
	        FrameAutoBackup.message.setVisible(true);
	        br.close();
		} catch (Exception e) {
			System.out.println("Exception --> " + e);
		}
    	
    }	

	// button function
	void AutomaticBackup() {
		System.out.println("Event --> automatic backup");
		
		if(checkInputCorrect() == false) return;  //controllo errori tramite funzione
		
		if(FrameAutoBackup.btn2.getText().equals("Auto Backup (Enabled)")) {
			SingleBackup();
		}
	}
	
	public void autoBackupControl() {
		if(FrameAutoBackup.btn2.getText() == "Auto Backup (E)" && checkInputCorrect() == true) {
			
			//get current date
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			
			try {
				BufferedReader br = new BufferedReader (new FileReader("res//info"));
				String last_date = null;
				String current_date = dtf.format(now);
				for(int i=0; i<3; i++) { //deve essere eseguito 3 volte perchè devo scorrere il file 3 volte (3 linee sul file info)
					last_date = br.readLine();
				}
				last_date = last_date.substring(13, 32); //dalla stringa del file info voglio solo la data
				
				long current_date_in_seconds = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(current_date).getTime() / 1000; 
				long last_date_in_seconds = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(last_date).getTime() / 1000;
				
				if(current_date_in_seconds - last_date_in_seconds >= 2592000) { //2592000 sono 30 giorni
					System.out.println("Differenza " + (current_date_in_seconds - last_date_in_seconds));
					SingleBackup();
				}
				br.close();
			} catch (Exception e) {
				System.out.println("Exception --> " + e);
			}
		}
	}
	
	void setStringToText() {
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
			LocalDateTime now = LocalDateTime.now();
			String last_date = dtf.format(now);
			FrameAutoBackup.last_backup.setText("last backup: " + last_date);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("res//info"));
			bw.write(FrameAutoBackup.start_path.getText());
			bw.write("\n");
			bw.write(FrameAutoBackup.destination_path.getText());
			bw.write("\n");
			bw.write(FrameAutoBackup.last_backup.getText());
			bw.write("\n");
			
			long last_date_in_seconds = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(last_date).getTime() / 1000;
			bw.write(""+last_date_in_seconds);
			
			bw.close();
		} catch(Exception ex) {
			System.out.println("Exception --> " + ex);
		}
	}
	
	public void setTextValues() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("res//info"));
			FrameAutoBackup.start_path.setText(br.readLine());
			FrameAutoBackup.destination_path.setText(br.readLine());
			FrameAutoBackup.last_backup.setText(br.readLine());
			br.close();
		}
		catch(Exception ex){
			System.out.println("Exception --> " + ex);
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("res//auto_generation"));
			if(br.readLine().equals("true")) {
				FrameAutoBackup.btn2.setText("Auto Backup (Enabled)");
			}
			else {
				FrameAutoBackup.btn2.setText("Auto Backup (Disabled)");
			}
			br.close();
		}catch(Exception ex) {
			System.out.println("Exception --> " + ex);
		}
	}

	public void SetSelected(){
		if(checkInputCorrect() == false) return;
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("res//auto_generation"));
			BufferedReader rw = new BufferedReader(new FileReader("res//info"));
			if(FrameAutoBackup.btn2.getText().equals("Auto Backup (Enabled)")) {
				bw.write("false");
				FrameAutoBackup.btn2.setText("Auto Backup (Disabled)");
				System.out.println("Event --> Auto Backup setted to Disabled");
			}
			else if(FrameAutoBackup.btn2.getText().equals("Auto Backup (Disabled)")){
				bw.write("true");
				FrameAutoBackup.btn2.setText("Auto Backup (Enabled)");
				System.out.println("Event --> Auto Backup setted to Enabled");
				JOptionPane.showMessageDialog(null, "Auto Backup has been activated\n\tFrom: " + rw.readLine() + "\n\tTo: " + rw.readLine() + "\nFor Default is setted every month", "AutoBackupProgram", 1);
				AutomaticBackup();
			}
			bw.close();
			rw.close();
		}catch(Exception ex) {
			System.out.println("Exception --> " + ex);
		}
		
	}
	
	public boolean checkInputCorrect() {
		String temp = "\\";
		
		//check if inputs are null
		if(FrameAutoBackup.start_path.getText().length() == 0 || FrameAutoBackup.destination_path.getText().length() == 0) {
			System.out.println("Error --> Input Missing!");
			FrameAutoBackup.message.setForeground(Color.RED);
			FrameAutoBackup.message.setText("Input Missing!");
			FrameAutoBackup.message.setVisible(true);
			return false;
		}
		
		//check if there is a \ char
		boolean check1 = false;
		boolean check2 = false;
		for(int i=0; i<FrameAutoBackup.start_path.getText().length(); i++) {
			if(FrameAutoBackup.start_path.getText().charAt(i) == temp.charAt(0)) check1 = true;
		}
		
		for(int i=0; i<FrameAutoBackup.destination_path.getText().length(); i++) {
			if(FrameAutoBackup.destination_path.getText().charAt(i) == temp.charAt(0)) check2 = true;
		}
		
		if(check1 != true || check2 != true) {
			System.out.println("Error --> Input Error!");
			FrameAutoBackup.message.setForeground(Color.RED);
			FrameAutoBackup.message.setText("Input Error!");
			FrameAutoBackup.message.setVisible(true);
			return false;
		}
		
		return true;
	}
	
    public static void copyDirectoryFileVisitor(String source, String target) throws IOException {
    	timer.start();
        TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(source, target);
        Files.walkFileTree(Paths.get(source), fileVisitor);
        timer.stop();
    }
    
    
    public void SelectionStart() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Choose a directory to save your file: ");
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		int returnValue = jfc.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (jfc.getSelectedFile().isDirectory()) {
				System.out.println("You selected the directory: " + jfc.getSelectedFile());
				FrameAutoBackup.start_path.setText(jfc.getSelectedFile().toString());
			}
		}

	}
    
    public void SelectionDestination() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Choose a directory to save your file: ");
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		int returnValue = jfc.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (jfc.getSelectedFile().isDirectory()) {
				System.out.println("You selected the directory: " + jfc.getSelectedFile());
				FrameAutoBackup.destination_path.setText(jfc.getSelectedFile().toString());
			}
		}

	}
    
}
