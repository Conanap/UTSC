package filesystem;

import java.util.EmptyStackException;
import java.util.Stack;
/**
* A directory stack used to save pushed directory stacks. Acts
* similar to a standard stack
*
* @author Albion Ka Hei Fung
*/
public class DirStack {
  private Stack<String> stack;

  public DirStack() {
    this.stack = new Stack<String>();
  }

  /**
  * Get the next directory in line. Returns null if no more.
  */
  public String popDir() throws EmptyStackException{
    String path;
      path = this.stack.pop();
    return (path);
  }

  /**
  * Push a directory into the stack
  *
  * @param path The path to push
  */
  public void pushDir(String path) {
    this.stack.push(path);
  }
}
