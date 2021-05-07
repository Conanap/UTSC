package driver;

import static driver.Tokenizer.parseFilePath;
import static driver.Tokenizer.tokenizeInput;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class TokenizerTest {
  @Test
  public void testEmptyInput() {
    String input = "";
    String[] expectedOutput = {};
    assertArrayEquals(expectedOutput, tokenizeInput(input));
  }

  @Test
  public void testSingleCommand() {
    String input = "cmd";
    String[] expectedOutput = {"cmd"};
    assertArrayEquals(expectedOutput, tokenizeInput(input));
  }

  @Test
  public void testommandWithSurroundingSpaces() {
    String input = "  cmd  ";
    String[] expectedOutput = {"cmd"};
    assertArrayEquals(expectedOutput, tokenizeInput(input));
  }

  @Test
  public void testCommandWithArguments() {
    String input = "cmd arg1 arg2";
    String[] expectedOutput = {"cmd", "arg1", "arg2"};
    assertArrayEquals(expectedOutput, tokenizeInput(input));
  }

  @Test
  public void testCommandWithQuotedArgument() {
    String input = "cmd \"argument\"";
    String[] expectedOutput = {"cmd", "\"argument\""};
    assertArrayEquals(expectedOutput, tokenizeInput(input));
  }

  @Test
  public void testCommandWithQuotedArgumentWithSpaces() {
    String input = "cmd \"argument with spaces\"";
    String[] expectedOutput = {"cmd", "\"argument with spaces\""};
    assertArrayEquals(expectedOutput, tokenizeInput(input));
  }

  @Test
  public void testCommandWithMixedQuotedUnquotedArguments() {
    String input = "cmd \"quoted arguments\" unquoted argument \"quoted\"";
    String[] expectedOutput =
        {"cmd", "\"quoted arguments\"", "unquoted", "argument", "\"quoted\""};
    assertArrayEquals(expectedOutput, tokenizeInput(input));
  }

  @Test
  public void testWhitespaceOnlyInput() {
    String input = " ";
    String[] expectedOutput = {};
    assertArrayEquals(expectedOutput, tokenizeInput(input));
  }

  @Test
  public void testQuotedWhitespaceInput() {
    String input = "\" \"";
    String[] expectedOutput = {"\" \""};
    assertArrayEquals(expectedOutput, tokenizeInput(input));
  }

  @Test
  public void testCommandWithQuotedEmptyString() {
    String input = "cmd \"\"";
    String[] expectedOutput = {"cmd", "\"\""};
    assertArrayEquals(expectedOutput, tokenizeInput(input));
  }

  @Test
  public void testHistoryRecallCommand() {
    String input = "!45";
    String[] expectedOutput = {"!", "45"};
    assertArrayEquals(expectedOutput, tokenizeInput(input));
  }

  @Test
  public void testInvalidHistoryRecallCommand() {
    String input = "! 45";
    String[] expectedOutput = {"!", " 45"};
    assertArrayEquals(expectedOutput, tokenizeInput(input));
  }

  @Test
  public void testParseFilePathRoot() {
    String[] out = parseFilePath(".");
    String[] expect = {".", null};
    assertArrayEquals(expect, out);
  }

  @Test
  public void testParseFilePathRandomLongPath() {
    String[] out = parseFilePath("a/b/c/d");
    String[] expect = {"a/b/c", "d"};
    assertArrayEquals(expect, out);
  }
}
