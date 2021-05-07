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

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class FileOut extends OutputStandard{
  
  private String flname;

  public FileOut(String name) {
    this.flname = name + ".txt";
    this.sb = new StringBuilder();
    this.setUp();
  }
  
  public void sendEOF() {
    try {
    PrintWriter pw = new PrintWriter(this.flname+".txt", "UTF-8");
    this.outString = this.sb.toString();
    } catch (UnsupportedEncodingException e) {
      System.out.println("Encoding not supported");
    }
    
  }
}
