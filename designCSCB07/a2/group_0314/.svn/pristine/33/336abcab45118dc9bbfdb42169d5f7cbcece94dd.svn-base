package command;

import java.util.HashMap;
import java.util.Map;
import filesystem.*;
import driver.CommandRunner;
import driver.Output;

/**
 * @author brody
 *
 */
/**
 * @author brody
 *
 */
public class History extends Command {

  /**
   * Keeps track of total number of commands that have been executed
   */
  private int cmdCount;
  /**
   * HashMap mapping command to the index of when they were executed
   */
  private Map<Integer, String> cmds;

  private Output output;

  @Override
  public void initialize(Directory curr, FileSystem fs, History his) {
    this.cmdCount = his.getCmdCount();
    this.cmds = his.getCmds();
  }

  @Override
  public void execute(String[] tokens, CommandRunner cmd, Output output) {
    this.output = output;
    if (tokens.length == 1) {
      output.sendOutBuffer(this.getHistory());
    } else if (tokens.length == 2) {
      output.sendOutBuffer(this.getHistory(Integer.parseInt(tokens[1])));
    }
  }

  @Override
  public boolean validate(String[] tokens) {
    // TODO Auto-generated method stub
    return tokens.length == 1 || tokens.length == 2;
  }

  @Override
  public String getManual() {
    return Man.formatManual("history [NUMBER]", 
        "List recent commands. If provided a number,"
        + " limit the number of listed commands to that number.");
  }

  @Override
  public String getUsage() {
    return Man.formatUsage("history [NUMBER]");
  }

  /**
   * Construct a History object
   */
  public History() {
    cmdCount = 1;
    cmds = new HashMap<Integer, String>();
  }

  /**
   * Stores input be retrieved later
   * 
   * @param input command being stored
   */
  public void addCmd(String input) {
    cmds.put(cmdCount++, input);
  }

  /**
   * @return String all commands that have been executed
   */
  public String getHistory() {
    return getHistory(cmdCount, 1);
  }


  /**
   * @return String last end commands that have been executed
   */
  public String getHistory(int end) {
    if (end < 0) {
      return "";
    } else {
      return getHistory(cmdCount, cmdCount - end);
    }
  }

  /**
   * @param numberOfCmds number of commands being retrieved
   * @return String numberOfCmds commands that have been executed
   */
  public String getHistory(int start, int end) {
    if (end < 0) {
      output.sendErrBuffer("Negative index error: " + end);
      return "";
    } else {
      String outStr = "";
      // base case, first command or numberOfCmds'th command
      if (start == end) {
        outStr = outStr + start + ". " + cmds.get(start) + "\n";
        // recursively get numberOfCmds commands
      } else {
        String prevLine = getHistory(start - 1, end);
        String currLine = cmds.get(start);
        if (currLine == null) {
          outStr = outStr + prevLine;
        } else {
          outStr = outStr + prevLine + start + ". " + cmds.get(start) + "\n";
        }
      }
      return outStr;
    }
  }

  /**
   * @return cmdCount number of commands that have been inputted
   */
  public int getCmdCount() {
    return cmdCount;
  }

  /**
   * @return cmd Map containing commands
   */
  public Map<Integer, String> getCmds() {
    return cmds;
  }

  public void setCmdCount(int cmdCount) {
    this.cmdCount = cmdCount;
  }

  public void setCmds(Map<Integer, String> cmds) {
    this.cmds = cmds;
  }
}
