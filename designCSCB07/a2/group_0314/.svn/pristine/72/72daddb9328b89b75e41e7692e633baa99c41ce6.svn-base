package driver;

import java.util.ArrayList;

/**
* @author Albion Fung
* A output buffer that includes stdout and stderr
*/
public class Output {
  private ArrayList<String> stdout;
  private ArrayList<String> stderr;

  /**
  * Constructor
  */
  public Output() {
    this.stdout = new ArrayList<String>();
    this.stderr = new ArrayList<String>();
  }

  /**
  * Send the output to the buffer
  * @param out    The output to be sent
  */
  public void sendOutBuffer(String out) {
    stdout.add(out);
  }

  /**
  * Send the error to the buffer
  * @param out    The error to be sent
  */
  public void sendErrBuffer(String err) {
    stderr.add(err);
  }

  /**
  * Get what's in the output buffer
  */
  public String getOutBuffer() {
    return getBuffer(stdout);
  }

  /**
  * Get what's in the error buffer
  */
  public String getErrBuffer() {
    return getBuffer(stderr);
  }

  /**
  * Concatenates the buffer into a single output string and clears that buffer
  * @param buffer    The specific buffer go concatenate for
  */
  private String getBuffer(ArrayList<String> buffer) {
    String out = "";
    for (String temp : buffer)
      out += temp;
    buffer.clear();
    return out;
  }

  /**
  * Flush the output buffer
  */
  public void flushOut() {
    stdout.clear();
  }

  /**
  *Flush the error buffer
  */
  public void flushErr() {
    stderr.clear();
  }
}
