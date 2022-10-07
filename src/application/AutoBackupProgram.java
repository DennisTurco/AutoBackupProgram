package application;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;


@SuppressWarnings("serial")
class AutoBackupProgram extends JFrame{
	static String current_file_opened;
	static String next_date_backup;
	static Integer days_interval_backup;
	
	private static TimerAutoBackup timer;
	private static JSONAutoBackup JSON;
	
	String source;
	String target;
	
	static int NORMAL_WRITE = 0;
	static int MODIFY_INFO = 1;
	
	public AutoBackupProgram() { 
		
		JSON = new JSONAutoBackup();
		
		//set text values
		setTextValues();
		
		//auto backup control
		//autoBackupControl();
	}
	
	// JMenuItem function
	public void Clear() { 
		System.out.println("Event --> clear");
		FrameAutoBackup.start_path.setText("");
		FrameAutoBackup.destination_path.setText("");
		FrameAutoBackup.message.setText("");
		FrameAutoBackup.last_backup.setText("");
	}
	
	// JMenuItem function
	public void Exit() {
		System.out.println("Event --> exit");
		System.exit(EXIT_ON_CLOSE);
	}
	
	// JMenuItem function //TODO: add
	public void Help() {
		System.out.println("Event --> help");
		
		ImageIcon icon = new ImageIcon(".//res//info.png");
		JOptionPane.showMessageDialog(null, 
				"....for questions please contact the author: -> dennisturco.github.io",
				"Help",
				JOptionPane.PLAIN_MESSAGE, icon); //messaggio popup
	}
	
	// JMenuItem function
	public void Credits() {
		System.out.println("Event --> credits");
		ImageIcon icon = new ImageIcon(".//res//author_logo.png");
		JOptionPane.showMessageDialog(null, 
				"<html><u>2022 © Dennis Turco</u></html>\r\n"
				+ "<html><i>Author</i>: Dennis Turco</html>\r\n"
				+ "<html><i>GitHub</i>: <a href='https://github.com/DennisTurco'>https://github.com/DennisTurco </a></html>\r\n"
				+ "<html><i>Web Site</i>: <a href='https://dennisturco.github.io/'>https://dennisturco.github.io/ </a></html>",
				"Credits",
				JOptionPane.PLAIN_MESSAGE, icon); //messaggio popup
	}
	
	// JMenuItem function
	public void Share() {
		System.out.println("Event --> share");
		
		//messaggio pop-up
		JOptionPane.showMessageDialog(null, "Share link copied to clipboard!");
        
		//copio nella clipboard il link
        String testString = "https://github.com/DennisTurco/Minesweeper-Game"; //TODO: mettere il link corretto
        StringSelection stringSelectionObj = new StringSelection(testString);
        Clipboard clipboardObj = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboardObj.setContents(stringSelectionObj, null);
	}
	
	// JMenuItem function
	public void viewHistory() throws Exception {
		System.out.println("Event --> history");
		Runtime.getRuntime().exec("notepad.exe .//res//log_file");
	}
	
	// JMenuItem function
	public void NewFile() {
		System.out.println("Event --> new file");
		
		// pulisco tutto
		Clear();
		
		// di base l'uoto enable è disattivato
		FrameAutoBackup.btn_automatic_backup.setText("Auto Backup (Disabled)");
		
		// tolgo il file attuale aperto
		current_file_opened = null;
	}
	
	// JMenuItem function
	public void RemoveSingleFile() {
		System.out.println("Event --> remove file");
		
		// ottengo il nome del file selezionato
		String filename = getFile(".//res//saves"); 
		
		//elimino
		File file = new File(".//res//saves//" + filename);
		if (file.delete()) System.out.println("Event --> file deleted: " + file.getName());
		else System.out.println("Failed to delete the file.");		
	}
	
	// JMenuItem function
	public void Open() {
		System.out.println("Event --> open");
		
		// ottengo il nome del file selezionato
		current_file_opened = getFile(".//res//saves"); 
		
		// leggo da file json
		JSON.ReadJSONFile(current_file_opened, ".//res//saves//");
		
		// aggiorno info.json
		JSON.WriteJSONFile("info.json", ".//res//");
	}
	
	// JMenuItem function
	public void SaveWithName() {
		System.out.println("Event --> save with name");
		
		//file
		current_file_opened = JOptionPane.showInputDialog(null, "Name of the file"); //messaggio popup
		if (current_file_opened == null) return;
		
		current_file_opened += ".json";
		
		JSON.WriteJSONFile(current_file_opened, ".//res//saves//");
	}
	
	// JMenuItem function
	public void Save() { 
		System.out.println("Event --> save");
		
		if (current_file_opened == null) {
			SaveWithName();
		}
		
		File file = new File(".//res//saves//" + current_file_opened); // controllo se il file esiste
		if(file.exists() && !file.isDirectory()) { 
			JSON.WriteJSONFile(current_file_opened, ".//res//saves//");
		}
	}
	
	// JMenuItem function
	public void BackupList() {
		//TODO: add
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
		name1 = path1.substring(path1.length()-1, path1.length()-1);
		
		for(int i=path1.length()-1; i>=0; i--) {
			if(path1.charAt(i) != temp.charAt(0)) name1 = path1.charAt(i) + name1;
			else break;
		}

		path2 = path2 + "\\" + name1 + " (Backup " + date + ")";
		
		//------------------------------COPY THE FILE OR DIRECTORY------------------------------
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
		LocalDateTime now2 = LocalDateTime.now();
		date = dtf2.format(now2);

        System.out.println("date backup: " + date);
    	setStringToText(); //chiamata alla funzione
        try {
			copyDirectoryFileVisitor(path1, path2); // funzione per copiare   
		} catch (IOException e) {
			System.out.println("Exception --> " + e);
		} 
		
        JOptionPane.showMessageDialog(null, "Files Copied!\nFrom: " + FrameAutoBackup.start_path.getText() + "\nTo: " + FrameAutoBackup.destination_path.getText(), "AutoBackupProgram", 1);
        FrameAutoBackup.message.setForeground(Color.GREEN);
        
        JSON.WriteJSONFile("info.json", ".//res//");
        
        //attivo il timer di n secondi
		timer = new TimerAutoBackup();
		timer.startTimer(); 
        FrameAutoBackup.message.setText("Files Copied!");
        FrameAutoBackup.message.setVisible(true);
    }	

	// button function
	public void AutomaticBackup() {
		System.out.println("Event --> automatic backup");
		
		JSON.ReadJSONFile("info.json", ".//res//");
		
		if(checkInputCorrect() == false) return;  //controllo errori tramite funzione 
		
		if(FrameAutoBackup.btn_automatic_backup.getText().equals("Auto Backup (Disabled)")) {
			// se il file non è stato salvato bisogna salvarlo prima di settare l'auto backup
			if (current_file_opened == null) SaveWithName();
			if (current_file_opened == null) return;
			
			// message
			days_interval_backup = Integer.parseInt(JOptionPane.showInputDialog(null, "Every how many days run the auto backup?")); //messaggio popup
			if (days_interval_backup == JOptionPane.CANCEL_OPTION) return;
			
			FrameAutoBackup.btn_automatic_backup.setText("Auto Backup (Enabled)");
			System.out.println("Event --> Auto Backup setted to Enabled");
			
			JOptionPane.showMessageDialog(null, "Auto Backup has been activated\n\tFrom: " + FrameAutoBackup.start_path.getText() + "\n\tTo: " + FrameAutoBackup.destination_path.getText() + "\nIs setted every " + days_interval_backup + " days", "AutoBackupProgram", 1);
		}
		
		else if(FrameAutoBackup.btn_automatic_backup.getText().equals("Auto Backup (Enabled)")){
			FrameAutoBackup.btn_automatic_backup.setText("Auto Backup (Disabled)");
			System.out.println("Event --> Auto Backup setted to Disabled");
		}

		// salvo nel JSON
		JSON.WriteJSONFile(current_file_opened, ".//res//saves//");
	}
	
	/*public void autoBackupControl() {
		if(FrameAutoBackup.btn_automatic_backup.getText() == "Auto Backup (E)" && checkInputCorrect() == true) {
			
			//get current date
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			
			String current_date = dtf.format(now);
			long current_date_in_seconds;
			try {
				
				current_date_in_seconds = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(current_date).getTime() / 1000;
				
				//TODO:get
				//next_date_backup 
				
				if(current_date_in_seconds >= next_date_backup) {
					SingleBackup();
				}	
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			} 
			
			
		}
	}*/
	
	private String getFile(String directory_path) {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Choose a file to open: ");
		jfc.setCurrentDirectory(new java.io.File(directory_path));
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int returnValue = jfc.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (jfc.getSelectedFile().isFile()) {
				System.out.println("You selected the file: " + jfc.getSelectedFile());
				
				//	dal percorso assoluto ottengo il nome 
				int counter = 0;
				for (long i=jfc.getSelectedFile().toString().length()-1; i>=0; i--) {
					if (jfc.getSelectedFile().toString().charAt((int) i) != '\\') counter++;
					else break;
				}
				
				return jfc.getSelectedFile().toString().substring(jfc.getSelectedFile().toString().length()-counter);
			}
		}
		return null;
	}
	
	public void setStringToText() {
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
			LocalDateTime now = LocalDateTime.now();
			String last_date = dtf.format(now);
			FrameAutoBackup.last_backup.setText("last backup: " + last_date);
			
			//next_date_backup = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(last_date).getTime() / 1000;
			JSON.WriteJSONFile("info.json", ".//res//");
		} catch(Exception ex) {
			System.out.println("Exception --> " + ex);
		}
	}
	
	public void setTextValues() {
		JSON.ReadJSONFile("info.json", ".//res//");
		
		if(days_interval_backup != null) {
			FrameAutoBackup.btn_automatic_backup.setText("Auto Backup (Enabled)");
		}
		else {
			FrameAutoBackup.btn_automatic_backup.setText("Auto Backup (Disabled)");
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
			
			//attivo il timer di n secondi
			timer = new TimerAutoBackup();
			timer.startTimer(); 
			
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
			
			//attivo il timer di n secondi
			timer = new TimerAutoBackup();
			timer.startTimer();
			
			return false;
		}
		
		return true;
	}
	
    public void copyDirectoryFileVisitor(String source, String target) throws IOException { //TODO: fix here
    	
    	//TODO: funziona solo per il path specifico, non i sottopath
    	int files_number = new File(source).list().length;  // ottengo il numero di files e directories sotto il path specifico 
    	System.out.println(files_number);
		
		
		//conto il numero di file nella directory e sotto-directory
		int file_number = countFilesInDirectory(new File(source));
		System.out.println(file_number);
		
		TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(source, target, file_number);
        Files.walkFileTree(Paths.get(source), fileVisitor);
    }
    
    private int countFilesInDirectory(File directory) {
    	int count = 0;
    	
    	for (File file : directory.listFiles()) {
    		if (file.isFile()) {
    			count++;
    		}
    	    	
	    	if (file.isDirectory()) {
	    		count += countFilesInDirectory(file);
	    	}
    	}
    	
    	return count;
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
