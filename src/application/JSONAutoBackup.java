package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class JSONAutoBackup {
	
	private JSONArray backup_list;
	
	JSONAutoBackup() {}
	
	//READ & WRITE JSON
    public void ReadJSONFile(String filename, String directory_path) {
		JSONParser jsonP = new JSONParser();
		
		try (FileReader reader = new FileReader(directory_path + filename)) {	
			//read JSON file
			Object obj = jsonP.parse(reader);
			JSONObject list = (JSONObject) obj;
			
			String name  = (String) list.get("filename");
			
			if (filename.equals("info.json")) {
				System.out.println("Event --> current file: " + name);
				ReadJSONFile(name, ".//res//saves//");
				return; //il return è essenziale per stoppare la ricorsione
			}
			
			String path1 = (String) list.get("start_path");
			String path2 = (String) list.get("destination_path");
			String last_backup = (String) list.get("last_backup");
			String next_date = (String) list.get("next_date_backup");
			String days_interval = (String) list.get("days_interval_backup");
			String automatic_backup = (String) list.get("automatic_backup");
			
			//update the variables
			AutoBackupProgram.current_file_opened = name;
			FrameAutoBackup.start_path.setText(path1);
			FrameAutoBackup.destination_path.setText(path2);
			FrameAutoBackup.last_backup.setText(last_backup);
			FrameAutoBackup.btn_automatic_backup.setText(automatic_backup);
			AutoBackupProgram.next_date_backup = next_date;
			if (AutoBackupProgram.getAutoBackupOption(automatic_backup)) AutoBackupProgram.auto_backup_option = true;
			else AutoBackupProgram.auto_backup_option = false;
			if (days_interval != null) AutoBackupProgram.days_interval_backup = Integer.parseInt(days_interval);
				
		} catch (FileNotFoundException e) {
			System.err.println("Exception --> " + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Exception --> " + e);
			e.printStackTrace();
		} catch (ParseException e) {
			System.err.println("Exception --> " + e);
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void WriteJSONFile(String filename, String directory_path) {
		JSONObject list = new JSONObject();
		
		if (filename.equals("info.json")) {
			list.put("filename", AutoBackupProgram.current_file_opened);
		}
		
		else {
			list.put("filename", filename);
			list.put("start_path", FrameAutoBackup.start_path.getText());
			list.put("destination_path", FrameAutoBackup.destination_path.getText());
			
			if (FrameAutoBackup.last_backup.getText() != null) {
				list.put("last_backup", FrameAutoBackup.last_backup.getText());
			}
			if (FrameAutoBackup.btn_automatic_backup.getText().equals("Auto Backup (Enabled)")) {
				list.put("automatic_backup", "Auto Backup (Enabled)"); 
				list.put("next_date_backup", AutoBackupProgram.next_date_backup); 
				list.put("days_interval_backup", "" + AutoBackupProgram.days_interval_backup); 
			}
			else { 
				list.put("automatic_backup", "Auto Backup (Disabled)"); 
				list.put("next_date_backup", null); 
				list.put("days_interval_backup", null); 
			}

		}
		
		// scrittura file json
		try (FileWriter file = new FileWriter(directory_path + filename)){
			file.write(list.toJSONString());
			file.flush();
		} catch (IOException e) {
			System.err.println("Exception --> " + e);
			e.printStackTrace();
		}
	}	
	
	@SuppressWarnings("unchecked")
	public void LoadJSONBackupList() {	
		backup_list = new JSONArray();
		
		File directory = new File(".//res//saves");
		File[] listOfFiles = directory.listFiles();
		int count = directory.list().length; // ottengo il numero di file nella directory
		
		for (int i=0; i<count; i++) {
	
			while(true) {
				if (listOfFiles[i].isFile()) break; //se incontro una directory non la leggo
				else i++;
			}
			
			JSONParser jsonP = new JSONParser();
			
			try (FileReader reader = new FileReader(".//res//saves//" + listOfFiles[i].getName())) {		
				Object obj = jsonP.parse(reader);
				JSONObject list = (JSONObject) obj;
				
				//lettura
				String name  = (String) list.get("filename");
				String path1 = (String) list.get("start_path");
				String path2 = (String) list.get("destination_path");
				String last_backup = (String) list.get("last_backup");
				String next_date = (String) list.get("next_date_backup");
				String days_interval = (String) list.get("days_interval_backup");
				String automatic_backup = (String) list.get("automatic_backup");
			
				//scrittura
				list.put("filename", name);
				list.put("start_path", path1);
				list.put("destination_path", path2);
				list.put("last_backup", last_backup);
				list.put("automatic_backup", automatic_backup);
				if (automatic_backup.equals("Auto Backup (Enabled)")) {
					list.put("next_date_backup", next_date);
					list.put("days_interval_backup", "" + days_interval);
				}
				else {
					list.put("next_date_backup", null);
					list.put("days_interval_backup", null);
				}
				
				//aggiungo alla lista
				backup_list.add(list); 
			
			} catch (FileNotFoundException e) {
				System.err.println("Exception --> " + e);
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("Exception --> " + e);
				e.printStackTrace();
			} catch (ParseException e) {
				System.err.println("Exception --> " + e);
				e.printStackTrace();
			}
		}
		
		try (FileWriter file = new FileWriter(".//res//backup_list.json")){
			file.write(backup_list.toJSONString());
			file.flush();
		} catch (IOException e) {
			System.err.println("Exception --> " + e);
			e.printStackTrace();
		}
	}
	
	
	public void openBackupList() {
		LoadJSONBackupList();
		
		JPanel pnl = new JPanel();
		pnl.setBounds(61, 11, 81, 140);
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS)); // VerticalLayout
		JLabel []labels = new JLabel[backup_list.size()];
			
		for (int i=0; i<backup_list.size(); i++) {	
			labels[i] = new JLabel();
			
			JSONObject json_obj = (JSONObject) backup_list.get(i);   //ottengo the i-esimo elemento
			
			if (json_obj.get("next_date_backup") != null) {
				labels[i].setText("<html><i>" + json_obj.get("filename").toString() + " - Next date: " + json_obj.get("next_date_backup").toString() + "</i></html>");	//lo passo alla JLabel
			}else {
				labels[i].setText("<html><i>" + json_obj.get("filename").toString() + "</i></html>");	//lo passo alla JLabel
			}
			
			pnl.add(labels[i]);
		}
				
		JOptionPane.showMessageDialog(null, pnl, "Backup List", JOptionPane.PLAIN_MESSAGE, null); //messaggio popup
	}
	
}
