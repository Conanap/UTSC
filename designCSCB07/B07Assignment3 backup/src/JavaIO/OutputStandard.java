// **********************************************************
// Assignment3:
// UTORID user_name: fungalbi
//
// Author: Albion Ka Hei Fung
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// *********************************************************
package JavaIO;

import java.util.ArrayList;

public abstract class OutputStandard {
  protected String outString;
  protected StringBuilder sb;
  protected final String separator =
      "----------------------------------------------------------------" +
          "-------\n";
  protected String[] titles;
  
  protected void setUp() {
    this.titles = new String[7];
    this.titles[0] = "1. Name of Author:\n";
    this.titles[1] = "2. umber of All Citations:\n";
    this.titles[2] = "3. Number of i0-index after 2009:\n";
    this.titles[3] = "4. Title of the first 3 publications:\n";
    this.titles[4] = "5. Total paper citation (first 5 papers):\n";
    this.titles[5] = "6. Total Co-authors:\n";
    this.titles[6] = "7. Co-Author list sorted: (Total: ";
  }

  public void sendOutput(String[] output) {
    String[] temp;
    
    this.sb.append(this.separator);
    
    // author
    this.sb.append(titles[0]);
    this.sb.append(output[0]+"\n");
    
    // no of all citations
    this.sb.append(titles[1]);
    this.sb.append(output[1]+"\n");
    
    // i10
    this.sb.append(titles[2]);
    this.sb.append(output[2]+"\n");
    
    // title of first 3 publications
    this.sb.append(titles[3]);
    temp = output[3].split("\n");
    for (int i = 0; i < temp.length; i++) {
      this.sb.append(i + "- ");
      this.sb.append(temp[i]+"\n");
    }
    
    // total paper citations
    this.sb.append(titles[4]);
    this.sb.append(output[4]+"\n");
    
    // total coauthors
    this.sb.append(titles[5]);
    this.sb.append(output[5]+"\n");
  }

  public void sendError(String error) {
    this.sb.append(error+"\n");
  }

  public void sendCoauthor(ArrayList<String> coauth) {
    this.sb.append(separator);
    this.sb.append(titles[6] + coauth.size() + "):\n");
    coauth.sort(String.CASE_INSENSITIVE_ORDER);
    for(String x : coauth)
      this.sb.append(x + "\n");
  }

  public abstract void sendEOF();
}
