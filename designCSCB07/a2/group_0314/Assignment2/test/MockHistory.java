import command.History;

public class MockHistory extends History{
  
  String cmds;
  int cmdCount;
  
  public MockHistory() {
    cmds = "";
  }
}