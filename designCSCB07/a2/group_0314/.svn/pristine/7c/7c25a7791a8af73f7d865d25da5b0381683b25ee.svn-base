import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Cd {
  /**
   * Given an String of directory names representing a directory path, 
   * and a FileSystem object, change current directory of FileSystem
   * to directory in path.
   * 
   * @param String path, FileSystem fs
   * @param void
   */
  public static void changeDir (String path, FileSystem fs) {
    ArrayList pathArray = new ArrayList<String>(Arrays.asList(path.split("/")));
    Iterator pathIt = pathArray.iterator();
    while (pathIt.hasNext()) {
      String nextDir = (String) pathIt.next();
      // change to parent directory
      if (nextDir.equals("..")) {
        fs.setCurr(fs.getCurr().getParent());
      }
      // go to root
      else if (nextDir.equals("")) {
        fs.currToRoot();
      }
      else if (nextDir.equals(".")) {
        continue;
      }
      // else change to directory within
      else {
        fs.setCurr(fs.getCurr().getDir(nextDir));
      }
    }
  }

}
