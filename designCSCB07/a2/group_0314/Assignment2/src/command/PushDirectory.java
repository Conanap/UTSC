package command;

import filesystem.*;
import driver.CommandRunner;
import driver.Output;

import filesystem.DirectoryNotFoundException;

/**
* pushes a directory and cds to a directory of a file system
*
* @author Albion Ka Hei Fung
*/
public class PushDirectory extends Command {
  private Directory curr;
  private FileSystem fs;
  private Cd cd;
  private Pwd pwd;
  private final String MAN_USAGE = "pushd DIR";
  private final String MAN_DESCRIPTION = "Save the current directory into the directory" +
      " stack and change current directory to the provided directory";

  public PushDirectory() {
    this.cd = new Cd();
    this.pwd = new Pwd();
  }
  /**
   * Pushes current working directory to a stack, then cd to a given directory
  * @param tokens    The tokens of the input (raw)
  * @param cmd       The command runner which is running the command
  * @param output    Output buffer for the shell instance
  */
  public void execute( String[] tokens, CommandRunner cmd, Output output) {
    // cd
    Directory temp;
    try {
      temp = this.cd.changeDir(tokens[1], this.fs, this.curr, output);
    } catch (DirectoryNotFoundException e) {
      return;
    }// if dir exist, then we push
    this.pwd.execute(tokens, cmd, output);
    this.fs.push(output.getOutBuffer());
    cmd.setCurrDir(temp);
  }

  /**
  * Initializing the command's objects and variables
  * @param curr     Current directory
  * @param fs       File system which the shell is running on
  * @param his      History of the shell's commands
  */
  public void initialize(Directory curr, FileSystem fs, History his) {
    this.curr = curr;
    this.fs = fs;
    this.cd.initialize(curr, fs, his);
    this.pwd.initialize(curr, fs, his);
  }

  /**
  * Check if the given input is a valid command for this command
  * @param tokens    The tokens of the input (raw)
  */
  public boolean validate(String[] tokens) {
    return tokens.length == 2;
  }

  /**
  * Get the manual for the command
  */
  public String getManual() {
    return Man.formatManual(MAN_USAGE, MAN_DESCRIPTION);
  }

  /**
  * Get the usage for the command
  */
  public String getUsage() {
    return Man.formatUsage(MAN_USAGE);
  }
}
