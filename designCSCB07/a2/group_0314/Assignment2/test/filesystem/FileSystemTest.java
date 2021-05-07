package filesystem;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.EmptyStackException;
import java.util.Stack;

public class FileSystemTest {
  private FileSystem fs;
  private MockDirectory rootDir;
  private MockDirStack dirStack;

  @Before
  public void setUp() {
    rootDir = new MockDirectory(3);
    dirStack = new MockDirStack();
    fs = new FileSystem(rootDir, dirStack);
  }

  @Test(expected = EmptyStackException.class)
  public void testPopEmpty() {
    fs.pop();
  }

  @Test
  public void testPopOne() {
    fs.push("a");
    assertEquals("a", fs.pop());
  }

  @Test
  public void testPopMultiple() {
    String[] entries = {"a", "b", "c"};

    for (int i = 0; i < entries.length; i++) {
      fs.push(entries[i]);
    }
    for (int j = entries.length - 1; j >= 0; j--) {
      assertEquals(entries[j], fs.pop());
    }
  }

  @Test
  public void testGetDirChildAFromRoot() throws DirectoryNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    Directory actualDir = fs.getDirectory("/a", currDir);
    Directory expectedDir = rootDir.getMockChildA();
    assertEquals(expectedDir, actualDir);
  }

  @Test
  public void testGetDirRoot() throws DirectoryNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    Directory actualDir = fs.getDirectory("/", currDir);
    Directory expectedDir = rootDir;
    assertEquals(expectedDir, actualDir);
  }

  @Test
  public void testGetDirParentFromRoot() throws DirectoryNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    Directory actualDir = fs.getDirectory("/..", currDir);
    Directory expectedDir = rootDir.getMockParent();
    assertEquals(expectedDir, actualDir);
  }

  @Test
  public void testGetDirCurrentDirectory() throws DirectoryNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    Directory actualDir = fs.getDirectory(".", currDir);
    Directory expectedDir = currDir;
    assertEquals(expectedDir, actualDir);
  }

  @Test(expected = DirectoryNotFoundException.class)
  public void testGetDirIllegalPathWithDots()
      throws DirectoryNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    fs.getDirectory(". .", currDir);
  }

  @Test(expected = DirectoryNotFoundException.class)
  public void testGetDirIllegalPathWithNames()
      throws DirectoryNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    fs.getDirectory("a b", currDir);
  }

  @Test(expected = DirectoryNotFoundException.class)
  public void testGetDirEmptyPath() throws DirectoryNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    fs.getDirectory("", currDir);
  }

  @Test(expected = DirectoryNotFoundException.class)
  public void testGetDirSpaceOnlyPath() throws DirectoryNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    fs.getDirectory(" ", currDir);
  }

  @Test
  public void testGetDirParent() throws DirectoryNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    Directory actualDir = fs.getDirectory("..", currDir);
    Directory expectedDir = currDir.getMockParent();
    assertEquals(expectedDir, actualDir);
  }

  @Test
  public void testGetDirChildA() throws DirectoryNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    Directory actualDir = fs.getDirectory("a", currDir);
    Directory expectedDir = currDir.getMockChildA();
    assertEquals(expectedDir, actualDir);
  }

  @Test(expected = DirectoryNotFoundException.class)
  public void testGetDirNonExistentChild() throws DirectoryNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    fs.getDirectory("b", currDir);
  }

  @Test
  public void testGetDirNestedDir() throws DirectoryNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    Directory actualDir = fs.getDirectory("a/a/a", currDir);
    Directory
        expectedDir =
        currDir.getMockChildA().getMockChildA().getMockChildA();
    assertEquals(expectedDir, actualDir);
  }

  @Test
  public void testGetDirNestedDirWithDoubleDots()
      throws DirectoryNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    Directory actualDir = fs.getDirectory("a/../a/../a", currDir);
    Directory
        expectedDir =
        currDir.getMockChildA().getMockParent().getMockChildA().getMockParent()
            .getMockChildA();
    assertEquals(expectedDir, actualDir);
  }

  @Test(expected = DirectoryNotFoundException.class)
  public void testGetDirThroughAFile() throws DirectoryNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    fs.getDirectory("A/a", currDir);
  }

  @Test
  public void testGetFileChildAFromRoot() throws FileNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    File actualFile = fs.getFile("/A", currDir);
    File expectedFile = rootDir.getMockFileA();
    assertEquals(expectedFile, actualFile);
  }

  @Test
  public void testGetFileFromCurrentDirectory() throws FileNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    File actualFile = fs.getFile("./A", currDir);
    File expectedFile = currDir.getMockFileA();
    assertEquals(expectedFile, actualFile);
  }

  @Test(expected = FileNotFoundException.class)
  public void testGetFileIllegalPathWithDots() throws FileNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    fs.getFile(". .", currDir);
  }

  @Test(expected = FileNotFoundException.class)
  public void testGetFileIllegalPathWithNames() throws FileNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    fs.getFile("a b", currDir);
  }

  @Test(expected = FileNotFoundException.class)
  public void testGetFileEmptyPath() throws FileNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    fs.getFile("", currDir);
  }

  @Test(expected = FileNotFoundException.class)
  public void testGetFileSpaceOnlyPath() throws FileNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    fs.getFile(" ", currDir);
  }

  @Test
  public void testGetFileChildA() throws FileNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    File actualFile = fs.getFile("A", currDir);
    File expectedFile = currDir.getMockFileA();
    assertEquals(expectedFile, actualFile);
  }

  @Test(expected = FileNotFoundException.class)
  public void testGetFileNonExistentChild() throws FileNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    fs.getFile("B", currDir);
  }

  @Test(expected = FileNotFoundException.class)
  public void testGetFilePathToDirectory() throws FileNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    fs.getFile("a", currDir);
  }

  @Test
  public void testGetFileNestedDir() throws FileNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    File actualFile = fs.getFile("a/a/A", currDir);
    File expectedFile = currDir.getMockChildA().getMockChildA().getMockFileA();
    assertEquals(expectedFile, actualFile);
  }

  @Test
  public void testGetFileNestedDirWithDoubleDots()
      throws FileNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    File actualFile = fs.getFile("a/../a/../A", currDir);
    File
        expectedFile =
        currDir.getMockChildA().getMockParent().getMockChildA().getMockParent()
            .getMockFileA();
    assertEquals(expectedFile, actualFile);
  }

  @Test(expected = FileNotFoundException.class)
  public void testGetFileThroughAFile() throws FileNotFoundException {
    MockDirectory currDir = new MockDirectory(10);
    fs.getFile("A/A", currDir);
  }

  /**
   * Mocks all DirStack methods.
   */
  private class MockDirStack extends DirStack {
    Stack<String> stack = new Stack<>();

    @Override
    public void pushDir(String dirPath) {
      stack.push(dirPath);
    }

    @Override
    public String popDir() {
      return stack.pop();
    }
  }

  /**
   * Mocks all Directory methods.
   */
  public class MockDirectory extends Directory {
    private MockDirectory parent = null;
    private MockDirectory childA = null;
    private MockFile fileA = null;

    public MockDirectory(int recurseLevel) {
      if (recurseLevel > 0) {
        parent = new MockDirectory(recurseLevel - 1);
        childA = new MockDirectory(recurseLevel - 1);
        fileA = new MockFile();
      }
    }

    @Override
    public MockDirectory getDir(String path) throws DirectoryNotFoundException {
      switch (path) {
        case ".":
          return this;
        case "..":
          return parent;
        case "a":
          return childA;
        default:
          throw new DirectoryNotFoundException();
      }
    }

    @Override
    public MockFile getFile(String path) throws FileNotFoundException {
      if (path.equals("A")) {
        return fileA;
      } else {
        throw new FileNotFoundException();
      }
    }

    @Override
    public Directory getParent() {
      return getMockParent();
    }

    public MockDirectory getMockParent() {
      return parent;
    }

    public MockDirectory getMockChildA() {
      return childA;
    }

    public MockFile getMockFileA() {
      return fileA;
    }
  }

  public class MockFile extends File {

  }
}
