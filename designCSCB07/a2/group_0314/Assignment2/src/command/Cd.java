package command;

import filesystem.*;
import driver.CommandRunner;
import driver.Output;

import filesystem.DirectoryNotFoundException;

public class Cd extends Command {

  /**
   * FileSystem from where directories are gotten
   */
  FileSystem fsRef;
  /**
   * Initial directory to be changed to target directory
   */
  Directory currDir;

  /**
   * Null constructor for uses of CommandRunner
   */
  public Cd() {}

  @Override
  public void initialize(Directory curr, FileSystem fs, History his) {
    fsRef = fs;
    currDir = curr;
  }

  @Override
  public void execute(String[] tokens, CommandRunner cmd, Output output) {
    Directory dir;
    try {
      dir = this.changeDir(tokens[1], fsRef, currDir, output);
      if (dir != null)
        cmd.setCurrDir(dir);
    } catch (DirectoryNotFoundException e) {
      output.sendErrBuffer(tokens[1] + ": directory not found\n");
    }
  }

  @Override
  public boolean validate(String[] tokens) {
    return tokens[0].equals("cd") && tokens.length > 1;
  }

  @Override
  public String getManual() {
    return Man.formatManual("cd DIR", 
        "cd - Change the shell working directory to DIR.");
  }

  @Override
  public String getUsage() {
    return Man.formatUsage("cd DIR");
  }

  /**
   * Given an String of directory names representing a directory path, and a
   * Directory object, find and return directory at path
   *
   * @param path a String giving the location of target Directory
   * @param currDir current directory
   * @param fs FileSystem used to handle file navigation
   * @return Directory directory located at path
   */
  public Directory changeDir(String path, FileSystem fs, Directory currDir,
      Output output) throws DirectoryNotFoundException {
    Directory newDir = fs.getDirectory(path, currDir);
    if (newDir == null) {
      output.sendErrBuffer(path + ": No such directory\n");
      throw new DirectoryNotFoundException();
    }
    return newDir;
  }
}
