package command;

import filesystem.*;
import driver.Output;
import driver.CommandRunner;

/**
* Outputs the argument
*
* @author Albion Ka Hei Fung
*/
public class Echo extends Command {
  private final String MAN_USAGE = "Echo \"STRING\"";
  private final String MAN_DESCRIPTION = "Prints the provided string.";
  public Echo() {}

  /**
  * Initialize the command
  * @param curr     Current directory
  * @param fs       File system which the shell is running on
  * @param his      History of the shell's commands
  */
  public void initialize(Directory curr, FileSystem fs, History his) {
  }

  /**
  * Exceute the command
  * @param tokens    The tokens of the input (raw)
  * @param cmd       The command runner which is running the command
  * @param output    Output buffer for the shell instance
  */
  public void execute(String[] tokens, CommandRunner cmd, Output output) {
    String out = tokens[1].substring(1, tokens[1].length() - 1);
    output.sendOutBuffer(out + "\n");
  }

  /**
  * Check if the given input is a valid command for this command
  * @param tokens    The tokens of the input (raw)
  */
  public boolean validate(String[] tokens) {
  	boolean test = tokens.length == 2; // exactly 2?
    if(test) { // if exactly 2, first char is "?
      int len = tokens[1].length();
      test = (tokens[1].substring(0,1).equals("\""));
      if(test) // if first char is ", is last also "?
        test = (tokens[1].substring(len- 1, len).equals("\""));
    }
    return test; // return validity
  }


  /**
  * Get the usage for the command
  */
  public String getUsage() {
  	return Man.formatUsage(MAN_USAGE);
  }

  /**
  * Get the manual for the command
  */
  public String getManual() {
    return Man.formatManual(MAN_USAGE, MAN_DESCRIPTION);
  }
}
