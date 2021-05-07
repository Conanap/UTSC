package driver;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static driver.Validator.validateTokens;

public class ValidatorTest {

  @Test
  public void testExit() {
    String[] tokens = {"exit"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testExitWithExtraArguments() {
    String[] tokens = {"exit", "arg1", "arg2"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testMkdirSingleDirectory() {
    String[] tokens = {"mkdir", "dir1"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testMkdirMultipleDirectories() {
    String[] tokens = {"mkdir", "dir1", "dir2"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testMkdirNoDirectory() {
    String[] tokens = {"mkdir"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testCd() {
    String[] tokens = {"cd", "dir"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testCdMultipleDirectories() {
    String[] tokens = {"cd", "dir1", "dir2"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testCdNoDirectory() {
    String[] tokens = {"cd"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testLsNoDirectory() {
    String[] tokens = {"ls"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testLsSingleDirectory() {
    String[] tokens = {"ls", "dir1"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testLsMultipleDirectory() {
    String[] tokens = {"ls", "dir1", "dir2"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testPwd() {
    String[] tokens = {"pwd"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testPwdWithArgument() {
    String[] tokens = {"pwd", "dir1", "dir2"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testPushdOneDirectory() {
    String[] tokens = {"pushd", "dir1"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testPushdMultipleDirectories() {
    String[] tokens = {"pushd", "dir1", "dir2"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testPushdNoDirectories() {
    String[] tokens = {"pushd"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testPopd() {
    String[] tokens = {"popd"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testPopdWithArgument() {
    String[] tokens = {"popd", "dir1", "dir2"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testHistoryNoArguments() {
    String[] tokens = {"history"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testHistoryOneArguments() {
    String[] tokens = {"history", "number"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testHistoryMultipleArguments() {
    String[] tokens = {"history", "number", "morenumbers"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testCatSingleFile() {
    String[] tokens = {"cat", "file1"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testCatMultipleFiles() {
    String[] tokens = {"cat", "file1", "file2"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testCatNoFile() {
    String[] tokens = {"cat"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testEchoTooFewArguments() {
    String[] tokens = {"echo"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testEcho() {
    String[] tokens = {"echo", "hello"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testEchoTooManyArguments() {
    String[] tokens = {"echo", "hello", "stuff"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testRedirectionWithMissingFilename() {
    String[] tokens = {"pwd", ">"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testRedirection() {
    String[] tokens = {"echo", "dir", ">", "file"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testRedirectionAppend() {
    String[] tokens = {"history", "4", ">>", "file"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testRedirectionWithExtraFilename() {
    String[] tokens = {"echo", "hello", ">", "file1", "file2"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testMultipleRedirection() {
    String[] tokens = {"echo", "not good", ">", "file1", ">", "file2"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testRedirectionOnly() {
    String[] tokens = {">", "file1"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testExitRedirectionOnly() {
    String[] tokens = {"exit", ">", "file1"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testManOneCommand() {
    String[] tokens = {"man", "cmd1"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testManMultipleCommands() {
    String[] tokens = {"man", "cmd2", "cmd2"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testManNoCommand() {
    String[] tokens = {"man"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testHistoryRecall() {
    String[] tokens = {"!10"};
    assertTrue(validateTokens(tokens));
  }

  @Test
  public void testInvalidHistoryRecallExtraArguments() {
    String[] tokens = {"!10 2"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testInvalidHistoryRecallDirtyNumber() {
    String[] tokens = {"!1O0"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testInvalidHistoryRecallBadSpace() {
    String[] tokens = {"! 100"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testInvalidHistoryRecallNonNumeric() {
    String[] tokens = {"!a"};
    assertFalse(validateTokens(tokens));
  }

  @Test
  public void testInvalidHistoryRecallNoNumber() {
    String[] tokens = {"!"};
    assertFalse(validateTokens(tokens));
  }
}
