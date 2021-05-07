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

/**
 * An object that parses the contents of an url (html file) into a string. Does
 * not support live urls.
 * 
 * @author Albion Ka Hei Fung
 *
 */
public class HTMLParser {
  public String getHTML(String urlString)
      throws MalformedURLException, IOException {
    // create object to store html source text as it is being collected
    StringBuilder html = new StringBuilder();
    URL url;
    try {
      // open connection to given url
      url = new File(urlString).toURI().toURL();
    } catch (IOException e) {
      url = new URL(urlString);
    }
    // create BufferedReader to buffer the given url's HTML source
    BufferedReader htmlbr =
        new BufferedReader(new InputStreamReader(url.openStream()));
    String line;
    // read each line of HTML code and store in StringBuilder
    while ((line = htmlbr.readLine()) != null) {
      html.append(line);
    }
    htmlbr.close();
    // convert StringBuilder into a String and return it
    return html.toString();
  }
}
