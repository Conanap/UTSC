import org.junit.Assert;
import org.junit.Assert.*;

import driver.CommandRunner;

import org.junit.Test;

import command.Cd;
import filesystem.Directory;
import filesystem.DirectoryNotFoundException;
import filesystem.FileSystem;

public class CdTest {

  @Test
  public void testChangeDirectory() {
    Cd changer = new Cd();
    MockFS mockFS = new MockFS();
    MockDirectory result = null;
    try {
       result = (MockDirectory) changer.changeDir(null, mockFS, null, null);
    } catch (DirectoryNotFoundException e) {}
    Assert.assertEquals("secret", result.getMessage());
  }
  
  public class MockDirectory extends Directory {
    private String message;

    public MockDirectory(String message) {
      this.message = message;
    } 
    
    public String getMessage() {
      return message;
    }
  }
  
  public class MockFS extends FileSystem {
    MockDirectory testDir;
    public MockFS() {testDir = new MockDirectory("secret");}
    
    public Directory getDirectory(String path, Directory curr){
      return testDir;
    }
  }
  
  public class MockCmdRunner extends CommandRunner {
    
    private MockDirectory currDir;
    public MockCmdRunner(FileSystem fs) {
      super(fs);
      currDir = new MockDirectory("not important");
    }
    
    public void setCurrDir (Directory newDir){
      this.currDir = (MockDirectory) newDir;
    }
    
    public Directory getCurrDir(Directory newDir) {
      return currDir;
    }
  }
}
