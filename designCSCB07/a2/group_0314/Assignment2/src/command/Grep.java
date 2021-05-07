package command;

import java.util.Map;
import java.util.regex.Pattern;

import driver.CommandRunner;
import driver.Output;
import filesystem.*;

/**
 * @author brody
 *
 */
public class Grep extends Command {

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

  /**
   * Null constructor
   */
  public Grep() {};

  @Override
  public void initialize(Directory curr, FileSystem fs, History his) {
    this.fsRef = fs;
    this.currDir = curr;

  }

  @Override
  public void execute(String[] tokens, CommandRunner cmd, Output output) {
    if (validate(tokens)) {
      // -R supplied, go to directory
      if (recurr) {
        try {
          // path not supplied, use current
          if (tokens.length == 3) {
            output.sendOutBuffer(grepDir(tokens[2], currDir));
          } else {
            // paths supplied, grep each path
            for (String token : tokens) {
              // skip "grep" "-r" and regex
              if (token.equals(tokens[0]) || token.equals(tokens[1])
                  || token.equals(tokens[2])) {
                continue;
              }
              output.sendOutBuffer(
                  grepDir(tokens[2], fsRef.getDirectory(tokens[3], currDir)));
            }
          }
        } catch (DirectoryNotFoundException e) {
          output.sendErrBuffer(tokens[2] + ": No such directory\n");
        }
        // -R not supplied
      } else {
        try {
          output.sendOutBuffer(
              grep(tokens[1], fsRef.getFile(tokens[2], currDir)));
        } catch (FileNotFoundException e) {
          output.sendErrBuffer(tokens[1] + ": No such file\n");
        }
      }
    }
  }

  @Override
  public boolean validate(String[] tokens) {
    if (tokens.length > 2 && tokens[0].equals("grep")) {
      if (tokens[1].equals("-r") || tokens[1].equals("-R")) {
        recurr = true;
      }
      return true;
    }
    return false;
  }

  @Override
  public String getManual() {
    return Man.formatManual("[-R] REGEX PATH ... ",
        "finds all lines in all files that contain REGEX,"
            + "print the path to the file (including the filename),"
            + "then a colon, then the line that contained REGEX.");
  }

  @Override
  public String getUsage() {
    return Man.formatUsage("[-R] REGEX PATH ... ");
  }

  /**
   * Method used to search through file to 
   * find lines in file at path matching a
   * regular expression.
   * 
   * @param regex regular expression being search for
   * @param path path where the file is located
   * @param file the file itself
   * @return all lines that match regex
   */
  public String grep(String regex, String path, File file) {
    String matches = "";
    if (path != "") {
      path = path + ":";
    } else {
      path = currDir.getPath() + ":";
    }
    String[] lines = file.getContent().split("\n");
    for (String line : lines) {
      if (Pattern.matches(regex, line)) {
        matches = matches + path + line + "\n";
      }
    }
    return matches;
  }

  public String grep(String regex, File file) {
    return grep(regex, "", file);
  }

  /**
   * Method used to search all files in directory provided as well as all
   * subdirectories for lines matching regex
   * 
   * @param regex regular expression to be matched
   * @param currDir current directory
   * @return all lines matching regex in current and sub directories
   */
  public String grepDir(String regex, Directory currDir) {
    String outStr = "";
    if (currDir.getFileContainer().isEmpty()
        && currDir.getDirContainer().isEmpty()) {
      outStr = "";
    } else {
      // grep all files in current directory
      Map<String, File> files = currDir.getFileContainer();
      for (Map.Entry<String, File> file : files.entrySet()) {
        String path = currDir.getPath() + file.getKey();
        outStr = outStr + grep(regex, path, file.getValue());
      }
      // check sub-directories
      Map<String, Directory> subDirs = currDir.getDirContainer();
      for (Map.Entry<String, Directory> subDir : subDirs.entrySet()) {
        outStr = outStr + grepDir(regex, subDir.getValue());
      }
    }
    return outStr;
  }
}
