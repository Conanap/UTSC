//**********************************************************
//Assignment3:
//UTORID user_name: fungalbi
//
//Author: Albion Ka Hei Fung
//
//
//Honor Code: I pledge that this program represents my own
//program code and that I have coded on my own. I received
//help from no one in designing and debugging my program.
//*********************************************************
package JavaIO;

public class ConsoleOut extends OutputStandard{
  
  public ConsoleOut() {
    this.sb = new StringBuilder();
    this.setUp();
  }
  
  public void sendEOF() {
    this.outString = this.sb.toString();
    System.out.println(this.outString);
  }
}
