import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author brody
 *
 */
public class Directory {
  private String dirName;
  private Directory parent;
  private Map<String, Directory> dirContainer =
      new HashMap<String, Directory>();
  private Map<String, File> fileContainer = new HashMap<String, File>();

  /**
   * Construct a new directory with name
   * @param name
   * @param parentDir
   */
  public Directory(String name, Directory parentDir) {
    dirName = name;
    parent = parentDir;
  }

  /**
   * Construct a new directory without name
   * @param parentDir
   */
  public Directory(Directory parentDir) {
    dirName = null;
    parent = parentDir;
  }
  
  /**
   * Construct root directory
   */
  public Directory() {
    dirName = "root";
    parent = this;
  }
  
  public String toString() {
    return this.getName();
  }
  
  /**
   * Returns name of directory
   * @return dirName
   */
  public String getName() {
    return this.dirName;
  }

  /**
   * Returns parent Directory object
   * @return parent
   */
  public Directory getParent() {
    return parent;
  }

  /**
   * Set parent directory
   * @param parent
   */
  public void setParent(Directory parent) {
    this.parent = parent;
  }

  /**
   * Maps new directory to dirName
   * @param dir
   * @param DirName
   */
  public void addDir(Directory dir, String DirName) {
    dirContainer.put(DirName, dir);
  }

  /**
   * Returns directory with name dirName
   * @param dirName
   * @return Directory
   */
  public Directory getDir(String dirName) {
    return dirContainer.get(dirName);
  }

  /**
   * Maps new file to fileName
   * @param infile
   * @param FileName
   */
  public void addFile(File infile, String FileName) {
     fileContainer.put(FileName, infile);
  }
  
  /**
   * Returns file with name fileName
   * @param fileName
   * @return
   */
  public File getFile(String fileName) {
    return fileContainer.get(fileName);
  }

  /**
   * Returns an ArrayList containing names of all directories contained
   * @return ArrayList<String> directories
   */
  public ArrayList listDirectories() {
    ArrayList<String> directories = new ArrayList<String>();
    for(String dirName : dirContainer.keySet()) {
      directories.add(dirName);
    }
    return directories;
  }

  /**
   * Returns an ArrayList containing names of all files contained
   * @return
   */
  public ArrayList listFiles() {
    ArrayList<String> files = new ArrayList<String>();
    for(String fileName : fileContainer.keySet()) {
      files.add(fileName);
    }
    return files;
  }

  public String getDirName() {
    return dirName;
  }

  public void setDirName(String dirName) {
    this.dirName = dirName;
  }
}
