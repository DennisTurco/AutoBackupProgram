package application;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

@SuppressWarnings("serial") //per togliere il warning
class FrameAutoBackup extends JFrame{
	static JTextField start_path = new JTextField();
	static JTextField destination_path = new JTextField();
	static JLabel message = new JLabel("");
	static JLabel last_backup = new JLabel();
	static JButton btn2 = new JButton();
	private JMenuBar menu_bar;
	
	private AutoBackupProgram auto_backup;
	
	private int width = 500;
	private int height = 400;

	public FrameAutoBackup() {  //costruttore senza parametri
		//------------------------------------------- set finestra ------------------------------------------- 
		this.setTitle("AutoBackupProgram");
		this.setSize(width, height);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getScreenWidth()) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - getScreenHeight()) / 2); // setto la finestra al centro
		this.setLayout(new BorderLayout());
		this.setResizable(false);  //in questo modo la finestra non cambia dimensione
		this.getContentPane().setBackground(new Color(18, 15, 37)); //setta il colore dello sfondo
		
		//------------------------------------------- Oggetto AutoBackupProgram -------------------------------------------
		auto_backup = new AutoBackupProgram();	
		
		//-------------------------------------------creazione oggetto actionlistener-------------------------------------------
		AutoBackupButtonsListener g = new AutoBackupButtonsListener();
		AutoBackupMenusListener m = new AutoBackupMenusListener();
		
		//-------------------------------------------set icon-------------------------------------------
		ImageIcon image = new ImageIcon("res//logo.png"); //crea un'icona
		setIconImage(image.getImage());	//cambia l'icona del frame
		
		//-------------------------------------------set Menu-------------------------------------------
		menu_bar = new JMenuBar();
		setJMenuBar(menu_bar);
		
		JMenu mnuFile = new JMenu("File");
		JMenu mnuOptions = new JMenu("Options");
		
		menu_bar.add(mnuFile);
		menu_bar.add(mnuOptions);
		
		// Menu Items
		JMenuItem open = new JMenuItem("open");
		JMenuItem save = new JMenuItem("save");
		JMenuItem save_with_name = new JMenuItem("save with name");
		JMenuItem clear = new JMenuItem("clear");
		JMenuItem list_of_backup = new JMenuItem("list of backup");
		JMenuItem history = new JMenuItem("history");
		JMenuItem help = new JMenuItem("help");
		JMenuItem share = new JMenuItem("share");
		JMenuItem quit = new JMenuItem("quit");
		
		mnuFile.add(open);
		mnuFile.add(save);
		mnuFile.add(save_with_name);
		mnuFile.add(clear);
		mnuFile.add(list_of_backup);
		mnuFile.add(history);
		mnuOptions.add(share);
		mnuOptions.add(help);
		mnuOptions.add(quit);
		
		// Action Command
		open.setActionCommand("Open");
		save.setActionCommand("Save");
		save_with_name.setActionCommand("Save With Name");
		clear.setActionCommand("Clear");
		list_of_backup.setActionCommand("List Of Backup");
		history.setActionCommand("History");
		share.setActionCommand("Share");
		help.setActionCommand("Help");
		quit.setActionCommand("Quit");
		
		// Action Listener
		open.addActionListener(m);
		save.addActionListener(m);
		save_with_name.addActionListener(m);
		clear.addActionListener(m);
		list_of_backup.addActionListener(m);
		history.addActionListener(m);
		share.addActionListener(m);
		help.addActionListener(m);
		quit.addActionListener(m);
		
		// Acceleration
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)); // ctrl+s
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)); // ctrl+s
		clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK)); // ctrl+c
		help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK)); // ctrl+c
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK)); // alt+f4
		
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
		
		//-------------------------------------------TOP ELEMENTS-------------------------------------------
		JLabel author = new JLabel("Author: © DennisTurco 2021");
		author.setFont(new Font("Arial", Font.BOLD, 15));
		author.setHorizontalTextPosition(0);
		panNorth.add(author);		
		
	}
	
	//metodi richiamanti dal Listener
	public void SingleBackup() { }
	public void SetSelected() { }
	public void SelectionStart() { }
	public void SelectionDestination() { }
	public void Exit() { auto_backup.Exit(); }
    
    // GETTER
 	public int getScreenWidth(){
 		return width;
 	}
 	
 	public int getScreenHeight(){
 		return height;
 	}
    
}
