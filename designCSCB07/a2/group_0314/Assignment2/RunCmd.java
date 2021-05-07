package driver;

import java.util.regex.Pattern;
import java.util.Arrays;
/**
* Runs the command and updates related files
*
* @author Albion Ka Hei Fung
*/
public class RunCmd {
  private static Directory curr;
  //private static Directory current = curr;

  /**
   * The method runs the command when given tokens, where the zeroth token is a
   * string representing the command you want to run, and the tokens after are
   * its arguments
   *
   * @param tokens  The tokens for the command
   * @param current The current directory in which the instance is running
   * @param fs      The file system in which the current isntance is running on
   */
  public static void runCmd(String[] tokens, Directory current,
                              FileSystem fs, History history) {
    // initialize var
    String out = "", errs = "";
    int status = 1;
    Directory temp; // a temp dir for comparing
    // validate command
    if (!Validator.validateTokens(tokens)) {
      // output usage message if invalid
      if (tokens[0].substring(0, 1).equals("!"))
        errs += Man.getUsage("!");
      else
        errs += tokens.length > 0 ? Man.getUsage(tokens[0]) : "";
    } else if(tokens.length > 2 && tokens[tokens.length - 2].equals(">"))
        Overwrite.overwrite(tokens, tokens[tokens.length - 1], fs, current, history);
      else if(tokens.length > 2 && tokens[tokens.length - 2].equals(">>"))
        Append.append(tokens, tokens[tokens.length - 1], fs, current, history);
      else { // running the command
      switch (tokens[0]) {
        // echo
        case "echo":
          // just echo no redirect
          out = Echo.echo(tokens[1]);
          break; // 1 is success
        // mkdir
        case "mkdir":
          int i = 1; // loop var
          // if more tokens and cmd did not fail
          while (i < tokens.length && status == 1) {
            // run command on next token
            status = MkDir.mkdir(tokens[i], fs, current);
            i++; //increment
          }
          // check exit status
          // 0 means the dirs before last dir does not exist
          if (status == 0) {
            errs += "Path not found: " +
                    Tokenizer.parseFilePath(tokens[(i != 1) ? --i : i])[0] +
                    "\n";
          }
            // 2 means the file name is . or ..
          else if (status == 2) {
            errs += "Illegal file name: " +
                    Tokenizer.parseFilePath(tokens[(i != 1) ? --i : i])[1] +
                    "\n";
          }
            // 3 means they're trying to create a dir that already exist
          else if (status == 3) {
            errs += "Directory already exist: " +
                    Tokenizer.parseFilePath(tokens[(i != 1) ? --i : i])[1] +
                    "\n";
          }
            // 4 means they're trying to create a dir but a file has same name
          else if (status == 4) {
            errs += "File already exist: " +
                    Tokenizer.parseFilePath(tokens[(i != 1) ? --i : i])[1] +
                    "\n";
          }
          break; // 1 means success
        // cd
        case "cd":
          // try to cd
          temp = Cd.changeDir(tokens[1], fs, current);
          // cd returns null if the dir doesn't exist
          if (temp != null)
            curr = temp;
          break;
        // ls
        case "ls":
          // see where you want us to ls
          // if cmd is just ls, we just just current
          if (tokens.length == 1)
            out = Ls.listContents(current);
          else {
            int r = 0, in = 1;
            if(tokens[1].toUpperCase().equals("-R")) {
              r = 1;
              in++;
            }
            if(tokens.length == 2)
              out = Ls.listContentsR(current);
            else {
              for (; in < tokens.length; in++) {
                if(r == 0)
                  out += Ls.listContents(tokens[in], fs, current);
                else if(r == 1)
                  out += Ls.listContentsR(tokens[in], fs, current);
              }
            }
          }
          break;
        // cat
        case "cat":
          out = Cat.cat(tokens, fs, current);
          break;
        // pushd
        case "pushd":
          //pushing
          temp = Pushd.pushd(tokens[1], fs, current);
          if (temp == null)// if dir DNE
            errs += "Cannot find directory. Did not push current dir\n";
          else // if dir exist, changing current dir
            curr = temp;
          break;
        // popd
        case "popd":
          // poping
          temp = Popd.popd(fs, current);
          if (temp == null)// if no more to pop / cant cd
            errs += "Either pushed directory no longer exist or no more" +
                    " directories to pop\n";
          else
            curr = temp; // cd
          break;
        // history
        case "history":
          // specified how many commands
          if (tokens.length > 1) {
            out = history.getHistory(Integer.parseInt(tokens[1]));
          } else { // all history
            out = history.getHistory();
          }
          break;
        // man
        case "man":
          out = Man.getDocumentation(tokens[1]);
          break;
        // pwd
        case "pwd":
          out = Pwd.printWorkingDirectory(current);
          break;
        case "cp":
          status = Cp.copy(tokens[1], tokens[2], current, fs);
          if(status == 1)
            errs += tokens[1] + ": No such file or directory\n";
          else if(status == 2)
            errs += "File already exist: " + tokens[2] + "\n";
          else if(status == 3)
            errs += tokens[2] + ": No such directory\n";
          break;
        case "mv":
          status = Mv.mv(tokens[1], tokens[2], current, fs);
          if(status == 1)
            errs += tokens[1] + ": No such file or directory\n";
          else if(status == 2)
            errs += "File already exist: " + tokens[2] + "\n";
          else if(status == 3)
            errs += tokens[2] + ": No such directory\n";
          break;
        // cannot find command
        default:
          if(Pattern.matches("!\\d*", tokens[0])) // !, b/c the way code works
            RunHistory.runHis(tokens[0], current, fs, history);
          // if user just presses entre, then we don't need to print
          else if (tokens.length > 0)
            errs += "-jshell: " + tokens[0] + ": command not found\n";
          break;
      }
    }
    //return output string
    Output.sendOutBuffer(out);
    Output.sendErrBuffer(errs);
  }

  /**
   * Use this method after running cd to update your instance's current
   * directory.
   */
  public static Directory getCurrDir() {
    return curr;
  }

  /**
   * Initialization when your instance first starts running
   *
   * @param dir Pass your instance's current directory, usually root
   */
  public static void init(Directory dir) {
    curr = dir;
  }
}
