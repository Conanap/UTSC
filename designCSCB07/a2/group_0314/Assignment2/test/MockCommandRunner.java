import driver.CommandRunner;

public class MockCommandRunner extends CommandRunner{
  
  String cmd;
  MockDirectory curr;
  MockFileSystem fs;
  MockHistory his;
  
  public MockCommandRunner(MockFileSystem fs) {
  }