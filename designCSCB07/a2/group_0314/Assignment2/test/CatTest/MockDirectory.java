package CatTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import filesystem.Directory;

/**
 * @author brody
 * Created to be used to test Cat class.
 * Moified by albion
 */
public class MockDirectory extends Directory{
  
  private String status;
  private Map<String, Directory> container;
  
  public MockDirectory(){

  }
  
  public ArrayList listDirectories(){
    ArrayList<String> testDirs =  new ArrayList<String>();
    if (status == "WITHSTUFF") {
      testDirs.add("dir1");
      testDirs.add("dir2");
    } else if (status == "-R") {
      testDirs.add("dirB");
      testDirs.add("dirC");
    }
    return testDirs;
  }
  
  public ArrayList listFiles(){
    ArrayList<String> testFiles =  new ArrayList<String>();
    if (status == "WITHSTUFF") {
      testFiles.add("file1");
      testFiles.add("file2");
    } else if (status == "-R") {
      testFiles.add("fileA");
      testFiles.add("fileB");
    }
    return testFiles;
  }
  
  public Map<String, Directory> getDirContainer(){
    if (status == "-R"){
      return container;
    } else {
      return new HashMap<String, Directory>();
    }
  }
}
