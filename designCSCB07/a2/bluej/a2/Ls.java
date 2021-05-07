import java.util.Iterator;

public class Ls {
  
  /**
   * Given a FileSystem return list of content in current directory
   * @param FileSystem fs
   * @return String outStr
   */
  public static String listContents (FileSystem fs) {
    String outStr = "";
    // get directories
    Iterator dirIt = fs.getCurr().listDirectories().iterator();
    while (dirIt.hasNext()) {
      outStr = outStr + (String) dirIt.next() + "/n";
    }
    // get files
    Iterator  fileIt = fs.getCurr().listFiles().iterator();
    while (fileIt.hasNext()) {
       outStr = outStr + (String) dirIt.next() + "/n";
    }
    return outStr;
  }
  
  /**
   * Given a FileSystem and a path.
   * return list of content in directory at path
   * 
   * @param String path
   * @param FileSystem fs
   * @return String outStr
   */
  public static String listContents (String path, FileSystem fs) {
    // Store current directory
    //Pushd.pushd(fs.getCurr());
    
    Cd.changeDir(path, fs);
    String outStr = "";
    // get directories
    Iterator dirIt = fs.getCurr().listDirectories().iterator();
    while (dirIt.hasNext()) {
      outStr = outStr + (String) dirIt.next() + "/n";
    }
    // get files
    Iterator  fileIt = fs.getCurr().listFiles().iterator();
    while (fileIt.hasNext()) {
       outStr = outStr + (String) dirIt.next() + "/n";
    }
    
    // go back to original directory
    //Popd.popd(fs);
    
    return outStr;
  }
}
