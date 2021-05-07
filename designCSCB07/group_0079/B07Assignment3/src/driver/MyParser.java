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
package driver;

import java.io.IOException;
import java.net.MalformedURLException;

import javaIO.*;

/**
 * Parses provided url to specific information, outputs to either system.out or
 * a specific file.
 * 
 * @author Albion Ka Hei Fung
 *
 */
public class MyParser {

  /**
   * Main method. Parse using this method.
   * 
   * @param args
   */
  public static void main(String[] args) {
    // create a parser
    MyParser mp = new MyParser();
    // create an html parser
    HTMLParser hp = new HTMLParser();
    // a output buffer thing, eg a file or to console
    OutputStandard outObj;
    // a parser to parse required context
    ContextParser cp = new ContextParser();

    // create the correct obj for the correct output
    if (args.length == 2)
      outObj = new FileOut(args[1]);
    else if (args.length == 1)
      outObj = new ConsoleOut();
    else {
      System.out.println(
          "Invalid arguments; usage: " + "comma_separated_urls outputfile");
      return; // end early, rest of code looks a easier on eyes, less indents
    }

    // parse eveything
    execute(args, mp, hp, outObj, cp);
  }

  /**
   * A method which parses the desired html files and outputs it to the given
   * output object.
   * 
   * @param args The arguments passed from command line
   * @param mp MyParser object
   * @param hp an HTMLParser
   * @param outObj An OutputStandard object, usually to console or a file.
   * @param cp ContextParser object.
   */
  public static void execute(String[] args, MyParser mp, HTMLParser hp,
      OutputStandard outObj, ContextParser cp) {
    // store the list of files / url to open
    String[] urlList = mp.getURLList(args[0]), output;
    String html; // this stores the entire page code
    // for each url
    for (String url : urlList) {
      try { // try to get required info
        html = hp.getHTML(url);
        cp.setHTML(html);
        // System.out.println(html);
        output = parseContext(cp);
        outObj.sendOutput(output); // send the output
      } catch (MalformedURLException e) {
        outObj.sendError(url + ": Malformed URL."); // send errors
      } catch (IOException e) {
        outObj.sendError(url + ": Invalid URL."); // send errors
      }
    }
    outObj.sendCoauthor(cp.getCoauthors());
    // send eof
    outObj.sendEOF();
  }

  /**
   * Parses a comma separated string of urls into an array of url, and replace
   * &#8208 with hyphens.
   * 
   * @param inURL The string of comma separated urls
   * @return The list of urls parsed
   */
  private String[] getURLList(String inURL) {
    // split up the string
    String[] URLList = inURL.split(",");
    // define the pattern
    String charPattern = "&#8208", temp;
    for (int i = 0; i < URLList.length; i++) {
      temp = "";
      // replace each &#8208 with -
      if (URLList[i].split(charPattern).length > 1) {
        for (String x : URLList[i].split(charPattern)) {
          temp += x;
          temp += "-";
        }
        // remove ending -
        temp = temp.substring(0, temp.length() - 1);
      }
      // if we had to replace anything
      if (!temp.equals("")) // set it to the string of corrected urls
        URLList[i] = temp;
    }
    return URLList;
  }

  /**
   * parses all of the context into an array of string from an string formatted
   * as a html site.
   * 
   * @param html The String formatted as html webpage
   * @return A list of information extracted from the string
   */
  public static String[] parseContext(ContextParser cp) {
    String out[] = new String[6];
    out[0] = cp.getAuthor();
    out[1] = cp.getNumberCitations();
    out[2] = cp.getNumberi10Index();
    out[3] = cp.getFirstThreePublication();
    out[4] = cp.getNumPublicationFirstFive();
    out[5] = cp.getNumCoauthor();
    return out;
  }
}
