package application;


// ######################################### WORK IN PROGRESS #########################################

class LoadingAutoBackup {
	
	private int trace;
	char []loading = {'[','_','_','_','_','_','_','_','_','_','_','_','_','_','_','_','_','_','_','_','_',']'};
	String loadingS;
	
	LoadingAutoBackup(int dimension_number) {
	    this.trace = 0;
	    FrameAutoBackup.message.setVisible(true);
	}

	public void addLoadingProgression() {
		
		if (trace >= 21) return; 
		
		trace++;
		loading[trace] = '#';
		
		loadingS = convertToString(loading);
		printLoadingProgression(loadingS);
	}
	
	public void printLoadingProgression(String text) {
		
		System.out.println(text);
		//FrameAutoBackup.message.setText(text);
	}
	
	private String convertToString(char []array) {
		String string = "";
		
		for (int i=0; i<loading.length; i++) {
			string += loading[i];
		}
		
		return string;
	}
}
