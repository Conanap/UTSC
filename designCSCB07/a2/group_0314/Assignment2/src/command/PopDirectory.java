package command;

import filesystem.*;
import driver.CommandRunner;
import driver.Output;

import java.util.EmptyStackException;

/**
* Pops a directory and cds to the directory of a file system
*
* @author Albion Ka Hei Fung
*/
public class PopDirectory extends Command {
  private Directory curr;
  private FileSystem fs;
  private Cd cd;
  private final String MAN_USAGE = "popd";
  private final String MAN_DESCRIPTION = "Remove top directory from the directory stack " +
          "and change current directory into it";

  public PopDirectory() {
    cd = new Cd();
  }

  /**
   * Pops the last pushed dir and cd to it
  * @param tokens    The tokens of the input (raw)
  * @param cmd       The command runner which is running the command
  * @param output    Output buffer for the shell instance
  */
  public void execute(String[] tokens, CommandRunner cmd, Output output) {
    String path;
    try {
      path = fs.pop(); // popping
    } catch (EmptyStackException e) {
      String errs = "No more directories to pop\n";
      output.sendErrBuffer(errs);
      return;
    }
    path = path.substring(0, path.length() - 1);
    String[] ntokens = {"cd", path};
    cd.execute(ntokens, cmd, output);
  }

  /**
  * Initializing the command's objects and variables
  * @param curr     Current directory
  * @param fs       File system which the shell is running on
  * @param his      History of the shell's commands
  */
  public void initialize(Directory curr, FileSystem fs, History his) {
    this.fs = fs;
    this.curr = curr;
    this.cd.initialize(curr, fs, his);
  }

  /**
  * Get the manual for the command
  */
  public String getManual() {
    return Man.formatManual(MAN_USAGE, MAN_DESCRIPTION);
  }

  /**
  * Check if the given input is a valid command for this command
  * @param tokens    The tokens of the input (raw)
  */
  public boolean validate(String[] tokens) {
    return tokens.length == 1;
  }

  /**
  * Get the usage for the command
  */
  public String getUsage() {
    return Man.formatUsage(MAN_USAGE);
  }
}
