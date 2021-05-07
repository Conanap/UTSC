package command;

import filesystem.Directory;
import filesystem.FileSystem;
import driver.CommandRunner;
import driver.Output;

/**
* @author Albion Fung
*/

/**
* Abstract class of how a command should work
*/
public abstract class Command {
  /**
  * Initializing the command's objects and variables
  * @param curr     Current directory
  * @param fs       File system which the shell is running on
  * @param his      History of the shell's commands
  */
  public abstract void initialize(Directory curr, FileSystem fs, History his);

  /**
  * Exceute the command
  * @param tokens    The tokens of the input (raw)
  * @param cmd       The command runner which is running the command
  * @param output    Output buffer for the shell instance
  */
  public abstract void execute(String[] tokens, CommandRunner cmd, Output output);

  /**
  * Check if the given input is a valid command for this command
  * @param tokens    The tokens of the input (raw)
  */
  public abstract boolean validate(String[] tokens);

  /**
  * Get the manual for the command
  */
  public abstract String getManual();

  /**
  * Get usage for the command
  */
  public abstract String getUsage();
}
