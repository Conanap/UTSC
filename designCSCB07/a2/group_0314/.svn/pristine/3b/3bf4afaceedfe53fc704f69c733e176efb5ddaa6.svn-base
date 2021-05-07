package command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import driver.Output;
import filesystem.Directory;

public class PwdTest {
  private Pwd p1;

  @Before
  public void setUp() {
    p1 = new Pwd();
  }

  @Test
  public void testNestedDirectories() {
    MockDirectory
        dir =
        new MockDirectory("dir3", new MockDirectory("dir2",
            new MockDirectory("dir1", new MockDirectory())));

    Output o = new MockOutput();
    p1.initialize(dir, null, null);
    p1.execute(new String[]{"pwd"}, null, o);
    assertEquals("/dir1/dir2/dir3\n", o.getOutBuffer());
  }

  @Test
  public void testRootDirectory() {
    MockDirectory dir = new MockDirectory();
    Output o = new MockOutput();
    p1.initialize(dir, null, null);
    p1.execute(new String[]{"pwd"}, null, o);
    assertEquals("/\n", o.getOutBuffer());
  }

  @Test
  public void testValidateValidInput() {
    String[] tokens = {"pwd"};
    assertTrue(p1.validate(tokens));
  }

  @Test
  public void testValidateInvalidInput() {
    String[] tokens = {"pwd", "hello"};
    assertFalse(p1.validate(tokens));
  }

  @Test
  public void testGetUsage() {
    String actual = p1.getUsage();
    String expected = "usage: pwd\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testGetManual() {
    String actual = p1.getManual();
    String expected = "Print the current working directory.\n\n$ pwd\n";
    assertEquals(expected, actual);
  }

  /**
   * Created for use with PwdTest. Mocks getParent and getDirName methods.
   */
  public class MockDirectory extends Directory {
    private MockDirectory parent;
    private String name;

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
  }

  public class MockOutput extends Output {
    String errorBuffer = new String();
    String outBuffer = new String();

    @Override
    public void sendOutBuffer(String out) {
      outBuffer += out;
    }

    @Override
    public String getOutBuffer() {
      return outBuffer;
    }

    @Override
    public void sendErrBuffer(String err) {
      errorBuffer += err;
    }

    @Override
    public String getErrBuffer() {
      return errorBuffer;
    }
  }
}
