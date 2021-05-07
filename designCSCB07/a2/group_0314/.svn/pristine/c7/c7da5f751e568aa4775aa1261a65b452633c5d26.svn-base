import org.junit.Test;

import CatTest.MockFileSystem;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Before;

import command.Curl;
import filesystem.DirectoryNotFoundException;

public class CurlTest{
  
  Curl curl;
  String[] testTokens;
  MockOutput output;
  MockFileSystem fs;
  MockHistory his;
  MockCommandRunner cmd;
  MockDirectory curr;
  
  @Before
  public void setUp(){
    fs = new MockFileSystem();
    try {
      curr = (MockDirectory) fs.getDirectory(".", fs.getRoot());
    } catch (DirectoryNotFoundException e) {}
    curl = new Curl();
    testTokens = new String[2];
    testTokens[0] = "curl";
    testTokens[1] = "http://www.textfiles.com/history/heroic.txt";
    curl.initialize(curr, fs, his);
  }
  
  @Test
  public void testURLRetrieve(){
    curl.execute(testTokens, cmd, output);
  }
  
  @Test
  public void testIOException() {
    testTokens[1] = "asdasfasdasdasd";
    curl.execute(testTokens, cmd, output);
    fail();
  }
  
  @Test
  public void testFileCreation() {
    curl.execute(testTokens, cmd, output);
    assertNotNull(curr.getFile("heroic.txt"));
  }
  
}