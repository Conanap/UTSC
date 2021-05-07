package filesystem;

/**
 * This represents a file implementation intended for use of the Shell. The
 * content of the file is stored as a String type variable.
 *
 * @author Law Chi Fai
 */

public class File {
  private String content;
  private String name;

  /**
   * Construct a new file with content
   * @param input   String Content of File
   */
  public File(String input) {
    this.content = input;
  }

  /**
   * Construct a new file 'without' content.
   */
  public File() {
    this.content = "";
  }

  /**
   * Retrieve content of file.
   */
  public String getContent() {
    return this.content;
  }

  /**
   * Set a new content for an existing file.
   * @param input   String Content to be set to file.
   */
  public void setContent(String input) {
    this.content = input;
  }

  /**
   * Append new content to existing File.
   * @param input   String Content to be appended to file.
   */
  public void appendToContent(String input) {
    this.content = this.content.concat(input);
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

}