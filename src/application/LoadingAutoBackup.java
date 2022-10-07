package application;

class LoadingAutoBackup {
	
	private int counter;
	private int trace;
	private int dimension_number;
	char []loading = {'[','_','_','_','_','_','_','_','_','_','_','_','_','_','_','_','_','_','_','_','_',']'};
	String loadingS;
	
	LoadingAutoBackup(int dimension_number) {
		this.counter = 0;
	    this.trace = 0;
	    this.dimension_number = dimension_number;
	    FrameAutoBackup.message.setVisible(true);
	}

	public void addLoadingProgression() {
		
		if (trace >= 22) return; 
		
		trace++;
		loading[trace] = '#';
		
		loadingS = convertToString(loading);
		printLoadingProgression(loadingS);
	}
	
	public void printLoadingProgression(String text) {
		
		System.out.println(text);
		FrameAutoBackup.message.setText(text);
	}
	
	private String convertToString(char []array) {
		String string = "";
		
		for (int i=0; i<loading.length; i++) {
			string += loading[i];
		}
		
		return string;
	}
}
