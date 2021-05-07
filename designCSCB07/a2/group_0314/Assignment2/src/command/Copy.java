package command;

import filesystem.*;
import driver.CommandRunner;
import driver.Output;
import driver.Tokenizer;

import filesystem.FileNotFoundException;
import filesystem.DirectoryNotFoundException;

public class Copy extends Command{
	private FileSystem fs;
	private Directory curr;
	private int status = 0;
	private final String MAN_USAGE = "cp src dest";
	private final String MAN_DESCRIPTION = "Copies the file or directory" +
									"into the dest directory.";

	public Copy(){}

  /**
  * Exceute the command
  * @param tokens    The tokens of the input (raw)
  * @param cmd       The command runner which is running the command
  * @param output    Output buffer for the shell instance
  */
	public void execute(String[] tokens, CommandRunner cmd, Output output) {
		String src = tokens[1], dest = tokens[2], fnlName;
		Directory srcDir, deDir;
		File srcFle, tfle;
		try {
			srcDir = fs.getDirectory(src, curr);
		} catch (DirectoryNotFoundException e) {
			srcDir = null;
		}
		try {
			srcFle = fs.getFile(src, curr);
		} catch(FileNotFoundException e) {
			srcFle = null;
		}
		if(srcDir == null && srcFle == null) {
			// print error
			output.sendErrBuffer(tokens[1] + ": No such file or directory\n");
			this.status = 1;
			return; // can't find source thing
		}
		try {
			deDir = fs.getDirectory(dest, curr);
			if(srcDir == null)
				fnlName = srcFle.getName();
			else
				fnlName = srcDir.getName();
		} catch(DirectoryNotFoundException e) {
			String[] destPath = Tokenizer.parseFilePath(dest);
			if(destPath[1] == null) {
				fnlName = destPath[0];
				deDir = fs.getRoot();
			}
			else {
				fnlName = destPath[1];
				try {
					deDir = fs.getDirectory(destPath[0], curr);
				} catch(DirectoryNotFoundException f) {
					output.sendErrBuffer(destPath[0] + ": No such directory\n");
					this.status = 1;
					return;
				}
			}
		}
		if (srcDir != null) {
			// move to dir
			deDir.addDir(srcDir, fnlName);
		} else {
			//moving file
			try {
				tfle = fs.getFile(dest + "/" + fnlName, curr);
			} catch(FileNotFoundException e) {
				deDir.addFile(srcFle, fnlName);
				return;
			}
			output.sendErrBuffer(tokens[2] + ": File already exist\n");
			this.status = 1; // file already exist
		}
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
	}

  /**
  * Check if the given input is a valid command for this command
  * @param tokens    The tokens of the input (raw)
  */
	public boolean validate(String[] tokens) {
		return tokens.length == 3;
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

	/**
	* Did the command succeed? Use this to find out. used for MV only in general.
	*/
	public int getStatus() {
		return this.status;
	}
}
