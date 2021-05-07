// **********************************************************
// Assignment2:
// Student1:
// UTORID user_name: fungalbi
// UT Student #: 1002444321
// Author: Albion Ka Hei Fung
//
// Student2:
// UTORID user_name:
// UT Student #:
// Author: Brody Chen
//
// Student3:
// UTORID user_name: lawchi1
// UT Student #: 1002470444
// Author: Chi Fai Law
//
// Student4:
// UTORID user_name: moha1075
// UT Student #: 1002307434
// Author: Rizadh Mohamed Nizam
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************


public class JShell {
  private static boolean exit;
  private static FileSystem fs;

  public static void main(String[] args) {
    exit = false;
    fs = new FileSystem();
    while (!exit) {
      System.out.print("JVM: ~User$ ");
      System.out.print(runCmd(Input.getInput()));
    }
    // reset for next run
    // exit = false;
  }

  private static void exit() {
    exit = true;
  }

  private static String runCmd(String[] tokens) {
    String out = "";
    if (!Validator.validateTokens(tokens))
      return Man.getUsage(tokens[0]);
    else {
      switch (tokens[0]) {
        case "exit":
          exit();
          break;
        case "echo":
          if (tokens.length == 2)
            out = Echo.echo(tokens[1]);
          else if (tokens.length == 4) {
            if (tokens[2] == ">")
              Overwrite.overwrite(Echo.echo(tokens[1]), tokens[3], fs);
            else
              Append.append(Echo.echo(tokens[1]), tokens[3], fs);
          }
          break;
        case "mkdir":
          for (int i = 1; i < tokens.length; i++)
            // Mkdir.mkdir(tokens[i]);
            out += "";
          break;
        case "cd":
          Cd.changeDir(tokens[1], fs);
          out += "";
          break;
        case "ls":
          for (int i = 1; i < tokens.length; i++)
            Ls.listContents(fs);
          break;
        case "cat":
          out = Cat.cat(tokens, fs);
          break;
        case "pushd":
          Pushd.pushd(fs);
          break;
        case "popd":
          Popd.popd(fs);
          break;
        case "history":
          // History.history(tokens[1]);
          out += "";
          break;
        case "man":
          out = Man.getDocumentation(tokens[1]);
          break;
        case "pwd":
          out = Pwd.printWorkingDirectory(fs);
          break;
        default:
          out = "-jshell: " + tokens[0] + ": command not found\n";
          break;
      }
    }
    return out;
  }
}
