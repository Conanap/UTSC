package command;

import driver.Tokenizer;
import filesystem.*;
import driver.CommandRunner;
import driver.Output;

/**
 * This represents a file related command that appends a file with stdin. The
 * content of the stdin is either stored within an existing file or, if the
 * filename does not exist, a new file with said filename.
 *
 */

public class Append extends Redirect{
  private String content;
  private String filename;
  private Directory curr;
  private FileSystem fs;
  private History history;
  private Redirect redirect;
  private final String MAN_USAGE = "COMMAND > FILE";
  private final String MAN_DESCRIPTION = "Redirects the standard output of a command to a file (overwrite).";
  
  public Append() {
    this.setType("ap");
  }
  
  public String getUsage(){
    return Man.formatUsage(MAN_USAGE);
  }
  
  public String getManual(){
    return Man.formatManual(MAN_USAGE, MAN_DESCRIPTION);
  }
}
