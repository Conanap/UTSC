package command;

import java.util.Iterator;
import java.util.Map;

import filesystem.*;
import driver.*;
import driver.Output;

public class Ls extends Command {

  /**
   * FileSystem holding all directories
   */
  private FileSystem fsRef;
  /**
   * Current directory
   */
  private Directory currDir;
  /**
   * If [-R] is used
   */
  private boolean recurr = false;

  public Ls() {}

  @Override
  public void initialize(Directory curr, FileSystem fs, History his) {
    fsRef = fs;
    currDir = curr;
  }

  @Override
  public void execute(String[] tokens, CommandRunner cmd, Output output) {
    if (this.validate(tokens)) {
      String outStr = "";
      if (recurr) {
        if (tokens.length == 2) {
          outStr = outStr + listContentsR(currDir);
        }
        for (String token : tokens) {
          if (token.equals(tokens[0])|| token.equals(tokens[1])) {
            continue;
          }
          outStr =
              outStr + listContentsR(token, this.fsRef, currDir, output);
        }
      } else if (tokens.length == 1){
        outStr = outStr + listContents(currDir);
      } else {
        for (String token : tokens) {
          if (token == tokens[0]) {
            continue;
          }
          outStr =
              outStr + listContents(token, this.fsRef, currDir, output);
        }
      }
      output.sendOutBuffer(outStr);
    } else {
      output.sendErrBuffer("");
    }
  }

  @Override
  public boolean validate(String[] tokens) {
    boolean valid = false;
    if (tokens.length >= 1){
      valid = tokens[0].equals("ls");
      if ((tokens.length > 1) && 
          (tokens[1].equals("-r") || tokens[1].equals("-R"))) {
        this.recurr = true;
      }
    }
    return valid;
  }

  @Override
  public String getManual() {
    return Man.formatManual("ls [-R] [PATH ...]",
        "List the contents of the specified directories.");
  }

  @Override
  public String getUsage() {
    return Man.formatUsage("ls [-R] [PATH ...]");
  }

  /**
   * Given a Directory return list of content in directory
   *
   * @param Directory currDir
   * @return String outStr
   */
  public String listContents(Directory currDir) {
    String outStr = "";
    // get directories
    Iterator<?> dirIt = currDir.listDirectories().iterator();
    while (dirIt.hasNext()) {
      outStr = outStr + (String) dirIt.next() + "\n";
    }
    // get files
    Iterator<?> fileIt = currDir.listFiles().iterator();
    while (fileIt.hasNext()) {
      outStr = outStr + (String) fileIt.next() + "\n";
    }
    return outStr;
  }

  public String listFile(File file) {
    return file.getName();
  }

  /**
   * Given current Directory and a path. return list of content in directory at
   * path
   *
   * @param path
   * @param fs
   * @param currDir
   * @param output
   * @return outStr
   */
  public String listContents(String path, FileSystem fs, Directory currDir,
      Output output) {
    String outStr = "";
    try {
      currDir = fs.getDirectory(path, currDir);
      if (currDir != null) {
        outStr = listContents(currDir);
      } else {

      }
    } catch (DirectoryNotFoundException e) {
      File file;
      try {
        file = fs.getFile(path, currDir);
        outStr = listFile(file);
      } catch (FileNotFoundException e1) {
        output.sendErrBuffer(path + ": No such file\n");
      }
    }
    return outStr;
  }

  /**
   * Given current Directory, return list of content in directory at path and
   * recursively print all contents of its sub directories
   * 
   * @param currDir
   * @return outStr
   */
  public String listContentsR(Directory currDir) {
    if (currDir.getDirContainer().isEmpty()) {
      return listContents(currDir);
    } else {
      String outStr = listContents(currDir);
      Map<String, Directory> subDirs = currDir.getDirContainer();
      for (Map.Entry<String, Directory> subDir : subDirs.entrySet()) {
        outStr = outStr + listContentsR(subDir.getValue());
      }
      return outStr;
    }
  }

  /**
   * Recursively prints contents of direct at path and all content in its sub
   * directories
   * 
   * @param path
   * @param fs
   * @param currDir
   * @param output
   * @return
   */
  public String listContentsR(String path, FileSystem fs, Directory currDir,
      Output output) {
    String outStr = "";
    try {
      currDir = fs.getDirectory(path, currDir);
      outStr = listContentsR(currDir);
    } catch (DirectoryNotFoundException e) {
      output.sendErrBuffer(path + ": No such file or directory\n");
    }
    return outStr;
  }
}
