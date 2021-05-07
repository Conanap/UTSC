import org.junit.Test;

import CatTest.MockFileSystem;
import command.Redirect;
import command.Append;
import command.Curl;
import command.Overwrite;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;

public class RedirectTest {
  Redirect redirect;
  String[] testTokens;
  MockOutput output;
  MockFileSystem fs = new MockFileSystem();
  MockHistory his;
  MockCommandRunner cmd;
  MockDirectory curr = fs.getDirectory(".");
  
  @Before
  public void setUp(){
    redirect = new Redirect();
    testTokens = new String[2];
    testTokens[0] = ".";
    testTokens[1] = "file";
    redirect.setType("ow");
    redirect.initialize(curr,fs, his);
    output.sendOutBuffer("how did we lose");
  }
  
  @Test
  public void testRedirectCreatesFile() {
    redirect.execute(testTokens, cmd, output);
    assertNotNull(curr.getFile("file"));
  }
  
  @Test
  public void testRedirectOverwriteFile() {
    MockFile file = new MockFile();
    file.setContent("Reginald shotcalling");
    redirect.execute(testTokens,cmd,output);
    assertEquals("how did we lose", curr.getFile("file").getContent());
}

  @Test
  public void testRedirectOverwriteDNEFile() {
    redirect.execute(testTokens,cmd,output);
    assertEquals("how did we lose", curr.getFile("file").getContent());
  }

  @Test
  public void testRedirectAppendFile() {
    MockFile file = new MockFile();
    file.setContent("are u srs");
    redirect.setType("ap");
    redirect.execute(testTokens,cmd,output);
    assertEquals("are u srs how did we lose", curr.getFile("file").getContent());
  }

  @Test
  public void testRedirectAppendDNEFile() {
    redirect.setType("ap");
    redirect.execute(testTokens,cmd,output);
    assertEquals("how did we lose", curr.getFile("file").getContent());
  }


}
