public class Pwd {
  public static String printWorkingDirectory(FileSystem fs) {
    StringBuilder directoryPaths = new StringBuilder();
    for (Directory currentDirectory = fs.getCurr();
         currentDirectory != currentDirectory.getParent();
         currentDirectory = currentDirectory.getParent()) {
      directoryPaths.append("/" + currentDirectory.getDirName());
    }
    return (directoryPaths.length() > 0 ? directoryPaths.toString() : "/") +
            "\n";
  }
}
