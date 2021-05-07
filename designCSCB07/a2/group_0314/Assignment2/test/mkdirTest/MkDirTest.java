package command;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Before;

import filesystem.File;
import filesystem.Directory;
import filesystem.FileSystem;
import driver.Output;

import java.util.ArrayList;

public class MkDirTest {
  private MockFileSystem fs = new MockFileSystem();
  private MockOutput out = new MockOutput();
  private MockDirectory curr = fs.getDirectory(".");
  private MakeDirectory mkdir = new MakeDirectory();
  
  @Before
  public void setUp(){
    mkdir.initialize(curr, fs, out);
    out.fflushErr();
    out.fflushOut();
  }

  public void testCreateOneDNEDir() {
    String[] in = {"mkdir", "./a"};
    mkdir.execute(in, null, curr);
    assertEquals("a", curr.getC1().getDirName());

  }

  public void testCreateExistingDir() {
    String[] in = {"mkdir", "a"};
    mkdir.execute(in, null, curr);
    assertEquals(null, curr.getC2());
  }

  public void testCreateSecondDir() {
    String[] in = {"mkdir", "b"};
    mkdir.execute(in, null, curr);
    assertEquals("b", curr.getC2().getDirName());
  }

  public void testInvalidNames() {
    curr.clearDirs();
    String[] in = {"mkdir", "..", "."};
    mkdir.execute(in, null, curr);
    assertEquals(null, curr.getC1());
    assertEquals(null, curr.getC2());
  }

  public void testCreatExistingFile() {
    MockFile file = new MockFile();
    String[] in = {"mkdir", "a"};
    curr.addFile(file);
    curr.clearDirs();
    mkdir.execute(in, null, curr);
    assertEquals(null, curr.getC1());
  }

  /**
 * Created for use with PwdTest, modded for Mkdir. Mocks getParent and getDirName methods.
 */
public class MockDirectory extends Directory {
  private MockDirectory parent, c1, c2;
  private String name;
  private MockFile file;

  /**
   * Constructor for creating a root directory
   */

  public MockDirectory() {
    parent = this;
  }

  /**
   * Constructor for creating a named, non-root directory
   */

  public MockDirectory(String name, MockDirectory parent) {
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

  public MockDirectory getParent() {
    return this.parent;
  }

  public void addDir(MockDirectory dir, String path) {
    if(this.c1 == null)
      this.c1 = dir;
    else if(this.c2 == null)
      this.c2 = dir;
  }

  public MockDirectory getC1 () {
    if (this.c1 == null)
      return null;
    else 
      return c1;
  }

  public void clearDirs() {
    this.c1 = null;
    this.c2 = null;
  }

  public MockDirectory getC2() {
    if (this.c2 == null)
      return null;
    else
      return c2;
  }

  public MockFile getFile(String name) {
    if(this.file == null)
      return null;
    else
      return file;
  }

  public void addFile(MockFile file) {
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

public class MockFile extends File{
  private String content;

  public String getContent() {
    return this.content;
  }

  public void setContent(String in) {
    this.content = in;
  }

  public void appendToContent(String app) {
    this.content = this.content.concat(app);
  }
}

public class MockFileSystem extends FileSystem{
  private boolean empty = true;
  private MockDirectory root = new MockDirectory();

  public MockFileSystem () {

  }

  public MockDirectory getDirectory(String path) {
    return this.root;
  }

  public void getFile(String path) {
    return;
  }
}

public class MockOutput extends Output {
  private String[] err = {"", ""};
  private String[] out = {"", ""};
  private int eCount = 0;
  private int oCount = 0;

  public void sendErrBuffer(String str) {
    err[eCount++] = str;
  }

  public void sendOutBuffer(String str) {
    out[oCount++] = str;
  }

  public String getOutBuffer() {
    oCount = 0;
    return out[0] + out[1];
  }

  public String getErrBuffer() {
    eCount = 0;
    return err[0] + err[1];
  }

  public void fflushOut() {
    oCount = 0;
    out[0] = out[1] = "";
  }

  public void fflushErr() {
    eCount = 0;
    err[0] = err[1] = "";
  }
}
}
