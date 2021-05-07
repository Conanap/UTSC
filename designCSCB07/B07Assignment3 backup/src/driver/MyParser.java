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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import JavaIO.*;

public class MyParser {

  /**
   * @param args
   */
  public static void main(String[] args) {
    // create a parser
    MyParser mp = new MyParser();
    // store the list of files / url to open
    String[] urlList = mp.getURLList(args[0]), output;
    // create an html parser
    HTMLParser hp = new HTMLParser();
    String html; // this stores the entire page code
    // a parser to parse required context
    ContextParser cp = new ContextParser();
    // a output buffer thing, eg a file or to console
    OutputStandard outObj;
    
    // create the correct obj for the correct output
    if(args.length == 2)
      outObj = new FileOut(args[1]);
    else if(args.length == 1)
      outObj = new ConsoleOut();
    else {
      System.out.println("Invalid arguments; usage: "
          + "(comma_separated_urls, outputfile)");
      return; // end early, rest of code looks a easier on eyes, less indents
    }
    
    // for each url
    for(String url : urlList) {
      try { // try to get required info
        System.out.println(url);
        html = hp.getHTML(url);
        //System.out.println(html);
        output = cp.parseContext(html);
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
      if(URLList[i].split(charPattern).length > 1) {
        for(String x : URLList[i].split(charPattern)) {
          temp += x;
          temp += "-";
        }
      }
      // if we had to replace anything
      if(!temp.equals("")) // set it to the string of corrected urls
        URLList[i] = temp;
    }
    return URLList;
  }

  /*
   * This is a debug/helper method to help you get started. Once you understand
   * how this method is being used, you are free to refactor it, modify it, or
   * change it, or remove it entirely in any way you like.
   */
  private static void DEBUGStarterCode(String[] args) {
    try {
      System.out.println("DEBUG: URLS are " + args[0]);
      System.out.println("DEBUG: FileName is " + args[1]);
    } catch (Exception e) {
      System.out.println("Did you change the run configuration in"
          + "Eclipse for argument0 and argument 1?");
    }


    // TODO Auto-generated method stub
    String inputFiles[] = args[0].split(",");
    for (String inputFile : inputFiles) {
      DEBUGextractAuthorsName(inputFile);
    }
  }


  /*
   * This is a debug/helper method to help you get started. Once you understand
   * how this method is being used i.e. the String re, Pattern, Matcher and how
   * the authors name is being extracted, feel free to remove this method or
   * refactor it in any way you like.
   */
  private static void DEBUGextractAuthorsName(String googleScholarURL) {
    try {
      HTMLParser googleScholarParser = new HTMLParser();
      String rawHTMLString = googleScholarParser.getHTML(googleScholarURL);

      String reForAuthorExtraction = "<span id=\"cit-name-display\" "
          + "class=\"cit-in-place-nohover\">(.*?)</span>";
      Pattern patternObject = Pattern.compile(reForAuthorExtraction);
      Matcher matcherObject = patternObject.matcher(rawHTMLString);
      while (matcherObject.find()) {
        System.out.println("DEBUG: Authors Name is " + matcherObject.group(1));
      }

    } catch (Exception e) {
      System.out
          .println("malformed URL or cannot open connection to " + "given URL");
    }
  }
}
