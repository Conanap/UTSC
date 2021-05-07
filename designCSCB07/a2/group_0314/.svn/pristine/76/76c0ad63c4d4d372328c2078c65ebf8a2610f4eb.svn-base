package command;

import driver.Tokenizer;
import filesystem.*;
import driver.CommandRunner;
import driver.Output;

/**
 * This represents a file related command that overwrites a file with stdin. If
 * The file already exists, the file is removed and re-created with a new file,
 * but with Standard Input as its content instead. Else, a new file is created
 * in the same manner.
 *
 * @author Law Chi Fai
 */

public class Overwrite extends Redirect{
  private String content;
  private String filename;
  private Directory curr;
  private FileSystem fs;
  private History history;
  private Redirect redirect;
  private final String MAN_USAGE = "COMMAND >> FILE";
  private final String MAN_DESCRIPTION = "Redirects the standard output to a file (append).";
  
  public Overwrite() {
    this.setType("ow");
  }
  
  public String getUsage(){
    return Man.formatUsage(MAN_USAGE);
  }
  
  public String getManual(){
    return Man.formatManual(MAN_USAGE, MAN_DESCRIPTION);
  }
}

