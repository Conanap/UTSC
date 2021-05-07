// **********************************************************
// Assignment0:
// UTORID: fungalbi
// UT Student #: 1002444321
// Author: Albion Ka Hei Fung
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences. In this semester
// we will select any three of your assignments from total of 5 and run it
// for plagiarism check.
// *********************************************************
// Albion Ka Hei Fung
// May 16, 2016
// v0.0.7
package a0;

public class Cfiltering {
  // this is a 2d matrix i.e. user*movie
  private int userMovieMatrix[][];
  // this is a 2d matrix i.e. user*movie
  private float userUserMatrix[][];
  // saving highest and lowest scores
  private String similar, dissim;
  // scores
  private float high, low;
  // save the dimensions
  private int x, y;

  /**
   * Default Constructor.
   */
  public Cfiltering() {
    // this is 2d matrix of size 1*1
    userMovieMatrix = new int[1][1];
    // this is 2d matrix of size 1*1
    userUserMatrix = new float[1][1];
    // set dimensions
    this.x = 1;
    this.y = 1;
    this.high = -1f;
    this.low = 2f;
  }

  /**
   * Constructs an object which contains two 2d matrices, one of size
   * users*movies which will store integer movie ratings and one of size
   * users*users which will store float similarity scores between pairs of
   * users.
   * 
   * @param numberOfUsers Determines size of matrix variables.
   * @param numberOfMovies Determines size of matrix variables.
   */
  public Cfiltering(int numberOfUsers, int numberOfMovies) {
    // creating user movie matrix
    userMovieMatrix = new int[numberOfUsers][numberOfMovies];
    // creating user user matrix
    userUserMatrix = new float[numberOfUsers][numberOfUsers];
    // set dimensions
    this.x = numberOfMovies;
    this.y = numberOfUsers;
    // set highest and lowest similiarity
    this.high = -1f;
    this.low = 2f;
  }

  /**
   * The purpose of this method is to populate the UserMovieMatrix. As input
   * parameters it takes in a rowNumber, columnNumber and a rating value. The
   * rating value is then inserted in the UserMovieMatrix at the specified
   * rowNumber and the columnNumber.
   * 
   * @param rowNumber The row number of the userMovieMatrix.
   * @param columnNumber The column number of the userMovieMatrix.
   * @param ratingValue The ratingValue to be inserted in the userMovieMatrix
   */
  public void populateUserMovieMatrix(int rowNumber, int columnNumber,
      int ratingValue) {
    // set the values
    userMovieMatrix[rowNumber][columnNumber] = ratingValue;
  }

  /**
   * Determines how similar each pair of users is based on their ratings. This
   * similarity value is represented with with a float value between 0 and 1,
   * where 1 is perfect/identical similarity. Stores these values in the
   * userUserMatrix.
   * 
   * @param usr1 User number 1 in the comparison.
   * @param usr2 User number 2 in the comparison.
   * @return Returns the score of the 2 users. 1 is best, 0 is worst.
   */
  public float calculateSimilarityScore(int usr1, int usr2) {
    // initialize vars
    int len = this.x;
    float score, temp1, temp2, temp, tot = 0f;
    // for every score
    for (int i = 0; i < len; i++) {
      // cast to float
      temp1 = (float) (this.userMovieMatrix[usr1][i]);
      temp2 = (float) (this.userMovieMatrix[usr2][i]);
      // add the scores together
      temp = (temp2 - temp1);
      // square it, the ancient way
      temp *= temp;
      // add to total
      tot += temp;
    }
    // square root of it to find distance
    score = (float) (Math.sqrt(tot));
    // find the score by def
    score = 1 / (1 + score);
    score = Math.round(score * 10000.0000f) / 10000.0000f;
    // return the score
    return score;
  }

  /**
   * Calculates all scores and populates userUserMatrix.
   */
  public void calculateSimilarityScoreForAllPairsOfUser() {
    // initialize vars
    float temp;
    // reset saved sim and dissm string
    this.similar = "";
    this.dissim = "";
    // loop in the vars
    for (int i = 0; i < y; i++) {
      for (int k = 0; k < y; k++) {
        // calculate each score
        temp = this.calculateSimilarityScore(i, k);
        // add to userUserMatrix
        userUserMatrix[i][k] = temp;
        // if the score is not of the user compared to themselves and has not
        // been calculated yet
        if ((i != k) && (k >= i)) {
          // check if they are the most similar or dissimilar users
          this.checkSim(i, k);
        }
      }
    }
  }

  /**
   * checks whether the score of the 2 provided users have the most similar or
   * dissimilar score. Adds it to the database of users that are as well.
   * 
   * @param i User 1 identifier.
   * @param k User 2 identifier.
   */
  private void checkSim(int i, int k) {
    // get their score
    float temp = userUserMatrix[i][k];
    // compare to high, if new high
    if (temp > this.high) {
      // set new high
      this.high = temp;
      // clear string and add new pair
      this.similar = (i + 1) + " " + (k + 1) + " ";
    }
    // if same as high
    else if (temp == this.high) {
      // add to string
      this.similar += ((i + 1) + " " + (k + 1) + " ");
    }
    // if new low
    if (temp < this.low) {
      // set new low
      this.low = temp;
      // clear string and add this pair
      this.dissim = (i + 1) + " " + (k + 1) + " ";
    }
    // if same as low
    else if (temp == this.low) {
      // add this low
      this.dissim += ((i + 1) + " " + (k + 1) + " ");
    }
  }

  /**
   * Prints out the similarity scores of the userUserMatrix, with each row and
   * column representing each/single user and the cell position (i,j)
   * representing the similarity score between user i and user j.
   */

  public void printUserUserMatrix() {
    // initialize var
    String temp;
    int index;
    // print initial line
    System.out.println("userUserMatrix is:");
    // for each horizontal line
    for (int i = 0; i < y; i++) {
      // each line starts with '['
      System.out.print("[");
      // print out each field
      for (int k = 0; k < y; k++) {
        temp = "" + userUserMatrix[i][k];
        temp = this.roundProper(temp);
        System.out.print(temp);
        if (k < y - 1)
          // if this is not the last field in the line, print ', ' as well
          System.out.print(", ");
      }
      // finish with ']' and start a new line
      System.out.println("]");
    }
  }

  /**
   * rounds the string properly to 4 decimal places. Only use with numbers in a
   * string (one number at a time). Does not work remove the last decimals if
   * more than 4 decimal places.
   * 
   * @param temp String which contains 1 number.
   */
  private String roundProper(String temp) {
    // initialize vars
    int index;
    // indexing the decimal
    index = temp.indexOf(".");
    // if no decimal
    if (index == -1)
      // add a decimal dot and 4 zeroes
      temp += ".0000";
    // if there are less than 4 decimals but has 1 or more
    else if (temp.length() - index < 5) {
      // calculate how many decimals missing
      int diff = temp.length() - index;
      switch (diff) {
        // for each case, add respective digits
        case 2:
          temp += "000";
          break;
        case 3:
          temp += "00";
          break;
        case 4:
          temp += "0";
          break;
        default:
          break;
      }
    }
    // return properly rounded string
    return temp;
  }

  /**
   * This function finds and prints the most similar pair of users in the
   * userUserMatrix.
   */

  public void findAndprintMostSimilarPairOfUsers() {
    // imitialize var
    String[] sim;
    String strt, temp;
    // desperately trying to keep in the 80 char limit
    strt = "The most similar pairs of users from above userUserMatrix are:";
    // print dat line
    System.out.println(strt);
    // split the string up into the user pairs
    sim = this.similar.split(" ");
    // print each pair of user out
    for (int i = 0; i < sim.length; i += 2) {
      System.out.println("User" + sim[i] + " and User" + sim[i + 1]);
    }
    // print the score
    temp = "" + this.high;
    temp = this.roundProper(temp);
    System.out.println("With similarity score of" + temp);
  }

  /**
   * This function finds and prints the most dissimilar pair of users in the
   * userUserMatrix.
   */
  public void findAndprintMostDissimilarPairOfUsers() {
    // imitialize var
    String[] dis;
    String strt, temp;
    // desperately trying to keep in the 80 char limit
    strt = "The most dissimilar pairs of users from above userUserMatrix are:";
    // print dat first line
    System.out.println(strt);
    // split the string up into the user pairs
    dis = this.dissim.split(" ");
    // print each pair of user out
    for (int i = 0; i < dis.length; i += 2) {
      System.out.println("User" + dis[i] + " and User" + dis[i + 1]);
    }
    // print the score
    temp = "" + this.low;
    temp = this.roundProper(temp);
    System.out.println("With similarity score of" + temp);
  }
}
