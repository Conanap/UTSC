import java.util.ArrayList;

import filesystem.Directory;
import filesystem.File;

/**
 * Created for use with PwdTest. Mocks getParent and getDirName methods.
 */
public class MockDirectory extends Directory {
  private Directory parent, c1, c2;
  private String name;
  private File file;

  /**
   * Constructor for creating a root directory
   */

  public MockDirectory() {
    parent = this;
  }

  /**
   * Constructor for creating a named, non-root directory
   */

  public MockDirectory(String name, Directory parent) {
    this.parent = parent;
    this.name = name;
  }

  /**
   * Returns the stored directory name. Will not work for root.
   *
   * @return name of directory
   */

  public String getDirName() {
    return this.name;
  }

  /**
   * Return the parent of this directory
   *
   * @return parent of this directory
   */

  public Directory getParent() {
    return this.parent;
  }

  public void addDir(MockDirectory dir, String path) {
    if(this.c1 == null)
      this.c1 = dir;
    else if(this.c2 == null)
      this.c2 = dir;
  }

  public Directory getC1 () {
    if (this.c1 == null)
      return null;
    else 
      return c1;
  }

  public void clearDirs() {
    this.c1 = null;
    this.c2 = null;
  }

  public Directory getC2() {
    if (this.c2 == null)
      return null;
    else
      return c2;
  }

  public File getFile(String name) {
    if(this.file == null)
      return null;
    else
      return file;
  }

  public void addFile(File file) {
    this.file = file;
  }
  
  public ArrayList listDirectories(){
    ArrayList<String> testDirs =  new ArrayList<>();
    testDirs.add("dir1");
    testDirs.add("dir2");
    return testDirs;
  }
  
  public ArrayList listFiles(){
    ArrayList<String> testFiles =  new ArrayList<>();
    testFiles.add("file1");
    return testFiles;
  }
}
