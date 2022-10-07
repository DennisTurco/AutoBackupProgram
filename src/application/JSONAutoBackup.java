package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class JSONAutoBackup {
	
	JSONAutoBackup() {}
	
	//READ & WRITE JSON
    public void ReadJSONFile(String filename, String directory_path) {
		JSONParser jsonP = new JSONParser();
		
		try (FileReader reader = new FileReader(directory_path + filename)) {	
			//read JSon file
			Object obj = jsonP.parse(reader);
			JSONObject list = (JSONObject) obj;
			
			String name  = (String) list.get("filename");
			
			if (filename == "info.json") {
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
			if (days_interval != null)AutoBackupProgram.days_interval_backup = Integer.parseInt(days_interval);
				
		} catch (FileNotFoundException e) {
			System.out.println("Exception --> " + e);
		} catch (IOException e) {
			System.out.println("Exception --> " + e);
		} catch (ParseException e) {
			System.out.println("Exception --> " + e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void WriteJSONFile(String filename, String directory_path) {
		JSONObject list = new JSONObject();
		
		if (filename == "info.json") { //TODO: fixhere non si aggiorna mai info.json
			list.put("filename", AutoBackupProgram.current_file_opened);
		}
		
		else {
			list.put("filename", filename);
			list.put("start_path", FrameAutoBackup.start_path.getText());
			list.put("destination_path", FrameAutoBackup.destination_path.getText());
			list.put("last_backup", FrameAutoBackup.last_backup.getText());
			if (FrameAutoBackup.btn_automatic_backup.getText() == "Auto Backup (Enabled)") {
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
			System.out.println("Exception --> " + e);
		}
	}	
}
