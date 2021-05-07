package command;

import filesystem.*;
import driver.CommandRunner;
import driver.Output;

public class Mv extends Command {
  private Directory curr;
  private FileSystem fs;
  private Copy cp;

  public Mv() {
    this.cp = new Copy();
  }

	public void execute(String[] tokens, CommandRunner cmd, Output output) {
	
  	  Directory srcDir = null;
      try {
        srcDir = fs.getDirectory(tokens[1], this.curr);
      } catch (DirectoryNotFoundException e) {
        //No message, copy catches. This is just to make sure it doesnt output
      }
      try {
        File srcFle = fs.getFile(tokens[1], this.curr);
      } catch (FileNotFoundException e) {
        //No message, copy catches. This is just to make sure it doesnt output
      }
      this.cp.execute(tokens, cmd, output);
      if(cp.getStatus() != 0)
      // error from cp
      return;
      else if (srcDir != null){
        // directory moved, remove original
        curr.removeDir(tokens[1]);
      }
      else {
        // file moved, remove original
        curr.removeFile(tokens[1]);
     }
	}

  public void initialize(Directory curr, FileSystem fs, History his) {
    this.curr = curr;
    this.fs = fs;
    this.cp.initialize(curr, fs, his);
  }

  public boolean validate(String[] tokens) {
    return tokens[0].equals("mv") && tokens.length == 3;
  }

  public String getUsage() {
    return "mv SOURCE DEST";
  }

  public String getManual() {
    return "mv - Moves File/Directory from SOURCE to DESTINATION.";
  }
}
