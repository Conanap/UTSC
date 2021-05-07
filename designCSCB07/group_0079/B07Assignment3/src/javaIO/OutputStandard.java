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
package javaIO;

import java.util.ArrayList;

/**
 * An abstract class in which takes care of properly formatting the output.
 * 
 * @author Albion Ka Hei Fung
 *
 */
public abstract class OutputStandard {
  protected String outString;
  protected StringBuilder sb;
  protected final String separator =
      "----------------------------------------------------------------"
          + "-------\n";
  protected String[] titles;

  /**
   * Sets up the Strings for cleaner code later
   */
  protected void setUp() {
    this.titles = new String[7];
    this.titles[0] = "1. Name of Author:\n";
    this.titles[1] = "2. Number of All Citations:\n";
    this.titles[2] = "3. Number of i10-index after 2009:\n";
    this.titles[3] = "4. Title of the first 3 publications:\n";
    this.titles[4] = "5. Total paper citation (first 5 papers):\n";
    this.titles[5] = "6. Total Co-authors:\n";
    this.titles[6] = "7. Co-Author list sorted: (Total: ";
  }

  /**
   * Send the output from parsing the html to the buffer. The function will
   * format the output before placing in buffer
   * 
   * @param output The list of strings of prased information
   */
  public void sendOutput(String[] output) {
    String[] temp;

    // starting separator
    this.sb.append(this.separator);

    // append rest of info
    for (int i = 0; i < 6; i++) {
      // title for each part
      this.sb.append(titles[i]);
      if (i == 3) { // if it's the first 3 publications
        temp = output[3].split("\n");
        // special format for each line
        for (int j = 0; j < temp.length; j++) {
          this.sb.append((j + 1) + "- ");
          this.sb.append(temp[j] + "\n");
        }
      } else // otherwise, just append the info
        this.sb.append(output[i] + "\n");
    }
  }

  /**
   * Sends the error to the buffer.
   * 
   * @param error The error string. Do not include a newline at the end.
   */
  public void sendError(String error) {
    this.sb.append(error + "\n");
  }

  /**
   * Send all the coauthors after all html files have been parsed.
   * 
   * @param coauth The arraylist containing all the coauthors.
   */
  public void sendCoauthor(ArrayList<String> coauth) {
    this.sb.append("\n");
    this.sb.append(separator);
    // number of co authors
    this.sb.append(titles[6] + coauth.size() + "):\n");
    // sort in alphabetical order
    coauth.sort(String.CASE_INSENSITIVE_ORDER);
    // correctly format into list
    for (String x : coauth)
      this.sb.append(x + "\n");
  }

  /**
   * when all files have been parsed, call this function in order to output
   * everything to the appropriate source
   */
  public abstract void sendEOF();
}
