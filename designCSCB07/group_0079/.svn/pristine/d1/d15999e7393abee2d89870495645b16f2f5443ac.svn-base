import java.io.*;
import java.util.ArrayList;


public class FindFiles {

	public static void main(String[] args) {
		String fileName = "files.txt";
        String line = null;
        String fileText = "";
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
            	fileText += line + ' ';
            }   
            // fileText is a String of multiple files separated by a space.
            // Use regex to find all java files containing 'test' in their name
            
            String regex = "[\\w]*[tT][eE][sS][tT][\\w]*\\.java";

            FileFinder javaFileFinder = new FileFinder(regex);
            ArrayList<String> filesFound = javaFileFinder.findFiles(fileText);
            System.out.println("Number of files found: " + filesFound.size());
            for (String file: filesFound){
            	System.out.println(file);
            }
                    
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file");                  
        }
      
	}

}
