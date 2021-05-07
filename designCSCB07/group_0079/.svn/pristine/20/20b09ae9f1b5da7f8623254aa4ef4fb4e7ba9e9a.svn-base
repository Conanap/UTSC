import java.util.Hashtable;
public class executionProgram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		filee[] fileArray;
		fileArray=new filee[10];
		Hashtable<String, String> ht = new Hashtable<String, String>();
		String fileExtension = "pdf";
		
		fileArray[0]=new openPDF();
		fileArray[1]=new openPDF();
		fileArray[2]=new OpenWord();
		fileArray[3]=new openTxt();
		fileArray[4]=new openPDF();
		fileArray[5]=new OpenWord();
		fileArray[6]=new openTxt();
		fileArray[7]=new openTxt();
		fileArray[8]=new openPDF();
		fileArray[9]=new OpenWord();
		
		
		for (filee singleFile :fileArray)
		{
			singleFile.NameOfFile();
			singleFile.readSingleLine();
			System.out.println();
			System.out.println();
			System.out.println();
		}
		
		ht.put("pdf", "openPDF");
		ht.put("doc", "openDOC");
		ht.put("txt", "openTXT");
		
		try {
		filee use =  (filee) Class.forName(ht.get(fileExtension)).newInstance();
		use.NameOfFile();
		} catch(Exception e) {}
		
	}
}
