package command;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import filesystem.Directory;
import filesystem.File;
import driver.Output;

public class CatTest {
  private MockFileSystem fs = new MockFileSystem();
  private MockOutput out = new MockOutput();
  private Concatenate cat;
  private MockDirectory curr = new MockDirectory();

  @Before
  public void setup() {
  	cat = new Concatenate();
  	cat.initialize(curr, fs, null);
  	out.fflushOut();
  	out.fflushErr();
  }

  @Test
  public void testCatDNEFile() {
    String[] in = {"cat", "poop"};
	 cat.execute(in, null, out);
   assertEquals("poop: no such file\n", out.getErrBuffer());
	 assertEquals("", out.getOutBuffer());
  }

  @Test
  public void testCatOneFile() {
    String[] in = {"cat", "poop"};
    MockFile f1 = new MockFile("ehhh lmao");
    fs.addFile(f1);
	 cat.execute(in, null, out);
	 assertEquals("ehhh lmao\n", out.getOutBuffer());
  }

  @Test
  public void testCatMultFile() {
    String[] in = {"cat", "poop", "rar'"};
    MockFile f2 = new MockFile("ehhh lmao");
    fs.addFile(f2);
	  cat.execute(in, null, out);
	  assertEquals("ehhh lmao\nehhhlmao\n", out.getOutBuffer());
  }

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

public class MockFile extends File{
  private String content;

  public MockFile(String content) {
    this.content = content;
  }
  public String getContent() {
    return this.content;
  }

  public void setContent(String in) {
    this.content = in;
  }
}

public class MockFileSystem extends FileSystem{
  private boolean empty;
  private MockFile[] mf;
  private int mfCount = 0;

  public MockFileSystem () {
    mf = new MockFile[2];
    empty = true;
  }

  public void getFile(String path) {
    return;
  }

  public void addFile(MockFile file) {
    mf[mfCount++] = file;
  }

  public MockFile getFile(String path, Directory dir) {
    if(path.equals("poop"))
      return mf[0];
    else if (path.equals("rar"))
      return mf[1];
    else
        return mf[1];
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
