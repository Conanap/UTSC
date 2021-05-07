import java.util.ArrayList;
import java.util.regex.*;


public class FileFinder {
	
  Pattern pattern;
  Matcher match;
  ArrayList<String> FilesFound = new ArrayList<String>();

  public FileFinder(String regex){
    pattern = Pattern.compile(regex);	
  }
	
  public ArrayList<String> findFiles(String input){
    ArrayList<String> result = new ArrayList<String>();
    this.match = pattern.matcher(input);
    while(match.find()) {
      result.add(input.substring(match.start(), match.end()));
    }
    return result;		
  }
}

