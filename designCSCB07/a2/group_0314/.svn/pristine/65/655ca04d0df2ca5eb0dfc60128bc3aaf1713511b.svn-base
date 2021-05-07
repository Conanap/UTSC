import driver.Output;

public class MockOutput extends Output{
  
  String out;
  String err;
  
  public MockOutput() {
    out = "";
    err = "";
  }
  
  public void sendOutBuffer(String out) {
    this.out = out;
  }

  public void sendErrBuffer(String err) {
    this.err = err;
  }
  
  public String getOutBuffer() {
    return out;
  }

  public String getErrBuffer() {
    return err;
  }

  public void fflushOut() {
    out = "";
  }
}
