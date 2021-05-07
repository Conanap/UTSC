/**
 * This represents a file related command that overwrites a file with stdin. If
 * The file already exists, the file is removed and re-created with a new file,
 * but with Standard Input as its content instead. Else, a new file is created
 * in the same manner.
 * 
 * 
 * @author  Law Chi Fai
 * @version 1.0
 * @since   2016-06-14
 */

public class Overwrite extends Redirect{
  private String content;
  private String filename;

  // Initialization
  public static void overwrite(String input, String filein, FileSystem fs) {
    redirect("ow", input, filein, fs);
  }
}

