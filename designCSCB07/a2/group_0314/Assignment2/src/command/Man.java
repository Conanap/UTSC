package command;

import driver.CommandRunner;
import driver.Output;
import filesystem.Directory;
import filesystem.FileSystem;

/**
 * Handles the storage and retrieval of usage messages and documentation
 */
public class Man extends Command {
  private static final String
          MAN_USAGE = "man COMMAND",
          MAN_DESCRIPTION = "Retrieves and outputs the manual of the COMMAND";

  public static String formatUsage(String usageMessage) {
    return "usage: " + usageMessage + "\n";
  }

  public static String formatManual(String usageMessage, String description) {
    return description + "\n\n$ " + usageMessage + "\n";
  }

  @Override
  public String getUsage() {
    return formatUsage(MAN_USAGE);
  }

  @Override
  public String getManual() {
    return formatManual(MAN_USAGE, MAN_DESCRIPTION);
  }

  @Override
  public void initialize(Directory curr, FileSystem fs, History his) {
  }

  @Override
  public void execute(String[] tokens, CommandRunner cmd, Output output) {
    output.sendOutBuffer(cmd.getCommand(tokens[1]).getManual());
  }

  @Override
  public boolean validate(String[] tokens) {
    return tokens.length == 2;
  }
}
