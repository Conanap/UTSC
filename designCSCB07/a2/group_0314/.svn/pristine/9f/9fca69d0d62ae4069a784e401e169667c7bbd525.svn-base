package command;
import filesystem.*;
import driver.CommandRunner;
import driver.Output;

import filesystem.FileNotFoundException;

/**
 * Outputs the contents of the file
 *
 * @author Albion Ka Hei Fung
 */
public class Concatenate extends Command {
  private Directory curr;
  private FileSystem fs;
  private final String MAN_USAGE = "cat PATH [...]";
  private final String MAN_DESCRIPTION = "View the contents of provided files.";

  public Concatenate() {
  }

  /**
   * Prints content of files.
   *
   * @param tokens The command itself and its arguments
   * @param fs     The file system of the current running instance
   * @param curr   The current working directory on the instance
   */
  public void execute(String[] tokens, CommandRunner cmd, Output output) {
    // init vars
    String out = "";
    File file;
    // cat through all args
    for (int i = 1; i < tokens.length; i++) {
      // if file exist, append output string with content
      try {
        out += this.fs.getFile(tokens[i], this.curr).getContent();
      } catch (FileNotFoundException e) {
        // if file doesn't exist, return error message
        output.sendErrBuffer(tokens[i] + ": no such file" + "\n");
      }
    }
    output.sendOutBuffer(out);
  }

  /**
  * Exceute the command
  * @param tokens    The tokens of the input (raw)
  * @param cmd       The command runner which is running the command
  * @param output    Output buffer for the shell instance
  */
  public void initialize(Directory curr, FileSystem fs, History history) {
    this.curr = curr;
    this.fs = fs;
  }

  /**
  * Check if the given input is a valid command for this command
  * @param tokens    The tokens of the input (raw)
  */
  public boolean validate(String[] tokens) {
    return tokens.length > 1;
  }

  /**
  * Get the manual for the command
  */
  public String getManual() {
    return Man.formatUsage(MAN_USAGE);
  }

  /**
  * Get the usage for the command
  */
  public String getUsage() {
    return Man.formatManual(MAN_USAGE, MAN_DESCRIPTION);
  }
}
