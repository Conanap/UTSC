package command;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import filesystem.Directory;

public class LsTest {

  Ls lister;

  @Before
  public void setUp() {
    lister = new Ls();
  }

  @Test
  public void testEmptyDir() {
    String list = lister.listContents(new MockDirectory("EMPTY"));
    Assert.assertEquals("", list);
  }

  @Test
  public void testWithContent() {
    String result = lister.listContents(new MockDirectory("WITHSTUFF"));
    Assert.assertEquals("dir1\ndir2\nfile1\nfile2\n", result);
  }

  @Test
  public void testR() {
    String result = lister.listContentsR(new MockDirectory("-R"));
    Assert.assertEquals("dirB\ndirC\nfileA\nfileB\ndir1\ndir2\nfile1\nfile2\n",
        result);
  }

  /**
   * @author brody Created to be used to test Ls class.
   */
  public class MockDirectory extends Directory {

    private String status;
    private Map<String, Directory> container;

    public MockDirectory(String status) {
      this.status = status;
      if (status == "-R") {
        container = new HashMap<String, Directory>();
        container.put("dirA", new MockDirectory("WITHSTUFF"));
      }
    }

    public ArrayList listDirectories() {
      ArrayList<String> testDirs = new ArrayList<String>();
      if (status == "WITHSTUFF") {
        testDirs.add("dir1");
        testDirs.add("dir2");
      } else if (status == "-R") {
        testDirs.add("dirB");
        testDirs.add("dirC");
      }
      return testDirs;
    }

    public ArrayList listFiles() {
      ArrayList<String> testFiles = new ArrayList<String>();
      if (status == "WITHSTUFF") {
        testFiles.add("file1");
        testFiles.add("file2");
      } else if (status == "-R") {
        testFiles.add("fileA");
        testFiles.add("fileB");
      }
      return testFiles;
    }

    public Map<String, Directory> getDirContainer() {
      if (status == "-R") {
        return container;
      } else {
        return new HashMap<String, Directory>();
      }
    }
  }
}
