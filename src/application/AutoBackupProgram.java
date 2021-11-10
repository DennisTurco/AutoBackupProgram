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
	
	JTextField start_path = new JTextField();
	JTextField destination_path = new JTextField();
	JLabel message = new JLabel("");
	JLabel last_backup = new JLabel();
	JButton btn2 = new JButton();
	boolean copied;
	
	public AutoBackupProgram() {  //costruttore senza parametri
		//------------------------------------------- set finestra ------------------------------------------- 
		setTitle("AutoBackupProgram");
		setSize(500, 400);
		setLocation(700, 300);
		setLayout(new BorderLayout());
		setResizable(false);  //in questo modo la finestra non cambia dimensione
		getContentPane().setBackground(new Color(18, 15, 37)); //setta il colore dello sfondo
		
		//-------------------------------------------creazione oggetto actionlistener-------------------------------------------
		AutoBackupProgramListener g = new AutoBackupProgramListener(this);
		
		
		//-------------------------------------------SET TEXT VALUES-------------------------------------------
		try {
			BufferedReader br = new BufferedReader(new FileReader("res//text1"));
			start_path.setText(br.readLine());
			destination_path.setText(br.readLine());
			last_backup.setText(br.readLine());
			br.close();
		}
		catch(Exception ex){
			System.out.println("Exception --> " + ex);
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("res//auto_generation"));
			if(br.readLine().equals("true")) {
				btn2.setText("Auto Backup (Actived)");
			}
			else {
				btn2.setText("Auto Backup (Disabled)");
			}
			br.close();
		}catch(Exception ex) {
			System.out.println("Exception --> " + ex);
		}
		
		//---------------------------------------AUTO BACKUP COLTROL---------------------------------------
		if(btn2.getText() == "Auto Backup (Actived)" && checkInputCorrect() == true) {
			
			//get current date
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			
			try {
				BufferedReader br = new BufferedReader (new FileReader("res//text1"));
				String last_date = null;
				String current_date = dtf.format(now);
				for(int i=0; i<3; i++) { //deve essere eseguito 3 volte perchè devo scorrere il file 3 volte (3 linee sul file text1)
					last_date = br.readLine();
				}
				last_date = last_date.substring(13, 32); //dalla stringa del file text1 voglio solo la data
				
				long current_date_in_seconds = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(current_date).getTime() / 1000; 
				long last_date_in_seconds = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(last_date).getTime() / 1000;
				
				if(current_date_in_seconds - last_date_in_seconds >= 86400) { //2592000 sono 30 giorni
					System.out.println("Differenza " + (current_date_in_seconds - last_date_in_seconds));
					SingleBackup();
				}
				br.close();
			} catch (Exception e) {
				System.out.println("Exception --> " + e);
			}
		}
		
		
		//-------------------------------------------set icon-------------------------------------------
		ImageIcon image = new ImageIcon("res//logo.png"); //crea un'icona
		setIconImage(image.getImage());	//cambia l'icona del frame
		
		//-------------------------------------------LATERAL ELEMENTS-------------------------------------------
		JPanel panEast = new JPanel();
		panEast.setLayout(new FlowLayout(1, 50, 50));
		panEast.setBackground(new Color(18, 15, 37));
		this.add(panEast, BorderLayout.EAST);
		
		JPanel panWest = new JPanel();
		panWest.setLayout(new FlowLayout(1, 50, 50));
		panWest.setBackground(new Color(18, 15, 37));
		this.add(panWest, BorderLayout.WEST);
		
		JPanel panNorth = new JPanel();
		panNorth.setLayout(new FlowLayout(1, 20, 20));
		panNorth.setBackground(new Color(18, 15, 37));
		this.add(panNorth, BorderLayout.NORTH);
		
		
		//-------------------------------------------CENTRAL ELEMENTS-------------------------------------------
		JPanel pan1 = new JPanel();
		pan1.setLayout(new GridLayout(10, 0, 0, 7));
		pan1.setBackground(new Color(18, 15, 37));
		this.add(pan1, BorderLayout.CENTER);
		
		JPanel pan3 = new JPanel();
		pan3.setLayout(new FlowLayout(1, 10, 0));
		pan3.setBackground(new Color(18, 15, 37));
		
		JPanel pan4 = new JPanel();
		pan4.setLayout(new FlowLayout(1, 10, 0));
		pan4.setBackground(new Color(18, 15, 37));
		
		//Start Path label
		JLabel path1_text = new JLabel("Start Path");
		path1_text.setForeground(Color.GREEN);
		path1_text.setFont(new Font("Arial", Font.BOLD, 12));
		pan1.add(path1_text);
		
		//add pan3
		pan1.add(pan3);
		
		//start_path TextField
		start_path.setFont(new Font("Arial", Font.BOLD, 10));
		start_path.setForeground(Color.BLACK);
		start_path.setPreferredSize(new Dimension(230, 20));
		pan3.add(start_path);
		
		//Destination Path label
		JLabel path2_text = new JLabel("Destination Path");
		path2_text.setForeground(Color.GREEN);
		path2_text.setFont(new Font("Arial", Font.BOLD, 12));
		pan1.add(path2_text);
		
		//add pan4
		pan1.add(pan4);
		
		//destination_path TextField
		destination_path.setFont(new Font("Arial", Font.BOLD, 10));
		destination_path.setForeground(Color.BLACK);
		destination_path.setPreferredSize(new Dimension(230, 20));
		pan4.add(destination_path);
		
		//Single Backup Button
		JButton btn1 = new JButton("Single Backup");
		btn1.setForeground(Color.BLACK);
		btn1.setFont(new Font("Arial", Font.BOLD, 12));
		pan1.add(btn1);
		btn1.addActionListener(g);
		
		//Automatic Backup Button
		btn2.setForeground(Color.BLACK);
		btn2.setFont(new Font("Arial", Font.BOLD, 12));
		pan1.add(btn2);
		btn2.addActionListener(g);
		
		
		//current_date
		last_backup.setVisible(true);
		last_backup.setFont(new Font("Arial", Font.BOLD, 10));
		last_backup.setForeground(Color.WHITE);
		pan1.add(last_backup);
		
		//message
		message.setVisible(false); //inizialmente non visibile
		message.setFont(new Font("Arial", Font.BOLD, 10));
		message.setHorizontalAlignment(0);
		pan1.add(message);
		
				
		JButton btnChoose1 = new JButton(" ");
		btnChoose1.setFont(new Font("Arial", Font.BOLD, 12));
		btnChoose1.setPreferredSize(new Dimension(33, 20));
		btnChoose1.setIcon(new ImageIcon("res//folder_icon.png"));
		btnChoose1.setOpaque(false);
		btnChoose1.setContentAreaFilled(false);
		btnChoose1.setBorderPainted(false);
		pan3.add(btnChoose1);
		btnChoose1.addActionListener(g);
		
		JButton btnChoose2 = new JButton(".");
		btnChoose2.setFont(new Font("Arial", Font.BOLD, 12));
		btnChoose2.setPreferredSize(new Dimension(33, 20));
		btnChoose2.setIcon(new ImageIcon("res//folder_icon.png"));
		btnChoose2.setOpaque(false);
		btnChoose2.setContentAreaFilled(false);
		btnChoose2.setBorderPainted(false);
		pan4.add(btnChoose2);
		btnChoose2.addActionListener(g);
		
		//-------------------------------------------BOTTOM ELEMENTS-------------------------------------------
		JPanel pan2 = new JPanel();
		pan2.setLayout(new FlowLayout());
		pan2.setBackground(new Color(18, 15, 37));
		this.add(pan2, BorderLayout.SOUTH);
		
		//button exit
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Comic Sans ms", Font.BOLD, 15));
		pan2.add(btnExit);
		btnExit.addActionListener(g);
		
		//button clear
		JButton btnClear = new JButton("Clear");
		btnClear.setFont(new Font("Comic Sans ms", Font.BOLD, 15));
		pan2.add(btnClear);
		btnClear.addActionListener(g);
		
		//hystory backup button
		JButton btnHistory = new JButton("History");
		btnHistory.setFont(new Font("Comic Sans ms", Font.BOLD, 15));
		pan2.add(btnHistory);
		btnHistory.addActionListener(g);
		
		
		//-------------------------------------------TOP ELEMENTS-------------------------------------------
		JLabel author = new JLabel("Author: © DennisTurco 2021");
		author.setFont(new Font("Arial", Font.BOLD, 15));
		author.setHorizontalTextPosition(0);
		panNorth.add(author);		
		
	}
	
	void Clear() {
		System.out.println("Event --> clear");
		start_path.setText("");
		destination_path.setText("");
		message.setText("");
	}
	
	void Exit() {
		System.out.println("Event --> exit");
		System.exit(EXIT_ON_CLOSE);
	}
	
	void viewHistory() throws Exception {
		System.out.println("Event --> history");
		
		Runtime runtime = Runtime.getRuntime();
		
		@SuppressWarnings("unused")  //per togliere il warning
		Process process = runtime.exec("C:\\WINDOWS\\system32\\notepad.exe res//log_file");
	}
	
	void SingleBackup() {
		System.out.println("Event --> single backup");
		
		String temp = "\\";
		
		//------------------------------INPUT CONTROL ERRORS------------------------------
		if(checkInputCorrect() == false) return;  //controllo errori tramite funzione
		
		//------------------------------SET ALL THE VARIABLES------------------------------
		String path1 = start_path.getText();
		String path2 = destination_path.getText();
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
            copyDirectoryFileVisitor(path1, path2); //chiamata alla funzione     
        } catch (IOException e) {
            System.out.println("Exception --> " + e);
            return;
        }
        
		try {
			BufferedReader br = new BufferedReader(new FileReader("res//text1"));
			System.out.println("Done");
	        JOptionPane.showMessageDialog(null, "Files Copied!\nFrom: " + br.readLine() + "\nTo: " + br.readLine(), "AutoBackupProgram", 1);
	        message.setForeground(Color.GREEN);
	        message.setText("Files Copied!");
	        message.setVisible(true);
	        br.close();
		} catch (Exception e) {
			System.out.println("Exception --> " + e);
		}
    	
    }	

	
	void AutomaticBackup() {
		System.out.println("Event --> automatic backup");
		
		if(checkInputCorrect() == false) return;  //controllo errori tramite funzione
		
		if(btn2.getText().equals("Auto Backup (Actived)")) {
			SingleBackup();
		}
	}
	
	void setStringToText() {
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
			LocalDateTime now = LocalDateTime.now();
			String last_date = dtf.format(now);
			last_backup.setText("last backup: " + last_date);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("res//text1"));
			bw.write(start_path.getText());
			bw.write("\n");
			bw.write(destination_path.getText());
			bw.write("\n");
			bw.write(last_backup.getText());
			bw.write("\n");
			
			long last_date_in_seconds = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(last_date).getTime() / 1000;
			bw.write(""+last_date_in_seconds);
			
			bw.close();
		} catch(Exception ex) {
			System.out.println("Exception --> " + ex);
		}
	}

	public void SetSelected(){
		if(checkInputCorrect() == false) return;
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("res//auto_generation"));
			BufferedReader rw = new BufferedReader(new FileReader("res//text1"));
			if(btn2.getText().equals("Auto Backup (Actived)")) {
				bw.write("false");
				btn2.setText("Auto Backup (Disabled)");
				System.out.println("Event --> Auto Backup setted to Disabled");
			}
			else if(btn2.getText().equals("Auto Backup (Disabled)")){
				bw.write("true");
				btn2.setText("Auto Backup (Actived)");
				System.out.println("Event --> Auto Backup setted to Actived");
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
		if(start_path.getText().length() == 0 || destination_path.getText().length() == 0) {
			System.out.println("Error --> Input Missing!");
			message.setForeground(Color.RED);
			message.setText("Input Missing!");
			message.setVisible(true);
			return false;
		}
		
		//check if there is a \ char
		boolean check1 = false;
		boolean check2 = false;
		for(int i=0; i<start_path.getText().length(); i++) {
			if(start_path.getText().charAt(i) == temp.charAt(0)) check1 = true;
		}
		
		for(int i=0; i<destination_path.getText().length(); i++) {
			if(destination_path.getText().charAt(i) == temp.charAt(0)) check2 = true;
		}
		
		if(check1 != true || check2 != true) {
			System.out.println("Error --> Input Error!");
			message.setForeground(Color.RED);
			message.setText("Input Error!");
			message.setVisible(true);
			return false;
		}
		
		return true;
	}
	
    public static void copyDirectoryFileVisitor(String source, String target)
        throws IOException {

        TreeCopyFileVisitor fileVisitor = new TreeCopyFileVisitor(source, target);
        Files.walkFileTree(Paths.get(source), fileVisitor);

    }
    
    
    public void SelectionStart() {
		
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Choose a directory to save your file: ");
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		int returnValue = jfc.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (jfc.getSelectedFile().isDirectory()) {
				System.out.println("You selected the directory: " + jfc.getSelectedFile());
				start_path.setText(jfc.getSelectedFile().toString());
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
				destination_path.setText(jfc.getSelectedFile().toString());
			}
		}

	}
    
}
