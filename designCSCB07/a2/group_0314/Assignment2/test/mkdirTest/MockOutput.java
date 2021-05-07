package mkdirTest;

import driver.Output;

public class MockOutput extends Output {
  private String[] err = {"", ""};
  private String[] out = {"", ""};
  private int eCount = 0;
  private int oCount = 0;

  public void sendErrBuffer(String str) {
    err[eCount++] = str;
  }

  public void sendOutBuffer(String str) {
    out[oCount++] = str;
  }

  public String getOutBuffer() {
    oCount = 0;
    return out[0] + out[1];
  }

  public String getErrBuffer() {
    eCount = 0;
    return err[0] + err[1];
  }

  public void fflushOut() {
    oCount = 0;
    out[0] = out[1] = "";
  }

  public void fflushErr() {
    eCount = 0;
    err[0] = err[1] = "";
  }
}